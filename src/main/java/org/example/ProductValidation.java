package org.example;

import java.util.Scanner;

 class ProductValidation {

    public static boolean isValidProduct(Products product) {
        if (product == null) {
            System.out.println("Validation Failed: Product is null.");
            return false;
        }
        if (product.getProductId() == null || product.getProductId().trim().isEmpty()) {
            System.out.println("Validation Failed: Product ID is missing.");
            return false;
        }
        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            System.out.println("Validation Failed: Product name is missing.");
            return false;
        }
        if (product.getPrice() < 0) {
            System.out.println("Validation Failed: Price cannot be negative.");
            return false;
        }
        if (product.getQuantity() < 0) {
            System.out.println("Validation Failed: Quantity cannot be negative.");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("=== Product Entry System ===");

        // Create product using Scanner input
        Products product = Products.createProductFromInput();

        // Validate product
        if (isValidProduct(product)) {
            System.out.println("\n✅ Product is valid!");
            product.displayProduct();
        } else {
            System.out.println("\n❌ Product validation failed.");
        }
    }
}
