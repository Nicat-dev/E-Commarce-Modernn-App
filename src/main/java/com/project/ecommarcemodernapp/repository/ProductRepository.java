package com.project.ecommarcemodernapp.repository;

import com.project.ecommarcemodernapp.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<OrderItem, Long> {

}
