package com.postech.auramsorder.gateway.stock;

import com.postech.auramsorder.adapter.dto.RequestStockReserveDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class StockService {

    @Value("${stock.service.url}")
    private String stockServiceUrl;

    private final RestTemplate restTemplate;

    public StockService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

//    public boolean reserveStock(List<RequestStockReserveDTO> items) {
//        for (RequestStockReserveDTO item : items) {
//            try {
//                ResponseEntity<Boolean> response =
//                        restTemplate.postForEntity(stockServiceUrl + "/new-reserve", item, Boolean.class);
//                if (!Boolean.TRUE.equals(response.getBody())) {
//                    return false;
//                }
//            } catch (Exception e) {
//                return false;
//            }
//        }
//        return true;
//    }

    public boolean reserveStock(List<RequestStockReserveDTO> items) {
        try {
            ResponseEntity<Boolean> response =
                    restTemplate.postForEntity(stockServiceUrl + "/new-reserve", items, Boolean.class);
            return Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            return false;
        }
    }

    public void releaseStock(List<RequestStockReserveDTO> items) {
        for (RequestStockReserveDTO item : items) {
            try {
                restTemplate.postForEntity(stockServiceUrl + "/return", item, Boolean.class);
            } catch (Exception e) {
                log.error("Falha ao realizar busca no estoque " + item.getSku(), e);
            }
        }
    }
}
