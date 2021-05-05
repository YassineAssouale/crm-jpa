package ya.crm.dao;

import java.util.List;

import ya.crm.exception.DaoException;
import ya.crm.model.User;

public interface UserDao {
	

	/**
	 * Get a user by id
	 * @param id the id
	 * @return the user
	 * @throws DaoException
	 */
	User getById(Integer id) throws DaoException;
	
	/**
	 * Get a user by username
	 * @param username the username
	 * @return the user
	 * @throws DaoException
	 */
	User getByUsername(String username) throws DaoException;
	
	/**
	 * Get a list of all users
	 * @return the list of all users
	 * @throws DaoException
	 */
	List<User> getAll() throws DaoException;
	
	/**
	 * Get a user by username and password
	 * @param username the username
	 * @param password the password
	 * @return DaoException
	 * @throws DaoException
	 */
	User getByUsernameAndPassword(String username, String password) throws DaoException;
	
	/**
	 * Create a new user
	 * @param user the user to create
	 * @throws DaoException
	 */
	void createUser(User user) throws DaoException;
	
	/**
	 * Update a user
	 * @param user the user to update
	 * @throws DaoException
	 */
	void updateUser(User user) throws DaoException;
	
	/**
	 * Delete a user
	 * @param user the user to delete
	 * @throws DaoException
	 */
	void deleteUser(User user) throws DaoException;
}
