package ya.crm.dao.impl.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ya.crm.dao.impl.CustomerDaoImpl;
import ya.crm.model.Customer;

@TestMethodOrder(OrderAnnotation.class)
public class CustomerDaoImplTest {

	public static final String LASTNAME = "GILBERT";
	
	Logger logger = LoggerFactory.getLogger(CustomerDaoImpl.class);
	
	private static EntityManager em;
	
	private static CustomerDaoImpl customerDao;
	
	@BeforeAll
	static void setup() {
		Map<String, String> properties = new HashMap<>();
		properties.put("hibernate.show_sql", "true");
		properties.put("hibernate.format_sql", "true");
		em = Persistence.createEntityManagerFactory("crm-test",properties).createEntityManager();
		
		customerDao = new CustomerDaoImpl(em);
	}
	
	@Test
	@Order(1)
	void getById() {
		Customer customer = customerDao.getCustomerById(1);
		Assertions.assertNotNull(customer, "cusotmer not found!");
	}
	
	@Test 
	@Order(2)
	void givenCustomerId() {
		Customer c = customerDao.getCustomerById(1);
		Assertions.assertEquals(LASTNAME, c.getLastname(), "Customer not found!");
	}
	
	@Test
	@Order(3)
	void givenCustomerLastname() {
		Customer c = customerDao.getCustomerByLastname(LASTNAME);
		Assertions.assertNotNull(c, "Customer not found!");
	}
	@Test 
	@Order(4)
	void givenCustomerLastnameExpected() {
		Customer c = customerDao.getCustomerByLastname(LASTNAME);
		Assertions.assertEquals(1, c.getId(), "Customer id should be 1");
	}
	
	@Test // With transaction
	@Order(5)
	void givenCustomer() {
		Customer c = new Customer();
		c.setFirstname("Winnie");
		c.setLastname("L'Ourson");
		c.setCompany("Diseny");
		c.setPhone("0222222222");
		c.setMobile("0897865434");
		c.setMail("Winnie@beau.com");
		c.setNotes("Les notes de Winnie");
		c.setActive(true);
		
		// Transaction 
		try {
			em.getTransaction().begin();
			customerDao.createCustomer(c);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			// Junit : Fail the test without a failure message
			fail();
		}
		
		Customer c1 = customerDao.getCustomerByLastname("L'Ourson");
		Assertions.assertNotNull(c1);
	}
	
	@Test
	@Order(6)
	void whenCallingNativeQueryMethod_thenReturnListOfCustomersWithMobile() {
		List<Customer> customers = customerDao.getCustomersWithMobile();
		Assertions.assertEquals(4, customers.size(), "Wrong number of customer with mobile");
	}
	
	@Test
	@Order(7)
	void whenCallingCriteriaQueryMethod_thenReturnCustomer() {
		Customer customer = customerDao.getCustomerByIdWithCriteriaQuery(2);
		Assertions.assertEquals("KENOBI", customer.getLastname(), "Customer lastname should be " + "KENOBI");
	}
	
	@Test
	@Order(8)
	void whenCallingNamedQueryMethod_thenReturnListOfInactiveCustomers() {
		List<Customer> customers = customerDao.getCustomersByActive(false);
		Assertions.assertEquals(2, customers.size(), "Wrong number of inactive numbers");
	}
	
	@Test
	@Order(9)
	void updatedCustomer() {
		Customer c = customerDao.getCustomerByLastname("MCCLANE");
		c.setCompany("AJA-VR");
		try {
			em.getTransaction().begin();
			customerDao.updateCustomer(c);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			fail();
		}
		
		Customer c2 = customerDao.getCustomerByLastname("MCCLANE");
		Assertions.assertEquals("AJA-VR",c2.getCompany(),"Company name should be AJA-VR");
	}
	
	@Test
	@Order(10)
	void givenCustomer_whenCallingTypedQueryMethodForDelete_thenReturnOK() {
		Customer c = customerDao.getCustomerById(4);
		try {
			em.getTransaction().begin();
			customerDao.deleteCustomer(c);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			fail(e.getMessage());
		}
		
		Customer deletedCustomer = customerDao.getCustomerById(4);
		logger.debug("DELETED CUSTOMER = {}", deletedCustomer);
		Assertions.assertNull(deletedCustomer, "Deleted customer should be null");
	}
	
	@Test
	@Order(11)
	void givenCustomer_whenCallingTypedQueryMethodforDeleteCusotmerWhitoutOrder_thenReturnOK() {
		List<Customer> customersBeforeDelete = customerDao.getAllCustomers();
		logger.debug("Customers size berfore deleting = {}", customersBeforeDelete.size());
		try {
			em.getTransaction().begin();
			customerDao.deleteCustomerWithoutOrder();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			fail(e.getMessage());
		}
		
		List<Customer> customersAfterDelete = customerDao.getAllCustomers();
		logger.debug("Customers size after deleting = {}", customersAfterDelete.size());
		
		Customer customerDeleted = customerDao.getCustomerById(4);
		Assertions.assertNull(customerDeleted,"Deleted customer must be null");
	}
	
}
