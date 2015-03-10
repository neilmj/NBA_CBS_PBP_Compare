import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//The goal with this class is to take in an NBA.com/Stats Play-by-play JSON and return a CSV string list.
public class RetrieveNBA {

	//Taken from http://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	//Taken from http://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
	public static JSONObject readJsonFromUrl(String url) throws IOException,
			JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	public List<String> retrieve(String input) throws IOException, JSONException {
		//Pull down the JSON and dive into the rowSet child.
		JSONObject json = readJsonFromUrl(input);
		JSONArray results = (JSONArray) json.get("resultSets");
		JSONArray playByPlay = (JSONArray) results.getJSONObject(0).get("rowSet");
		
		//Parse the JSON by row, put it into a List, and return
		List<String> pbpList = new ArrayList<String>();
		pbpList.addAll(Arrays.asList(playByPlay.toString().split("]")));
		return pbpList;
	}
}