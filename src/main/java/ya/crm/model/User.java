package ya.crm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(name="users")
//@NamedQuery(name = "User.findByActive", query = "SELECT u FROM User u WHERE u.active = :active")
@NamedQuery(name = "User.findByUsernameAndPassword", query="SELECT u FROM User u WHERE u.username=:username and u.password=:password")
public class User implements Serializable{
	
	private static final long serialVersionUID = 7555071581414458777L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 100)
	private String username;
	@Column(length = 100)
	private String password;
	@Column(length = 100)
	private String mail;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(Integer id, String username, String password, String mail) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.mail = mail;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", mail=" + mail + "]";
	}
	
	

}
