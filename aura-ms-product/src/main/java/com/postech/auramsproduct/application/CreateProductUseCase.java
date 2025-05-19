package com.postech.auramsproduct.application;

import com.postech.auramsproduct.domain.Product;
import com.postech.auramsproduct.adapters.dto.ProductDTO;
import com.postech.auramsproduct.gateway.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CreateProductUseCase {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public CreateProductUseCase(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public Product execute(ProductDTO productDto) {
        if (productDto.getSku() == null || productDto.getSku().toString().isEmpty()) {
            throw new IllegalArgumentException("SKU cannot be null or empty");
        }
        if (productDto.getName() == null || productDto.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (productDto.getPrice() == null || productDto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if (productDto.getDescription() == null || productDto.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }

        Product product = new Product(
                productDto.getId(),
                productDto.getName(),
                productDto.getSku(),
                productDto.getPrice(),
                productDto.getDescription());

        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, Product.class);
    }

}