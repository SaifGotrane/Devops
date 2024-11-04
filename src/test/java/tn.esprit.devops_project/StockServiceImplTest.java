package tn.esprit.devops_project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.StockRepository;
import tn.esprit.devops_project.services.StockServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class  StockServiceImplTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockServiceImpl stockService; // Changer ici pour utiliser StockServiceImpl

    private Stock stock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stock = new Stock();
        stock.setIdStock(1L);
        stock.setTitle("Stock Title");
    }

    @Test
    void testAddStock() {
        when(stockRepository.save(stock)).thenReturn(stock);

        Stock addedStock = stockService.addStock(stock);
        assertNotNull(addedStock);
        assertEquals(stock.getIdStock(), addedStock.getIdStock());
        assertEquals(stock.getTitle(), addedStock.getTitle());

        verify(stockRepository, times(1)).save(stock);
    }

    @Test
    void testRetrieveStock() {
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));

        Stock retrievedStock = stockService.retrieveStock(1L);
        assertNotNull(retrievedStock);
        assertEquals(stock.getIdStock(), retrievedStock.getIdStock());
        assertEquals(stock.getTitle(), retrievedStock.getTitle());

        verify(stockRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveAllStock() {
        Stock stock2 = new Stock();
        stock2.setIdStock(2L);
        stock2.setTitle("Another Stock");

        when(stockRepository.findAll()).thenReturn(Arrays.asList(stock, stock2));

        List<Stock> stocks = stockService.retrieveAllStock();
        assertNotNull(stocks);
        assertEquals(2, stocks.size());
        assertEquals(stock.getTitle(), stocks.get(0).getTitle());

        verify(stockRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveStock_NotFound() {
        when(stockRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NullPointerException.class, () -> stockService.retrieveStock(1L));
        assertEquals("Stock not found", exception.getMessage());

        verify(stockRepository, times(1)).findById(1L);
    }
}