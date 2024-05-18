package distribuidora.scrapping.repositories.postgres;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import distribuidora.scrapping.entities.ProductoInternoStatus;

public interface ProductoInternoStatusRepository
		extends
			JpaRepository<ProductoInternoStatus, Integer> {

	@Query("""
			SELECT pis
			FROM ProductoInternoStatus pis
				INNER JOIN pis.productoInterno p
			WHERE p.id IN :productIds
			""")
	List<ProductoInternoStatus> findAllByProductIds(List<Integer> productIds);

	@Query("""
			SELECT pis
			FROM ProductoInternoStatus pis
				INNER JOIN pis.productoInterno p
				INNER JOIN p.client c
			WHERE c.id = :clientId
			""")
	List<ProductoInternoStatus> findByClientId(Integer clientId);

	@Query("""
			SELECT pis
			FROM ProductoInternoStatus pis
				INNER JOIN pis.productoInterno p
				INNER JOIN p.client c
			WHERE c.id = :clientId
				AND p.id = :productId
			""")
	ProductoInternoStatus findByProductId(Integer productId, Integer clientId);

}
