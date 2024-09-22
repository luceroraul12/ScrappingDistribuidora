package distribuidora.scrapping.entities;

import java.util.Date;

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
public class Label {
	@Id
	@Column(name = "id", nullable = false)
	private Integer id;
	@Column
	private String name;
	@Column
	private String description;
	@Column
	private Date dateCreated;
	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client;
}
