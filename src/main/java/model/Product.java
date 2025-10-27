package model;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private int threeFoldLimit;

    public Product(int id, String name, String description, double price, int quantity, int threeFoldLimit) {
        this.id = id; this.name = name; this.description = description;
        this.price = price; this.quantity = quantity; this.threeFoldLimit = threeFoldLimit;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public int getThreeFoldLimit() { return threeFoldLimit; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setThreeFoldLimit(int threeFoldLimit) { this.threeFoldLimit = threeFoldLimit; }

    @Override
    public String toString() {
        String status = quantity <= threeFoldLimit ? "⚠️ Low Stock" : "✅ In stock";
        return String.format("📦 ID:%d | %s | ₹%.2f | Qty:%d | Limit:%d | %s",
                id, name, price, quantity, threeFoldLimit, status);
    }
}
