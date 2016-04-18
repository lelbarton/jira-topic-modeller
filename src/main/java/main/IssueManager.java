package main;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cc.mallet.types.Instance;

public class IssueManager implements Iterable<Issue> {

	protected Set<Issue> issues;
	protected MalletTopicModeller topicModelManager;

	public IssueManager() {
		this.issues = new HashSet<Issue>();
		this.topicModelManager = MalletTopicModeller.getInstance();
	}

	/*
	 * Adds the given issue to the collection of issues
	 * 
	 * @param issue The issue to be added to the collection
	 */
	public void addIssue(Issue issue) {
		issues.add(issue);
		Instance instance = topicModelManager.addInstance(issue);
		issue.setInstance(instance);
	}

	public void writeToCsv(String fileName) {
		TopicWriter writer = new TopicWriter(fileName);

		for (Issue issue : issues) {
			String topic = topicModelManager.getMainTopicString(issue.getInstance());
			writer.writeTopicModel(issue.getKey(), issue.getDisplayName(), issue.getSummary(), issue.getContent(),
					topic);
		}

		writer.closeTopicModelCsv();
	}

	public void trainTopicModel() {
		try {
			topicModelManager.trainTopicModel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("Could not train topic models");
			e.printStackTrace();
		}
	}

	public Iterator<Issue> iterator() {
		return this.issues.iterator();
	}

}
