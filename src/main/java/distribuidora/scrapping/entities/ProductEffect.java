package distribuidora.scrapping.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProductEffect {
	@Id
	@Column(name = "id", nullable = false)
	private Integer id;
	@Column(nullable = false, length = 2000)
	private String description;
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = true)
	private ProductoInterno product;
	@ManyToOne
	@JoinColumn(name = "label_id", nullable = true)
	private Label label;
	@ManyToOne
	@JoinColumn(name = "lv_type_id")
	private LookupValor lvType;
}
