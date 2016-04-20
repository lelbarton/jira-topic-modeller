package retrieve;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import main.Issue;
import main.IssueManager;
import main.JiraIssue;

public class JiraJSONParser {

	public static void parse(String input, IssueManager issuesManager) {
		System.out.println("Parsing response...");
		JSONTokener tokener = new JSONTokener(input);

		JSONObject obj = new JSONObject(tokener);
		JSONArray issues = (JSONArray) obj.get("issues");

		// issues occasionally throw JSON exceptions and are skipped
		int skippedIssues = 0;

		for (int i = 0; i < issues.length(); i++) {
			JSONObject issue = issues.getJSONObject(i);
			try {
				// get issue key and id number
				String key = issue.getString("key");
				String idString = issue.getString("id");
				int id = Integer.parseInt(idString);

				JSONObject fields = issue.getJSONObject("fields");

				// get date, summary, and main text
				String summary = fields.getString("summary");
				String desc = fields.getString("description");
				String date = fields.getString("created");

				// get author info
				JSONObject creatorInfo = fields.getJSONObject("creator");
				String displayName = creatorInfo.getString("displayName");

				// create new issue using the extracted data and add to manager
				Issue jiraIssue = new JiraIssue(key, id, summary, desc, date, displayName);
				issuesManager.addIssue(jiraIssue);

			} catch (JSONException jse) {
				// if any problems with parsing, skip the issue
				skippedIssues++;
			}
		}
		System.out.println("Number of issues skipped due to JSON exceptions: " + skippedIssues);
	}
}
