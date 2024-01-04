public interface ShoppingManager {
    void displayMenu();
    void addProduct(Product product);
    void deleteProduct(String productId);
    void printProductList();
    void saveProductsToFile();
    void loadProductsFromFile();

}
