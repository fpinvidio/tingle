package com.plip.system.providers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.plip.exceptions.persistence.NoPageRecievedException;
import com.plip.exceptions.persistence.NullModelAttributesException;
import com.plip.exceptions.persistence.PageNotFoundException;
import com.plip.persistence.daos.impls.OrderDaoImpl;
import com.plip.persistence.daos.impls.PageProductDaoImpl;
import com.plip.persistence.daos.impls.ProductDaoImpl;
import com.plip.persistence.daos.interfaces.OrderDao;
import com.plip.persistence.daos.interfaces.PageProductDao;
import com.plip.persistence.daos.interfaces.ProductDao;
import com.plip.persistence.model.Order;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.PageProduct;
import com.plip.persistence.model.Product;
import com.plip.system.communication.WebServiceManager;
import com.plip.system.config.SystemUtils;

public class RemotePageProvider implements PageProvider{

	@Override
	public Page getLastPage() throws NoPageRecievedException,
			PageNotFoundException {
		
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("date", String.valueOf(new Date())));
			
		WebServiceManager wsManager = new WebServiceManager(urlParameters); 
		wsManager.setUrl(new SystemUtils().getParam("DusaServer"));
		Thread myThread = new Thread(wsManager);
		myThread.start(); 
		
		JSONObject response = wsManager.getResponse();
		JSONObject order = null;
		JSONArray pageProducts = null;
		try {
			order = response.getJSONObject("order");
			pageProducts = response.getJSONArray("page_products");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		addOrder(order);
		
		return null;
	}
	
	public void addOrder(JSONObject jsonOrder){
		OrderDao orderDao = new OrderDaoImpl();
		Order order = new Order();
		try {
			order.setCode(jsonOrder.getString("code"));
			order.setClient(jsonOrder.getString("client"));
			order.setInsertDate(new Date());
			orderDao.addOrder(order);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (NullModelAttributesException e) {
			e.printStackTrace();
		}
	}
	
	public void addProducts(JSONArray jsonPageProducts){
		ProductDao productDao =  new ProductDaoImpl();
		Product product = new Product();
		for (int i = 0 ; i < jsonPageProducts.length() ; i ++){
			JSONObject jsonObject;
			try {
				jsonObject = jsonPageProducts.getJSONObject(i);
				String name = jsonObject.getString("name");
				String code = jsonObject.getString("code");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Busco si esta el product ( por nombre y codigo) y si no esta lo agrego a la base.
			//Seteo order
			//1 seteo product
			//seteo page product
			//seteo page y devuelvo page.
		}
	
	}

}
