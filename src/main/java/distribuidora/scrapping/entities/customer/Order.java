package distribuidora.scrapping.entities.customer;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import distribuidora.scrapping.configs.Constantes;
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
	// TODO: Si en algun momento se audita la tabla habra que tener en cuenta el
	// estado de los orderHasProduct
	@OneToMany(mappedBy = "order")
	private List<OrderHasProduct> orderHasProducts;

	
	/**
	 * Crear un nuevo pedido desde el lado del vendedor
	 * 
	 * @param customer
	 * @param client
	 * @param date
	 * @param status
	 */
	public Order(Client client) {
		super();
		this.client = client;
		this.date = new Date();
		this.status = Constantes.ORDER_STATUS_PENDING;
	}


	public Order() {
		super();
	}
}
