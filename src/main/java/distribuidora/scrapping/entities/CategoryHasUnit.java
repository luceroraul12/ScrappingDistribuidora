package distribuidora.scrapping.entities;

import distribuidora.scrapping.entities.LookupValor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "lv_category_has_lv_unit")
public class CategoryHasUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "lv_category_id")
    private LookupValor category;
    @ManyToOne
    @JoinColumn(name = "lv_unit_id")
    private LookupValor unit;
}