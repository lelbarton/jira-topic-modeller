package main;

import java.io.IOException;

import retrieve.IIssueFetcher;
import retrieve.JIRAIssueFetcher;
import retrieve.JiraJSONParser;

public class Main {

	static String JIRAConnectURL = "https://connectopensource.atlassian.net/rest/api/latest/search?jql=project=CONN";

	public static void main(String[] args) throws IOException {
		IIssueFetcher fetcher = new JIRAIssueFetcher();
		String json = fetcher.httpGet(JIRAConnectURL);
		JiraJSONParser.parseJson(json);
		// System.out.println(json);
		System.out.println("done");
	}
}
