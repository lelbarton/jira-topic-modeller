package retrieve;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JiraJSONParser {

	public static void parseJson(String input) {
		JSONTokener tokener = new JSONTokener(input);

		JSONObject obj = new JSONObject(tokener);

		// JSONObject response = (JSONObject) obj.get("response");
		JSONArray issues = (JSONArray) obj.get("issues");
		System.out.println(issues.getJSONObject(0));
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
