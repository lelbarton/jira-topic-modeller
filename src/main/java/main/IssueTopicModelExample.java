package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;
import java.util.regex.Pattern;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.PrintInputAndTarget;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceLowercase;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.IDSorter;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.LabelSequence;

public class IssueTopicModelExample {

	// based on sample code from http://mallet.cs.umass.edu/topics-devel.php
	public static void main(String[] args) throws Exception {

		// Begin by importing documents from text to feature sequences
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

		// Pipes: lowercase, tokenize, remove stopwords, map to features
		pipeList.add(new CharSequenceLowercase());
		pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
		pipeList.add(new TokenSequenceRemoveStopwords(new File("stoplists/en.txt"), "UTF-8", false, false, false));
		pipeList.add(new TokenSequence2FeatureSequence());

		InstanceList instances = new InstanceList(new SerialPipes(pipeList));

		Reader fileReader = new InputStreamReader(new FileInputStream(new File(args[0])), "UTF-8");
		instances
				.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"), 3, 2, 1)); // data,
																															// label,
																															// name
																															// fields

		// Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
		// Note that the first parameter is passed as the sum over topics, while
		// the second is the parameter for a single dimension of the Dirichlet
		// prior.
		int numTopics = 100;
		ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);

		model.addInstances(instances);

		// Use two parallel samplers, which each look at one half the corpus and
		// combine
		// statistics after every iteration.
		model.setNumThreads(2);

		// Run the model for 50 iterations and stop (this is for testing only,
		// for real applications, use 1000 to 2000 iterations)
		model.setNumIterations(50);
		model.estimate();

		// Show the words and topics in the first instance

		// The data alphabet maps word IDs to strings
		Alphabet dataAlphabet = instances.getDataAlphabet();

		FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
		LabelSequence topics = model.getData().get(0).topicSequence;

		Formatter out = new Formatter(new StringBuilder(), Locale.US);
		for (int position = 0; position < tokens.getLength(); position++) {
			out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)),
					topics.getIndexAtPosition(position));
		}
		System.out.println(out);

		// Estimate the topic distribution of the first instance,
		// given the current Gibbs state.
		double[] topicDistribution = model.getTopicProbabilities(0);

		// Get an array of sorted sets of word ID/count pairs
		ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();

		// Show top 5 words in topics with proportions for the first document
		for (int topic = 0; topic < numTopics; topic++) {
			Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();

			out = new Formatter(new StringBuilder(), Locale.US);
			out.format("%d\t%.3f\t", topic, topicDistribution[topic]);
			int rank = 0;
			while (iterator.hasNext() && rank < 5) {
				IDSorter idCountPair = iterator.next();
				out.format("%s (%.0f) ", dataAlphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight());
				rank++;
			}
			System.out.println(out);
		}

		// Create a new instance with high probability of topic 0
		StringBuilder topicZeroText = new StringBuilder();
		Iterator<IDSorter> iterator = topicSortedWords.get(0).iterator();

		int rank = 0;
		while (iterator.hasNext() && rank < 5) {
			IDSorter idCountPair = iterator.next();
			topicZeroText.append(dataAlphabet.lookupObject(idCountPair.getID()) + " ");
			rank++;
		}

		// Create a new instance named "test instance" with empty target and
		// source fields.
		InstanceList testing = new InstanceList(instances.getPipe());
		testing.addThruPipe(new Instance(topicZeroText.toString(), null, "test instance", null));

		TopicInferencer inferencer = model.getInferencer();
		double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), 10, 1, 5);
		System.out.println("0\t" + testProbabilities[0]);
	}

	private class ImportExample {

		Pipe pipe;

		public ImportExample() {
			pipe = buildPipe();
		}

		public Pipe buildPipe() {
			ArrayList pipeList = new ArrayList();

			// Read data from File objects
			pipeList.add(new Input2CharSequence("UTF-8"));

			// Regular expression for what constitutes a token.
			// This pattern includes Unicode letters, Unicode numbers,
			// and the underscore character. Alternatives:
			// "\\S+" (anything not whitespace)
			// "\\w+" ( A-Z, a-z, 0-9, _ )
			// "[\\p{L}\\p{N}_]+|[\\p{P}]+" (a group of only letters and numbers
			// OR
			// a group of only punctuation marks)
			Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");

			// Tokenize raw strings
			pipeList.add(new CharSequence2TokenSequence(tokenPattern));

			// Normalize all tokens to all lowercase
			pipeList.add(new TokenSequenceLowercase());

			// Remove stopwords from a standard English stoplist.
			// options: [case sensitive] [mark deletions]
			pipeList.add(new TokenSequenceRemoveStopwords(false, false));

			// Rather than storing tokens as strings, convert
			// them to integers by looking them up in an alphabet.
			pipeList.add(new TokenSequence2FeatureSequence());

			// Do the same thing for the "target" field:
			// convert a class label string to a Label object,
			// which has an index in a Label alphabet.
			pipeList.add(new Target2Label());

			// Now convert the sequence of features to a sparse vector,
			// mapping feature IDs to counts.
			pipeList.add(new FeatureSequence2FeatureVector());

			// Print out the features and the label
			pipeList.add(new PrintInputAndTarget());

			return new SerialPipes(pipeList);
		}

		public InstanceList readDirectory(File directory) {
			return readDirectories(new File[] { directory });
		}

		public InstanceList readDirectories(File[] directories) {

			// Construct a file iterator, starting with the
			// specified directories, and recursing through subdirectories.
			// The second argument specifies a FileFilter to use to select
			// files within a directory.
			// The third argument is a Pattern that is applied to the
			// filename to produce a class label. In this case, I've
			// asked it to use the last directory name in the path.
			// FileIterator iterator = new FileIterator(directories, new
			// FileFilter(), FileIterator.LAST_DIRECTORY);

			// Construct a new instance list, passing it the pipe
			// we want to use to process instances.
			InstanceList instances = new InstanceList(pipe);

			// Now process each instance provided by the iterator.
			// instances.addThruPipe(iterator);

			return instances;
		}

		// public static void main(String[] args) throws IOException {

		ImportExample importer = new ImportExample();
		// InstanceList instances = importer.readDirectory(new File(args[0]));

	}
}
