package com.example.cs262.model;

import com.example.cs262.gui.Payment2Controller;
import com.example.cs262.products.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.cs262.model.Customer.addProductToCart;
import static com.example.cs262.model.Customer.cartItems;

public class Admin extends User {

    public Admin(String userName, String password, String address, String email, String contactnumber) {
        super(userName, password, address, email, contactnumber);
    }
    public Admin() {
        // Initialization code if needed
    }
    // Method to add a product to the database
    public static void addProduct(String category, String name, double price, String rating, String imageURL, String extraField) {
        try {
            // Check if the product already exists in the database
            if (!isProductInDatabase(category, name, price, rating, imageURL, extraField)) {
                insertProductIntoDatabase(category, name, price, rating, imageURL, extraField);
            } else {
                System.out.println("Product already exists in the database. Skipping addition.");
            }
        } catch (SQLException e) {
            System.err.println("Error adding product to database: " + e.getMessage());
        }
    }

    // Helper method to check if a product exists in the database
    private static boolean isProductInDatabase(String category, String name, double price, String rating, String imageURL, String extraField) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM products WHERE category = ? AND name = ? AND price = ? AND rating = ? AND imageURL = ? AND extraField = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkSql)) {
            stmt.setString(1, category);
            stmt.setString(2, name);
            stmt.setDouble(3, price);
            stmt.setString(4, rating);
            stmt.setString(5, imageURL);
            stmt.setString(6, extraField);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Helper method to insert a product into the database
    private static void insertProductIntoDatabase(String category, String name, double price, String rating, String imageURL, String extraField) throws SQLException {
        String sql = "INSERT INTO products (category, name, price, rating, imageURL, extraField) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category);
            stmt.setString(2, name);
            stmt.setDouble(3, price);
            stmt.setString(4, rating);
            stmt.setString(5, imageURL);
            stmt.setString(6, extraField);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product successfully added to the database.");
            }
        }
    }

    // Method to display all products from the database in their respective sections
    public static void displayAllProducts() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM products";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String category = rs.getString("category");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    String rating = rs.getString("rating");
                    String imageURL = rs.getString("imageURL");
                    String extraField = rs.getString("extraField");
                    int stock = rs.getInt("stock");

                    FXMLLoader loader = new FXMLLoader(Admin.class.getResource("/com/example/cs262/Cart.fxml"));
                    AnchorPane item = loader.load();

                    Product controller = loader.getController();
                    controller.setData(name, price, rating, imageURL, stock);

                    Button addToCartButton = (Button) item.lookup("#addButton");
                    addToCartButton.setOnAction(event -> {
                        Product product = createProduct(category, name, price, rating, imageURL, extraField);
                        if (!cartItems.contains(product)) {
                            addProductToCart(product);
                        }
                    });

                    // Add item to the appropriate section based on its category
                    switch (category) {
                        case "Fruit":
                            Controller.getInstance().getHFruits().getChildren().add(item);
                            break;
                        case "Vegetable":
                            Controller.getInstance().getVegeBox().getChildren().add(item);
                            break;
                        case "Beverages":
                            Controller.getInstance().getBeveragesBox().getChildren().add(item);
                            break;
                        case "MilkAndEggs":
                            Controller.getInstance().getDairyBox().getChildren().add(item);
                            break;
                        case "Laundry":
                            Controller.getInstance().getLaundryBox().getChildren().add(item);
                            break;
                        default:
                            System.err.println("Unknown category: " + category);
                            break;
                    }
                }
            }
        } catch (SQLException | IOException e) {
            System.err.println("Error fetching products from database: " + e.getMessage());
        }
    }

    // Helper method to create a product object based on category
    private static Product createProduct(String category, String name, double price, String rating, String imageURL, String extraField) {
        switch (category) {
            case "Fruit":
                Fruit fruit = new Fruit(name, price, rating, imageURL, extraField);
                fruit.setSeason(extraField);
                return fruit;
            case "Vegetable":
                Vegetable vegetable = new Vegetable(name, price, rating, imageURL, extraField);
                vegetable.setIsOrganic(extraField);
                return vegetable;
            case "Beverages":
                Beverages beverages = new Beverages(name, price, rating, imageURL, extraField);
                beverages.setSize(extraField);
                return beverages;
            case "MilkAndEggs":
                MilkAndEggs milkAndEggs = new MilkAndEggs(name, price, rating, imageURL, extraField);
                milkAndEggs.setExpirationDate(extraField);
                return milkAndEggs;
            case "Laundry":
                Laundry laundry = new Laundry(name, price, rating, imageURL, extraField);
                laundry.setBrand(extraField);
                return laundry;
            default:
                System.err.println("Unknown category: " + category);
                return null;
        }



    }
    @FXML
    private Button Inventory;

    @FXML
    private void handleInventory(ActionEvent event) {
        loadView("/com/example/cs262/Inventory.fxml");
    }
    @FXML
    private BorderPane LoaderPane;


    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            ScrollPane newLoadedPane = loader.load();
            LoaderPane.setCenter(newLoadedPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private Button selectImageButton; // Button to trigger the file chooser

    @FXML
    private TextField imageTextField; // The TextField where the image path will be displayed

    // Method to handle the file chooser for selecting a photo
    @FXML
    public void selectImage() {
        // Create a FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        // Open the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());

        // If a file was selected, set the path to the TextField
        if (selectedFile != null) {
            // Get the absolute path
            String absolutePath = selectedFile.getAbsolutePath();

            // Convert the absolute path to a relative resource path
            String relativePath = convertToResourcePath(absolutePath);

            // Set the relative path to the TextField
            imageTextField.setText(relativePath);
        }
    }

    // Helper method to convert the absolute path to a resource path
    private String convertToResourcePath(String absolutePath) {
        // Assuming your project structure is as follows:
        // src/main/resources/com/example/cs262/Fruits/Fruits.png
        // You can adjust the base path according to your project structure
        String basePath = new File("src/main/resources").getAbsolutePath();

        // Replace the base path with an empty string to get the relative path
        String relativePath = absolutePath.replace(basePath, "");

        // Normalize the path to ensure it starts with a '/'
        return relativePath.replace("\\", "/"); // Handle Windows file separators
    }


    @FXML
    private ComboBox<String> comboBox;


    public void setupComboBox() {
        if (comboBox == null) {
            System.err.println("ComboBox is not initialized!");
            return; // Exit if comboBox is null
        }

        ObservableList<String> items = FXCollections.observableArrayList("Fruit", "Vegetable", "Beverages","MilkAndEggs", "Laundry");
        comboBox.setItems(items);
        comboBox.setOnAction(event -> {
            String selectedItem = comboBox.getSelectionModel().getSelectedItem();
            System.out.println("Selected Item: " + selectedItem);
        });
    }

    @FXML
    private  void initialize() {
        setupComboBox();
    }
    @FXML
    private Button AddProductButton;

    @FXML
    private void handleAddProduct() {
        loadView("/com/example/cs262/AddProductForm.fxml");
    }
    @FXML
    private TextField ProductNameExtraField;

    @FXML
    private TextField ProductNameField;

    @FXML
    private TextField ProductNamePrice;

    @FXML
    private TextField ProductNameRating;

    @FXML
    private Button Addtodatabasebutton;

    @FXML
    private void handleAddtodatabasebutton(){

        String Category = comboBox.getSelectionModel().getSelectedItem();
        String Name = ProductNameField.getText();
        double Price = Double.parseDouble(ProductNamePrice.getText());
        String Rating =ProductNameRating.getText();
        String ImageURL = imageTextField.getText();
        String ExtraField = ProductNameExtraField.getText();

        addProduct(Category,Name,Price,Rating,ImageURL,ExtraField);

    }

    public List<Product> retrieveProductsFromDatabase() {
        List<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM products";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String category = rs.getString("category");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    String rating = rs.getString("rating");
                    String imageURL = rs.getString("imageURL");
                    String extraField = rs.getString("extraField");

                    // Create a Product object and add it to the list
                    Product product = new Product(name, price, rating, imageURL);
                    product.setCategory(category);
                    product.setExtraField(extraField);
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving products from database: " + e.getMessage());
        }

        return products; // Return the list of products
    }

    List<Product> products = retrieveProductsFromDatabase();


    @FXML
    private VBox AdminProductLoad;

    public void loadproducts(){

        for (Product product : products) {
            try {
                FXMLLoader itemLoader = new FXMLLoader(getClass().getResource("/com/example/cs262/PaymentBar.fxml"));
                Parent itemRoot = itemLoader.load();

                // Get the controller of the PaymentBar.fxml
                Payment2Controller controller = itemLoader.getController();
                controller.setData(
                        product.getName(),
                        product.getPrice(),
                        product.getImageURL(),
                        String.valueOf(0)
                );

                // Add the item to the VBox

                //  AdminProductLoad.getChildren().add(itemRoot);

            } catch (IOException e) {
                System.err.println("Error loading PaymentBar.fxml: " + e.getMessage());
            }
        }
    }


}