package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginView {
    
    private Stage primaryStage;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label messageLabel;
    private Button loginButton;
    private Button registerButton;
    
    public LoginView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeComponents();
    }
    
    private void initializeComponents() {
        primaryStage.setTitle("GoVlash Laundry - Login");
        
        // Create login form
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        // Title
        Label titleLabel = new Label("GoVlash Laundry");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(titleLabel, 0, 0, 2, 1);
        
        Label subtitleLabel = new Label("Login to your account");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        grid.add(subtitleLabel, 0, 1, 2, 1);
        
        // Username field
        Label userLabel = new Label("Username:");
        grid.add(userLabel, 0, 2);
        
        usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        grid.add(usernameField, 1, 2);
        
        // Password field
        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 3);
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        grid.add(passwordField, 1, 3);
        
        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        
        loginButton = new Button("Login");
        registerButton = new Button("Register as Customer");
        
        buttonBox.getChildren().addAll(loginButton, registerButton);
        grid.add(buttonBox, 1, 4);
        
        // Message label for validation errors
        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");
        grid.add(messageLabel, 0, 5, 2, 1);
        
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
    }
    
    public void show() {
        primaryStage.show();
    }
    
    // Getters for form data
    public String getUsername() {
        return usernameField.getText().trim();
    }
    
    public String getPassword() {
        return passwordField.getText().trim();
    }
    
    public void setMessage(String message, boolean isError) {
        messageLabel.setText(message);
        if (isError) {
            messageLabel.setStyle("-fx-text-fill: red;");
        } else {
            messageLabel.setStyle("-fx-text-fill: green;");
        }
    }
    
    public void clearFields() {
        usernameField.clear();
        passwordField.clear();
        messageLabel.setText("");
    }
    
    // Event handler setters
    public void setLoginButtonAction(Runnable action) {
        loginButton.setOnAction(e -> action.run());
    }
    
    public void setRegisterButtonAction(Runnable action) {
        registerButton.setOnAction(e -> action.run());
    }
}