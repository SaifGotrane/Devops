package tn.esprit.devops_project;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.repositories.InvoiceRepository;
import tn.esprit.devops_project.repositories.OperatorRepository;
import tn.esprit.devops_project.repositories.SupplierRepository;
import tn.esprit.devops_project.services.InvoiceServiceImpl;

import java.util.*;

@ExtendWith(MockitoExtension.class)  // Automatically initializes mocks
class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private OperatorRepository operatorRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Test
    void testRetrieveAllInvoices() {
        Invoice invoice1 = new Invoice();
        Invoice invoice2 = new Invoice();
        when(invoiceRepository.findAll()).thenReturn(List.of(invoice1, invoice2));

        List<Invoice> invoices = invoiceService.retrieveAllInvoices();

        assertNotNull(invoices, "Invoices list should not be null");
        assertEquals(2, invoices.size(), "Invoices list size should be 2");
    }

    @Test
    void testCancelInvoice() {
        Invoice invoice = new Invoice();
        invoice.setIdInvoice(1L);
        invoice.setArchived(false);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        invoiceService.cancelInvoice(1L);

        assertTrue(invoice.getArchived(), "Invoice should be archived after cancellation");
        verify(invoiceRepository, times(1)).save(invoice);
    }

    @Test
    void testRetrieveInvoice() {
        Invoice invoice = new Invoice();
        invoice.setIdInvoice(1L);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        Invoice result = invoiceService.retrieveInvoice(1L);

        assertNotNull(result, "Retrieved invoice should not be null");
        assertEquals(1L, result.getIdInvoice(), "Invoice ID should match");
    }

    @Test
    void testGetInvoicesBySupplier() {
        // Setup
        Supplier supplier = new Supplier();
        Invoice invoice = new Invoice();
        supplier.setInvoices(Set.of(invoice));  // Initialize with a Set

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        // Execution
        List<Invoice> invoices = invoiceService.getInvoicesBySupplier(1L);  // No need for conversion here

        // Assertion
        assertNotNull(invoices, "Invoices list should not be null");
        assertEquals(1, invoices.size(), "Invoices list size should be 1");
    }


    @Test
    void testAssignOperatorToInvoice() {
        Invoice invoice = new Invoice();
        invoice.setIdInvoice(1L);
        Operator operator = new Operator();
        operator.setInvoices(new HashSet<>());

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(operatorRepository.findById(1L)).thenReturn(Optional.of(operator));

        invoiceService.assignOperatorToInvoice(1L, 1L);

        assertTrue(operator.getInvoices().contains(invoice), "Operator should be assigned to the invoice");
        verify(operatorRepository, times(1)).save(operator);
    }

    @Test
    void testGetTotalAmountInvoiceBetweenDates() {
        Date startDate = new Date();
        Date endDate = new Date();
        when(invoiceRepository.getTotalAmountInvoiceBetweenDates(startDate, endDate)).thenReturn(1000.0f);

        float totalAmount = invoiceService.getTotalAmountInvoiceBetweenDates(startDate, endDate);

        assertEquals(1000.0f, totalAmount, "Total amount should match expected value");
    }

    @Test
    void testCancelInvoice_NotFound() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> invoiceService.cancelInvoice(1L));
    }
}
