package DAO;

import model.Product;
import org.junit.jupiter.api.*;
import testutils.TestDbUtils;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductsDAOImplIT {

    private ProductsDAO dao;
    private DataSource ds;

    @BeforeEach
    void setup() throws Exception {
        ds = TestDbUtils.createH2DataSource();
        TestDbUtils.runSqlScript(ds, "/Test.sql");
        dao = new ProductsDAOImpl(ds);
    }

    @Test
    void testCRUD() throws Exception {
        Product p = new Product(0, "Laptop", "Gaming Laptop", 1200.0, 10, 5);
        int id = dao.addProduct(p);
        assertTrue(id > 0);

        Product fetched = dao.getProductById(id);
        assertEquals("Laptop", fetched.getName());

        fetched.setName("Updated Laptop");
        dao.updateProduct(fetched);
        Product updated = dao.getProductById(id);
        assertEquals("Updated Laptop", updated.getName());

        List<Product> list = dao.getAllProducts();
        assertTrue(list.size() > 0);

        dao.deleteProduct(id);
        assertNull(dao.getProductById(id));
    }
}
