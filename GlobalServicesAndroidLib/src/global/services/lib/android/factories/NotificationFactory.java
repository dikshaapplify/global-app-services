package global.services.lib.android.factories;

import global.services.lib.android.httpclient.RequestMethod;
import global.services.lib.android.httpclient.RestClient;
import global.services.lib.android.objects.Notification;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class NotificationFactory {
	private static final String NOTIFICATION_SERVLET = "http://global-app-services.appspot.com/globalservices/notificationservlet";
	private String userId_;
	private Long appId_;
	private RestClient noteRest;

	public NotificationFactory(String userId, Long appId) {
		userId_ = userId;
		appId_ = appId;
		noteRest = new RestClient(NOTIFICATION_SERVLET);
	}

	public String GetNotesXMLContent() {
		noteRest.ClearParams();
		noteRest.AddParam("userid", userId_);
		noteRest.AddParam("appid", String.valueOf(appId_));

		try {
			noteRest.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// textView.setText(e.getMessage());
		}
		String strResponse = noteRest.getResponse();
		return strResponse;
	}
	
	
	
	public List<Notification> GetNotifications() {
		List<Notification> noteList = new ArrayList<Notification>();
		String strElemName;
		String id;
		String userId;
		String appId;
		String tittle;
		String content;
		String fromDate;
		String toDate;

		String strResponse = GetNotesXMLContent();
		strResponse = strResponse.replace("\n", "");
		XmlPullParser notes;
		try {

			notes = XmlPullParserFactory.newInstance().newPullParser();
			notes.setInput(new StringReader(strResponse));
		} catch (XmlPullParserException e) {
			notes = null;
		}
		if (notes != null) {
			int eventType = -1;
			// boolean bFoundScores = false;

			// Find Score records from XML
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {

					// Get the name of the tag (eg scores or score)
					strElemName = notes.getName();

					if (strElemName.equals("note")) {
						// bFoundScores = true;
						Notification noteObj = new Notification();
						id = notes.getAttributeValue(null, "id");
						noteObj.setId(Long.parseLong(id));
						userId = notes.getAttributeValue(null, "useid");
						noteObj.setUserId(userId);

						appId = notes.getAttributeValue(null, "appid");
						noteObj.setAppId(Long.parseLong(appId));

						tittle = notes.getAttributeValue(null, "tittle");
						noteObj.setTitle(tittle);

						content = notes.getAttributeValue(null, "content");
						noteObj.setContent(content);

						
						fromDate = notes.getAttributeValue(null, "fromdate");
						noteObj.setFromDate(Long.parseLong(fromDate));

						toDate = notes.getAttributeValue(null, "todate");
						noteObj.setToDate(Long.parseLong(toDate));

						noteList.add(noteObj);

					}
				}
				try {
					eventType = notes.next();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		return noteList;

	}
}
