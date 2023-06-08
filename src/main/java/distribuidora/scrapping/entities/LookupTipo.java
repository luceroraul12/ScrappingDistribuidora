package distribuidora.scrapping.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "lookup_tipo")
public class LookupTipo {

	@Id
	@Column(name = "id", nullable = false)
	private Integer id;
	private String codigo;
	private String descripcion;

}
