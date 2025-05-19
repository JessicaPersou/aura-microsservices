package com.postech.auramsstock.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.auramsstock.adapters.dto.ReserveStockDTO;
import com.postech.auramsstock.adapters.dto.StockDTO;
import com.postech.auramsstock.application.DeleteStockUseCase;
import com.postech.auramsstock.application.FindStockUseCase;
import com.postech.auramsstock.application.ReserveStockUseCase;
import com.postech.auramsstock.application.UpdateStockUseCase;
import com.postech.auramsstock.database.jpa.entity.StockEntity;
import com.postech.auramsstock.domain.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class StockControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReserveStockUseCase reserveStockUseCase;

    @Mock
    private FindStockUseCase findStockUseCase;

    @Mock
    private UpdateStockUseCase updateStockUseCase;

    @Mock
    private DeleteStockUseCase deleteStockUseCase;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StockController stockController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(stockController).build();
        objectMapper = new ObjectMapper();
    }

//    @Test
//    public void testGetStockById() throws Exception {
//        Long stockId = 1L;
//        StockEntity stockEntity = new StockEntity();
//        StockDTO stockDTO = new StockDTO();
//
//        when(findStockUseCase.findById(stockId)).thenReturn(Optional.of(stockEntity));
//        when(modelMapper.map(stockEntity, StockDTO.class)).thenReturn(stockDTO);
//
//        mockMvc.perform(get("/api/v1/stocks/{id}", stockId))
//                .andExpect(status().isOk());
//
//        verify(findStockUseCase, times(1)).findById(stockId);
//        verify(modelMapper, times(1)).map(stockEntity, StockDTO.class);
//    }
//
//    @Test
//    public void testReserveProcess() throws Exception {
//        ReserveStockDTO reserveStockDTO = new ReserveStockDTO();
//        String requestBody = objectMapper.writeValueAsString(List.of(reserveStockDTO));
//
//        doNothing().when(reserveStockUseCase).reserveStock(anyList());
//
//        mockMvc.perform(post("/api/v1/stocks/new-reserve")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isOk())
//                .andExpect(content().string("true"));
//
//        verify(reserveStockUseCase, times(1)).reserveStock(anyList());
//    }

    @Test
    public void testStockReturn() throws Exception {
        ReserveStockDTO reserveStockDTO = new ReserveStockDTO();
        String requestBody = objectMapper.writeValueAsString(reserveStockDTO);

        doNothing().when(reserveStockUseCase).returnStock(any(ReserveStockDTO.class));

        mockMvc.perform(post("/api/v1/stocks/return")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(reserveStockUseCase, times(1)).returnStock(any(ReserveStockDTO.class));
    }

    @Test
    public void testDeleteStock() throws Exception {
        Long stockId = 1L;

        doNothing().when(deleteStockUseCase).delete(stockId);

        mockMvc.perform(delete("/api/v1/stocks/{id}", stockId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(deleteStockUseCase, times(1)).delete(stockId);
    }

    @Test
    public void testUpdateStock() throws Exception {
        Long stockId = 1L;
        StockDTO stockDTO = new StockDTO();
        Stock updatedStock = new Stock();
        StockDTO updatedStockDTO = new StockDTO();
        String requestBody = objectMapper.writeValueAsString(stockDTO);

        when(updateStockUseCase.updateStock(eq(stockId), any(StockDTO.class))).thenReturn(updatedStock);
        when(modelMapper.map(updatedStock, StockDTO.class)).thenReturn(updatedStockDTO);

        mockMvc.perform(put("/api/v1/stocks/{id}", stockId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(updateStockUseCase, times(1)).updateStock(eq(stockId), any(StockDTO.class));
        verify(modelMapper, times(1)).map(updatedStock, StockDTO.class);
    }
}