package tn.esprit.devops_project;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.StockRepository;
import tn.esprit.devops_project.services.StockServiceImpl;

import java.util.*;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@AllArgsConstructor
public class StockServiceImplMock {

    @Mock
    StockRepository stockRepository;

    @InjectMocks
    StockServiceImpl stockService;

    Set<Product> products = new HashSet<>();

    Stock stock = new Stock(1, "product1", products);

    List<Stock> listStocks = Arrays.asList(
            new Stock(2, "product2", products),
            new Stock(3, "product3", products)
    );

    @Test
    @Order(1)
    public void testRetrieveStock() {
        Mockito.when(stockRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(stock));

        Stock result = stockService.retrieveStock(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(stock.getIdStock(), result.getIdStock());
        verify(stockRepository, times(1)).findById(1L);
    }

    @Test
    @Order(2)
    public void testAddStock() {
        Mockito.when(stockRepository.save(stock)).thenReturn(stock);

        Stock result = stockService.addStock(stock);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(stock.getIdStock(), result.getIdStock());
        Assertions.assertEquals(stock.getTitle(), result.getTitle());
        verify(stockRepository, times(1)).save(stock);
    }

    @Test
    @Order(3)
    public void testRetrieveAllStock() {
        Mockito.when(stockRepository.findAll()).thenReturn(listStocks);

        List<Stock> result = stockService.retrieveAllStock();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("product2", result.get(0).getTitle());
        verify(stockRepository, times(1)).findAll();
    }

    @Test
    @Order(4)
    public void testRetrieveStock_NotFound() {
        Mockito.when(stockRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NullPointerException.class, () -> stockService.retrieveStock(1L));
        Assertions.assertEquals("Stock not found", exception.getMessage());
        verify(stockRepository, times(1)).findById(1L);
    }
}
