import java.io.Serializable;
public class Clothing extends Product implements Serializable{
    private String size;
    private String color;
    private static final long serialVersionUID = 123456789L;

    public Clothing() {
        // Default constructor
        super("", "", 0, 0.0);  // You may need to provide default values
        this.size = "";
        this.color = "";
    }
    public Clothing(String productId, String productName, int availableItems, double price, String size, String color) {
        super(productId, productName, availableItems, price);
        this.size = size;
        this.color = color;
    }

    // Getters and setters for Clothing-specific attributes

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    @Override
    public String toString() {
        return "Clothing{" +
                "productId='" + getProductId() + '\'' +
                ", productName='" + getProductName() + '\'' +
                ", size='" + size + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}