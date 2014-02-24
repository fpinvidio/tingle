package com.plip.system.validators;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.plip.exceptions.persistence.StatusNotFoundException;
import com.plip.exceptions.persistence.TrayStatusNotFoundException;
import com.plip.persistence.daos.impls.StatusDaoImpl;
import com.plip.persistence.daos.impls.TrayStatusDaoImpl;
import com.plip.persistence.daos.interfaces.TrayStatusDao;
import com.plip.persistence.model.Image;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.PageProduct;
import com.plip.persistence.model.Product;
import com.plip.persistence.model.Status;
import com.plip.persistence.model.Tray;
import com.plip.persistence.model.TrayStatus;

public class TrayValidator {
	private HashSet<Product> recognizedProducts = new HashSet<>(0);
	public long validateTray(Tray tray) {
		if (tray != null) {
			Page page = tray.getPage();
			
			List<TrayStatus> statusList = new ArrayList<TrayStatus>();
			if (page != null) {
				TrayStatusDao trayStatusDao = new TrayStatusDaoImpl();
				Set<PageProduct> pageProducts = page.getPageProducts();
				try {
					statusList = trayStatusDao
							.getStatusByTray(tray.getIdTray());
				} catch (TrayStatusNotFoundException e) {
					e.printStackTrace();
				}
				for (TrayStatus trayStatus : statusList) {
					int idStatus = trayStatus.getStatus().getIdStatus().intValue();
					switch (idStatus) {
					case Status.STATUS_INVALID_QUANTITY: 
						return saveInvalidTrayStatus(tray);
						
					case Status.STATUS_PRODUCT_RECOGNIZED: 
						decreaseProductQuantity(trayStatus, pageProducts);
						break;
					case Status.STATUS_PRODUCT_NOT_RECOGNIZED:
						 if(productUnrecognizedError(pageProducts)){
							 return saveInvalidTrayStatus(tray);	 
						 }
						 break;
					default:
						break;
					}
				}

				for (PageProduct pageProduct : pageProducts) {
					if (recognizedProducts.contains(pageProduct.getProduct())
							&& pageProduct.getQuantity() != 0) {
						return saveInvalidTrayStatus(tray);
					}
				}
				return saveValidTrayStatus(tray);
			}

		}
		return 0;
	}

	public long saveInvalidTrayStatus(Tray tray) {
		/* Save Status when tray is invalid*/
		TrayStatusDaoImpl trayStatusDao = new TrayStatusDaoImpl();
		StatusDaoImpl statusDao = new StatusDaoImpl();
		long trayStatusId=0;
		if (tray != null) {
			try {
				Status trayDetectedStatus = statusDao
						.getStatus(Status.STATUS_INVALID_TRAY);
				TrayStatus trayDetected = new TrayStatus(tray,
						trayDetectedStatus, new Date());
				trayStatusId=trayStatusDao.addTrayStatus(trayDetected);
			} catch (StatusNotFoundException e) {
				e.printStackTrace();
			}
		}
		return trayStatusId;
	}

	public void decreaseProductQuantity(TrayStatus trayStatus,
			Set<PageProduct> pageProducts) {

		for (PageProduct pageProduct : pageProducts) {
			if (pageProduct.getProduct().equals(trayStatus.getProduct())) {
				pageProduct.setQuantity(pageProduct.getQuantity() - trayStatus.getQuantity());
				if (!recognizedProducts.contains(pageProduct.getProduct())) {
					recognizedProducts.add(pageProduct.getProduct());
				}
				break;
			}
		}
		
	}

	public long saveValidTrayStatus(Tray tray) {
		/* Save Status when tray is valid */
		TrayStatusDaoImpl trayStatusDao = new TrayStatusDaoImpl();
		StatusDaoImpl statusDao = new StatusDaoImpl();
		long trayStatusId = 0;
		if (tray != null) {
			try {
				Status trayDetectedStatus = statusDao
						.getStatus(Status.STATUS_VALID_TRAY);
				TrayStatus trayDetected = new TrayStatus(tray,
						trayDetectedStatus, new Date());
				trayStatusId = trayStatusDao.addTrayStatus(trayDetected);
			} catch (StatusNotFoundException e) {
				e.printStackTrace();
			}
		}
		return trayStatusId;
	}
	
	public boolean productUnrecognizedError(Set<PageProduct> pageProducts){
		   for(PageProduct pageProduct : pageProducts){
		   Product product = pageProduct.getProduct();
		   if(product.getImageNumber() == 0){
		   return false;   
		   }
		   }
		   return true;
	}
}