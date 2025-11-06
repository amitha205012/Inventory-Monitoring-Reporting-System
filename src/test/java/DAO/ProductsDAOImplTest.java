package DAO;

import model.Product;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductsDAOImplTest {

    @Mock
    Connection mockConnection;

    @Mock
    PreparedStatement mockStatement;

    @Mock
    ResultSet mockResultSet;

    @InjectMocks
    ProductsDAOImpl dao;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        dao = new ProductsDAOImpl(mockConnection);
    }

    @Test
    void testAddProduct() throws Exception {
        Product p = new Product(0, "Laptop", "Gaming Laptop", 1200.0, 10, 5);
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        int id = dao.addProduct(p);
        assertTrue(id > 0);

        verify(mockStatement, times(1)).executeUpdate();
    }
}
