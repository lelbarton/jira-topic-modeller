package main;

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
		// for (Issue issue : issues) {
		// System.out.println(issue.getAuthor());
		// }
	}
}
