package org.example;

import java.util.Scanner;

 class Products {
    private String productId;
    private String productName;
    private String description;
    private double price;
    private int quantity;

    public Products(String productId, String productName, String description, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters
    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setPrice(double price) {
        if (price < 0) {
            System.out.println("Error: Price cannot be negative.");
            return;
        }
        this.price = price;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            System.out.println("Error: Quantity cannot be negative.");
            return;
        }
        this.quantity = quantity;
    }

    // Utility methods
    public void updateQuantity(int quantity) {
        if (this.quantity + quantity < 0) {
            System.out.println("Error: Insufficient quantity in stock.");
            return;
        }
        this.quantity += quantity;
    }

    public boolean isInStock() {
        return quantity > 0;
    }

    public void displayProduct() {
        System.out.println("\n--- Product Details ---");
        System.out.println("Product ID: " + productId);
        System.out.println("Product Name: " + productName);
        System.out.println("Description: " + description);
        System.out.println("Price: " + price);
        System.out.println("Quantity: " + quantity);
    }

    // Method to create a product using Scanner
    public static Products createProductFromInput() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Product ID: ");
        String id = sc.nextLine();

        System.out.print("Enter Product Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Description: ");
        String desc = sc.nextLine();

        System.out.print("Enter Price: ");
        double price = sc.nextDouble();

        System.out.print("Enter Quantity: ");
        int qty = sc.nextInt();

        return new Products(id, name, desc, price, qty);
    }
}
