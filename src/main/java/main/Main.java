package main;

import retrieve.HttpIssueFetcher;
import retrieve.JiraJSONParser;

public class Main {

	// URL to an issue tracking system that returns JSON or XML, e.g. JIRA
	// NB: current maxResults setting gets ALL issues available, which can take
	// some time to retrieve & process
	static final String ISSUES_URL = "https://connectopensource.atlassian.net/rest/api/latest/search?jql=project=CONN&maxResults=-1";
	// where to save csv results
	static final String OUTFILE = "/Users/Laura/Desktop/connTopicModel.csv";

	public static void main(String[] args) throws Exception {
		HttpIssueFetcher fetcher = new HttpIssueFetcher();
		String json = fetcher.httpGet(ISSUES_URL);

		IssueManager issuesManager = new IssueManager();
		JiraJSONParser.parse(json, issuesManager);
		issuesManager.trainTopicModel();
		issuesManager.writeToCsv(OUTFILE);
	}
}
