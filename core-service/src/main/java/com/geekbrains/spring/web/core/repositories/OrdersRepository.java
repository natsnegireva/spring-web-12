package com.geekbrains.spring.web.core.repositories;

import com.geekbrains.spring.web.core.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.username = ?1")
    List<Order> findAllByUsername(String username);

    @Query("select o from Order o where o.id = ?1")
    Order fingByOrderId(Long id);

    @Query("select o from Order o where o.id = ?1 and o.status = ?1")
    Order setStatus(Long id, String status);
}
