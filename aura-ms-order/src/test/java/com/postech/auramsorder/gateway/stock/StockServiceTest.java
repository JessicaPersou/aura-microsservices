package com.postech.auramsorder.gateway.stock;

import com.postech.auramsorder.adapter.dto.RequestStockReserveDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class StockServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StockService stockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stockService.setStockServiceUrl("http://mock-stock-service");
    }

    @Test
    void testReserveStock_Success() {
        List<RequestStockReserveDTO> items = Collections.singletonList(new RequestStockReserveDTO());
        when(restTemplate.postForEntity(eq("http://mock-stock-service/new-reserve"), eq(items), eq(Boolean.class)))
                .thenReturn(ResponseEntity.ok(true));

        boolean result = stockService.reserveStock(items);

        assertTrue(result);
        verify(restTemplate, times(1))
                .postForEntity(eq("http://mock-stock-service/new-reserve"), eq(items), eq(Boolean.class));
    }

    @Test
    void testReserveStock_Failure() {
        List<RequestStockReserveDTO> items = Collections.singletonList(new RequestStockReserveDTO());
        when(restTemplate.postForEntity(anyString(), any(), eq(Boolean.class)))
                .thenThrow(new RuntimeException("Error"));

        boolean result = stockService.reserveStock(items);

        assertFalse(result);
        verify(restTemplate, times(1))
                .postForEntity(eq("http://mock-stock-service/new-reserve"), eq(items), eq(Boolean.class));
    }

    @Test
    void testReleaseStock_Success() {
        RequestStockReserveDTO item = new RequestStockReserveDTO();
        List<RequestStockReserveDTO> items = Collections.singletonList(item);

        stockService.releaseStock(items);

        verify(restTemplate, times(1))
                .postForEntity(eq("http://mock-stock-service/return"), eq(item), eq(Boolean.class));
    }

    @Test
    void testReleaseStock_Failure() {
        RequestStockReserveDTO item = new RequestStockReserveDTO();
        List<RequestStockReserveDTO> items = Collections.singletonList(item);
        doThrow(new RuntimeException("Error"))
                .when(restTemplate).postForEntity(anyString(), any(), eq(Boolean.class));

        stockService.releaseStock(items);

        verify(restTemplate, times(1))
                .postForEntity(eq("http://mock-stock-service/return"), eq(item), eq(Boolean.class));
    }
}