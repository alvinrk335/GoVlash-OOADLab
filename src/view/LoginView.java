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

        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        // Title
        Label titleLabel = new Label("GoVlash Laundry");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));

        Label subtitleLabel = new Label("Login to your account");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        // Form fields
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefWidth(250);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefWidth(250);

        // Message label
        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        // Buttons
        loginButton = new Button("Login");
        loginButton.setPrefWidth(120);
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"));

        registerButton = new Button("Register as Customer");
        registerButton.setPrefWidth(180);
        registerButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        registerButton.setOnMouseEntered(e -> registerButton.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white;"));
        registerButton.setOnMouseExited(e -> registerButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;"));

        HBox buttonBox = new HBox(15, loginButton, registerButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Form container with light background
        VBox formBox = new VBox(10, titleLabel, subtitleLabel, usernameField, passwordField, messageLabel, buttonBox);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(20));
        formBox.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-background-radius: 10;");

        root.getChildren().add(formBox);

        Scene scene = new Scene(root, 400, 350);
        primaryStage.setScene(scene);
    }

    public void show() {
        primaryStage.show();
    }

    // Getters and setters
    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getPassword() {
        return passwordField.getText().trim();
    }

    public void setMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    public void clearFields() {
        usernameField.clear();
        passwordField.clear();
        messageLabel.setText("");
    }

    public void setLoginButtonAction(Runnable action) {
        loginButton.setOnAction(e -> action.run());
    }

    public void setRegisterButtonAction(Runnable action) {
        registerButton.setOnAction(e -> action.run());
    }
}
