package tests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import com.plip.exceptions.persistence.PageNotFoundException;
import com.plip.exceptions.persistence.ProductNotFoundException;
import com.plip.persistence.daos.impls.PageDaoImpl;
import com.plip.persistence.daos.impls.ProductDaoImpl;
import com.plip.persistence.daos.interfaces.PageDao;
import com.plip.persistence.daos.interfaces.ProductDao;
import com.plip.persistence.model.Image;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.PageProduct;
import com.plip.persistence.model.Product;

public class ProductTest {

	@Test
	public void getProductImages() {

		ProductDao productDao = new ProductDaoImpl();
		Product _6copin;
		try {
			_6copin = productDao.getProductByName("6Copin");
			Set images = _6copin.getImages();
			Iterator it = images.iterator();
			while (it.hasNext()) {
				Image img = (Image) it.next();
				System.out.println(img.getPath());
			}

		} catch (ProductNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void getOrderProducts() {
		PageDao pageDao = new PageDaoImpl();
		try {
			ArrayList<Page> pageList = pageDao.getPagesByOrderId(1);
			for (Page page : pageList){
				Set<PageProduct> pageProducts = page.getPageProducts();
				Product productToCompare = null;
				for (PageProduct next : pageProducts){
					productToCompare = next.getProduct();
				}
			}
		} catch (PageNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getProducts() {
		PageDao pageDao = new PageDaoImpl();
		ArrayList<Product> products = pageDao.getPageProductsByOrderId(Long.valueOf(1));
		Product prod = products.get(0);
	}
}
