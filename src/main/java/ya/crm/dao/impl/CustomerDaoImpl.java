package ya.crm.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ya.crm.dao.CustomerDao;
import ya.crm.exception.DaoException;
import ya.crm.model.Customer;

public class CustomerDaoImpl implements CustomerDao{
	// Log
	Logger logger = LoggerFactory.getLogger(CustomerDaoImpl.class);
	
	private EntityManager em;
	
	public CustomerDaoImpl(EntityManager entityManager) {
		this.em = entityManager;
	}

	@Override
	public List<Customer> getAllCustomers() throws DaoException {
		logger.debug("Récupération des clients");
		try {
			// Query or TypedQuery<T> : JPQL used with Java objects
			TypedQuery<Customer> query = em.createQuery("SELECT c FROM Customer c ORDER BY c.id",Customer.class);
			return query.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public Customer getCustomerById(Integer id) throws DaoException {
		try {
			return em.find(Customer.class, id);
		} catch (Exception e) {
			throw new DaoException(e);
		}	
	}

	@Override
	public Customer getCustomerByLastname(String lastname) throws DaoException {
		try {
			TypedQuery<Customer> query = em.createQuery("SELECT c FROM Customer c WHERE c.lastname=:lastname", Customer.class);
			query.setParameter("lastname", lastname);
			return query.getSingleResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void createCustomer(Customer customer) throws DaoException {
		try {
			em.persist(customer);
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}

	@Override
	public void updateCustomer(Customer customer) throws DaoException {
		try {
			em.merge(customer);				
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void deleteCustomer(Customer customer) throws DaoException {
		try {
			em.remove(customer);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	// Using namedQuery : see Customer class
	@Override
	public List<Customer> getCustomersByActive(Boolean active) throws DaoException {
		logger.debug("Retreiving customers by acitve = {}", active);
		try {
			Query namedQuery = em.createNamedQuery("Customer.findByActive");
			namedQuery.setParameter("active", active);
			return (List<Customer>) namedQuery.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<Customer> getCustomersWithMobile() throws DaoException {
		try {
			Query nativeQuery = em.createNativeQuery("SELECT * FROM customers WHERE mobile IS NOT NULL", Customer.class);
			return (List<Customer>) nativeQuery.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	// Using criteriaQuery
	@Override
	public Customer getCustomerByIdWithCriteriaQuery(Integer id) throws DaoException {
		try {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);
			
			Root<Customer> customerRoot = criteriaQuery.from(Customer.class);
			
			criteriaQuery.select(customerRoot).where(criteriaBuilder.equal(customerRoot.get("id"),id));
			
			TypedQuery<Customer> query = em.createQuery(criteriaQuery);
			
			return query.getSingleResult();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	// Using CriteriaQuery
	public void deleteCustomerWithoutOrder() throws DaoException {
		try {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaDelete<Customer> criteriaDelete = criteriaBuilder.createCriteriaDelete(Customer.class);
			
			Root<Customer> customerRoot = criteriaDelete.from(Customer.class);
			criteriaDelete.where(criteriaBuilder.isEmpty(customerRoot.get("orders")));
			
			em.createQuery(criteriaDelete).executeUpdate();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}
}
