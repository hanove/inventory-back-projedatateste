package com.example.autoflexspring.controller;

import com.example.autoflexspring.dto.ProductInputResponseDTO;
import com.example.autoflexspring.dto.ProductRequestDTO;
import com.example.autoflexspring.dto.ProductResponseDTO;
import com.example.autoflexspring.repository.InputRepository;
import com.example.autoflexspring.repository.ProductRepository;
import com.example.autoflexspring.service.InputService;
import com.example.autoflexspring.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@CrossOrigin(origins = {"http://localhost:5173"}) //remove this in production
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<ProductResponseDTO> products = productRepository.findAll(pageable)
                .map(product -> new ProductResponseDTO(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getInputs().stream()
                                .map(pi -> new ProductInputResponseDTO(
                                        pi.getId(),
                                        pi.getInput().getId(),
                                        pi.getInput().getName(),
                                        pi.getInputNeeded()
                                )).toList()
                ));
        return !products.isEmpty() ? ResponseEntity.ok(products) : ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        var productResponseDTO = new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getInputs().stream()
                        .map(pi -> new ProductInputResponseDTO(
                                pi.getId(),
                                pi.getInput().getId(),
                                pi.getInput().getName(),
                                pi.getInputNeeded()
                        )).toList()
        );
        return ResponseEntity.ok(productResponseDTO);
    }

    @GetMapping("/canbeproduced")
    public ResponseEntity<Page<ProductResponseDTO>> getAllCanBeProduced(@RequestParam int page, @RequestParam int size, @RequestParam String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        var products = productService.getProducibleProducts(pageable);
        return !products.isEmpty() ? ResponseEntity.ok(products) : ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productDTO) {
        var productResponseDTO = productService.createProduct(productDTO);
        return ResponseEntity.ok(productResponseDTO);
    }

    @PutMapping
    public ResponseEntity<ProductResponseDTO> updateProduct(@RequestBody ProductRequestDTO productDTO) {
        var productResponseDTO = productService.updateProduct(productDTO);
        return ResponseEntity.ok(productResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        var deleted = productService.deleteProduct(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.noContent().build();
        }
    }

}
