package main;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractIssuesManager implements Iterable<Issue> {

	protected Set<Issue> issues;
	private MALLETInstanceManager topicModelManager;

	public AbstractIssuesManager() {
		this.issues = new HashSet<Issue>();
		this.topicModelManager = MALLETInstanceManager.getInstance();

	}

	/*
	 * Adds the given issue to the collection of issues
	 * 
	 * @param issue The issue to be added to the collection
	 */
	public void addIssue(Issue issue) {
		issues.add(issue);
		topicModelManager.addInstance(issue);
	}

	public Iterator<Issue> iterator() {
		return this.issues.iterator();
	}

	public void printTopics() throws Exception {
		// TODO Auto-generated method stub
		topicModelManager.sampleAnalyze();
	}
}
