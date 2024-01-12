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
    private List<Manager> managers;
    private List<User> userList;

    public WestminsterShoppingManager() {
        this.productList = new ArrayList<>();
        this.shoppingCartList = new ArrayList<>();  // Ensure proper initialization
        this.managers = new ArrayList<>();
        this.userList = new ArrayList<>();

        loadManagersFromFile();
        loadProductsFromFile(); // Load products from file when the application starts

    }
    public List<Product> getProductList() {
        return productList;
    }
    public List<Product> getShoppingCartList() {
        return shoppingCartList;
    }
    public List<User> getUserList() {
        return userList;
    }

    public void openGUI() {
        loadProductsFromFile(); // Ensure products are loaded before opening the GUI

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
                shoppingManager.loadProductsFromFile(); // Load products from file when the application starts

                ShoppingGUI shoppingGUI = new ShoppingGUI(shoppingManager.getProductList(), shoppingManager.getShoppingCartList(),shoppingManager.getUserList());
                shoppingGUI.setVisible(true);
            }
        });
    }


    @Override
    public void displayMenu() {
        System.out.println("1. Add a new product");
        System.out.println("2. Delete a product");
        System.out.println("3. Print the list of products");
        System.out.println("4. Save products to file");
        System.out.println("5. Exit");
        System.out.println("6. Open GUI");
        System.out.println("6. Register Another Manager");
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

    public void saveProductsToFile() {
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

    //Manager Section

    private void loadManagersFromFile() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("managers.dat"))) {
            managers = (List<Manager>) objectInputStream.readObject();
            System.out.println("Managers loaded from file: " + managers.size());
        } catch (FileNotFoundException e) {
            System.out.println("No previous manager data found. \nYou can Log In Using Default Account!.\n");
            addDefaultManager();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading managers from file.\nYou can Log In Using Default Account!.\n");
            addDefaultManager();

        }
    }
    private void addDefaultManager(){
        Manager manager = new Manager("admin123","admin123");
        managers.add(manager);
        System.out.println("\nYou can Log In default Backup manager Account!\n");
    }
    private void removeDefaultManager(){
        for (Iterator<Manager> iterator = managers.iterator(); iterator.hasNext(); ) {
            Manager manager = iterator.next();
            if (manager.getUsername().equals("admin123")) {
                iterator.remove();
                break;
            }
        }
    }

    private void saveManagersToFile() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("managers.dat"))) {
            outputStream.writeObject(managers);
            System.out.println("Managers saved to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving managers to file.");
        }
    }

    public void addManager(Manager manager) {
        managers.add(manager);
        saveManagersToFile();
    }

    private void registerNewManager(Scanner scanner) {
        while(true) {
            System.out.println("Enter username for the new manager: ");
            String newUsername = scanner.nextLine();
            System.out.println("Enter password for the new manager: ");
            String newPassword = scanner.nextLine();

            Manager newManager = new Manager(newUsername, newPassword);

            if (usernameExists(newUsername) || newUsername.equals("admin123")) {
                System.out.println("Username already exists. Please choose a different one.");
                System.out.println("Press X to go back to menu \nOr Any key to try again: ");
                String quit_choice= scanner.nextLine();
                if (quit_choice.equalsIgnoreCase("X")){
                    break;
                }
                else {
                    continue;
                }
            } else {
                managers.add(newManager);
                saveManagersToFile();
                System.out.println("Registration successful for the new manager.");
            }
        }
    }
    private boolean usernameExists(String username) {
        for (Manager manager : managers) {
            if (manager.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    public Manager authenticate(String username, String password) {
        for (Manager manager : managers) {
            if (manager.getUsername().equals(username) && manager.getPassword().equals(password)) {
                return manager;
            }
        }
        return null; // Return null if not found
    }
    public User authenticateUser(String username, String password){
        for (User user: userList){
            if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }




    public static void main(String[] args) {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        Scanner scanner = new Scanner(System.in);
        boolean authenticated = false;
        Manager loggedInManager = null;

        do {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            loggedInManager = shoppingManager.authenticate(username, password);

            if (loggedInManager == null) {
                System.out.println("Invalid username or password. Please try again.");
            } else {
                System.out.println("Login successful. Welcome, " + loggedInManager.getUsername() + "!");
                authenticated = true;
            }
        } while (!authenticated);
        shoppingManager.removeDefaultManager();
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
                case 7:
                    // Register a new manager (added this option)
                    shoppingManager.registerNewManager(scanner);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 5);

        // Clean up resources if needed
        scanner.close();
    }


}
