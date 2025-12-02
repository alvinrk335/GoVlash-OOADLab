package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.time.LocalDate;

public class RegisterView {
    
    private Stage primaryStage;
    private TextField usernameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private ComboBox<String> genderBox;
    private DatePicker dobPicker;
    private Label messageLabel;
    private Button registerButton;
    private Button backButton;
    
    public RegisterView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeComponents();
    }
    
    private void initializeComponents() {
        primaryStage.setTitle("GoVlash Laundry - Customer Registration");
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        
        // Title
        Label titleLabel = new Label("Customer Registration");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        // Form
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER);
        
        // Username
        form.add(new Label("Username:"), 0, 0);
        usernameField = new TextField();
        usernameField.setPromptText("Enter unique username");
        form.add(usernameField, 1, 0);
        
        // Email
        form.add(new Label("Email:"), 0, 1);
        emailField = new TextField();
        emailField.setPromptText("yourname@gmail.com");
        form.add(emailField, 1, 1);
        
        // Password
        form.add(new Label("Password:"), 0, 2);
        passwordField = new PasswordField();
        passwordField.setPromptText("Min 6 characters");
        form.add(passwordField, 1, 2);
        
        // Confirm Password
        form.add(new Label("Confirm Password:"), 0, 3);
        confirmPasswordField = new PasswordField();
        form.add(confirmPasswordField, 1, 3);
        
        // Gender
        form.add(new Label("Gender:"), 0, 4);
        genderBox = new ComboBox<>();
        genderBox.getItems().addAll("Male", "Female");
        form.add(genderBox, 1, 4);
        
        // Date of Birth
        form.add(new Label("Date of Birth:"), 0, 5);
        dobPicker = new DatePicker();
        form.add(dobPicker, 1, 5);
        
        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        registerButton = new Button("Register");
        backButton = new Button("Back to Login");
        buttonBox.getChildren().addAll(registerButton, backButton);
        
        // Message label
        messageLabel = new Label();
        
        root.getChildren().addAll(titleLabel, form, buttonBox, messageLabel);
        
        Scene scene = new Scene(root, 500, 400);
        primaryStage.setScene(scene);
    }
    
    public void show() {
        primaryStage.show();
    }
    
    // Getters for form data
    public String getUsername() {
        return usernameField.getText().trim();
    }
    
    public String getEmail() {
        return emailField.getText().trim();
    }
    
    public String getPassword() {
        return passwordField.getText();
    }
    
    public String getConfirmPassword() {
        return confirmPasswordField.getText();
    }
    
    public String getGender() {
        return genderBox.getValue();
    }
    
    public LocalDate getDateOfBirth() {
        return dobPicker.getValue();
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
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        genderBox.setValue(null);
        dobPicker.setValue(null);
        messageLabel.setText("");
    }
    
    // Event handler setters
    public void setRegisterButtonAction(Runnable action) {
        registerButton.setOnAction(e -> action.run());
    }
    
    public void setBackButtonAction(Runnable action) {
        backButton.setOnAction(e -> action.run());
    }
}