package distribuidora.scrapping.repositories.postgres;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import distribuidora.scrapping.entities.ProductoInterno;

public interface ProductoInternoRepository extends JpaRepository<ProductoInterno, Integer> {

    @Query("SELECT pi FROM ProductoInterno pi " +
            "WHERE pi.externalProduct IS NOT NULL")
    List<ProductoInterno> getProductosReferenciados();

    @Query("SELECT pi FROM ProductoInterno pi " +
            "WHERE pi.id IN (:productoInternoIds)")
    List<ProductoInterno> getProductosPorIds(@Param("productoInternoIds") List<Integer> productoInternoIds);

    @Query("""
    		SELECT pi 
    		FROM ProductoInterno pi
    			LEFT JOIN pi.lvCategoria cat
    			INNER JOIN pi.client c
    		WHERE c.id = :clientId
    		ORDER BY cat.descripcion, pi.nombre, pi.descripcion	
    		""")
    List<ProductoInterno> getAllProductosByUserId(Integer clientId);

    @Query("""
    		SELECT COUNT(*) 
    		FROM ProductoInterno pi
    		WHERE pi.id IN (:productIds)
    		""")
	int countProductsByIds(@Param("productIds") List<Integer> productIds);

    @Query("""
    		SELECT pi
    		FROM ProductoInterno pi
    			INNER JOIN pi.client c
    		WHERE pi.id = :productId
    			AND c.id = :clientId
    		""")
	ProductoInterno findByIdAndClientId(Integer productId, Integer clientId);

}
