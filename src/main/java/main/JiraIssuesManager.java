package main;

public class JiraIssuesManager extends AbstractIssuesManager {

	public JiraIssuesManager() {
		super();

	}

	@Override
	public void addIssue(Issue issue) {
		issues.add(issue);
	}

}
