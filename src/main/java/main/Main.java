package main;

import java.util.Iterator;

import retrieve.IIssueFetcher;
import retrieve.JIRAIssueFetcher;
import retrieve.JiraJSONParser;

public class Main {

	static String JIRAConnectURL = "https://connectopensource.atlassian.net/rest/api/latest/search?jql=project=CONN";

	// static MALLETInstanceManager instanceManager = new
	// MALLETInstanceManager();

	public static void main(String[] args) throws Exception {
		IIssueFetcher fetcher = new JIRAIssueFetcher();
		String json = fetcher.httpGet(JIRAConnectURL);
		AbstractIssuesManager issuesManager = JiraJSONParser.parseJson(json);
		issuesManager.printTopics();

		Iterator<Issue> iter = issuesManager.iterator();
		int oneOnly = 0;
		while (iter.hasNext()) {
			Issue issue = iter.next();
			oneOnly++;
			if (oneOnly == 1) {
				issue.getTopics();

			}
		}
		// TopicWriter writer = new
		// TopicWriter("/Users/Laura/Desktop/topicTestFile.csv");

	}
}
