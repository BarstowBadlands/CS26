package com.example.cs262.model;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Controller class responsible for managing the interaction between the GUI components
 * and the application logic.
 */
public class Controller {

    private static Controller instance;
    public VBox CartScrollPane;

    @FXML
    private HBox BeveragesBox;
    public HBox getBeveragesBox() {
        return BeveragesBox;
    }


    @FXML
    private HBox DairyBox;
    public HBox getDairyBox() {
        return DairyBox;
    }


    @FXML
    private Button HomeButton;



    @FXML
    private HBox LaundryBox;
    public HBox getLaundryBox() {
        return LaundryBox;
    }

    /**
     * Default constructor. Saves the instance when the controller is initialized.
     */
    public Controller() {
        instance = this; // Save the instance when the controller is initialized
    }

    /**
     * Retrieves the singleton instance of the controller.
     *
     * @return the controller instance
     */
    public static Controller getInstance() {
        return instance;
    }

    @FXML
    private ImageView BanerImage;

    @FXML
    private ScrollPane MainScrollPane;

    @FXML
    private HBox HFruits;

    public HBox getHFruits() {
        return this.HFruits;
    }


    @FXML
    private ImageView FruitImage;

    @FXML
    private StackPane productBar;


    public void setMenuImages(String URL){
        Image image = new Image(getClass().getResourceAsStream(URL));
        FruitImage.setImage(image);
    }
    public StackPane getproductBar() {
        return productBar;
    }

    @FXML
    private HBox VegeBox;

    /**
     * Provides access to the VegeBox HBox.
     *
     * @return the VegeBox HBox
     */
    public HBox getVegeBox() {
        return VegeBox;
    }

    @FXML
    void handleHomeButtonClick(ActionEvent event) {
        System.out.println("HI");
    }

    @FXML
    private Button Favorite;

    @FXML
    void handleFavoriteClick(ActionEvent event) {
        System.out.println("Hello");
    }

    @FXML
    Button BtnCart;

    /**
     * Initializes the controller and sets up the initial application state.
     */
    @FXML
    public void initialize() {

        Admin.addProduct("Vegetable","Brocolli",29.00,"4.0(2000)","/com/example/cs262/Fruits/Vegetable/Brocolli.png","Yes");
        Admin.displayAllProducts();
        // Setting click event for HFruits HBox
        HFruits.setOnMouseClicked(event -> handleClick());

        setMenuImages("/com/example/cs262/Fruits/Fruits.png");

        Image image = new Image(getClass().getResourceAsStream("/com/example/cs262/Fruits/ScreenShot.png"));
        BanerImage.setImage(image);



    }

    /**
     * Handles the click event for the HFruits HBox.
     */
    @FXML
    static void handleClick() {

    }

    /**
     * Handles the cart button click event.
     *
     * @param event the ActionEvent triggered when the cart button is clicked
     */
    @FXML
    public void handleBtnCartClicked(ActionEvent event) {
        System.out.println("ambuttt aniii niyaaaa");
        Customer.viewCart();
    }
}
