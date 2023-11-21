package distribuidora.scrapping.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import distribuidora.scrapping.entities.customer.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

}
