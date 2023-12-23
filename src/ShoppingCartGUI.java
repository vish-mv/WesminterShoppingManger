import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ShoppingCartGUI extends JFrame {

    private List<Product> shoppingCart;
    private JTextArea cartDetailsTextArea;

    public ShoppingCartGUI(List<Product> shoppingCart) {
        this.shoppingCart = shoppingCart;
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Shopping Cart");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Shopping cart details text area
        cartDetailsTextArea = new JTextArea();
        cartDetailsTextArea.setEditable(false);

        // Populate shopping cart details
        populateCartDetails();

        // Layout setup
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(cartDetailsTextArea), BorderLayout.CENTER);

        add(mainPanel);
    }

    private void populateCartDetails() {
        StringBuilder cartDetails = new StringBuilder();
        double totalPrice = 0;

        if (shoppingCart == null || shoppingCart.isEmpty()) {
            cartDetails.append("No products in the cart.");
        } else {
            for (Product product : shoppingCart) {
                cartDetails.append("Product ID: ").append(product.getProductId()).append("\n");
                cartDetails.append("Product Name: ").append(product.getProductName()).append("\n");
                cartDetails.append("Price: $").append(product.getPrice()).append("\n");
                cartDetails.append("\n");

                totalPrice += product.getPrice();
            }
        }

        cartDetails.append("Total Price: $").append(totalPrice).append("\n");

        cartDetailsTextArea.setText(cartDetails.toString());
    }

    public static void main(String[] args) {
        // Example usage:
        // Assuming you have a shopping cart (List<Product> shoppingCart) in your WestminsterShoppingManager
        WestminsterShoppingManager manager = new WestminsterShoppingManager();
        manager.loadProductsFromFile();  // Load products from file
        manager.openGUI();  // Open the main shopping GUI
        // ... Perform actions, add products to the shopping cart, etc.

        List<Product> shoppingCart = manager.getShoppingCartList();  // Assuming you have a getter for shopping cart

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ShoppingCartGUI(shoppingCart).setVisible(true);
            }
        });
    }
}
