package com.example.autoflexspring.repository;

import com.example.autoflexspring.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getProductById(Long id);

    @Query("SELECT DISTINCT p FROM Product p WHERE NOT EXISTS (SELECT pi FROM ProductInput pi WHERE pi.product = p AND pi.input.inStock < pi.inputNeeded)")
    Page<Product> findProducibleProducts(Pageable pageable);
}
