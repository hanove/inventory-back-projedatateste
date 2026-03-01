package com.example.autoflexspring.controller;
import com.example.autoflexspring.dto.InputDTO;
import com.example.autoflexspring.repository.InputRepository;
import com.example.autoflexspring.service.InputService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/inputs")
@CrossOrigin(origins = {"http://localhost:5173"}) //remove this in production
public class InputController {

    private final InputRepository inputRepository;
    private final InputService inputService;

    @GetMapping
    public ResponseEntity<Page<InputDTO>> getAllInputs(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<InputDTO> inputs = inputRepository.findAll(pageable)
                .map(input -> new InputDTO(
                        input.getId(),
                        input.getName(),
                        input.getInStock()
                ));
        return !inputs.isEmpty() ? ResponseEntity.ok(inputs) : ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InputDTO> getInputById(@PathVariable Long id) {
        var input = inputRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Input not found with id: " + id));
        return ResponseEntity.ok(new InputDTO(input.getId(), input.getName(), input.getInStock()));
    }

    @PostMapping
    public ResponseEntity<InputDTO> createInput(@RequestBody InputDTO inputDTO) {
        var input = inputRepository.save(inputService.inputDTOToEntity(inputDTO));
        return ResponseEntity.ok(new InputDTO(input.getId(), input.getName(), input.getInStock()));
    }

    @PutMapping
    public ResponseEntity<InputDTO> updateInput(@RequestBody InputDTO inputDTO) {
        var input = inputRepository.findById(inputDTO.id())
                .orElseThrow(() -> new RuntimeException("Input not found with id: " + inputDTO.id()));
        input.setName(inputDTO.name());
        input.setInStock(inputDTO.inStock());
        var updatedInput = inputRepository.save(input);
        return ResponseEntity.ok(new InputDTO(updatedInput.getId(), updatedInput.getName(), updatedInput.getInStock()));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteInput(@RequestParam Long id) {
        if (!inputRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        inputRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
