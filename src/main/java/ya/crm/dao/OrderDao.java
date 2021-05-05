package ya.crm.dao;

import java.util.List;

import ya.crm.exception.DaoException;
import ya.crm.model.Order;

public interface OrderDao {
	
	/**
	 * Get an order by id
	 * 
	 * @param id the order id
	 * @return the order
	 * @throws DaoException
	 */
	Order getById(Integer id) throws DaoException;

	/**
	 * Get all orders
	 * 
	 * @return a list of orders
	 * @throws DaoException
	 */
	List<Order> getAllOrders() throws DaoException;

	/**
	 * Get all orders for a specific customer
	 * 
	 * @return a list of orders
	 * @throws DaoException
	 */
	List<Order> getOrdersByCustomer(Integer id) throws DaoException;

	/*
	 * Get orders by type and status 
	 * 
	 * @param String type, String status 
	 * @return a list of orders by type and status 
	 * @throws DaoException
	 */
	List<Order> getOrdersByTypeAndStatus(String type, String status) throws DaoException;

	/*
	 * Get Orders for a Number of days 
	 * 
	 * @return a list of orders for a number of days 
	 * @throws DaoException
	 */
	List<Order> getOrdersForNumberOfDays(Double days) throws DaoException;

	/**
	 * Create an order
	 * 
	 * @param order the order to create
	 * @throws DaoException
	 */
	void createOrder(Order order) throws DaoException;

	/**
	 * Update an order
	 * 
	 * @param order the order to update
	 * @throws DaoException
	 */
	void updateOrder(Order order) throws DaoException;
	
	/**
	 * Update every order with the olderStatus using the newStatus
	 * 
	 */
	void updateOrderStatus(String newStatus, String oldStatus, String type) throws DaoException;

	/**
	 * Delete an order
	 * 
	 * @param order the order to delete
	 * @throws DaoException
	 */
	void deleteOrder(Order order) throws DaoException;
	
}
