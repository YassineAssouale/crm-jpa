package ya.crm.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ya.crm.dao.OrderDao;
import ya.crm.exception.DaoException;
import ya.crm.model.Order;

public class OrderDaoImpl implements OrderDao{
	
	Logger logger = LoggerFactory.getLogger(CustomerDaoImpl.class);
	
	private EntityManager em;
	
	public OrderDaoImpl(EntityManager entityManager) {
		this.em = entityManager;
	}

	@Override
	public Order getById(Integer id) throws DaoException {
		try {
			return em.find(Order.class, id);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	public Order getByLabel(String label) throws DaoException{
		try {
			TypedQuery<Order> query = em.createQuery("SELECT o FROM Order o WHERE o.label=?1", Order.class);
			query.setParameter(1, label);
			/*
			 * TypedQuery<Order> query = em.createQuery("SELECT o FROM Order o WHERE o.label=:label", Order.class);
			 * query.setParameter("lastname", lastname);
			 */
			return query.getSingleResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	@Override
	public List<Order> getAllOrders() throws DaoException {
		try {
			TypedQuery query = em.createQuery("SELECT o FROM Order ORDER BY o.id", Order.class);
			return query.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	@Override
	public void createOrder(Order order) throws DaoException {
		try {
			em.persist(order);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void updateOrder(Order order) throws DaoException {
		try {
			em.merge(order);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void deleteOrder(Order order) throws DaoException {
		try {
			// Allows to resynchronize && possbily reattache the customer to database before deleting it
			em.remove(em.merge(order));
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	// Using criteriaQuery
	@Override
	public List<Order> getOrdersByCustomer(Integer id) throws DaoException {
		try {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);

			Root<Order> orderRoot = criteriaQuery.from(Order.class);
			criteriaQuery.select(orderRoot).where(criteriaBuilder.equal(orderRoot.get("customer"), id));

			TypedQuery<Order> query = em.createQuery(criteriaQuery);

			return query.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	// Using namedQuery : see Order class
	@Override
	public List<Order> getOrdersByTypeAndStatus(String type, String status) throws DaoException {
		try {
			Query namedQuery = em.createNamedQuery("Order.findByTypeAndStatus");
			namedQuery.setParameter("type", type);
			namedQuery.setParameter("status", status);
			return (List<Order>)namedQuery.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	// Using nativeQuery
	@Override
	public List<Order> getOrdersForNumberOfDays(Double days) throws DaoException {
		try {
			Query nativeQuery = em.createNativeQuery("SELECT * FROM orders WHERE number_of_days > ?", Order.class);
			nativeQuery.setParameter(1, days);
			return (List<Order>) nativeQuery.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	// Using criteriaUpdate
	@Override
	public void updateOrderStatus(String newStatus, String oldStatus, String type) throws DaoException {

			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaUpdate<Order> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Order.class);
			
			Root<Order> orderRoot = criteriaUpdate.from(Order.class);
			criteriaUpdate.set("status", newStatus);
			
			Predicate oldStatusPredicate = criteriaBuilder.equal(orderRoot.get("status"), oldStatus);
			Predicate typePredicate = criteriaBuilder.equal(orderRoot.get("type"), type);
			
			criteriaUpdate.where(criteriaBuilder.and(oldStatusPredicate, typePredicate));
			em.createQuery(criteriaUpdate).executeUpdate();		
	}
}
