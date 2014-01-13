package daos;

import java.util.ArrayList;

import org.junit.Test;

import com.plip.persistence.dao.impls.OrderDaoImpl;
import com.plip.persistence.dao.impls.PageDaoImpl;
import com.plip.persistence.dao.interfaces.OrderDao;
import com.plip.persistence.dao.interfaces.PageDao;
import com.plip.persistence.exceptions.NullModelAttributesException;
import com.plip.persistence.exceptions.OrderNotFoundException;
import com.plip.persistence.exceptions.PageNotFoundException;
import com.plip.persistence.model.Order;
import com.plip.persistence.model.Page;

public class PageTest {

	@Test
	public void test() {
		PageDao pageDao = new PageDaoImpl();
		OrderDao orderDao = new OrderDaoImpl();
	
		Order order;	
		
		try {
			order = orderDao.getOrderByCode("1234");
			Page page = new Page (order, null, 5, 1);
			Page page1 = new Page (order, null, 5, 2);
			pageDao.addPage(page);
			pageDao.addPage(page1);
			ArrayList<Page> pages =pageDao.getPagesByOrderId(1);
					
		} catch (NullModelAttributesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OrderNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PageNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
