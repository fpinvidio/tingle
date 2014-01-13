package daos;

import java.util.Set;

import org.junit.Test;

import com.plip.persistence.dao.impls.ProductCategoryDaoImpl;
import com.plip.persistence.dao.impls.ProductDaoImpl;
import com.plip.persistence.dao.interfaces.ProductCategoryDao;
import com.plip.persistence.dao.interfaces.ProductDao;
import com.plip.persistence.exceptions.NullModelAttributesException;
import com.plip.persistence.exceptions.ProductCategoryNotFoundException;
import com.plip.persistence.exceptions.ProductNotFoundException;
import com.plip.persistence.model.Product;
import com.plip.persistence.model.ProductCategory;

public class ProductTest {

	@Test
	public void addUpdateAndGetProduct() throws NullModelAttributesException, ProductCategoryNotFoundException, ProductNotFoundException {
		/*ProductCategoryDao productCategoryDao = new ProductCategoryDaoImpl();
		ProductCategory belleza = productCategoryDao.getProductCategoryByName("belleza");
		
		Product tolerine = new Product("Tolerine", 100, 122, "Roche Posey", belleza, true);*/
		ProductDao productDao = new ProductDaoImpl();
		//productDao.addProduct(tolerine);
		
		Product t = productDao.getProductByName("");
			Set images = t.getImages();
	}		
	
	@Test
	public void deleteProduct() throws ProductCategoryNotFoundException, ProductNotFoundException {
		ProductDao productDao = new ProductDaoImpl();
		Product tolerine = productDao.getProductByName("Tolerine");
		productDao.deleteProduct(tolerine.getIdProduct());
	}		
			
}
