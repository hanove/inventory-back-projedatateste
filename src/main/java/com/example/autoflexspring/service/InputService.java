package com.example.autoflexspring.service;

import com.example.autoflexspring.dto.InputDTO;
import com.example.autoflexspring.model.Input;
import com.example.autoflexspring.repository.InputRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InputService {

    private final InputRepository inputRepository;

    public Input inputDTOToEntity(InputDTO inputDTO){
        Input input = new Input();
        input.setName(inputDTO.name());
        input.setInStock(inputDTO.inStock());
        return input;
    }

}
