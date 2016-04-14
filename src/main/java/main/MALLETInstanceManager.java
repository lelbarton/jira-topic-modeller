package main;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;
import java.util.regex.Pattern;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.Alphabet;
import cc.mallet.types.IDSorter;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

public class MalletInstanceManager implements Iterable<Instance> {

	private static MalletInstanceManager singletonInstance;
	private InstanceList instances;
	private ArrayList<Pipe> pipeList;
	private ParallelTopicModel model;

	private int NUM_TOPICS = 20;
	private int NUM_ITERATIONS = 1000;

	private MalletInstanceManager() {
		this.model = new ParallelTopicModel(NUM_TOPICS, 1.0, 0.01);
		createPipeList();

	}

	private void createPipeList() {
		this.pipeList = new ArrayList<Pipe>();
		pipeList.add(new CharSequenceLowercase());
		pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
		// TODO this might need fixing
		// pipeList.add(new TokenSequenceRemoveStopwords(new
		// File("stoplists/en.txt"), "UTF-8", false, false, false));
		pipeList.add(new TokenSequenceRemoveStopwords(false, false));
		pipeList.add(new TokenSequence2FeatureSequence());

		this.instances = new InstanceList(new SerialPipes(pipeList));
	}

	public static MalletInstanceManager getInstance() {
		if (singletonInstance == null) {
			singletonInstance = new MalletInstanceManager();
		}
		return singletonInstance;
	}

	public Instance addInstance(Issue issue) {
		Instance instance = new Instance(issue.getContent(), issue.getID(), issue.getKey(), issue.getContent());
		instances.addThruPipe(instance);
		return instance;
		// System.out.println(instance.getData());
	}

	public Iterator<Instance> iterator() {
		return instances.iterator();
	}

	public void analyzeTopics() throws Exception {
		// from MALLET website
		// TODO insert URL for their website
		// int numTopics = 20;

		model.addInstances(instances);

		// Use two parallel samplers, which each look at one half the corpus and
		// combine
		// statistics after every iteration.
		model.setNumThreads(2);

		// Run the model for 50 iterations and stop (this is for testing only,
		// for real applications, use 1000 to 2000 iterations)
		model.setNumIterations(NUM_ITERATIONS);
		model.estimate();
		writeTopicsToCsv();
	}

	public void writeTopicsToCsv() {
		TopicWriter writer = new TopicWriter("/Users/Laura/Desktop/topicTestFile.csv");
		// The data alphabet maps word IDs to strings
		Alphabet dataAlphabet = instances.getDataAlphabet();

		for (int j = 0; j < instances.size(); j++) {
			Instance instance = model.getData().get(j).instance;
			// FeatureSequence tokens = (FeatureSequence)
			// model.getData().get(j).instance.getData();
			// LabelSequence topicsLS = model.getData().get(j).topicSequence;

			Formatter out = new Formatter(new StringBuilder(), Locale.US);

			// Estimate the topic distribution of the first instance,
			// given the current Gibbs state.
			double[] topicDistribution = model.getTopicProbabilities(j);
			double max = 0.0;
			int topic = 0;
			for (int i = 0; i < topicDistribution.length; i++) {
				if (topicDistribution[i] > max) {
					max = topicDistribution[i];
					topic = i;
				}
			}

			// Get an array of sorted sets of word ID/count pairs
			ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();

			Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();

			// out = new Formatter(new StringBuilder(), Locale.US);
			out.format("%d\t%.3f\t", topic, topicDistribution[topic]);
			int rank = 0;
			while (iterator.hasNext() && rank < 5) {
				IDSorter idCountPair = iterator.next();
				out.format("%s (%.0f) ", dataAlphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight());
				rank++;
			}
			System.out.println(instance.getName().toString());
			System.out.println(out);
			writer.writeTopicModel(instance.getName().toString(), instance.getName().toString(), out.toString(),
					"contents forthcoming");
		}
		writer.closeTopicModelCsv();
	}
}
