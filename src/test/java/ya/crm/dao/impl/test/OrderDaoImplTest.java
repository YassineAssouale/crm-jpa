package ya.crm.dao.impl.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ya.crm.dao.OrderDao;
import ya.crm.dao.impl.OrderDaoImpl;
import ya.crm.model.Order;

public class OrderDaoImplTest {
	
	private static EntityManager em;
	
	private static OrderDao orderDao;

	@BeforeAll
	static void setup() {
		Map<String, String> properties = new HashMap<>();
		properties.put("hibernate.show_sql", "true");
		properties.put("hibernate.format_sql", "true");
		em = Persistence.createEntityManagerFactory("crm-test", properties).createEntityManager();
		orderDao = new OrderDaoImpl(em);
	}
	
	@Test
    void whenCallingNamedQueryMethod_thenReturnListOfOrdersByTypeAndStatus() {
        List<Order> orders = orderDao.getOrdersByTypeAndStatus("Forfait", "En attente");
        Assertions.assertEquals(1, orders.size(), "Wrong number of orders");
    }
	
	@Test
	void whenCallingNativeQueryMethod_thenReturnOrdersListMoreThanXDays() {
		List<Order> orders = orderDao.getOrdersForNumberOfDays(Double.valueOf(4));
		Assertions.assertEquals(1, orders.size(), "Wrong number of orders with more than 4 days");
	}
	
	@Test
	void whenCallingCriteriaQueryMethod_thenReturnListOrderByCustomerId() {
		List<Order> orders = orderDao.getOrdersByCustomer(1);
		Assertions.assertEquals(2, orders.size(), "Wrong number of Orders for this customer");
	}
	
	@Test
	void whenCallingCriteriaUpdateQueryMethodForUpdate_thenReturnOK() {
		try {
			em.getTransaction().begin();
			orderDao.updateOrderStatus("Terminé", "En cours", "Forfait");
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			fail();
		}
		List<Order> updateOrders = orderDao.getOrdersByTypeAndStatus("Forfait", "Terminé");
		Assertions.assertEquals(1,  updateOrders.size(), "Wrong number of order with type Forfait and status Terminé");
	}

}
