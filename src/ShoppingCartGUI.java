import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCartGUI extends JFrame {

    private List<Product> shoppingCart;
    private JTable cartTable;
    private JTextArea discountArea;
    private JLabel finalTotalLabel;

    public ShoppingCartGUI(List<Product> shoppingCart) {
        this.shoppingCart = shoppingCart;
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Shopping Cart");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Shopping cart table
        cartTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(cartTable);

        // Discount area
        discountArea = new JTextArea();
        discountArea.setEditable(false);

        // Final total label
        finalTotalLabel = new JLabel("Final Total: $0.00");

        // Layout setup
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(discountArea, BorderLayout.NORTH);
        bottomPanel.add(finalTotalLabel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Populate cart details
        populateCartDetails();
    }

    private void populateCartDetails() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Product");
        model.addColumn("Quantity");
        model.addColumn("Price");

        double products_price = 0;

        for (Product product : shoppingCart) {
            double price = product.getPrice();

            Object[] rowData = {getProductDetails(product), 1, price};  // Fixed quantity of 1
            model.addRow(rowData);

            products_price += price;
        }

        // Apply discount if applicable
        double discount=applyDiscount(products_price);
        double totalPrice=products_price-discount;

        cartTable.setModel(model);

        finalTotalLabel.setText("      Final Total: $" + totalPrice);
    }

    private String getProductDetails(Product product) {
        if (product instanceof Electronics) {
            Electronics electronic = (Electronics) product;
            return "ID: " + product.getProductId() + ", Name: " + product.getProductName() +
                    ", Brand: " + electronic.getBrand() + ", Warranty: " + electronic.getWarrantyPeriod() + " weeks";
        } else if (product instanceof Clothing) {
            Clothing clothing = (Clothing) product;
            return "ID: " + product.getProductId() + ", Name: " + product.getProductName() +
                    ", Color: " + clothing.getColor() + ", Size: " + clothing.getSize();
        } else {
            return "ID: " + product.getProductId() + ", Name: " + product.getProductName();
        }
    }

    private double applyDiscount(double products_price) {
        int electronicsCount = 0;
        int clothingCount = 0;

        // Count the number of products for each class
        for (Product product : shoppingCart) {
            if (product instanceof Electronics) {
                electronicsCount++;
            } else if (product instanceof Clothing) {
                clothingCount++;
            }
        }
        // Check if there are at least three products of the same class
        if (electronicsCount >= 3) {
            // Apply a 20% discount to Electronics
            return applyDiscountToClass(Electronics.class, 0.20,products_price);
        } else if (clothingCount >= 3) {
            // Apply a 20% discount to Clothing
            return applyDiscountToClass(Clothing.class, 0.20,products_price);
        }
        else {
            return 0;
        }

    }

    private double applyDiscountToClass(Class<? extends Product> productClass, double discountPercentage, double products_price) {
        DefaultTableModel model = (DefaultTableModel) cartTable.getModel();

            double discount = products_price * discountPercentage;
            // Accumulate the total discount
        // Update the discount area
        discountArea.setText("\n\tDiscount applied (" + (discountPercentage * 100) + "%): -$" + discount+"\n");
        return discount;
    }
    public static void main(String[] args) {
        // Example usage:
        // Assuming you have a shopping cart (List<Product> shoppingCart) in your WestminsterShoppingManager
        WestminsterShoppingManager manager = new WestminsterShoppingManager();
        manager.loadProductsFromFile();  // Load products from file
        manager.openGUI();  // Open the main shopping GUI
        // ... Perform actions, add products to the shopping cart, etc.

        List<Product> shoppingCart = manager.getShoppingCartList();  // Assuming you have a getter for the shopping cart

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ShoppingCartGUI(shoppingCart).setVisible(true);
            }
        });
    }
}
