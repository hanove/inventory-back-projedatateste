package com.example.autoflexspring.dto;

import java.util.List;

public record ProductResponseDTO(Long id, String name, Double price, List<ProductInputResponseDTO> inputs) {
}
