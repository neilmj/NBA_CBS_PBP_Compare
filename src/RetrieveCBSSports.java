import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

//The goal with this class is to take in a CBSSports.com NBA Play-by-play JSON and return a CSV string list.
public class RetrieveCBSSports {
	
	//Taken from http://stackoverflow.com/questions/4328711/read-url-to-string-in-few-lines-of-java-code
	public static String getText(String url) throws Exception {
		URL website = new URL(url);
		URLConnection connection = website.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));

		StringBuilder response = new StringBuilder();
		String inputLine;

		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		in.close();
		return response.toString();
	}

	public List<String> retrieve(String input) {
		List<String> playByPlay = new ArrayList<String>();
		try {
			//Pull down Play-by-play page
			String content = RetrieveCBSSports.getText(input);
			//Isolate PBP HTML Table
			content = content
					.substring(content.indexOf("data condensed stack") - 14);
			content = content.substring(0, content.indexOf("</table>") + 8);
			int quarter = 0;
			//Iterate through the PBP rows and parse out the important bits of info
			for (String row : content.split("</tr>")) {
				//Use regex to wipe HTML syntax
				String[] rowClean = row.replaceAll("<a[^>]*>", "")
						.replaceAll("<[^>]*a>", "").replaceAll("<[^>]*>", ",")
						.split(",");
				//Set quarter each time we come across a row indicating the quarter
				if(row.contains("Qtr"))
				{
					quarter = Character.getNumericValue(row.substring(row.indexOf(" Qtr")-3,row.indexOf(" Qtr")-2).toCharArray()[0]);
				} 
				//Parse out the information and add it to the List
				else if (rowClean.length >= 9
						&& (rowClean[2].contains(".") || rowClean[2]
								.contains(":"))) {
					playByPlay.add(quarter + "," + rowClean[2] + ","
							+ rowClean[4].replaceAll("&nbsp;", " ") + ","
							+ rowClean[6] + "," + rowClean[8]);
				}
			}
		} catch (Exception e) {
			//Error
		}
		return playByPlay;
	}
}