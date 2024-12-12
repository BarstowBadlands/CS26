package com.example.cs262.model;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.example.cs262.model.Customer.cartItems;


public class Product {
    private static Product instance;

    // Singleton Pattern
    public Product() {
        instance = this; // Set the singleton instance during initialization
    }

    public static Product getInstance() {
        return instance;
    }

    @FXML
    private TextField quantityField;

    public String getQuantity() {
        return quantityField.getText();
    }

    public void setQuantity(String quantity) {
        quantityField.setText(quantity);
    }
    @FXML
    private Button decreaseQuantity;

    @FXML
    void increaseQuantity() {
        String productName = getName();
        CartItems item = Customer.findCartItemByName(productName);
        if (item != null) {
            item.setQuantity(item.getQuantity() + 1); // Update quantity
            Customer.updateProductQuantityInCart(item); // Update in cart
            quantityField.setText(String.valueOf(item.getQuantity()));
            productPrice1.setText(String.format("₱%.2f", item.getQuantity() * item.getPrice()));
            Customer.getInstance().setTotalPrice("10000");
            System.out.println("Gana:"+Customer.getInstance().getTotalPrice());
            System.out.println("Price:" + (item.getPrice()*item.getQuantity()));
        }
    }

    @FXML
    void decreaseQuantity() {
        String productName = getName();
        CartItems item = Customer.findCartItemByName(productName);
        if (item != null && item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1); // Update quantity
            Customer.updateProductQuantityInCart(item); // Update in cart
            quantityField.setText(String.valueOf(item.getQuantity())); // Update the UI in real-time
            productPrice1.setText(String.format("₱%.2f", item.getQuantity() * item.getPrice()));// Update the UI in real-time
            System.out.println("Price:" + (item.getPrice()*item.getQuantity()));
        }
    }

    @FXML
    private Button deleteButton;

    @FXML
    public void handledeleteButtonClick(ActionEvent event) {
        // Get the product name from the label
        String productNameToDelete = productName1.getText();

        // Remove the product from the cartItems list
        cartItems.removeIf(product -> product.getName().equals(productNameToDelete));

        // Remove the product from the productQuantities map
        Customer.productQuantities.remove(productNameToDelete);

        // Remove the product's UI component from the VBox
        VBox cartBox = Customer.getInstance().getCartBox();
        for (Node node : cartBox.getChildren()) {
            if (node instanceof AnchorPane) {
                Label productLabel = (Label) node.lookup("#productName1");
                if (productLabel != null && productLabel.getText().equals(productNameToDelete)) {
                    cartBox.getChildren().remove(node); // Remove the product from the UI
                    break;
                }
            }
        }

        System.out.println("Deleted product from cart: " + productNameToDelete);
    }

    // FXML fields
    @FXML
    private Button addButton;

    @FXML
    private ImageView productImage;

    @FXML
    private AnchorPane productContainer;

    @FXML
    private Label productName;

    @FXML
    private Label productPrice;

    @FXML
    private Label productRating;

    @FXML
    private AnchorPane cartBar;

    @FXML
    private ImageView productImage1;

    @FXML
    private Label productName1;

    @FXML
    private Label productPrice1;

    @FXML
    private Label productRating1;

    private String imageURL;

    @FXML
    public Label stock;

    // Product attributes
    private double price;
    private String rating;
    private String name;

    private String Category;

    /**
     * Constructor for Product. Initializes product attributes.
     */
    public Product( String name, double price, String rating, String imageURL) {
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.imageURL = imageURL;
    }

    @FXML
    public void initialize() {
        // Initialization logic if necessary
    }

    /**
     * Handle clicking on the product container to open the product page.
     */
    @FXML
    public void handleproductContainerClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cs262/Productpage.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/example/cs262/Productpage.fxml").toExternalForm());

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set product data in the UI components.
     */
    public void setData(String name, double price, String rating, String imageURL, int stock) {
        this.imageURL = imageURL;

        // Load the image safely
        Image image = new Image(getClass().getResourceAsStream(imageURL));
        if (productImage != null) {
            productImage.setImage(image);
        }
        if (productName != null) {
            productName.setText(name);
        }
        if (productPrice != null) {
            productPrice.setText("₱" + String.format("%.2f", price));
        }
        if (productRating != null) {
            productRating.setText(rating);
        }
        if (this.stock != null) {
            this.stock.setText("In Stock: "+ Integer.toString(stock));
        }
    }

    /**
     * Set product data for a cart item.
     */
    public void setDataofCartItem(String name, double price, String rating, String imageURL, int stock) {
        this.imageURL = imageURL;

        Image image = new Image(getClass().getResourceAsStream(imageURL));
        if (productImage1 != null) {
            productImage1.setImage(image);
        }
        if (productName1 != null) {
            productName1.setText(name);
        }
        if (productPrice1 != null) {
            productPrice1.setText("₱" + String.format("%.2f", price));
        }
        if (productRating1 != null) {
            productRating1.setText(rating);
        }

        if (this.stock != null) {
            this.stock.setText("In Stock: " + Integer.toString(stock));
        }

        if (quantityField != null) {
            CartItems item = Customer.findCartItemByName(name); // Fetch the specific cart item
            if (item != null) {
                quantityField.setText(String.valueOf(item.getQuantity()));
                System.out.println("Quantity: " + item.getQuantity());
            }
        }
        this.name = name;
    }


    // Getters and Setters for product fields
    public String getName() {

        if (productName1 != null && productName1.getText() != null) {
            return productName1.getText(); // Use the cart item's label
        }
        return name; // Fall back to the instance variable
    }



    public void setName(String name) {
        this.name = name;
        if (productName != null) {
            productName.setText(name);
        }
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        if (productPrice != null) {
            productPrice.setText("₱" + String.format("%.2f", price));
        }
    }

    public String getRating() {
        return (productRating != null) ? productRating.getText() : rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
        if (productRating != null) {
            productRating.setText(rating);
        }
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setExtraField(String extraField) {
        // Example of extra logic for additional fields
    }


    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    @FXML
    private void handleRestock() {
        // Create a TextInputDialog to ask for the restock amount
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Restock Product");
        dialog.setHeaderText("Enter the restock amount");
        dialog.setContentText("Restock quantity:");

        // Show the dialog and get the result
        dialog.showAndWait().ifPresent(restockQuantity -> {
            try {
                // Validate the input is a number
                int quantity = Integer.parseInt(restockQuantity);
                if (quantity > 0) {
                    // Call the method to update the stock in the database
                    updateStockInDatabase(quantity);
                } else {
                    // Show an error if the quantity is invalid
                    showError("Please enter a valid positive number.");
                }
            } catch (NumberFormatException e) {
                // Handle invalid input
                showError("Invalid input. Please enter a valid number.");
            }
        });
    }

    private void updateStockInDatabase(int quantity) {
        // SQL query to update the stock based on the product name
        String sql = "UPDATE products SET stock = stock + ? WHERE name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the restock quantity and the product name in the query
            stmt.setInt(1, quantity);
            stmt.setString(2, "Apple");  // Use the product name instead of the product ID

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Successfully updated the stock, so update the UI
                refreshProductList();
            } else {
                showError("Failed to update stock. Product not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error updating stock: " + e.getMessage());
        }
    }

    public void refreshProductList() {
        // Clear existing UI components
        Admin.getInstance().getHFruits().getChildren().clear();
        Admin.getInstance().getVegeBox().getChildren().clear();
        Admin.getInstance().getBeveragesBox().getChildren().clear();
        Admin.getInstance().getDairyBox().getChildren().clear();
        Admin.getInstance().getLaundryBox().getChildren().clear();

        // Reload products from the database
        Admin.displayAllProducts();
    }


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}