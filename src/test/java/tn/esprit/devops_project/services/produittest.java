package tn.esprit.devops_project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.repositories.StockRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class Produittest {

    private ProductServiceImpl productService;
    private List<Product> productList;
    private List<Stock> stockList;

    // In-memory repositories
    private final ProductRepository productRepository = new ProductRepository() {
        @Override
        public <S extends Product> S save(S entity) {
            productList.add(entity);
            return entity;
        }

        @Override
        public Optional<Product> findById(Long id) {
            return productList.stream().filter(p -> p.getIdProduct().equals(id)).findFirst();
        }

        @Override
        public List<Product> findAll() {
            return new ArrayList<>(productList);
        }

        @Override
        public void deleteById(Long id) {
            productList.removeIf(product -> product.getIdProduct().equals(id));
        }

        @Override
        public List<Product> findByStockIdStock(Long id) {
            return productList.stream()
                    .filter(product -> product.getStock() != null && product.getStock().getIdStock().equals(id))
                    .toList();
        }

        @Override
        public List<Product> findByCategory(ProductCategory category) {
            return productList.stream().filter(p -> p.getCategory() == category).toList();
        }
    };

    private final StockRepository stockRepository = new StockRepository() {
        @Override
        public Optional<Stock> findById(Long id) {
            return stockList.stream().filter(s -> s.getIdStock().equals(id)).findFirst();
        }

        @Override
        public void save(Stock stock) {
            stockList.add(stock);
        }

        @Override
        public List<Stock> findAll() {
            return new ArrayList<>(stockList);
        }
    };

    @BeforeEach
    public void setUp() {
        productList = new ArrayList<>();
        stockList = new ArrayList<>();
        productService = new ProductServiceImpl(productRepository, stockRepository);
    }

    @Test
    public void testAddProduct() {
        // Create a stock and save it
        Stock stock = new Stock();
        stock.setIdStock(1L);
        stockRepository.save(stock);
        
        Product product = new Product();
        product.setTitle("Test Product");
        product.setPrice(10.0f);
        product.setQuantity(100);
        product.setCategory(ProductCategory.ELECTRONICS); // Assuming this category exists
        product.setStock(stock);

        Product savedProduct = productService.addProduct(product, stock.getIdStock());

        assertNotNull(savedProduct);
        assertEquals("Test Product", savedProduct.getTitle());
        assertEquals(10.0f, savedProduct.getPrice());
        assertEquals(100, savedProduct.getQuantity());
        assertEquals(ProductCategory.ELECTRONICS, savedProduct.getCategory());
        assertEquals(stock, savedProduct.getStock());
    }

    @Test
    public void testRetrieveProduct() {
        // Create and save a product
        Stock stock = new Stock();
        stock.setIdStock(1L);
        stockRepository.save(stock);
        
        Product product = new Product();
        product.setIdProduct(1L);
        product.setTitle("Test Product");
        product.setPrice(10.0f);
        product.setQuantity(100);
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setStock(stock);
        
        productRepository.save(product);
        
        Product retrievedProduct = productService.retrieveProduct(1L);

        assertNotNull(retrievedProduct);
        assertEquals(product, retrievedProduct);
    }

    @Test
    public void testRetrieveAllProducts() {
        // Create and save products
        Stock stock = new Stock();
        stock.setIdStock(1L);
        stockRepository.save(stock);
        
        Product product1 = new Product();
        product1.setIdProduct(1L);
        product1.setTitle("Product 1");
        product1.setPrice(10.0f);
        product1.setQuantity(100);
        product1.setCategory(ProductCategory.ELECTRONICS);
        product1.setStock(stock);
        
        Product product2 = new Product();
        product2.setIdProduct(2L);
        product2.setTitle("Product 2");
        product2.setPrice(20.0f);
        product2.setQuantity(200);
        product2.setCategory(ProductCategory.ELECTRONICS);
        product2.setStock(stock);
        
        productRepository.save(product1);
        productRepository.save(product2);
        
        List<Product> products = productService.retreiveAllProduct();

        assertNotNull(products);
        assertEquals(2, products.size());
    }

    @Test
    public void testRetrieveProductByCategory() {
        // Create and save products
        Stock stock = new Stock();
        stock.setIdStock(1L);
        stockRepository.save(stock);
        
        Product product1 = new Product();
        product1.setIdProduct(1L);
        product1.setTitle("Product 1");
        product1.setPrice(10.0f);
        product1.setQuantity(100);
        product1.setCategory(ProductCategory.ELECTRONICS);
        product1.setStock(stock);
        
        Product product2 = new Product();
        product2.setIdProduct(2L);
        product2.setTitle("Product 2");
        product2.setPrice(20.0f);
        product2.setQuantity(200);
        product2.setCategory(ProductCategory.CLOTHING); // Different category
        product2.setStock(stock);
        
        productRepository.save(product1);
        productRepository.save(product2);
        
        List<Product> products = productService.retrieveProductByCategory(ProductCategory.ELECTRONICS);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(product1, products.get(0));
    }

    @Test
    public void testDeleteProduct() {
        // Create and save a product
        Stock stock = new Stock();
        stock.setIdStock(1L);
        stockRepository.save(stock);
        
        Product product = new Product();
        product.setIdProduct(1L);
        product.setTitle("Test Product");
        product.setPrice(10.0f);
        product.setQuantity(100);
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setStock(stock);
        
        productRepository.save(product);
        
        productService.deleteProduct(1L);

        assertFalse(productRepository.findAll().contains(product));
    }

    @Test
    public void testRetrieveProductStock() {
        // Create and save products
        Stock stock = new Stock();
        stock.setIdStock(1L);
        stockRepository.save(stock);
        
        Product product1 = new Product();
        product1.setIdProduct(1L);
        product1.setTitle("Product 1");
        product1.setPrice(10.0f);
        product1.setQuantity(100);
        product1.setCategory(ProductCategory.ELECTRONICS);
        product1.setStock(stock);
        
        Product product2 = new Product();
        product2.setIdProduct(2L);
        product2.setTitle("Product 2");
        product2.setPrice(20.0f);
        product2.setQuantity(200);
        product2.setCategory(ProductCategory.ELECTRONICS);
        product2.setStock(stock);
        
        productRepository.save(product1);
        productRepository.save(product2);
        
        List<Product> products = productService.retreiveProductStock(stock.getIdStock());

        assertNotNull(products);
        assertEquals(2, products.size());
    }
}
