package com.example.autoflexspring.service;

import com.example.autoflexspring.dto.ProductInputResponseDTO;
import com.example.autoflexspring.dto.ProductRequestDTO;
import com.example.autoflexspring.dto.ProductResponseDTO;
import com.example.autoflexspring.model.Input;
import com.example.autoflexspring.model.Product;
import com.example.autoflexspring.model.ProductInput;
import com.example.autoflexspring.repository.InputRepository;
import com.example.autoflexspring.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final InputRepository inputRepository;

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productDTO){
        //create the product entity
        var product = new Product();
        product.setName(productDTO.name());
        product.setPrice(productDTO.price());

        //Save the product to generate an ID for it
        product = productRepository.save(product);

        //Create the product-input relationships
        var productInputs = new ArrayList<ProductInput>();
        for (var inputDTO : productDTO.inputs()) {
            var input = inputRepository.findById(inputDTO.inputId())
                    .orElseThrow(() -> new RuntimeException("Input not found with id: " + inputDTO.inputId()));

            var productInput = new ProductInput();
            productInput.setProduct(product);
            productInput.setInput(input);
            productInput.setInputNeeded(inputDTO.quantity());
            productInputs.add(productInput);
        }

        product.setInputs(productInputs);
        product = productRepository.save(product);

        return new ProductResponseDTO(
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
    }

    @Transactional
    public ProductResponseDTO updateProduct(ProductRequestDTO productDTO) {
        var existingProduct = productRepository.findById(productDTO.id())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productDTO.id()));

        existingProduct.setName(productDTO.name());
        existingProduct.setPrice(productDTO.price());

        existingProduct.getInputs().clear();

        for (var inputDTO : productDTO.inputs()) {
            var input = inputRepository.findById(inputDTO.inputId())
                    .orElseThrow(() -> new RuntimeException("Input not found with id: " + inputDTO.inputId()));

            var productInput = new ProductInput();
            productInput.setProduct(existingProduct);
            productInput.setInput(input);
            productInput.setInputNeeded(inputDTO.quantity());
            existingProduct.getInputs().add(productInput);
        }
        productRepository.save(existingProduct);

        return new ProductResponseDTO(
                existingProduct.getId(),
                existingProduct.getName(),
                existingProduct.getPrice(),
                existingProduct.getInputs().stream()
                        .map(pi -> new ProductInputResponseDTO(
                                pi.getId(),
                                pi.getInput().getId(),
                                pi.getInput().getName(),
                                pi.getInputNeeded()
                        )).toList()
        );
    }

    @Transactional
    public boolean deleteProduct(Long id){
        if (!productRepository.existsById(id)) {
            return false;
        }
        productRepository.deleteById(id);
        return true;
    }

    public Page<ProductResponseDTO> getProducibleProducts(Pageable pageable){
        Page<ProductResponseDTO> products = productRepository.findProducibleProducts(pageable)
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
        return products;
    }

    private boolean canBeProduced(Product product){
        return product.getInputs().stream().allMatch(pi -> pi.getInput().getInStock() >= pi.getInputNeeded());
    }

}
