package distribuidora.scrapping.entities.customer;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import distribuidora.scrapping.entities.Client;
import lombok.Data;

@Entity
@Table(name = "order_customer")
@Data
public class Order {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
	private Integer id;
	@ManyToOne
    @JoinColumn(name = "customer_id")
	private Customer customer;
	@ManyToOne
    @JoinColumn(name = "client_id")
	private Client client;
	private Date date;
	private String status;
}
