import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ShoppingGUI extends JFrame {
    private List<Product> productList; // Assuming productList is populated somewhere
    private List<Product> shoppingCartList =new ArrayList<>(); // Assuming shoppingCart is populated somewhere


    private JComboBox<String> productTypeComboBox;
    private JTable productTable;
    private JTextArea productDetailsTextArea;
    private JButton addToCartButton;
    private JButton viewShoppingCartButton;
    private JScrollPane tableScrollPane;

    public ShoppingGUI(List<Product> productList, List<Product>  shoppingCartList) {
        this.productList = productList;
        this.shoppingCartList = shoppingCartList;
        initializeGUI();
        populateTable("all"); // Display all products initially
        productTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Get the selected row index
                int selectedRow = productTable.getSelectedRow();

                // If a row is selected
                if (selectedRow >= 0) {
                    // Display product details in the text area
                    displayProductDetails(productList.get(selectedRow));
                }
            }
        });
    }

    private void displayProductDetails(Product product) {
        String details = "Product ID: " + product.getProductId() + "\n" +
                "Product Name: " + product.getProductName() + "\n" +
                "Available Items: " + product.getAvailableItems() + "\n" +
                "Price: " + product.getPrice() +product;
        productDetailsTextArea.setText(details);
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        populateTable("all"); // Call a method to populate the table with the new product list
    }

    private void initializeGUI() {
        setTitle("Shopping GUI");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Product type dropdown
        String[] productTypes = {"All", "Electronics", "Clothes"};
        productTypeComboBox = new JComboBox<>(productTypes);
        productTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) productTypeComboBox.getSelectedItem();
                populateTable(selectedType);
            }
        });

        // Product table
        productTable = new JTable();
        tableScrollPane = new JScrollPane(productTable);

        // Product details text area
        productDetailsTextArea = new JTextArea();
        productDetailsTextArea.setEditable(false);

        // Buttons
        addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row index
                int selectedRow = productTable.getSelectedRow();

                // If a row is selected
                if (selectedRow >= 0) {
                    // Get the selected product
                    Product selectedProduct = productList.get(selectedRow);
                    // Add the selected product to the shopping cart
                    shoppingCartList.add(selectedProduct);
                    // Display a message or update the GUI to indicate success
                    JOptionPane.showMessageDialog(ShoppingGUI.this, "Product added to the shopping cart");

                    // Optionally update the shopping cart details in the GUI
                    // shoppingCartDetailsTextArea.setText(shoppingCart.getDetails());
                } else {
                    // If no row is selected, display an error message
                    JOptionPane.showMessageDialog(ShoppingGUI.this, "Please select a product to add to the shopping cart", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        viewShoppingCartButton = new JButton("View Shopping Cart");
        viewShoppingCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the Shopping Cart GUI
                openShoppingCartGUI();
            }
        });

        // Layout setup
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Select Product Type:"));
        controlPanel.add(productTypeComboBox);
        controlPanel.add(viewShoppingCartButton);

        JPanel productDetailsPanel = new JPanel();
        productDetailsPanel.setLayout(new BoxLayout(productDetailsPanel, BoxLayout.Y_AXIS));
        productDetailsPanel.add(productDetailsTextArea);
        productDetailsPanel.add(addToCartButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(productDetailsPanel, BorderLayout.SOUTH);

        add(controlPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void populateTable(String productType) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Product ID");
        model.addColumn("Product Name");
        model.addColumn("Available Items");
        model.addColumn("Price");

        for (Product product : productList) {
            if (productType.equals("All") || (productType.equals("Electronics") && product instanceof Electronics)
                    || (productType.equals("Clothes") && product instanceof Clothing)) {
                Object[] rowData = {product.getProductId(), product.getProductName(),
                        product.getAvailableItems(), product.getPrice()};
                model.addRow(rowData);
            }
        }

        productTable.setModel(model);
    }

    private void openShoppingCartGUI() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ShoppingCartGUI(shoppingCartList).setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        WestminsterShoppingManager manager = new WestminsterShoppingManager();
        manager.loadProductsFromFile(); // or add products

        // Create the GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ShoppingGUI shoppingGUI = new ShoppingGUI(manager.getProductList(), manager.getShoppingCartList());
                shoppingGUI.setVisible(true);
            }
        });
    }
}
