package distribuidora.scrapping.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import distribuidora.scrapping.entities.customer.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

	@Query("""
			SELECT o 
			FROM Order o
				INNER JOIN o.client c
			WHERE c.id = :clientId
				AND o.status = 'PENDING'
			""")
	Order findOrderPendingByClientId(Integer clientId);

}
