package main;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class AnnotatedIssue {

	private StanfordCoreNLP nlpPipe;
	private Properties props;

	private Annotation annotation;
	private int issueID;

	public AnnotatedIssue(int id, String input) {
		this.issueID = id;
		this.props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
		this.nlpPipe = new StanfordCoreNLP(props);
		this.annotation = nlpPipe.process(input);
		// System.out.println("created AnnotatedIssue");
	}

	public void printLemmas() {
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				String lemma = token.lemma();
				System.out.println(lemma);
			}
		}
		System.out.println("\n");
	}

	// Getters and Setters

	public int getID() {
		return this.issueID;
	}

}
