package com.postech.auramsorder.gateway.product;

import com.postech.auramsorder.adapter.dto.ProductDTO;
import com.postech.auramsorder.config.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class ProductService {

    @Value("${product.service.url}")
    private String productServiceUrl;

    private final RestTemplate restTemplate;

    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean verifyProduct(String sku) {
        try {
            ResponseEntity<String> response =
                    restTemplate.getForEntity(productServiceUrl + sku, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            throw new ProductNotFoundException("Product not found: " + sku);
        }
    }

    public BigDecimal getProductPrice(List<String> sku) {
        ResponseEntity<List<ProductDTO>> listProducst = restTemplate.exchange(productServiceUrl + "list/sku",
                HttpMethod.POST,
                new HttpEntity<>(sku),
                new ParameterizedTypeReference<List<ProductDTO>>() {
                });
        if (listProducst.getStatusCode().is2xxSuccessful()) {
            return calculatePriceAmount(listProducst.getBody());
        }
        throw new ProductNotFoundException("Product not found: " + sku);
    }

    public BigDecimal calculatePriceAmount(List<ProductDTO> listProducts) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (ProductDTO productDTO : listProducts) {
            BigDecimal price = productDTO.getPrice().setScale(2, RoundingMode.HALF_UP);
            totalPrice = totalPrice.add(price);
        }
        return totalPrice;
    }

    public String formatarValor(BigDecimal valor) {
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatoMoeda.format(valor);
    }

}
