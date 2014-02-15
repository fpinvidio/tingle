package com.plip.system.communication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.plip.exceptions.system.AdministratorPanelConnectionException;

public class WebServiceManager implements Runnable {

	private String url;
	private List<NameValuePair> parameters;
	private JSONObject response;
	
	public WebServiceManager(List<NameValuePair> urlParameters) {
		super();
		this.parameters = urlParameters;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<NameValuePair> getParameters() {
		return parameters;
	}

	public void setParameters(List<NameValuePair> parameters) {
		this.parameters = parameters;
	}
	
	public JSONObject getResponse() {
		return response;
	}

	public void setResponse(JSONObject response) {
		this.response = response;
	}

	@Override
	public void run() {
		try {
			httpRequest(url);
		} catch (AdministratorPanelConnectionException e) {
			e.printStackTrace();
		}
	}
	
	public void httpRequest(String url)
			throws AdministratorPanelConnectionException {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost request = new HttpPost(url);
			request.setEntity(new UrlEncodedFormEntity(parameters));
			HttpResponse response = httpClient.execute(request);
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
			    result.append(line);
			}

			JSONObject o = new JSONObject(result.toString());
			this.response = o;
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
}
