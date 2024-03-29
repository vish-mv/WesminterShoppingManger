import org.junit.Test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class WestminsterShoppingManagerTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
    }

    // Test case for getProductList method
    @Test
    public void getProductList() {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        List<Product> productList = shoppingManager.getProductList();
        assertNotNull(productList); // Check if the returned list is not null
    }

    // Test case for getShoppingCartList method
    @Test
    public void getShoppingCartList() {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        List<Product> shoppingCartList = shoppingManager.getShoppingCartList();
        assertNotNull(shoppingCartList); // Check if the returned list is not null
    }

    // Test case for getUserList method
    @Test
    public void getUserList() {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        List<User> userList = shoppingManager.getUserList();
        assertNotNull(userList); // Check if the returned list is not null
    }

    // Test case for addProduct method
    @Test
    public void addProduct() {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        Product product = new Electronics("1", "Test Product", 10, 19.99, "Test Brand", 12);
        shoppingManager.addProduct(product);
        List<Product> productList = shoppingManager.getProductList();
        assertTrue(productList.contains(product)); // Check if the added product is in the list
    }

    // Test case for printProductList method
    @Test
    public void printProductList() {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        // Redirect System.out to capture the printed output
        shoppingManager.addProduct(new Electronics("1", "Test Product", 10, 19.99, "Test Brand", 12));

        shoppingManager.printProductList();

        // Check if the printed output contains the expected content (you need to modify this based on your print format)
        assertTrue(outContent.toString().contains("Product Type : Electronics:"));
    }

    // Test case for saveProductsToFile method



    // Test case for loadProductsFromFile method
    @Test
    public void loadProductsFromFile() {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        shoppingManager.addProduct(new Electronics("1", "Test Product", 10, 19.99, "Test Brand", 12));
        shoppingManager.saveProductsToFile();

        WestminsterShoppingManager newShoppingManager = new WestminsterShoppingManager();
        newShoppingManager.loadProductsFromFile();
        List<Product> newProductList = newShoppingManager.getProductList();

        // Check if the product is loaded correctly from the file
        assertTrue(newProductList.stream().anyMatch(p -> p.getProductId().equals("1")));
    }

    // Test case for authenticate method
    @Test
    public void authenticate() {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        Manager manager = new Manager("testUser", "testPassword");
        shoppingManager.managers.add(manager);

        Manager authenticatedManager = shoppingManager.authenticate("testUser", "testPassword");
        assertNotNull(authenticatedManager); // Check if the manager is authenticated successfully

        Manager wrongCredentialsManager = shoppingManager.authenticate("wrongUser", "wrongPassword");
        assertNull(wrongCredentialsManager); // Check if the wrong credentials result in null
    }

    // Test case for saveUsersToFile method

    @Test
    public void saveUsersToFile() {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        List<User> userList = new ArrayList<>();
        User testUser = new User("TestName", "TestUserName", "TestPassword", 1, "" );  // Assuming you have a User class
        userList.add(testUser);

        // Save users to file
        shoppingManager.saveUsersToFile(userList);

        // Check if the saved users are not null
        assertNotNull(shoppingManager.getUserList());
        // Assuming that you have some logic in your saveUsersToFile method to update the userList field
        assertTrue(shoppingManager.getUserList().contains(testUser));
    }
}
