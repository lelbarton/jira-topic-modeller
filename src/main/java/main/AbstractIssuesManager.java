package main;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractIssuesManager implements Iterable<Issue> {

	protected Set<Issue> issues;

	public AbstractIssuesManager() {
		this.issues = new HashSet<Issue>();
	}

	/*
	 * Adds the given issue to the collection of issues
	 * 
	 * @param issue The issue to be added to the collection
	 */
	public void addIssue(Issue issue) {
		issues.add(issue);
	}

	/*
	 * Removes the given issue from the collection of issues
	 * 
	 * @param issue the issue to be removed
	 * 
	 * @return returns true if the issue was present and has been removed
	 */
	public boolean removeIssue(Issue issue) {
		return issues.remove(issue);
	}

	public Iterator<Issue> iterator() {
		return this.issues.iterator();
	}
}
