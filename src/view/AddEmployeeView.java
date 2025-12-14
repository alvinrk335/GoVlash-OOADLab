package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.time.LocalDate;

public class AddEmployeeView {
    private Stage primaryStage;

    private TextField nameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private ComboBox<String> genderBox;
    private DatePicker dobPicker;
    private TextField roleField;

    private Label messageLabel;
    private Button addButton;
    private Button backButton;

    public AddEmployeeView(Stage stage) {
        this.primaryStage = stage;
        initializeComponents();
    }

    private void initializeComponents() {
        primaryStage.setTitle("GoVlash Laundry - Add New Employee");

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Add New Employee");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER);

        Label nameLabel = new Label("Name:");
        nameField = new TextField();
        nameField.setPromptText("Enter employee name");

        Label emailLabel = new Label("Email:");
        emailField = new TextField();
        emailField.setPromptText("Enter employee email");

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password (min 6 char)");

        Label confirmPasswordLabel = new Label("Confirm Password:");
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Re-enter password");

        Label genderLabel = new Label("Gender:");
        genderBox = new ComboBox<>();
        genderBox.getItems().addAll("Male", "Female");
        genderBox.setPromptText("Select gender");

        Label dobLabel = new Label("Date of Birth:");
        dobPicker = new DatePicker();
        dobPicker.setPromptText("Select date of birth");

        Label roleLabel = new Label("Role:");
        roleField = new TextField();
        roleField.setPromptText("Admin, Laundry Staff, Receptionist");

        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);
        form.add(emailLabel, 0, 1);
        form.add(emailField, 1, 1);
        form.add(passwordLabel, 0, 2);
        form.add(passwordField, 1, 2);
        form.add(confirmPasswordLabel, 0, 3);
        form.add(confirmPasswordField, 1, 3);
        form.add(genderLabel, 0, 4);
        form.add(genderBox, 1, 4);
        form.add(dobLabel, 0, 5);
        form.add(dobPicker, 1, 5);
        form.add(roleLabel, 0, 6);
        form.add(roleField, 1, 6);

        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: blue;");

        addButton = new Button("Add Employee");
        backButton = new Button("Back");

        VBox buttonBox = new VBox(10, addButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(titleLabel, form, messageLabel, buttonBox);

        Scene scene = new Scene(root, 500, 550);
        primaryStage.setScene(scene);
    }

    public void setAddAction(Runnable action) {
        addButton.setOnAction(e -> action.run());
    }

    public void setBackAction(Runnable action) {
        backButton.setOnAction(e -> action.run());
    }

    public String getEmployeeName() {
        return nameField.getText().trim();
    }

    public String getEmployeeEmail() {
        return emailField.getText().trim();
    }

    public String getPassword() {
        return passwordField.getText().trim();
    }

    public String getConfirmPassword() {
        return confirmPasswordField.getText().trim();
    }

    public String getGender() {
        return genderBox.getValue();
    }

    public LocalDate getDOB() {
        return dobPicker.getValue();
    }

    public String getRole() {
        return roleField.getText().trim();
    }

    public void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    public void clearForm() {
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        genderBox.setValue(null);
        dobPicker.setValue(null);
        roleField.clear();
    }

    public void show() {
        primaryStage.show();
    }
}
