package com.postech.auramsproduct.adapters;

import com.postech.auramsproduct.adapters.dto.ProductDTO;
import com.postech.auramsproduct.application.CreateProductUseCase;
import com.postech.auramsproduct.application.FindProductUseCase;
import com.postech.auramsproduct.domain.Product;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final FindProductUseCase findProductUseCase;
    private final ModelMapper modelMapper;

    public ProductController(
            CreateProductUseCase createProductUseCase,
            FindProductUseCase findProductUseCase,
            ModelMapper modelMapper) {
        this.createProductUseCase = createProductUseCase;
        this.findProductUseCase = findProductUseCase;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        Product createdProduct = createProductUseCase.execute(productDTO);
        ProductDTO responseDTO = modelMapper.map(createdProduct, ProductDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{sku}")
    public ResponseEntity<ProductDTO> getProductBySku(@PathVariable String sku) {
        ProductDTO response = findProductUseCase.findBySku(sku);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> listProducts() {
        List<ProductDTO> response = findProductUseCase.findAll();
        return ResponseEntity.ok(response);
    }

    @PostMapping("list/sku")
    public ResponseEntity<List<ProductDTO>> listProductsBySku(@RequestBody List<String> sku) {
        List<ProductDTO> response = findProductUseCase.findAllBySku(sku);
        return ResponseEntity.ok(response);
    }
}