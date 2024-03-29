package global.services.client;

import global.services.client.rpc.AdvertisementService;
import global.services.client.rpc.AdvertisementServiceAsync;
import global.services.shared.Advertisement;
import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;
import gwtupload.client.SingleUploader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CreateAdvertisement {
	private TextBox txtAppId= new TextBox();
	private TextBox txtTitle= new TextBox();
	private TextBox txtContent= new TextBox();
	private TextBox txtType= new TextBox();
	private TextBox txtStoreUrl= new TextBox();
	private String userId_ = null;
	private String iconFileId = null;
	private FlowPanel panelImages = new FlowPanel();
	private Advertisement advObj = null;
	
	private AdvertisementServiceAsync advSvc = GWT.create(AdvertisementService.class);

	private Button btnAddAdv = new Button("Create advertisement", new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			String appID = txtAppId.getText();

			if ((appID != null)&&(!appID.equals(""))) {
				// formUpload.submit();
				AsyncCallback<Long> callback = new AsyncCallback<Long>() {
					public void onFailure(Throwable caught) {
						// TODO: Do something with errors.
					}

					public void onSuccess(Long result) {
					}
				};
				if (advObj == null) {
					advObj = new Advertisement (appID);
					
					advObj.setUserId(userId_);
					advObj.setTittle(txtTitle.getText());
					advObj.setContent(txtContent.getText());
					advObj.setType(txtType.getText());
					advObj.setStoreUrl(txtStoreUrl.getText());
					advObj.setIconFile(Long.valueOf(iconFileId));
				
					advSvc.InsertAdv(advObj, callback);
				} else {
					advObj.setAppName(appID);
					advObj.setTittle(txtTitle.getText());
					advObj.setContent(txtContent.getText());
					advObj.setType(txtType.getText());
					advObj.setStoreUrl(txtStoreUrl.getText());
					advObj.setIconFile(Long.valueOf(iconFileId));
					
					advSvc.UpdateAdv(advObj, callback);
				}
				//GlobalServices.ComebackHome(true);
				History.newItem("root-advertisement");
			}
		}
	});
		
	public CreateAdvertisement(String userId) {
		userId_ = userId;
	}

	public CreateAdvertisement(String userId, Long appId) {
		userId_ = userId;
		advSvc.SelectAdv(userId, appId, new AsyncCallback<Advertisement>() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors.
			}

			public void onSuccess(Advertisement result) {
				advObj = result;
				btnAddAdv.setText("Update advertisement");
				iconFileId = String.valueOf(advObj.getIconFile());
				String fileUrl = "http://global-app-services.appspot.com/globalservices/fileservlet?fileid=";
				fileUrl += iconFileId;
				new PreloadedImage(fileUrl, showImage);
				txtAppId.setText(advObj.getAppName());
				txtContent.setText(advObj.getContent());
				txtStoreUrl.setText(advObj.getStoreUrl());
				txtTitle.setText(advObj.getTittle());
				txtType.setText(advObj.getType());
			}
		});
	}

	public Widget Initialize() {
		VerticalPanel mainContent = new VerticalPanel();
		mainContent.setStyleName("contentBackgroud");
		
		mainContent.add(new Label("Create new advertisement score"));
		SingleUploader iconUploader = new SingleUploader(FileInputType.LABEL);
		//iconUploader.add(new Hidden("userid", loginInfo.getEmailAddress()), 0);
		iconUploader.setServletPath(iconUploader.getServletPath() + "?userid=" + userId_);
		iconUploader.setAutoSubmit(true);
		mainContent.add(new Label("Icon app:"));
		// imgIcon = new Image();
		// mainContent.add(imgIcon);
		iconUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		mainContent.add(panelImages);
		mainContent.add(iconUploader);
		//formUpload = (FormPanel) FileUploader.getFileUploaderWidget();
		//mainContent.add(formUpload);

		mainContent.add(new Label("App Name:"));
		txtAppId = new TextBox();
		mainContent.add(txtAppId);

		mainContent.add(new Label("Title:"));
		txtTitle = new TextBox();
		mainContent.add(txtTitle);

		mainContent.add(new Label("Content:"));
		txtContent = new TextBox();
		mainContent.add(txtContent);

		mainContent.add(new Label("Type:"));
		txtType = new TextBox();
		mainContent.add(txtType);

		mainContent.add(new Label("Store Url"));
		txtStoreUrl = new TextBox();
		mainContent.add(txtStoreUrl);

		HorizontalPanel controlButton = new HorizontalPanel();
		controlButton.add(btnAddAdv);
		controlButton.add(new Button("Cancel", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//GlobalServices.ComebackHome(false);
				History.newItem("root-advertisement");
			}
		}));
		mainContent.add(controlButton);
		return mainContent;

	}

	// Load the image in the document and in the case of success attach it to
	// the viewer
	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
		public void onFinish(IUploader uploader) {
			// Window.alert("Upload da finish");
			if (uploader.getStatus() == Status.SUCCESS) {
				
				iconFileId = uploader.getServerResponse();
					
				/*
				UrlBuilder fileUrl = new UrlBuilder();
				fileUrl.setHost(GWT.getHostPageBaseURL());
				fileUrl.setPath("globalservices/download");
				fileUrl.setParameter("fileid", iconFileId);
				
				Window.alert("Image url: " + fileUrl.buildString());
				*/
				String fileUrl = "http://global-app-services.appspot.com/globalservices/fileservlet?fileid=";
				fileUrl += iconFileId;
				PreloadedImage image = new PreloadedImage(fileUrl, showImage);
				// The server sends useful information to the client by default
				UploadedInfo info = uploader.getServerInfo();
				System.out.println("File name " + info.name);
				System.out.println("File content-type " + info.ctype);
				System.out.println("File size " + info.size);

				// You can send any customized message and parse it
				System.out.println("Server message " + info.message);
			}
		}
	};

	// Attach an image to the pictures viewer
	private OnLoadPreloadedImageHandler showImage = new OnLoadPreloadedImageHandler() {
		public void onLoad(PreloadedImage image) {
			image.setWidth("75px");
			panelImages.clear();
			panelImages.add(image);
		}
	};

}
