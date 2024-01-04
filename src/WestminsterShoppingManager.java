import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class WestminsterShoppingManager implements ShoppingManager {
    private static final int MAX_PRODUCTS = 50;
    private List<Product> productList;
    private List<Product> shoppingCartList;


    public WestminsterShoppingManager() {
        this.productList = new ArrayList<>();
        loadProductsFromFile(); // Load products from file when the application starts
        this.shoppingCartList = new ArrayList<>();  // Ensure proper initialization
    }


    public void openGUI() {
        loadProductsFromFile(); // Ensure products are loaded before opening the GUI

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
                shoppingManager.loadProductsFromFile(); // Load products from file when the application starts

                ShoppingGUI shoppingGUI = new ShoppingGUI(shoppingManager.getProductList(), shoppingManager.getShoppingCartList());
                shoppingGUI.setVisible(true);
            }
        });
    }
    public List<Product> getProductList() {
        return productList;
    }
    public List<Product> getShoppingCartList() {
        return shoppingCartList;
    }

    @Override
    public void displayMenu() {
        System.out.println("1. Add a new product");
        System.out.println("2. Delete a product");
        System.out.println("3. Print the list of products");
        System.out.println("4. Save products to file");
        System.out.println("5. Exit");
        System.out.println("6. Open GUI");
        System.out.print("Select an option: ");
    }


    public void addProduct(Product product) {
        if (productList.size() < MAX_PRODUCTS) {
            productList.add(product);
            System.out.println("Product added successfully.");
        } else {
            System.out.println("Maximum number of products reached. Cannot add more products.");
        }
    }

    public void deleteProduct(String productId) {
        for (Iterator<Product> iterator = productList.iterator(); iterator.hasNext(); ) {
            Product product = iterator.next();
            if (product.getProductId().equals(productId)) {
                System.out.println("Deleted product: " + product);
                iterator.remove();
                break;
            }
        }
        System.out.println("Total products left in the system: " + productList.size());
    }

    public void printProductList() {
        // Sort the product list alphabetically by product ID
        Collections.sort(productList, Comparator.comparing(Product::getProductId));

        for (Product product : productList) {
            if (product instanceof Electronics) {
                System.out.println("Electronics: " + product);
            } else if (product instanceof Clothing) {
                System.out.println("Clothing: " + product);
            }
        }
    }

    private void saveProductsToFile() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("products.txt"))) {
            outputStream.writeObject(productList);
            System.out.println("Products saved to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving products to file.");
        }
    }

    @SuppressWarnings("unchecked")
    public void loadProductsFromFile() {
        List<Product> loadedProducts = new ArrayList<>();

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("products.txt"))) {
            loadedProducts = (List<Product>) objectInputStream.readObject();
            System.out.println("Products loaded from file: " + loadedProducts.size());
        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting with an empty product list.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading products from file.");
            e.printStackTrace();
        }

        productList = new ArrayList<>(loadedProducts); // Assign the loaded products to the main list

    }

    public static void main(String[] args) {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        Scanner scanner = new Scanner(System.in);

        int choice;
        do {
            shoppingManager.displayMenu();
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    // Add a new product
                    shoppingManager.addProduct(shoppingManager.createProduct(scanner));
                    break;
                case 2:
                    // Delete a product
                    System.out.println("Enter product ID to delete: ");
                    String productIdToDelete = scanner.nextLine();
                    shoppingManager.deleteProduct(productIdToDelete);
                    break;
                case 3:
                    // Print the list of products
                    shoppingManager.printProductList();
                    break;
                case 4:
                    // Save products to file
                    shoppingManager.saveProductsToFile();
                    break;
                case 5:
                    // Exit
                    break;
                case 6:
                    // Open GUI
                    shoppingManager.openGUI();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 5);

        // Clean up resources if needed
        scanner.close();
    }

    private Product createProduct(Scanner scanner) {

        System.out.println("Enter product ID: ");
        String productId = scanner.nextLine();

        System.out.println("Enter product name: ");
        String productName = scanner.nextLine();

        System.out.println("Enter number of available items: ");
        int availableItems = scanner.nextInt();

        System.out.println("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character

        System.out.println("Enter product type (Electronics(E) or Clothing(C)): ");
        String productType = scanner.nextLine();

        if ("Electronics".equalsIgnoreCase(productType)||"E".equalsIgnoreCase(productType)) {
            System.out.println("Enter brand: ");
            String brand = scanner.nextLine();

            System.out.println("Enter warranty period: ");
            int warrantyPeriod = scanner.nextInt();

            return new Electronics(productId, productName, availableItems, price, brand, warrantyPeriod);
        } else if ("Clothing".equalsIgnoreCase(productType)||"C".equalsIgnoreCase(productType)) {
            System.out.println("Enter size: ");
            String size = scanner.nextLine();

            System.out.println("Enter color: ");
            String color = scanner.nextLine();

            return new Clothing(productId, productName, availableItems, price, size, color);
        } else {
            System.out.println("Invalid product type. Product not added.");
            return null;
        }
    }
}
