    import javax.swing.*;
    import javax.swing.table.DefaultTableModel;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    public class ShoppingCartGUI extends JFrame {

        private List<Product> shoppingCart;
        private JTable cartTable;
        private JLabel finalTotalLabel;
        private JButton buyButton;
        private static User current_user;
        private JLabel label1;
        private JLabel label2;
        private JLabel label3 ;
        private WestminsterShoppingManager manager;

        public ShoppingCartGUI(List<Product> shoppingCart, User current_user, WestminsterShoppingManager manager) {
            this.shoppingCart = shoppingCart;
            ShoppingCartGUI.current_user = current_user;
            this.manager = manager;
            initializeGUI();
        }

        private void initializeGUI() {
            setTitle("Shopping Cart");
            setSize(800, 600);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            setMinimumSize(new Dimension(700, 500));

            // Shopping cart table  
            cartTable = new JTable();
            JScrollPane tableScrollPane = new JScrollPane(cartTable);
            label1 = new JLabel("Total : 0 $");
            label1.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            label2 = new JLabel("First Purchase Discount (10%)                                        Checking");
            label2.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            label3 = new JLabel("Three Times in same Category Discount (20%)            Not Added Yet");
            label3.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            buyButton = new JButton("Purchase");
            buyButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            buyButton = new JButton("Purchase");
            buyButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            buyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Reduce quantity from the main product list
                    reduceQuantityInMainProductList();

                    // Increase user's purchase count
                    increaseUserPurchaseCount();

                    // Display a message or update the GUI to indicate success
                    JOptionPane.showMessageDialog(ShoppingCartGUI.this, "Purchase successful!");

                    // Optionally update the shopping cart details in the GUI
                    populateCartDetails();
                }
            });

            // Final total label
            finalTotalLabel = new JLabel("Final Total: $0.00");
            finalTotalLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

            // Layout setup
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(tableScrollPane, BorderLayout.NORTH);

            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

            bottomPanel.add(label1);
            bottomPanel.add(label2);
            bottomPanel.add(label3);
            bottomPanel.add(finalTotalLabel);


            mainPanel.add(bottomPanel, BorderLayout.CENTER);
            add(buyButton,BorderLayout.SOUTH);

            add(mainPanel);

            // Populate cart details
            populateCartDetails();
        }
        private void reduceQuantityInMainProductList() {
            for (Product product : shoppingCart) {
                for (Product mainProduct : manager.getProductList()) {
                    if (product.getProductId().equals(mainProduct.getProductId())) {
                        mainProduct.setAvailableItems(mainProduct.getAvailableItems() - 1);
                        break;
                    }
                }
            }
        }

        private void increaseUserPurchaseCount() {
            if (current_user != null) {
                current_user.setPurchased_count(current_user.getPurchased_count() + 1);
                manager.saveUsersToFile(manager.getUserList());
            }
        }

        private void populateCartDetails() {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Product ID");
            model.addColumn("Product");
            model.addColumn("Quantity");
            model.addColumn("Price");

            double products_price = 0;

            for (Product product : shoppingCart) {
                double price = product.getPrice();
                String productID = product.getProductId();
                int row_number=0;

                // Check if the product with the current product ID is already in the table
                boolean productExistsInTable = false;
                for (int row = 0; row < model.getRowCount(); row++) {
                    String tableProductID = (String) model.getValueAt(row, 0);  // Assuming column 0 is for Product ID
                    if (tableProductID.equals(productID)) {
                        // Product with the same ID is already in the table, update the quantity
                        int currentQuantity = (int) model.getValueAt(row, 2);
                        model.setValueAt(currentQuantity + 1, row, 2);  // Increment the quantity
                        model.setValueAt((currentQuantity + 1)*price, row, 3);
                        productExistsInTable = true;
                        row_number=row;
                        break;
                    }
                }

                if (!productExistsInTable) {
                    // Product with the current product ID is not in the table, add a new row
                    Object[] rowData = {productID, getProductDetails(product), 1, price};  // Fixed quantity of 1
                    model.addRow(rowData);
                }
                products_price+=price;

            }



            // Apply discount if applicable
            double discount=applyDiscount(products_price);
            double tempTotalPrice=products_price-discount;
            double totalPrice= tempTotalPrice-applyFirstPurDiscount(tempTotalPrice);
            label1.setText("Total : +"+products_price+ " $");
            cartTable.setModel(model);

            finalTotalLabel.setText("Final Total: " + totalPrice+ " $");
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
                // Accumulate the discount
            // Update the discount
            label3.setText("Three Times in same Category Discount (20%)            "+discount+" $");
            return discount;
        }
        private double applyFirstPurDiscount(Double productsPrice) {
            try {
                if (current_user != null) {
                    int purchased_item = current_user.getPurchased_count();
                    double discount = 0;
                    if (purchased_item == 0) {
                        discount = productsPrice * 0.1;
                    }
                    label2.setText("First Purchase Discount (10%)                                        " + discount + " $");
                    return discount;
                } else {
                    label2.setText("First Purchase Discount (10%)                                        User not logged in");
                    return 0;
                }
            } catch (NullPointerException e) {
                System.out.println("Error: " + e.getMessage());
                return 0;
            }
        }


        public static void main(String[] args) {
            // Example usage:
            WestminsterShoppingManager manager = new WestminsterShoppingManager();
            manager.loadProductsFromFile();  // Load products from file
            manager.openGUI();  // Open the main shopping GUI
            // ... Perform actions, add products to the shopping cart, etc.

            List<Product> shoppingCart = manager.getShoppingCartList();  // Assuming you have a getter for the shopping cart
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ShoppingCartGUI(shoppingCart,current_user,manager).setVisible(true);
                }
            });
        }
    }
