package retrieve;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import main.AbstractIssuesManager;
import main.Issue;
import main.JiraIssue;
import main.JiraIssuesManager;

public class JiraJSONParser {

	public static AbstractIssuesManager parseJson(String input) {
		JSONTokener tokener = new JSONTokener(input);

		JSONObject obj = new JSONObject(tokener);
		JSONArray issues = (JSONArray) obj.get("issues");

		AbstractIssuesManager issuesManager = new JiraIssuesManager();

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
				String userName = creatorInfo.getString("name");

				// create new issue using the extracted data
				Issue newIssue = new JiraIssue(key, id, summary, desc, date, displayName, userName);

				// add new issue to issues collection
				issuesManager.addIssue(newIssue);

			} catch (JSONException jse) {
				// if any problems with parsing, skip this issue
			}
		}
		return issuesManager;
	}
}
