package tn.esprit.devops_project;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@AllArgsConstructor
public class StockServiceImplMock {
    @Mock
    StockRepository stockRepository;

    @InjectMocks
    StockServiceImpl stockService;


    Set<Product> products = new HashSet<Product>();


    Stock stock = new Stock(1,"product1",products);


    List<Stock> listStocks = new ArrayList<Stock>() {
        {
            add(new Stock(2,"product2",products));
            add(new Stock(3,"product3",products));
        }
    };


    @Test
    public void testRetrieveStock() {
        Mockito.when(stockRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(stock));
        Stock stock1 = stockService.retrieveStock((long)1);
        Assertions.assertNotNull(stock1);
    }
}
