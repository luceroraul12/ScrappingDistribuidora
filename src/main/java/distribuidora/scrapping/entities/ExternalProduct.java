package distribuidora.scrapping.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

/**
 * Es la entidad con la que llegan al FrontEnd
 */
@Data
@Builder
@Entity
@Table(name = "external_product")
public class ExternalProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column
	private String title;
	@Column
	private Double price;
	@Column(columnDefinition = "DATE")
	private Date date;
	@ManyToOne
	@JoinColumn(name = "lv_distribuidora_id")
	private LookupValor distribuidora;
	@Column
	private String code;

	/**
	 * Retorna el valor del precio
	 * 
	 * @return en caso de que el valor sea null se retornara 0.0
	 */
	public Double getPrecioPorCantidadEspecifica() {
		return price != null ? price : 0.0;
	}

	public ExternalProduct(Integer id, String title, Double price, Date date,
			LookupValor distribuidora, String code) {
		super();
		this.id = id;
		this.title = title;
		this.price = price;
		this.date = date;
		this.distribuidora = distribuidora;
		this.code = code;
	}

	public ExternalProduct() {
		super();
	}

}
