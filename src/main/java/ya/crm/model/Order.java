package ya.crm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;

@Entity
@Table(name="orders")
@NamedQuery(name="Order.findByTypeAndStatus", query="SELECT o FROM Order o WHERE o.type=:type and o.status=:status")
public class Order implements Serializable{

	private static final long serialVersionUID = 5087070270529990806L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(length = 100)
	private String label;
	
	@Column(name="adr_et", columnDefinition = "DECIMAL", precision = 5, scale = 2)
	private Double adrEt;
	
	@Column(name="number_of_days", columnDefinition = "DECIMAL", precision = 5, scale = 2)
	private Double numberOfDays;
	
	@Column(columnDefinition = "DECIMAL", precision = 5, scale = 2)
	private Double tva;
	
	@Column(length = 100)
	private String status;
	
	@Column(length = 100)
	private String type;
	
	@Column(columnDefinition = "TEXT")
	private String notes;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Order(Integer id, String label, Double adrEt, Double numberOfDays, Double tva, String status, String type,
			String notes, Customer customer) {
		super();
		this.id = id;
		this.label = label;
		this.adrEt = adrEt;
		this.numberOfDays = numberOfDays;
		this.tva = tva;
		this.status = status;
		this.type = type;
		this.notes = notes;
		this.customer = customer;
	}

	public Order(Double adrEt) {
		super();
		this.adrEt = adrEt;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Double getAdrEt() {
		return adrEt;
	}

	public void setAdrEt(Double adrEt) {
		this.adrEt = adrEt;
	}

	public Double getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(Double numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public Double getTva() {
		return tva;
	}

	public void setTva(Double tva) {
		this.tva = tva;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", label=" + label + ", adrEt=" + adrEt + ", numberOfDays=" + numberOfDays + ", tva="
				+ tva + ", status=" + status + ", type=" + type + ", notes=" + notes + ", customer=" + customer + "]";
	}
	
	

}
