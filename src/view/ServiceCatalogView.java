package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Service;
import controller.ServiceController;

import java.util.List;

/**
 * ServiceCatalogView adalah tampilan katalog layanan laundry.
 * Fitur:
 * - Menampilkan daftar layanan yang tersedia
 * - Menampilkan detail layanan ketika dipilih
 * - Memungkinkan pelanggan memilih layanan
 * - Menampilkan pesan informasi atau error
 */
public class ServiceCatalogView {
    
    private Stage primaryStage;
    private ServiceController serviceController;
    private ListView<Service> serviceListView;
    private TextArea serviceDetailsArea;
    private Label messageLabel;
    private Button selectButton;
    private Button backButton;
    private Service selectedService;
    private Runnable onServiceSelected;
    
    /**
     * Konstruktor ServiceCatalogView
     * @param primaryStage Stage utama
     */
    public ServiceCatalogView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.serviceController = new ServiceController();
        initializeComponents();
    }
    
    /**
     * Inisialisasi komponen UI dan layout
     */
    private void initializeComponents() {
        primaryStage.setTitle("GoVlash Laundry - Service Catalog");
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        
        // Title
        Label titleLabel = new Label("Available Laundry Services");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Main content
        HBox mainContent = new HBox(20);
        mainContent.setAlignment(Pos.CENTER);
        
        // Left panel - List of services
        VBox leftPanel = new VBox(10);
        leftPanel.setPrefWidth(300);
        Label servicesLabel = new Label("Select a Service:");
        servicesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        serviceListView = new ListView<>();
        serviceListView.setPrefHeight(300);
        serviceListView.setCellFactory(listView -> new ServiceListCell());
        serviceListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedService = newSelection;
            if (newSelection != null) {
                showServiceDetails(newSelection);
                selectButton.setDisable(false);
            } else {
                serviceDetailsArea.clear();
                selectButton.setDisable(true);
            }
        });
        leftPanel.getChildren().addAll(servicesLabel, serviceListView);
        
        // Right panel - Service details
        VBox rightPanel = new VBox(10);
        rightPanel.setPrefWidth(300);
        Label detailsLabel = new Label("Service Details:");
        detailsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        serviceDetailsArea = new TextArea();
        serviceDetailsArea.setPrefHeight(300);
        serviceDetailsArea.setEditable(false);
        serviceDetailsArea.setWrapText(true);
        serviceDetailsArea.setPromptText("Select a service to view details");
        rightPanel.getChildren().addAll(detailsLabel, serviceDetailsArea);
        
        mainContent.getChildren().addAll(leftPanel, rightPanel);
        
        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        selectButton = new Button("Select This Service");
        selectButton.setPrefWidth(150);
        selectButton.setDisable(true);
        backButton = new Button("Back");
        backButton.setPrefWidth(100);
        buttonBox.getChildren().addAll(selectButton, backButton);
        
        // Message label
        messageLabel = new Label();
        
        root.setTop(titleLabel);
        root.setCenter(mainContent);
        root.setBottom(new VBox(10, buttonBox, messageLabel));
        
        setupEventHandlers();
        loadServices();
        
        Scene scene = new Scene(root, 650, 500);
        primaryStage.setScene(scene);
    }
    
    /**
     * Setup event handler tombol
     */
    private void setupEventHandlers() {
        selectButton.setOnAction(e -> {
            if (selectedService != null && onServiceSelected != null) {
                onServiceSelected.run();
            }
        });
    }
    
    /**
     * Load layanan dari controller ke ListView
     */
    private void loadServices() {
        List<Service> services = serviceController.getAllServices();
        ObservableList<Service> serviceList = FXCollections.observableArrayList(services);
        serviceListView.setItems(serviceList);
        
        if (services.isEmpty()) {
            showMessage("No services available at the moment", false);
            serviceDetailsArea.setText("No services to display. Please contact admin.");
        }
    }
    
    /**
     * Menampilkan detail dari layanan yang dipilih
     * @param service Layanan yang dipilih
     */
    private void showServiceDetails(Service service) {
        StringBuilder details = new StringBuilder();
        details.append("Service: ").append(service.getServiceName()).append("\n\n");
        details.append("Price: Rp ").append(String.format("%.0f", service.getServicePrice())).append(" per kg\n\n");
        details.append("Duration: ").append(service.getServiceDuration()).append(" days\n\n");
        details.append("Description:\n");
        details.append("Professional laundry service with high-quality cleaning and care for your clothes. ");
        details.append("We use premium detergents and modern equipment to ensure your garments are ");
        details.append("cleaned thoroughly and returned in excellent condition.\n\n");
        details.append("Weight Limits:\n");
        details.append("- Minimum: 2 kg\n");
        details.append("- Maximum: 50 kg per transaction\n\n");
        details.append("Process:\n");
        details.append("1. Drop off your laundry\n");
        details.append("2. We'll weigh and process your items\n");
        details.append("3. Professional cleaning process\n");
        details.append("4. Quality check and packaging\n");
        details.append("5. Ready for pickup in ").append(service.getServiceDuration()).append(" days");
        
        serviceDetailsArea.setText(details.toString());
    }
    
    /**
     * Menampilkan pesan di bawah tombol
     * @param message Pesan
     * @param isError True jika pesan error, false jika info
     */
    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }
    
    public void show() {
        primaryStage.show();
    }
    
    public Service getSelectedService() {
        return selectedService;
    }
    
    public void setOnServiceSelected(Runnable action) {
        this.onServiceSelected = action;
    }
    
    public void setBackButtonAction(Runnable action) {
        backButton.setOnAction(e -> action.run());
    }
    
    /**
     * Custom ListCell untuk menampilkan nama, harga, dan durasi layanan
     */
    private static class ServiceListCell extends ListCell<Service> {
        @Override
        protected void updateItem(Service service, boolean empty) {
            super.updateItem(service, empty);
            if (empty || service == null) {
                setText(null);
            } else {
                setText(service.getServiceName() + "\nRp " + String.format("%.0f", service.getServicePrice()) + 
                       " - " + service.getServiceDuration() + " days");
            }
        }
    }
}
