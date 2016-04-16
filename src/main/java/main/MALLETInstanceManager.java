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
import cc.mallet.types.IDSorter;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

public class MalletInstanceManager {
	// some code adapted from http://mallet.cs.umass.edu/topics-devel.php
	private static MalletInstanceManager singletonInstance;
	private InstanceList instances;
	private ArrayList<Pipe> pipeList;
	private ParallelTopicModel model;

	// Run the model for 50 iterations and stop for testing,
	// use 1000 to 2000 iterations for real application
	private static final int NUM_ITERATIONS = 1000;
	private static final int NUM_TOPICS = 20;
	private static final int TOP_WORDS = 5;

	private MalletInstanceManager() {
		this.model = new ParallelTopicModel(NUM_TOPICS, 1.0, 0.01);
		createPipeList();
	}

	public static MalletInstanceManager getInstance() {
		if (singletonInstance == null) {
			singletonInstance = new MalletInstanceManager();
		}
		return singletonInstance;
	}

	private void createPipeList() {
		this.pipeList = new ArrayList<Pipe>();
		pipeList.add(new CharSequenceLowercase());
		pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
		pipeList.add(new TokenSequenceRemoveStopwords(false, false));
		pipeList.add(new TokenSequence2FeatureSequence());

		this.instances = new InstanceList(new SerialPipes(pipeList));
	}

	/*
	 * Add a new instance created from an issue. Currently addThruPipe is
	 * redundant as all issues are considered part of the training corpus and
	 * will be added through pipe when trainTopics is called, but this will be
	 * useful in future when new issues are created and model is already trained
	 */
	public Instance addInstance(Issue issue) {
		String subjectAndContent = issue.getSummary().concat(issue.getContent());
		Instance instance = new Instance(subjectAndContent, issue.getID(), issue.getKey(), issue.getContent());
		instances.addThruPipe(instance);

		return instance;
	}

	/*
	 * Analyze the contents of all instances and estimate NUM_TOPICS probable
	 * topics therein
	 */
	public void trainTopicModel() throws Exception {
		// adapted from http://mallet.cs.umass.edu/topics-devel.php
		model.addInstances(instances);
		model.setNumThreads(2);
		model.setNumIterations(NUM_ITERATIONS);
		model.estimate();
	}

	/*
	 * Given an instance, get the topic for which it has the heaviest weighting
	 * and return a formatted string representing the top five words
	 */
	public String getMainTopicString(Instance inst) {
		// adapted from http://mallet.cs.umass.edu/topics-devel.php
		int i = instances.indexOf(inst);
		double[] topicDistribution = model.getTopicProbabilities(i);
		int topic = getMainTopicIndex(topicDistribution);

		// Get an array of sorted sets of word ID/count pairs
		ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
		Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();

		Formatter out = new Formatter(new StringBuilder(), Locale.US);
		// include commas for csv column splits
		out.format("%d%s\t%.3f\t%s", topic, ",", topicDistribution[topic], ",");
		int rank = 0;
		while (iterator.hasNext() && rank < TOP_WORDS) {
			IDSorter idCountPair = iterator.next();
			out.format("%s (%.0f) ", instances.getDataAlphabet().lookupObject(idCountPair.getID()),
					idCountPair.getWeight());
			rank++;
		}
		// out.close();
		return out.toString();
	}

	/*
	 * Topics are weighted by a percentage represented as a double in the given
	 * array. Return the index for the largest weight.
	 */
	private int getMainTopicIndex(double[] topicDistribution) {
		double max = 0.0;
		int topic = 0;
		for (int i = 0; i < topicDistribution.length; i++) {
			if (topicDistribution[i] > max) {
				max = topicDistribution[i];
				topic = i;
			}
		}
		return topic;
	}
}
