import java.io.*;
import java.util.*;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class WestminsterShoppingManager implements ShoppingManager {
    private static final int MAX_PRODUCTS = 50;
    private List<Product> productList;
    private List<Product> shoppingCartList;
    List<Manager> managers;
    private List<User> userList;

    public WestminsterShoppingManager() {
        this.productList = new ArrayList<>();
        this.shoppingCartList = new ArrayList<>();  // Ensure proper initialization
        this.managers = new ArrayList<>();
        this.userList = new ArrayList<>();
        System.out.println("\n--------Data Load Log---------");
        loadManagersFromFile();
        loadProductsFromFile(); // Load products from file when the application starts
        loadUsersFromFile();
        System.out.println("------------------------------\n");

    }

    //---------------- ArrayList Management -----------------------------------------
    public List<Product> getProductList() {
        return productList;
    }
    public List<Product> getShoppingCartList() {
        return shoppingCartList;
    }
    public List<User> getUserList() {
        return userList;
    }


    //------------------ Product Management --------------------------------------------------
    public void addProduct(Product product) {
        if (productList.size() < MAX_PRODUCTS) {
            productList.add(product);
            System.out.println("Product added successfully.");
        } else {
            System.out.println("Maximum number of products reached. Cannot add more products.");
        }
    }

    private Product createProduct(Scanner scanner) {
        while (true) {
            String productId, productName;
            int availableItems;
            double price;

            // Input validation for product ID
            while (true) {
                System.out.print("Enter product ID: ");
                String input = scanner.nextLine();
                if (input != null && !input.isEmpty() && !containsSymbol(input)) {
                    productId = input;
                    break;
                }
                System.out.println("Invalid input for Product ID. Please try again.");
            }

            // Input validation for product name
            while (true) {
                System.out.print("Enter product name: ");
                String input = scanner.nextLine();
                if (input != null && !input.isEmpty() && !containsSymbol(input)) {
                    productName = input;
                    break;
                }
                System.out.println("Invalid input for Product Name. Please try again.");
            }

            // Input validation for available items
            while (true) {
                try {
                    System.out.print("Enter number of available items: ");
                    availableItems = scanner.nextInt();
                    if (availableItems != 0) {
                        break;
                    }
                    System.out.println("Available Items Cannot Be Zero");
                } catch (InputMismatchException e) {
                    System.out.println("Invalid data type! Please enter a valid integer.");
                    scanner.nextLine(); // Consume the invalid input
                }
            }

            // Input validation for price
            while (true) {
                try {
                    System.out.print("Enter price: ");
                    price = scanner.nextDouble();
                    if (price != 0) {
                        break;
                    }
                    System.out.println("Price Cannot Be Zero");
                } catch (InputMismatchException e) {
                    System.out.println("Invalid data type! Please enter a valid double.");
                    scanner.nextLine(); // Consume the invalid input
                }
            }

            // Consume the newline character left in the buffer
            scanner.nextLine();

            // Input validation for product type
            while (true) {
                System.out.print("Enter product type (Electronics(E) or Clothing(C)): ");
                String productType = scanner.nextLine();

                if ("Electronics".equalsIgnoreCase(productType) || "E".equalsIgnoreCase(productType)) {
                    System.out.println("Enter brand: ");
                    String brand = scanner.nextLine();

                    // Input validation for warranty period
                    while (true) {
                        try {
                            System.out.print("Enter warranty period: ");
                            int warrantyPeriod = scanner.nextInt();
                            return new Electronics(productId, productName, availableItems, price, brand, warrantyPeriod);
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid data type! Please enter a valid integer.");
                            scanner.nextLine(); // Consume the invalid input
                        }
                    }
                } else if ("Clothing".equalsIgnoreCase(productType) || "C".equalsIgnoreCase(productType)) {
                    System.out.println("Enter size: ");
                    String size = scanner.nextLine();

                    System.out.println("Enter color: ");
                    String color = scanner.nextLine();
                    scanner.nextLine();
                    return new Clothing(productId, productName, availableItems, price, size, color);
                } else {
                    System.out.println("Invalid product type. Product not added. Please try again.");
                }
            }
        }
    }

    private boolean containsSymbol(String input) {
        // Add your symbol validation logic here
        // For example, check if the input contains any symbols
        return input.matches(".*[^a-zA-Z0-9].*");
    }

    public void deleteProduct(String productId) {
        for (Iterator<Product> iterator = productList.iterator(); iterator.hasNext(); ) {
            Product product = iterator.next();
            if (product.getProductId().equals(productId)) {
                System.out.println("Product deleted Successfully!");
                System.out.println("Deleted product: \n" + product);
                iterator.remove();
                break;
            } else {
                System.out.println("Invalid Product ID");
            }
        }
        System.out.println("Total products left in the system: " + productList.size());
    }

    public void printProductList() {
        // Sort the product list alphabetically by product ID
        Collections.sort(productList, Comparator.comparing(Product::getProductId));

        for (Product product : productList) {
            System.out.println("---------------\n");
            if (product instanceof Electronics) {
                System.out.println("Product Type : Electronics: " + product.toString());
            } else if (product instanceof Clothing) {
                System.out.println("Product Type : Clothing: " + product.toString());
            }
            System.out.println("---------------");
        }

    }

    public void saveProductsToFile() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("products.txt"))) {
            outputStream.writeObject(productList);
            System.out.println("Products saved to file successfully.");
        } catch (IOException e) {
            System.out.println("Error saving products to file.");
        }
    }

    public void loadProductsFromFile() {
        List<Product> loadedProducts = new ArrayList<>();

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("products.txt"))) {
            loadedProducts = (List<Product>) objectInputStream.readObject();
            System.out.println("Products loaded from file: " + loadedProducts.size());
        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting with a new Products Database.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No Previous Data Found! Error loading products from file.");
        }

        productList = new ArrayList<>(loadedProducts); // Assign the loaded products to the main list

    }

    //----------------------------- Manager Management ----------------------------------
    private void loadManagersFromFile() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("managers.txt"))) {
            managers = (List<Manager>) objectInputStream.readObject();
            System.out.println("Managers loaded from file: " + managers.size());
        } catch (FileNotFoundException e) {
            System.out.println("No previous manager data found.");
            addDefaultManager();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous manager data! Error loading managers from file.");
            addDefaultManager();

        }
    }
    private void addDefaultManager(){
        Manager manager = new Manager("admin123","admin123");
        managers.add(manager);
        System.out.println("You can Log In via default Backup manager Account!");
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
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("managers.txt"))) {
            outputStream.writeObject(managers);
            System.out.println("Managers saved to file successfully.");
        } catch (IOException e) {
            System.out.println("Error saving managers to file.");
        }
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
                break;
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

    //----------------------- User Management -----------------------------------
    public void saveUsersToFile(List<User> userList) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("users.txt"))) {
            outputStream.writeObject(userList);
            this.userList=userList;
            System.out.println("Users in the array from file: " + userList.size());
            System.out.println("Users saved to file successfully.");
        } catch (IOException e) {
            System.out.println("Error saving Users to file.");
        }
    }
    void loadUsersFromFile() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("users.txt"))) {
            userList = (List<User>) objectInputStream.readObject();
            System.out.println("Users loaded from file: " + userList.size());
        } catch (FileNotFoundException e) {
            System.out.println("No previous User data found.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous User data! Error loading users from file.");
        }
    }

    public User authenticateUser(String username, String password){
        for (User user: userList){
            if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }


    //---------------------- GUI Management -------------------------------------------
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

//------------------------------ Main Part -----------------------------------------
    @Override
    public void displayMenu() {
        System.out.println();
        System.out.println("1. Add a new product");
        System.out.println("2. Delete a product");
        System.out.println("3. Print the list of products");
        System.out.println("4. Save products to file");
        System.out.println("5. Load products From file");
        System.out.println("6. Register Another Manager");
        System.out.println("7. Open GUI");
        System.out.println("8. Exit");
        System.out.print("Select an option: ");
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
                System.out.println("Login successful. Welcome, " + loggedInManager.getUsername() + "! Press Enter To Continue");
                authenticated = true;
            }
        } while (!authenticated);
        shoppingManager.removeDefaultManager();
        String choice;
        do {
            scanner.nextLine();
            shoppingManager.displayMenu();
            choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    // Add a new product
                    shoppingManager.addProduct(shoppingManager.createProduct(scanner));
                    break;
                case "2":
                    // Delete a product
                    System.out.print("Enter product ID to delete: ");
                    String productIdToDelete = scanner.nextLine();
                    shoppingManager.deleteProduct(productIdToDelete);
                    break;
                case "3":
                    // Print the list of products
                    shoppingManager.printProductList();
                    break;
                case "4":
                    // Save products to file
                    shoppingManager.saveProductsToFile();
                    break;
                case "5":
                    //Load products from file
                    shoppingManager.loadProductsFromFile();
                    break;

                case "6":
                    // Register a new manager
                    shoppingManager.registerNewManager(scanner);
                    break;

                case "7":
                    // Open GUI
                    System.out.println("--- Saving Changes ---");
                    System.out.println("--- Opening GUI ---");
                    shoppingManager.saveProductsToFile();
                    shoppingManager.openGUI();
                    break;
                case "8":
                    // Exit
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != "8");
        // Clean up resources if needed
        scanner.close();
    }
}
