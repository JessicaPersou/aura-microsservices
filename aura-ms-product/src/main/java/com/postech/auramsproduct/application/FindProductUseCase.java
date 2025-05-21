package com.postech.auramsproduct.application;

import com.postech.auramsproduct.adapters.dto.ProductDTO;
import com.postech.auramsproduct.config.exception.ProductNotFoundException;
import com.postech.auramsproduct.domain.Product;
import com.postech.auramsproduct.gateway.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindProductUseCase {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public FindProductUseCase(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public ProductDTO findBySku(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado com SKU: " + sku));

        return modelMapper.map(product, ProductDTO.class);
    }

    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado com ID: " + id));

        return modelMapper.map(product, ProductDTO.class);
    }

    public List<ProductDTO> findAllBySku(List<String> sku) {
        return productRepository.findAllProductsBySku(sku).stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }
}