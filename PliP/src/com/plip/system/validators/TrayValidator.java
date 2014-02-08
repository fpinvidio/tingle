package com.plip.system.validators;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.plip.persistence.dao.impls.StatusDaoImpl;
import com.plip.persistence.dao.impls.TrayStatusDaoImpl;
import com.plip.persistence.dao.interfaces.TrayStatusDao;
import com.plip.persistence.exceptions.StatusNotFoundException;
import com.plip.persistence.exceptions.TrayStatusNotFoundException;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.PageProduct;
import com.plip.persistence.model.Product;
import com.plip.persistence.model.Status;
import com.plip.persistence.model.Tray;
import com.plip.persistence.model.TrayStatus;

public class TrayValidator {
	private HashSet<Product> recognizedProducts = new HashSet<>(0);
	public void validateTray(Tray tray) {
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (TrayStatus trayStatus : statusList) {
					long idStatus = (long) trayStatus.getStatus().getIdStatus();
					switch ((int) idStatus) {
					case Status.STATUS_INVALID_QUANTITY:
						saveInvalidTrayStatus(tray);
						return;
					case Status.STATUS_PRODUCT_RECOGNIZED:
						decreaseProductQuantity(trayStatus, pageProducts);
						break;
					default:
						break;
					}
				}

				for (PageProduct pageProduct : pageProducts) {
					if (recognizedProducts.contains(pageProduct.getProduct())
							&& pageProduct.getQuantity() != 0) {
						saveInvalidTrayStatus(tray);
						return;
					}
				}
				saveValidTrayStatus(tray);
			}

		}
	}

	public void saveInvalidTrayStatus(Tray tray) {
		/* Save Status when tray is invalid*/
		TrayStatusDaoImpl trayStatusDao = new TrayStatusDaoImpl();
		StatusDaoImpl statusDao = new StatusDaoImpl();
		if (tray != null) {
			try {
				Status trayDetectedStatus = statusDao
						.getStatus(Status.STATUS_INVALID_TRAY);
				TrayStatus trayDetected = new TrayStatus(tray,
						trayDetectedStatus, new Date());
				trayStatusDao.addTrayStatus(trayDetected);
			} catch (StatusNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void decreaseProductQuantity(TrayStatus trayStatus,
			Set<PageProduct> pageProducts) {

		for (PageProduct pageProduct : pageProducts) {
			if (pageProduct.getProduct().equals(trayStatus.getProduct())) {
				trayStatus.setQuantity(trayStatus.getQuantity() - 1);
			}
		}
		if (!recognizedProducts.contains(trayStatus.getProduct())) {
			recognizedProducts.add(trayStatus.getProduct());
		}
	}

	public void saveValidTrayStatus(Tray tray) {
		/* Save Status when tray is valid */
		TrayStatusDaoImpl trayStatusDao = new TrayStatusDaoImpl();
		StatusDaoImpl statusDao = new StatusDaoImpl();
		if (tray != null) {
			try {
				Status trayDetectedStatus = statusDao
						.getStatus(Status.STATUS_INVALID_TRAY);
				TrayStatus trayDetected = new TrayStatus(tray,
						trayDetectedStatus, new Date());
				trayStatusDao.addTrayStatus(trayDetected);
			} catch (StatusNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}