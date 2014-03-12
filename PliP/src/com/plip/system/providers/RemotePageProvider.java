package com.plip.system.providers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.plip.exceptions.persistence.NoPageRecievedException;
import com.plip.exceptions.persistence.NullModelAttributesException;
import com.plip.exceptions.persistence.PageNotFoundException;
import com.plip.exceptions.persistence.ProductNotFoundException;
import com.plip.persistence.daos.impls.OrderDaoImpl;
import com.plip.persistence.daos.impls.PageDaoImpl;
import com.plip.persistence.daos.impls.PageProductDaoImpl;
import com.plip.persistence.daos.impls.ProductDaoImpl;
import com.plip.persistence.daos.interfaces.OrderDao;
import com.plip.persistence.daos.interfaces.PageDao;
import com.plip.persistence.daos.interfaces.PageProductDao;
import com.plip.persistence.daos.interfaces.ProductDao;
import com.plip.persistence.model.Order;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.PageProduct;
import com.plip.persistence.model.Product;
import com.plip.system.communication.RemoteServerConnector;
import com.plip.system.config.SystemUtils;

public class RemotePageProvider implements PageProvider{

	@Override
	public Page getLastPage() throws NoPageRecievedException,
			PageNotFoundException {
		
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("date", String.valueOf(new Date())));
			
		RemoteServerConnector rServerConnector = new RemoteServerConnector();
	
		JSONObject response = rServerConnector.connectRemoteServer(new SystemUtils().getParam("DusaServer"));
		
		PageDao pageDao = new PageDaoImpl();
		PageProductDao pageProductDao = new PageProductDaoImpl();
		JSONObject order = null;
		JSONArray pageProducts = null;
		int pageNumber = 0;
		int productQuantity = 0;
		try {
			order = response.getJSONObject("order");
			pageProducts = response.getJSONArray("page_products");
			productQuantity = response.getInt("product_quantity");
			pageNumber = response.getInt("page_number");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		pageDao.deleteLastPage();
		Page page = addPageWithProducts(pageProducts);
		Order orderModel = addOrder(order);
		page.setOrder(orderModel);
		page.setProductQuantity(productQuantity);
		page.setPageNumber(pageNumber);
		try{
			pageDao.addPage(page);	
			Set<PageProduct> pageProductsModels = page.getPageProducts();
			for(PageProduct pageProduct : pageProductsModels){
				pageProductDao.addPageProduct(pageProduct);
			}
			}catch(NullModelAttributesException e){
			e.printStackTrace();	
			}
		return page;
	}
	
	public Order addOrder(JSONObject jsonOrder){
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
		return order;
	}
	
	public Page addPageWithProducts(JSONArray jsonPageProducts){
		ProductDao productDao =  new ProductDaoImpl();
		Product product = new Product();
		
		int quantity = 0;
	
		Page page = new Page();
		Set<PageProduct> pageProductsSet = new HashSet<PageProduct>();
//		try{
//		pageDao.addPage(page);	
//		}catch(NullModelAttributesException e){
//		e.printStackTrace();	
//		}
		for (int i = 0 ; i < jsonPageProducts.length() ; i ++){
			JSONObject jsonPageProduct;
			try {
				jsonPageProduct = jsonPageProducts.getJSONObject(i);
				quantity = jsonPageProduct.getInt("quantity");
				JSONObject jsonProduct = jsonPageProduct.getJSONObject("product");
				String name = jsonProduct.getString("name");
				int code = jsonProduct.getInt("code");
				String laboratory = jsonProduct.getString("laboratory");
				try{
				product  = productDao.getProductByNameAndCode(name, code);
				
				}catch(ProductNotFoundException e){
					product.setCode(code);
					product.setName(name);
					product.setDescription(name);
					product.setEnabled(true);
					product.setLaboratory(laboratory);
					try {
						productDao.addProduct(product);
					} catch (NullModelAttributesException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PageProduct pageProduct = new PageProduct();
			pageProduct.setProduct(product);
			pageProduct.setQuantity(quantity);
			pageProduct.setPage(page);  
			pageProductsSet.add(pageProduct);	
		}
		page.setPageProducts(pageProductsSet);
		return page;
	
	}

}
