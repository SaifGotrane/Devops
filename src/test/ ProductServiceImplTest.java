package tn.esprit.devops_project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.repositories.StockRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Stock stock;
    private Product product;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Set up mock objects for each test
        stock = new Stock();
        stock.setIdStock(1L);
        
        product = new Product();
        product.setIdProduct(1L);
        product.setTitle("Product 1");
        product.setPrice(100.0f);
        product.setQuantity(10);
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setStock(stock);
    }

    @Test
    void testAddProduct() {
        // Arrange
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        Product savedProduct = productService.addProduct(product, 1L);

        // Assert
        assertNotNull(savedProduct);
        assertEquals("Product 1", savedProduct.getTitle());
        assertEquals(100.0f, savedProduct.getPrice());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testRetrieveProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        Product retrievedProduct = productService.retrieveProduct(1L);

        // Assert
        assertNotNull(retrievedProduct);
        assertEquals("Product 1", retrievedProduct.getTitle());
        assertEquals(100.0f, retrievedProduct.getPrice());
    }

    @Test
    void testRetrieveProductNotFound() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> {
            productService.retrieveProduct(1L);
        });
        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void testRetrieveAllProduct() {
        // Arrange
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        // Act
        var products = productService.retreiveAllProduct();

        // Assert
        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
    }

    @Test
    void testRetrieveProductByCategory() {
        // Arrange
        when(productRepository.findByCategory(ProductCategory.ELECTRONICS)).thenReturn(Arrays.asList(product));

        // Act
        var products = productService.retrieveProductByCategory(ProductCategory.ELECTRONICS);

        // Assert
        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
        assertEquals(ProductCategory.ELECTRONICS, products.get(0).getCategory());
    }

    @Test
    void testDeleteProduct() {
        // Arrange
        doNothing().when(productRepository).deleteById(1L);

        // Act
        productService.deleteProduct(1L);

        // Assert
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRetrieveProductStock() {
        // Arrange
        when(productRepository.findByStockIdStock(1L)).thenReturn(Arrays.asList(product));

        // Act
        var products = productService.retreiveProductStock(1L);

        // Assert
        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
        assertEquals(1L, products.get(0).getStock().getIdStock());
    }
}
