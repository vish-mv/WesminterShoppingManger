import java.io.Serializable;
/*
  Represents electronic products and extends the `Product` class.
  Includes attributes such as the brand and warranty period.
  Implements the `Serializable` interface for object serialization support.
 */
public class Electronics extends Product implements Serializable {
    private String brand;
    private int warrantyPeriod;
    private static final long serialVersionUID = 123456789L;

    public Electronics() {
        // Default constructor
        super("", "", 0, 0.0);  // You may need to provide default values
        this.brand = "";
        this.warrantyPeriod = 0;
    }
    public Electronics(String productId, String productName, int availableItems, double price, String brand, int warrantyPeriod) {
        super(productId, productName, availableItems, price);
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }

    // Getters and setters for Electronics-specific attributes

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }
    @Override
    public String toString() {
        return "Product Name: " + getProductName() +"\n"+
                "Product ID: " + getProductId() +"\n"+
                "Price: " + getPrice() +"$"+"\n"+
                "Brand: " + getBrand() +"\n"+
                "Warranty: " + getWarrantyPeriod()+ " Weeks";
    }
}