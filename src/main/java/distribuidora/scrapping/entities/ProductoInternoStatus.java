package distribuidora.scrapping.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "productos_internos_status")
@Getter
@Setter
public class ProductoInternoStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "producto_interno_id")
    private ProductoInterno productoInterno;

    private Boolean isUnit;

    private Boolean hasStock;
}