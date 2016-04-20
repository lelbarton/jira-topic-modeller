package retrieve;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpIssueFetcher {

	public HttpIssueFetcher() {
		// constructor
	}

	/**
	 * From http://rest.elkstein.org/2008/02/using-rest-in-java.html
	 * 
	 * @param urlStr
	 *            is the URL to query for issues
	 * @return JSON or XML string response from given URL
	 * @throws IOException
	 */

	public String httpGet(String urlStr) throws IOException {
		URL url = new URL(urlStr);
		System.out.println("Fetching issues...");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		if (connection.getResponseCode() != 200) {
			throw new IOException(connection.getResponseMessage());
		}

		// Buffer the result into a string
		BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();

		connection.disconnect();
		return sb.toString();
	}

}
