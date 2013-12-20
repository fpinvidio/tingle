package com.plip.persistence.dao.impls;

import com.plip.persistence.dao.interfaces.ProductDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.Product;

public class ProductDaoImpl implements ProductDao{

	DaoManager daoManager;
	
	public ProductDaoImpl(DaoManager daoManager) {
		super();
		this.daoManager = daoManager;
	}

	@Override
	public Integer addProduct(Product product) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product getProduct(int idProduct) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateProduct(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePosition(Integer productId) {
		// TODO Auto-generated method stub
		
	}
}
