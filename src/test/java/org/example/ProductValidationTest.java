package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductValidationTest {

    @Test
    void testValidProduct() {
        // Create product directly (no user input)
        Products product = new Products("P101", "Laptop", "High-end laptop", 85000, 5);

        boolean result = ProductValidation.isValidProduct(product);

        if (result) {
            System.out.println("\n✅ Product is valid!");
            product.displayProduct();
        } else {
            System.out.println("\n❌ Product validation failed.");
        }

        // Assertion for automated testing
        assertTrue(result, "Product should be valid if inputs are correct.");
    }

    @Test
    void testInvalidProduct() {
        // Example of invalid product (empty ID)
        Products product = new Products("", "Laptop", "High-end laptop", 85000, 5);

        boolean result = ProductValidation.isValidProduct(product);

        if (!result) {
            System.out.println("\n❌ Product validation failed (as expected).");
        } else {
            System.out.println("\n⚠️ Product should have been invalid!");
        }

        assertFalse(result, "Product should be invalid if ID is empty.");
    }
}
