package ya.crm.dao.impl.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
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

import ya.crm.dao.UserDao;
import ya.crm.dao.impl.CustomerDaoImpl;
import ya.crm.dao.impl.UserDaoImpl;
import ya.crm.model.User;


@TestMethodOrder(OrderAnnotation.class)
public class UserDaoImplTest {
	
	public static final String USERNAME = "tliotard";
	
	Logger logger = LoggerFactory.getLogger(CustomerDaoImpl.class);
	
	private static EntityManager em;
	
	private static UserDao userDao;

	@BeforeAll
	static void setup() {
		Map<String, String> properties = new HashMap<>();
		properties.put("hibernate.show_sql", "true");
		properties.put("hibernate.format_sql", "true");
		em = Persistence.createEntityManagerFactory("crm-test", properties).createEntityManager();
		userDao = new UserDaoImpl(em);
	}
	
	@Test
	@Order(1)
	void test() {
		logger.debug("test 1");
		User user = userDao.getById(1);
		Assertions.assertNotNull(user, "user not found");
	}
	
	@Test
	@Order(2)
	void givenUserId() {
		logger.debug("test 2");
		User user = userDao.getById(1);
		Assertions.assertEquals(USERNAME, user.getUsername(), "user not found");
	}

	@Test
	@Order(3)
	void givenUserUsername() {
		logger.debug("test 3");
		User user = userDao.getByUsername(USERNAME);
		Assertions.assertNotNull(user, "user not found");
	}

	@Test
    void whenCallingNamedQueryMethod_thenReturnUserByUsernameAndPassword() {
        User user = userDao.getByUsernameAndPassword("tliotard", "mdp49");
        Assertions.assertEquals(USERNAME, user.getUsername(), "User not found");
    }
	
	@Test
	@Order(4)
	void givenUser() {
		User newUser = new User();
		newUser.setUsername("Winnie");
		newUser.setPassword("LOurson");				
		newUser.setMail("Winnie@beau.com");
		try {
			em.getTransaction().begin();
			userDao.createUser(newUser);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			fail();
		}

		User user = userDao.getByUsername("Winnie");
		Assertions.assertNotNull(user);
	}

	@Test
	@Order(5)
	void upDatedUser() {
		User user = userDao.getByUsername("Winnie");
		user.setPassword("Miel");
		

		try {
			em.getTransaction().begin();
			userDao.updateUser(user);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			fail();
		}

		User updateduser = userDao.getByUsername("Winnie");
		Assertions.assertEquals("Miel", updateduser.getPassword());
	}
	
	@Test
	@Order(6)
	void givenUser_whenCallingTypedQueryMethodeForDelete_thenReturnOK() {
		User user = userDao.getById(3);
		
		try {
			em.getTransaction().begin();
			userDao.deleteUser(user);
			em.getTransaction().commit();
		} catch (Exception e) {
		em.getTransaction().rollback();
			fail(e.getMessage());
		}
	
		User deletedUser = userDao.getById(3);
		logger.debug("DELETED CUSTOMER = {}", deletedUser);
		Assertions.assertNull(deletedUser, "Deleted user must be null");
	}
}
