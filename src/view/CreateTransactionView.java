package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Service;
import model.Transaction;
import model.User;
import controller.TransactionController;

public class CreateTransactionView {
    
    private Stage primaryStage;
    private TransactionController transactionController;
    private User currentUser;
    private Service selectedService;
    private Label serviceInfoLabel;
    private TextField weightField;
    private TextArea notesArea;
    private Label totalPriceLabel;
    private Label messageLabel;
    private Button createButton;
    private Button backButton;
    
    public CreateTransactionView(Stage primaryStage, User user, Service service) {
        this.primaryStage = primaryStage;
        this.currentUser = user;
        this.selectedService = service;
        this.transactionController = new TransactionController();
        initializeComponents();
    }
    
    private void initializeComponents() {
        primaryStage.setTitle("GoVlash Laundry - Create Transaction");
        
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        
        // Title
        Label titleLabel = new Label("Create New Laundry Order");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Service information
        VBox serviceInfoBox = new VBox(5);
        serviceInfoBox.setAlignment(Pos.CENTER);
        serviceInfoBox.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-background-radius: 5;");
        
        Label serviceLabel = new Label("Selected Service:");
        serviceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        
        serviceInfoLabel = new Label(selectedService.getServiceName() + " - Rp " + 
                                   String.format("%.0f", selectedService.getServicePrice()) + 
                                   " per kg - " + selectedService.getServiceDuration() + " days");
        serviceInfoLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        
        serviceInfoBox.getChildren().addAll(serviceLabel, serviceInfoLabel);
        
        // Form
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setAlignment(Pos.CENTER);
        form.setPrefWidth(400);
        
        // Weight input
        form.add(new Label("Weight (kg):"), 0, 0);
        weightField = new TextField();
        weightField.setPromptText("Enter weight (2-50 kg)");
        weightField.textProperty().addListener((obs, oldText, newText) -> updateTotalPrice());
        form.add(weightField, 1, 0);
        
        // Weight validation label
        Label weightHint = new Label("Minimum: 2 kg, Maximum: 50 kg");
        weightHint.setStyle("-fx-text-fill: gray; -fx-font-size: 10px;");
        form.add(weightHint, 1, 1);
        
        // Notes input
        form.add(new Label("Notes (Optional):"), 0, 2);
        notesArea = new TextArea();
        notesArea.setPromptText("Special instructions or notes...");
        notesArea.setPrefRowCount(3);
        notesArea.setPrefWidth(200);
        form.add(notesArea, 1, 2);
        
        // Total price display
        totalPriceLabel = new Label("Total: Rp 0");
        totalPriceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        totalPriceLabel.setStyle("-fx-text-fill: #2e7d32;");
        
        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        createButton = new Button("Create Order");
        createButton.setPrefWidth(120);
        createButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        backButton = new Button("Back");
        backButton.setPrefWidth(120);
        
        buttonBox.getChildren().addAll(createButton, backButton);
        
        // Message label
        messageLabel = new Label();
        
        root.getChildren().addAll(
            titleLabel,
            serviceInfoBox,
            form,
            totalPriceLabel,
            buttonBox,
            messageLabel
        );
        
        setupEventHandlers();
        
        Scene scene = new Scene(root, 500, 600);
        primaryStage.setScene(scene);
    }
    
    private void setupEventHandlers() {
        createButton.setOnAction(e -> createTransaction());
    }
    
    private void updateTotalPrice() {
        try {
            String weightText = weightField.getText().trim();
            if (!weightText.isEmpty()) {
                double weight = Double.parseDouble(weightText);
                double total = weight * selectedService.getServicePrice();
                totalPriceLabel.setText("Total: Rp " + String.format("%.0f", total));
            } else {
                totalPriceLabel.setText("Total: Rp 0");
            }
        } catch (NumberFormatException e) {
            totalPriceLabel.setText("Total: Rp 0");
        }
    }
    
    private void createTransaction() {
        try {
            String weightText = weightField.getText().trim();
            if (weightText.isEmpty()) {
                showMessage("Please enter the weight of your laundry!", true);
                return;
            }
            
            double weight = Double.parseDouble(weightText);
            
            if (weight < 2 || weight > 50) {
                showMessage("Weight must be between 2-50 kg!", true);
                return;
            }
            
            String notes = notesArea.getText().trim();
            if (notes.isEmpty()) {
                notes = "Standard laundry service";
            }
            
            Transaction transaction = new Transaction(
                selectedService.getServiceID(),
                currentUser.getUserID(),
                weight,
                notes
            );
            
            if (transactionController.createTransaction(transaction)) {
                showMessage("Order created successfully! Your laundry will be ready in " + 
                          selectedService.getServiceDuration() + " days.", false);
                
                // Clear form
                weightField.clear();
                notesArea.clear();
                totalPriceLabel.setText("Total: Rp 0");
                
                // Show success dialog
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Order Created");
                successAlert.setHeaderText("Your laundry order has been placed!");
                successAlert.setContentText("Service: " + selectedService.getServiceName() + "\n" +
                                           "Weight: " + weight + " kg\n" +
                                           "Total: Rp " + String.format("%.0f", weight * selectedService.getServicePrice()) + "\n" +
                                           "Ready in: " + selectedService.getServiceDuration() + " days\n\n" +
                                           "Status: Pending (waiting for staff assignment)");
                successAlert.showAndWait();
                
            } else {
                showMessage("Failed to create order. Please try again.", true);
            }
            
        } catch (NumberFormatException e) {
            showMessage("Please enter a valid weight number!", true);
        }
    }
    
    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        if (isError) {
            messageLabel.setStyle("-fx-text-fill: red;");
        } else {
            messageLabel.setStyle("-fx-text-fill: green;");
        }
    }
    
    public void show() {
        primaryStage.show();
    }
    
    public void setBackButtonAction(Runnable action) {
        backButton.setOnAction(e -> action.run());
    }
}