import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Compare {
	public static void main(String[] args) throws Exception {
		RetrieveNBA nba = new RetrieveNBA();
		RetrieveCBSSports cbs = new RetrieveCBSSports();
		//The two input links
		String cbsLink = "http://www.cbssports.com/nba/gametracker/playbyplay/NBA_20150117_ATL@CHI";
		String nbaLink = "http://stats.nba.com/stats/playbyplayv2?EndPeriod=10&EndRange=55800&GameID=0021400602&RangeType=2&Season=2014-15&SeasonType=Regular+Season&StartPeriod=1&StartRange=0";
		List<String> cbsList = cbs.retrieve(cbsLink), nbaList = nba.retrieve(nbaLink), jumpShotsMatched = new ArrayList<String>(), reboundsMatched = new ArrayList<String>(), layupMatched = new ArrayList<String>(), dunkMatched = new ArrayList<String>(), jumpShotsUnmatched = new ArrayList<String>(), reboundsUnmatched = new ArrayList<String>(), layupUnmatched = new ArrayList<String>(), dunkUnmatched = new ArrayList<String>();
		int nbaListCount = 1, JumpShotFound = 0, ReboundFound = 0, LayupFound = 0, DunkFound = 0, JumpShotNotFound = 0, ReboundNotFound = 0, LayupNotFound = 0, DunkNotFound = 0;
		double JumpShotSumOffset = 0.0, ReboundSumOffset = 0.0, LayupSumOffset = 0.0, DunkSumOffset = 0.0;

		while (nbaListCount < nbaList.size()) {
			String nbaListPlay = nbaList.remove(nbaListCount);
			String[] nbaCol = nbaListPlay.split(",");
			String nbaName = nbaCol[15].replace("\"", "");
			String nbaEvent = nbaCol[8] + nbaCol[9] + nbaCol[10];
			if (nbaListPlay.contains("REBOUND")) {
				boolean found = false;
				for (int i = cbsList.size() - 1; i > 0; i--) {
					String cbsLine = cbsList.get(i);
					if (cbsLine.contains(nbaName)
							&& cbsLine.contains("Rebound")
							&& cbsLine.contains(nbaCol[5] + ",")) {
						String[] cbsCol = cbsLine.split(",");
						cbsList.remove(i);
						i = 0;	//Stop the for loop if found
						found = true;
						DateFormat formatter = new SimpleDateFormat("mm:ss");
						Date nbaTime = formatter.parse(nbaCol[7].replace("\"",""));
						if (cbsCol[1].contains(".")) {
							formatter = new SimpleDateFormat("ss.SS");
						}
						Date cbsTime = formatter.parse(cbsCol[1]);
						int timeDiff = (int) Math
								.abs((nbaTime.getTime() - cbsTime.getTime()) / 1000);
						if (timeDiff > 20) {
							ReboundNotFound++;
							reboundsUnmatched.add(nbaListPlay);
						} else {
							ReboundSumOffset += timeDiff;
							reboundsMatched.add("<tr><td>" + nbaCol[5] + "</td><td>" + nbaCol[7].replace("\"", "") + "</td><td>" + cbsCol[1] + "</td><td>" + timeDiff + "</td><td>" + nbaEvent.replace("null", "").replace("\"", "") + "</td><td>" + cbsCol[4] + "</td></tr>");
							ReboundFound++;
						}
					}
				}
				if (!found) {
					ReboundNotFound++;
					reboundsUnmatched.add(nbaListPlay);
				}
			} else if (nbaListPlay.contains("Jump Shot")) {
				boolean found = false;
				for (int i = cbsList.size() - 1; i > 0; i--) {

					String cbsLine = cbsList.get(i);
					String[] cbsCol = cbsLine.split(",");
					if (cbsLine.contains(nbaName)
							&& cbsLine.contains("Jump Shot")
							&& cbsLine.contains(nbaCol[5] + ",")) {
						cbsList.remove(i);
						i = 0;	//Stop the for loop if found
						found = true;
						DateFormat formatter = new SimpleDateFormat("mm:ss");
						Date nbaTime = formatter.parse(nbaCol[7].replace("\"",""));
						if (cbsCol[1].contains(".")) {
							formatter = new SimpleDateFormat("ss.SS");
						}
						Date cbsTime = formatter.parse(cbsCol[1]);
						int timeDiff = (int) Math
								.abs((nbaTime.getTime() - cbsTime.getTime()) / 1000);
						if (timeDiff > 20) {
							JumpShotNotFound++;
							jumpShotsUnmatched.add(nbaListPlay);
						} else {
							JumpShotSumOffset += timeDiff;
							jumpShotsMatched.add("<tr><td>" + nbaCol[5] + "</td><td>" + nbaCol[7].replace("\"", "") + "</td><td>" + cbsCol[1] + "</td><td>" + timeDiff + "</td><td>" + nbaEvent.replace("null", "").replace("\"", "") + "</td><td>" + cbsCol[4] + "</td></tr>");
							JumpShotFound++;
						}
					}
				}
				if (!found) {
					jumpShotsUnmatched.add(nbaListPlay);
					JumpShotNotFound++;
				}
			} else if (nbaListPlay.contains("Layup")) {
				boolean found = false;
				for (int i = cbsList.size() - 1; i > 0; i--) {
					String cbsLine = cbsList.get(i);
					if (cbsLine.contains(nbaName) && cbsLine.contains("Layup")
							&& cbsLine.contains(nbaCol[5] + ",")) {
						String[] cbsCol = cbsLine.split(",");
						cbsList.remove(i);
						i = 0;	//Stop the for loop if found
						found = true;
						DateFormat formatter = new SimpleDateFormat("mm:ss");
						Date nbaTime = formatter.parse(nbaCol[7].replace("\"",""));
						if (cbsCol[1].contains(".")) {
							formatter = new SimpleDateFormat("ss.SS");
						}
						Date cbsTime = formatter.parse(cbsCol[1]);
						int timeDiff = (int) Math
								.abs((nbaTime.getTime() - cbsTime.getTime()) / 1000);
						if (timeDiff > 20) {
							LayupNotFound++;
							layupUnmatched.add(nbaListPlay);
						} else {
							LayupSumOffset += timeDiff;
							layupMatched.add("<tr><td>"
									+ nbaCol[5]
									+ "</td><td>"
									+ nbaCol[7].replace("\"", "")
									+ "</td><td>"
									+ cbsCol[1]
									+ "</td><td>"
									+ timeDiff
									+ "</td><td>"
									+ nbaEvent.replace("null", "").replace(
											"\"", "") + "</td><td>" + cbsCol[4]
									+ "</td></tr>");
							LayupFound++;
						}
					}
				}
				if (!found) {
					layupUnmatched.add(nbaListPlay);
				}
			} else if (nbaListPlay.contains("Dunk")) {
				boolean found = false;
				for (int i = cbsList.size() - 1; i > 0; i--) {
					String cbsLine = cbsList.get(i);
					if (cbsLine.contains(nbaName) && cbsLine.contains("Dunk")
							&& cbsLine.contains(nbaCol[5] + ",")) {
						String[] cbsCol = cbsLine.split(",");
						cbsList.remove(i);
						i = 0;
						found = true;
						DateFormat formatter = new SimpleDateFormat("mm:ss");
						Date nbaTime = formatter.parse(nbaCol[7].replace("\"",
								""));
						if (cbsCol[1].contains(".")) {
							formatter = new SimpleDateFormat("ss.SS");
						}
						Date cbsTime = formatter.parse(cbsCol[1]);
						int timeDiff = (int) Math
								.abs((nbaTime.getTime() - cbsTime.getTime()) / 1000);
						if (timeDiff > 20) {
							DunkNotFound++;
							dunkUnmatched.add(nbaListPlay);
						} else {
							DunkSumOffset += timeDiff;
							dunkMatched.add("<tr><td>"
									+ nbaCol[5]
									+ "</td><td>"
									+ nbaCol[7].replace("\"", "")
									+ "</td><td>"
									+ cbsCol[1]
									+ "</td><td>"
									+ timeDiff
									+ "</td><td>"
									+ nbaEvent.replace("null", "").replace(
											"\"", "") + "</td><td>" + cbsCol[4]
									+ "</td></tr>");
							DunkFound++;
						}
					}
				}
				if (!found) {
					dunkUnmatched.add(nbaListPlay);
				}
			}
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date curDate = new Date();
		try {
			FileWriter fWriter = new FileWriter("compareResults_"
					+ dateFormat.format(curDate) + ".html");
			BufferedWriter writer = new BufferedWriter(fWriter);
			writer.write("<html>");
			writer.write("<head>");
			writer.write("<title>Neil Johnson - Comparison bewteen NBA.com and CBSSports.com Play-by-Play data</title>");
			writer.write("<script src=\"http://www.kryogenix.org/code/browser/sorttable/sorttable.js\"></script>");
			writer.write("<link rel=\"stylesheet\" href=\"http://yui.yahooapis.com/pure/0.6.0/pure-min.css\">");
			writer.write("<style>table.sortable thead { background-color:#eee;} table.sortable td {padding-left: 10px;  padding-right: 10px;}</style>");
			writer.write("</head>");
			writer.write("<body>");
			writer.write("<h1><a name=\"Top\">Game Play-by-play Comparison between NBA.com/Stats and CBSSports.com</a></h1>");
			writer.write("<ul>");
			writer.write("<li><a href=\"#JumpShots\">Jump Shots</a></li>");
			writer.write("<li><a href=\"#Rebounds\">Rebounds</a></li>");
			writer.write("<li><a href=\"#Layups\">Layups</a></li>");
			writer.write("<li><a href=\"#Dunks\">Dunks</a></li>");
			writer.write("</ul>");
			writer.write("<a href=\"" + nbaLink
					+ "\" target=\"_new\">NBA Play-by-play Source</a><br>");
			writer.write("<a href=\"" + cbsLink
					+ "\" target=\"_new\">CBS Play-by-play Source</a><p>");
			writer.write("<table><tr><td>Average Jump Shot Offset: "
					+ (JumpShotSumOffset / (double) JumpShotFound) + "</td><td>");
			writer.write("Percent of Jump Shots matched: "
					+ ((double) JumpShotFound / ((double) JumpShotFound + (double) JumpShotNotFound))
					+ "</td></tr>");
			writer.write("<tr><td>Average Rebound Offset: "
					+ (ReboundSumOffset / (double) ReboundFound) + "</td><td>");
			writer.write("Percent of Rebound matched: "
					+ ((double) ReboundFound / ((double) ReboundFound + (double) ReboundNotFound))
					+ "</td></tr>");
			writer.write("<tr><td>Average Layup Offset: "
					+ (LayupSumOffset / (double) LayupFound) + "</td><td>");
			writer.write("Percent of Layup matched: "
					+ ((double) LayupFound / ((double) LayupFound + (double) LayupNotFound))
					+ "</td></tr>");
			writer.write("<tr><td>Average Dunk Offset: "
					+ (DunkSumOffset / (double) DunkFound) + "</td><td>");
			writer.write("Percent of Dunk matched: "
					+ ((double) DunkFound / ((double) DunkFound + (double) DunkNotFound))
					+ "</td></tr></table>");
			writer.write("<p><a href=\"#Top\"><font size=1>Back to top.</font></a> <font size=5><a name=\"JumpShots\">Jump Shots:</a></font><p>");
			;
			writer.write("<table class=\"sortable\"><thead><tr><td>Qtr</td><td>NBA Clock</td><td>CBS Clock</td><td>Abs. Diff.</td><td>NBA Play-by-play Line</td><td>CBS Play-by-play Line</td></tr></thead>");
			for (String line : jumpShotsMatched) {
				writer.write(line);
			}
			writer.write("</table><p>");
			writer.write("<a href=\"#Top\"><font size=1>Back to top.</font></a> <font size=5><a name=\"Rebounds\">Rebounds:</font><p>");
			writer.write("<table class=\"sortable\"><thead><tr><td>Qtr</td><td>NBA Clock</td><td>CBS Clock</td><td>Abs. Diff.</td><td>NBA Play-by-play Line</td><td>CBS Play-by-play Line</td></tr></thead>");
			for (String line : reboundsMatched) {
				writer.write(line);
			}
			writer.write("</table><p>");
			writer.write("<a href=\"#Top\"><font size=1>Back to top.</font></a> <font size=5><a name=\"Layups\">Layups:</font><p>");
			writer.write("<table class=\"sortable\"><thead><tr><td>Qtr</td><td>NBA Clock</td><td>CBS Clock</td><td>Abs. Diff.</td><td>NBA Play-by-play Line</td><td>CBS Play-by-play Line</td></tr></thead>");
			for (String line : layupMatched) {
				writer.write(line);
			}
			writer.write("</table><p>");
			writer.write("<a href=\"#Top\"><font size=1>Back to top.</font></a> <font size=5><a name=\"Dunks\">Dunks:</font><p>");
			writer.write("<table class=\"sortable\"><thead><tr><td>Qtr</td><td>NBA Clock</td><td>CBS Clock</td><td>Abs. Diff.</td><td>NBA Play-by-play Line</td><td>CBS Play-by-play Line</td></tr></thead>");
			for (String line : dunkMatched) {
				writer.write(line);
			}
			writer.write("</table><p>");
			writer.write("</body>");
			writer.write("</html>");
			writer.close();
		} catch (Exception e) {
			// catch any exceptions here
		}
	}

}
