package com.plip.systemconfig;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import com.plip.imageprocessing.matchers.MinDistanceMatcher;
import com.plip.imageprocessing.processors.ObjectCounter;
import com.plip.imageprocessing.processors.TrayProcessor;
import com.plip.systemconfig.exceptions.AdministratorPanelConnectionException;
import com.plip.systemmonitor.MainSystemMonitor;

public class SystemUtils {
	
	public static String url;

	public SystemUtils(){
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
		
		url = new String(props.getProperty("plipAdministratorPanelUrl"));
	}
	
		// HTTP POST request
		public static void connectPlipAdministratiorPanel (String urlParameters) throws AdministratorPanelConnectionException {
			
			try{
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
	 
			//add request header
			con.setRequestMethod("POST");
			//con.setRequestProperty("User-Agent", USER_AGENT);
			//con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	 
			//urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
	 
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
	 
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println(response.toString());	
			}catch(IOException e){
				throw new AdministratorPanelConnectionException();
			}
		}
}
