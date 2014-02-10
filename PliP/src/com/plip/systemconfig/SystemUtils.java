package com.plip.systemconfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.plip.imageprocessing.matchers.MinDistanceMatcher;
import com.plip.imageprocessing.processors.ObjectCounter;
import com.plip.imageprocessing.processors.TrayProcessor;
import com.plip.persistence.managers.images.HashImageManager;
import com.plip.systemconfig.exceptions.AdministratorPanelConnectionException;
import com.plip.systemmonitor.MainSystemMonitor;

public class SystemUtils {

	public static String url;

	public SystemUtils() {
		super();
		loadParams();
	}

	public void loadParams() {
		Properties props = new Properties();
		InputStream is = null;
		try {
			File f = new File("./res/config.properties");
			is = new FileInputStream(f);
		} catch (Exception e) {
			is = null;
		}

		try {
			if (is == null) {
				is = getClass().getResourceAsStream("./res/config.properties");
			}

			props.load(is);
		} catch (Exception e) {
		}

		MainSystemMonitor.imageResolutionWidth = new Integer(
				props.getProperty("imageResolutionWidth"));
		MainSystemMonitor.imageResolutionHeight = new Integer(
				props.getProperty("imageResolutionHeight"));
		MainSystemMonitor.captureResolutionWidth = new Integer(
				props.getProperty("captureResolutionWidth"));
		MainSystemMonitor.captureResolutionHeight = new Integer(
				props.getProperty("captureResolutionHeight"));

		MainSystemMonitor.cameraInput = new Integer(
				props.getProperty("cameraInput"));

		ObjectCounter.minAreaThreshold = new Integer(
				props.getProperty("minAreaThreshold"));
		ObjectCounter.maxAreaThreshold = new Integer(
				props.getProperty("maxAreaThreshold"));

		MinDistanceMatcher.minDistanceThreshold = new Double(
				props.getProperty("minMatchingDistance"));

		TrayProcessor.thr1 = new Double(props.getProperty("minHueThreshold"));
		TrayProcessor.thr2 = new Double(props.getProperty("minSatThreshold"));
		TrayProcessor.thr3 = new Double(props.getProperty("minValueThreshold"));
		TrayProcessor.thr4 = new Double(props.getProperty("maxHueThreshold"));
		TrayProcessor.thr5 = new Double(props.getProperty("maxSatThreshold"));
		TrayProcessor.thr6 = new Double(props.getProperty("maxValueThreshold"));
		
		HashImageManager.imagesRootPath = new String(props.getProperty("imagesUploadPath"));

		url = new String(props.getProperty("plipAdministratorPanelUrl"));
	}

	// HTTP POST request
	public static void connectPlipAdministratiorPanel(
			List<NameValuePair> parameters)
			throws AdministratorPanelConnectionException {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost request = new HttpPost(url);
			request.setEntity(new UrlEncodedFormEntity(parameters));
			HttpResponse response = httpClient.execute(request);
		} catch (Exception ex) {

		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}

}
