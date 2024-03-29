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
        private List<Product> shoppingCartList; // Assuming shoppingCart is populated somewhere
        private List<User> userList;
        private boolean login_checker=false;
        private static User current_user;


        private JComboBox<String> productTypeComboBox;
        private JTable productTable;
        private JTextArea productDetailsTextArea;
        private JButton addToCartButton;
        private JButton viewShoppingCartButton;
        private JButton loginButton;
        private JButton registerButton;
        private JScrollPane tableScrollPane;

        public ShoppingGUI(List<Product> productList, List<Product>  shoppingCartList, List<User> userList) {
            this.productList = productList;
            this.shoppingCartList = shoppingCartList;
            this.userList=userList;
            initializeGUI();
            populateTable("All"); // Display all products initially
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
            String details = "Selected Product - Details" + "\n\n" +
                    "Product ID: " + product.getProductId() + "\n" +
                    "Product Name: " + product.getProductName() + "\n" +
                    "Available Items: " + product.getAvailableItems() + "\n" +
                    "Price: " + product.getPrice() + "\n";

            // Create a JLabel to display the bold text

            if (product instanceof Electronics) {
                Electronics electronic = (Electronics) product; // Cast to Electronics
                details += "Brand: " + electronic.getBrand() + "\n";
                details += "Warranty: " + electronic.getWarrantyPeriod() + " weeks\n";
            } else if (product instanceof Clothing) {
                Clothing clothe = (Clothing) product; // Cast to Electronics
                details += "Colour: " + clothe.getColor() + "\n";
                details += "Size: " + clothe.getSize() + "\n";
            }
            productDetailsTextArea.setText(details);
        }

        public void setProductList(List<Product> productList) {
            this.productList = productList;
            populateTable("All"); // Call a method to populate the table with the new product list
        }

        private void initializeGUI() {
            setTitle("Shopping GUI");
            setSize(800, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setMinimumSize(new Dimension(700, 500));
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
                    if(login_checker){
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
                    else{
                     showLoginRegisterOptionBox();
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
            loginButton = new JButton("Login");
            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openLoginGUI();
                }
            });
            registerButton = new JButton("Register");
            registerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openRegisterGUI();
                }
            });

            // Layout setup
            JPanel controlPanel = new JPanel();
            controlPanel.add(new JLabel("Select Product Type:"));
            controlPanel.add(productTypeComboBox);
            controlPanel.add(viewShoppingCartButton);
            controlPanel.add(loginButton);
            controlPanel.add(registerButton);

            JPanel productDetailsPanel = new JPanel();
            productDetailsPanel.setLayout(new BoxLayout(productDetailsPanel, BoxLayout.Y_AXIS));
            productDetailsPanel.add(productDetailsTextArea);
            productDetailsPanel.add(addToCartButton);

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(tableScrollPane, BorderLayout.CENTER);
            mainPanel.add(productDetailsPanel, BorderLayout.SOUTH);

            add(controlPanel, BorderLayout.NORTH);
            add(mainPanel, BorderLayout.CENTER);
            productDetailsTextArea.setText("""
                    Selected Product - Details
                    
                    Click On a Product To See Information
                    """);
        }

        private void populateTable(String productType) {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Product ID");
            model.addColumn("Product Name");
            model.addColumn("Available Items");
            model.addColumn("Price");
            model.addColumn("Info");

            List<Integer> rowsToColor = new ArrayList<>(); // Keep track of rows to color

            for (int i = 0; i < productList.size(); i++) {
                Product product = productList.get(i);
                if (productType.equals("All") || (productType.equals("Electronics") && product instanceof Electronics)
                        || (productType.equals("Clothes") && product instanceof Clothing)) {
                    Object[] rowData = {product.getProductId(), product.getProductName(),
                            product.getAvailableItems(), product.getPrice(), getInfoText(product)};
                    model.addRow(rowData);

                    // Check if available items are less than 4 and mark the row for color change
                    if (product.getAvailableItems() < 4) {
                        rowsToColor.add(i);
                    }
                }
            }

            // Set the model to the table
            productTable.setModel(model);

            // Change the color of specified rows
            for (int row : rowsToColor) {
                model.setValueAt("<html><font color='red'>" + model.getValueAt(row, 2) + "</font></html>", row, 2);
            }
        }

        private String getInfoText(Product product) {
            if (product instanceof Electronics) {
                Electronics electronic = (Electronics) product;
                return electronic.getBrand() + " , " + electronic.getWarrantyPeriod() + " weeks warranty";
            } else if (product instanceof Clothing) {
                Clothing clothe = (Clothing) product;
                return clothe.getSize()+ " , " + clothe.getColor() ;
            } else {
                return "";  // Add handling for other product types if needed
            }
        }
        private void showLoginRegisterOptionBox() {
            String[] options = {"Login", "Register"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "To proceed, please login or register.",
                    "Login/Register",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            // Process the user's choice
            switch (choice) {
                case 0: // Login
                    openLoginGUI();
                    break;
                case 1: // Register
                    openRegisterGUI();
                    break;
                default:
                    break;
            }
        }
        private void openLoginGUI() {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new LoginGUI(ShoppingGUI.this, userList).setVisible(true);
                }
            });
        }
        public void updateLoginButton(User user) {
            loginButton.setText(user.getName());
            loginButton.setEnabled(false); // Disable the button after login
            registerButton.setVisible(false); // Hide the register button
            current_user=user;
            login_checker=true;
        }


        private void openRegisterGUI() {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new RegisterGUI(userList).setVisible(true);
                }
            });
        }

        private void openShoppingCartGUI() {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ShoppingCartGUI(shoppingCartList, current_user).setVisible(true);
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
                    ShoppingGUI shoppingGUI = new ShoppingGUI(manager.getProductList(), manager.getShoppingCartList(),manager.getUserList());
                    shoppingGUI.setVisible(true);
                }
            });
        }
    }
