package com.example.autoflexspring.dto;

import java.util.List;

public record ProductRequestDTO(Long id, String name, Double price, List<ProductInputRequestDTO> inputs) {
}
