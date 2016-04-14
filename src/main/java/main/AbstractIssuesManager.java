package main;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cc.mallet.types.Instance;

public abstract class AbstractIssuesManager implements Iterable<Issue> {

	protected Set<Issue> issues;
	private MalletInstanceManager topicModelManager;

	public AbstractIssuesManager() {
		this.issues = new HashSet<Issue>();
		this.topicModelManager = MalletInstanceManager.getInstance();

	}

	/*
	 * Adds the given issue to the collection of issues
	 * 
	 * @param issue The issue to be added to the collection
	 */
	public void addIssue(Issue issue) {
		issues.add(issue);
		Instance instance = topicModelManager.addInstance(issue);
		issue.setTopics(instance);
	}

	public Iterator<Issue> iterator() {
		return this.issues.iterator();
	}

	public void printTopics() throws Exception {
		// TODO Auto-generated method stub
		topicModelManager.analyzeTopics();
	}
}
