package ya.crm.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ya.crm.dao.UserDao;
import ya.crm.exception.DaoException;
import ya.crm.model.User;

public class UserDaoImpl implements UserDao{
	

	Logger logger = LoggerFactory.getLogger(CustomerDaoImpl.class);
	
	private EntityManager em;

	public UserDaoImpl(EntityManager entitymanager) {
		this.em = entitymanager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getById(Integer id) throws DaoException {
		logger.debug("Récupération de l'Id utilisateur = {}", id);
		try {
			return em.find(User.class, id);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	public User getByUsername(String username) throws DaoException {
		try {
			TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = ?1", User.class);
			query.setParameter(1, username);
			return query.getSingleResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<User> getAll() throws DaoException {
		try {
			TypedQuery<User> query = em.createQuery("SELECT u FROM User u ORDER BY u.id", User.class);
			return query.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createUser(User user) throws DaoException {
		try {
			em.persist(user);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateUser(User user) throws DaoException {
		try {
			em.merge(user);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteUser(User user) throws DaoException {
		try {
			em.remove(em.merge(user)); //Permet de resynchroniser (et possiblement rattacher) le client à la BDD avant de le supprimer
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public User getByUsernameAndPassword(String username, String password) throws DaoException {
		logger.debug("Paramètres username ={}, password = {}", username, password);		
		try {
			Query namedQuery = em.createNamedQuery("User.findByUsernameAndPassword");
			namedQuery.setParameter("username", username);
			namedQuery.setParameter("password", password);
			return (User) namedQuery.getSingleResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
