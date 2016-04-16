package main;

import retrieve.IIssueFetcher;
import retrieve.JIRAIssueFetcher;
import retrieve.JiraJSONParser;

public class Main {

	// URL to an issue tracking system that returns JSON or XML, e.g. JIRA
	static final String ISSUES_URL = "https://connectopensource.atlassian.net/rest/api/latest/search?jql=project=CONN";
	static final String OUTFILE = "/Users/Laura/Desktop/topicModelTest2.csv";

	public static void main(String[] args) throws Exception {
		IIssueFetcher fetcher = new JIRAIssueFetcher();
		String json = fetcher.httpGet(ISSUES_URL);
		IssuesManager issuesManager = JiraJSONParser.parseJson(json);
		issuesManager.trainTopicModel();
		issuesManager.writeToCsv(OUTFILE);
	}
}
