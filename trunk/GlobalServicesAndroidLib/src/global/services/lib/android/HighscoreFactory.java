package global.services.lib.android;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class HighscoreFactory {

	private static final String HIGHSCORE_SERVLET = "http://global-score.appspot.com/highscore";
	private static final String REQUEST_TYPE_SUBMIT = "submitscore";
	private static final String REQUEST_TYPE_GET = "getscore";
	private String userId_;
	private Long appId_;
	private RestClient highScoreRest;
	public HighscoreFactory(String userId, Long appId) {
		userId_ = userId;
		appId_ = appId;
		highScoreRest = new RestClient(HIGHSCORE_SERVLET);
		
	}
	
	public void SubmitScore(Highscore score) {
		highScoreRest.ClearParams();
		highScoreRest.AddHeader("requesttype", REQUEST_TYPE_SUBMIT);
		highScoreRest.AddHeader("userid", score.getUserID());
		highScoreRest.AddHeader("appid", String.valueOf(score.getGameID()));
		highScoreRest.AddHeader("subboard", score.getSubBoard());
		highScoreRest.AddHeader("player", score.getPlayer());
		highScoreRest.AddHeader("score", String.valueOf(score.getHighScore()));
		highScoreRest.AddHeader("during", String.valueOf(score.getDuring()));
		highScoreRest.AddHeader("location", score.getLocation());
		highScoreRest.AddHeader("comment", score.getComment());
		highScoreRest.AddHeader("date", String.valueOf(score.getDate()));
		//highScoreRest.AddHeader("avatar", score.getAvatar()); Not support yet
		/*
		long now = System.currentTimeMillis();
		highScoreRest.AddHeader("Date", String.valueOf(now));
		*/

		try {
			highScoreRest.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// textView.setText(e.getMessage());
		}
	}

	public List<Highscore> GetScores() {
		List<Highscore> scoreList = new ArrayList<Highscore>();
		String strElemName; 
		String id;
		String userId;
		String appId;
		String level;
		String player;
		String score;
		String during;
		String comment;
		String location;
		//String avatar; Not support yet
		String date;

		highScoreRest.ClearParams();
		highScoreRest.AddHeader("requesttype", REQUEST_TYPE_GET);
		highScoreRest.AddHeader("userid", userId_);
		highScoreRest.AddHeader("appid", String.valueOf(appId_));

		try {
			highScoreRest.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// textView.setText(e.getMessage());
		}
		String strResponse = highScoreRest.getResponse();
		strResponse = strResponse.replace("\n", "");
		XmlPullParser scores;
		try {

			scores = XmlPullParserFactory.newInstance().newPullParser();
			scores.setInput(new StringReader(strResponse));
		} catch (XmlPullParserException e) {
			scores = null;
		}
		if (scores != null) {
			int eventType = -1;
			// boolean bFoundScores = false;

			// Find Score records from XML
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {

					// Get the name of the tag (eg scores or score)
					strElemName = scores.getName();

					if (strElemName.equals("score")) {
						// bFoundScores = true;
						Highscore scoreObj = new Highscore();
						id = scores.getAttributeValue(null, "id");
						scoreObj.setId(Long.parseLong(id));
						userId = scores.getAttributeValue(null, "useid");
						scoreObj.setUserID(userId);
						appId = scores.getAttributeValue(null, "appid");
						scoreObj.setGameID(Long.parseLong(appId));
						level = scores.getAttributeValue(null,
								"subboard");
						scoreObj.setSubBoard(level);
						player = scores
								.getAttributeValue(null, "player");
						scoreObj.setPlayer(player);
						score = scores.getAttributeValue(null, "score");
						scoreObj.setHighScore(Integer.parseInt(score));
						during = scores
								.getAttributeValue(null, "during");
						scoreObj.setDuring(Long.parseLong(during));
						comment = scores.getAttributeValue(null,
								"comment");
						scoreObj.setComment(comment);
						location = scores.getAttributeValue(null,
								"location");
						scoreObj.setLocation(location);
						//avatar = scores.getAttributeValue(null, "avatar"); Not support yet
						//scoreObj.setAvatar(avatar);Not support yet
						date = scores.getAttributeValue(null, "date");
						scoreObj.setDate(Long.parseLong(date));
						
						scoreList.add(scoreObj);
						
					}
				}
				try {
					eventType = scores.next();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return scoreList;
	}

}