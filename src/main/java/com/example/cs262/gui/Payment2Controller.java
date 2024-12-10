package com.example.cs262.gui;

import com.example.cs262.model.CartItems;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.LinkedList;

public class Payment2Controller {

    @FXML
    private Button BackButton;

    @FXML
    private ImageView animatedImage;

    @FXML
    private Label DeliveryText, PaymentText, TotalText;

    @FXML
    private Label productName1;

    @FXML
    private Label productPrice11;

    @FXML
    private Label quantity;

    @FXML
    private VBox DisplayItems;

    @FXML
    private ImageView productImage1;

    private LinkedList<CartItems> Items = new LinkedList<>();

    // Initialize method called after FXML is loaded
    public void initialize() {
        System.out.println("Initializing Payment2Controller...");

        loadAnimatedImage();        // Load and display the animated image
        checkLabelInitialization(); // Check if FXML components are loaded
        animateImage();             // Start image animation
    }

    // Load the animated image
    private void loadAnimatedImage() {
        try {
            Image image = new Image(getClass().getResourceAsStream("/com/example/cs262/Fruits/PngItem_37779.png"));
            if (image != null) {
                animatedImage.setImage(image);
            } else {
                System.err.println("Animated image resource not found!");
            }
        } catch (Exception e) {
            System.err.println("Error loading animated image: " + e.getMessage());
        }
    }

    // Check if labels are initialized
    private void checkLabelInitialization() {
        if (DeliveryText == null || PaymentText == null || TotalText == null) {
            System.err.println("One or more labels are not initialized!");
        }
    }

    // Animate the image
    private void animateImage() {
        if (animatedImage != null) {
            TranslateTransition transition = new TranslateTransition(Duration.seconds(2), animatedImage);
            transition.setByX(300);
            transition.setAutoReverse(true);
            transition.setCycleCount(TranslateTransition.INDEFINITE);
            transition.play();
        } else {
            System.err.println("Animated image is not initialized.");
        }
    }

    // Set delivery and payment details
    public void setDetails(String deliveryMethod, String paymentMethod, String total) {
        if (DeliveryText != null && PaymentText != null && TotalText != null) {
            DeliveryText.setText(deliveryMethod);
            PaymentText.setText(paymentMethod);
            TotalText.setText(total);
        } else {
            System.err.println("DeliveryText, PaymentText, or TotalText is not initialized!");
        }
    }

    // Display cart items in the VBox
    public void displayCartItems(LinkedList<CartItems> cartItems) {
        DisplayItems.getChildren().clear(); // Clear existing items

        this.Items= cartItems;
        for (CartItems item : cartItems) {
            try {
                FXMLLoader itemLoader = new FXMLLoader(getClass().getResource("/com/example/cs262/PaymentBar.fxml"));
                Parent itemRoot = itemLoader.load();

                // Get the controller of the PaymentBar.fxml
                Payment2Controller controller = itemLoader.getController();
                controller.setData(
                        item.getName(),
                        item.getPrice(),
                        item.getImage(),
                        String.valueOf(item.getQuantity())
                );

                // Add the item to the VBox
                DisplayItems.getChildren().add(itemRoot);

            } catch (IOException e) {
                System.err.println("Error loading PaymentBar.fxml: " + e.getMessage());
            }
        }
    }

    // Set data for individual product display
    public void setData(String name, double price, String imageURL, String quantityValue) {
        try {
            Image image = new Image(getClass().getResourceAsStream(imageURL));
            if (productName1 != null) {
                productName1.setText(name);
            }
            if (productPrice11 != null) {
                productPrice11.setText("₱" + String.format("%.2f", price));
            }
            if (quantity != null) {
                quantity.setText(quantityValue);
            }
            if (productImage1 != null) {
                productImage1.setImage(image);
            } else {
                System.err.println("Product image not initialized.");
            }
        } catch (Exception e) {
            System.err.println("Error setting product data: " + e.getMessage());
        }
    }

    @FXML
    public void handleConfirmOrderbutton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cs262/Receipts.fxml"));
            Parent root = loader.load();

            ReceiptController controller = loader.getController();

            // Set details for the second scene (Payment2Controller)

            // Ensure displayCartItems() is executed after the FXML components are initialized
            Platform.runLater(() -> {
                controller.setReceiptdata(Items,TotalText.getText(),PaymentText.getText(),DeliveryText.getText());

            });

            // Show the second scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace(); // Handle loading errors
        }
    }

    @FXML
    private void handleBackButtonAction() {
        // Get the current stage (CartFrame)
        Stage cartStage = (Stage) BackButton.getScene().getWindow();

        // Find the main stage
        Stage mainStage = (Stage) cartStage.getOwner(); // Assuming the CartFrame was opened from the MainFrame

        // Apply a slide-out animation to the CartFrame
        TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), cartStage.getScene().getRoot());
        slideOut.setFromX(0); // Start at the current position
        slideOut.setToX(-cartStage.getScene().getWidth()); // Slide out to the left

        slideOut.setOnFinished(event -> {
            // After the slide-out animation, close the CartFrame stage
            cartStage.close();

            // Bring the MainFrame to the foreground
            if (mainStage != null) {
                mainStage.show();

                // Optional: Add a slide-in animation for the MainFrame
                Parent mainRoot = mainStage.getScene().getRoot();
                TranslateTransition slideIn = new TranslateTransition(Duration.millis(100), mainRoot);
                mainRoot.setTranslateX(-mainStage.getScene().getWidth()); // Start outside the view
                slideIn.setFromX(-mainStage.getScene().getWidth());
                slideIn.setToX(0); // Slide into view
                slideIn.play();
            }
        });

        // Play the slide-out animation
        slideOut.play();
    }
}
