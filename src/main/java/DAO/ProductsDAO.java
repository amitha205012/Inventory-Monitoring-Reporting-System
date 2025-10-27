package DAO;

import model.Product;
import java.util.List;

public interface ProductsDAO {
    int addProduct(Product p);
    List<Product> getAllProducts();
    Product getProductById(int id);
    void updateProduct(Product p);
    void deleteProduct(int id);
    List<Product> getProductsByPriceRange(double min, double max);
}
