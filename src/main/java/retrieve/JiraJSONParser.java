package retrieve;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import main.Issue;
import main.JiraIssue;

public class JiraJSONParser {

	public static void parseJson(String input) {
		JSONTokener tokener = new JSONTokener(input);

		JSONObject obj = new JSONObject(tokener);

		// JSONObject response = (JSONObject) obj.get("response");
		JSONArray issues = (JSONArray) obj.get("issues");

		for (int i = 0; i < issues.length(); i++) {
			JSONObject issue = issues.getJSONObject(i);
			try {
				// get issue key and id number
				String key = issue.getString("key");
				String idString = issue.getString("id");
				int id = Integer.parseInt(idString);

				// get date, summary, and main text
				JSONObject fields = issue.getJSONObject("fields");
				String summary = fields.getString("summary");

				String desc = fields.getString("description");

				String date = fields.getString("created");

				// get author info
				JSONObject creatorInfo = fields.getJSONObject("creator");
				String displayName = creatorInfo.getString("displayName");
				String userName = creatorInfo.getString("name");

				// create new issue using the extracted data
				Issue newIssue = new JiraIssue(key, id, summary, desc, date, displayName, userName);
				// System.out.println(key);

				// TODO: put issues into some sort of collection

			} catch (JSONException jse) {
				System.out.println(jse.getStackTrace());
			}
		}

		// JSONObject sample = issues.getJSONObject(0);
		// System.out.println(sample.getJSONObject("fields"));
		// System.out.println(issues.getJSONObject(0));
	}
}
//
// for (int i = 0; i < venues.length(); i++) {
// JSONObject currentVenue = venues.getJSONObject(i);
// String name = currentVenue.getString("name");
//
// JSONObject location = currentVenue.getJSONObject("location");
// double lat = location.getDouble("lat");
// double lng = location.getDouble("lng");
//
// EatingPlace newEatingPlace = new EatingPlace(name, (new LatLon(lat,
// lng)));
// PlaceFactory.getInstance().add(newEatingPlace);
// }
//
//
// } catch (JSONException jse) {
// System.out.println(jse.getStackTrace());
// }
// }
