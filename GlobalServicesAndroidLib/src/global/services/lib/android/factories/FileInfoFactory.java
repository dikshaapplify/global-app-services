package global.services.lib.android.factories;

import global.services.lib.android.httpclient.RequestMethod;
import global.services.lib.android.httpclient.RestClient;
import global.services.lib.android.objects.FileInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class FileInfoFactory {
	private static final String FILEINFO_SERVLET = "http://global-app-services.appspot.com/globalservices/fileservlet";
	private static final String REQUEST_TYPE_GET_INFO = "getinfo";
	private String userId_;
	private RestClient fileRest;

	public FileInfoFactory(String userId) {
		userId_ = userId;
		fileRest = new RestClient(FILEINFO_SERVLET);

	}

	public String GetFilesXMLContent() {
		fileRest.ClearParams();

		fileRest.AddParam("requesttype", REQUEST_TYPE_GET_INFO);
		fileRest.AddParam("userid", userId_);

		try {
			fileRest.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// textView.setText(e.getMessage());
		}
		String strResponse = fileRest.getResponse();
		return strResponse;
	}

	public List<FileInfo> GetFileInfos(Long fileId) {
		List<FileInfo> fileList = new ArrayList<FileInfo>();

		String strElemName;
		String id;
		String userId;
		String fileName;
		String fileType;
		String fileSize;

		String strResponse = GetFilesXMLContent();
		strResponse = strResponse.replace("\n", "");
		XmlPullParser files;
		try {

			files = XmlPullParserFactory.newInstance().newPullParser();
			files.setInput(new StringReader(strResponse));
		} catch (XmlPullParserException e) {
			files = null;
		}
		if (files != null) {
			int eventType = -1;
			// boolean bFoundScores = false;

			// Find Score records from XML
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {

					// Get the name of the tag (eg scores or score)
					strElemName = files.getName();

					if (strElemName.equals("fileinfo")) {
						// bFoundScores = true;
						FileInfo fileInfoObj = new FileInfo();
						id = files.getAttributeValue(null, "id");
						fileInfoObj.setId(Long.parseLong(id));
						userId = files.getAttributeValue(null, "useid");
						fileInfoObj.setUserId(userId);

						fileName = files.getAttributeValue(null, "filename");
						fileInfoObj.setFileName(fileName);

						fileType = files.getAttributeValue(null, "filetype");
						fileInfoObj.setFileType(fileType);

						fileSize = files.getAttributeValue(null, "filesize");
						fileInfoObj.setFileSize(fileSize);
						fileList.add(fileInfoObj);

					}
				}
				try {
					eventType = files.next();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		return fileList;

	}

	public InputStream Download(Long fileId) {
		InputStream is = null;
		fileRest.ClearParams();
		fileRest.AddParam("requesttype", "download");
		fileRest.AddParam("userid", userId_);
		fileRest.AddParam("fileid", String.valueOf(fileId));
		File file = null;
		try {
			fileRest.Execute(RequestMethod.POST);
			is = fileRest.getInstream();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return is;
		
	}
}
