package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.User;

public class AdminDashboardView {
    private Stage primaryStage;
    private User currentUser;
    private Runnable onManageEmployees;
    private Runnable onManageServices;
    private Runnable onViewTransactions;
    private Runnable onSendNotifications;
    private Runnable onLogout;
    
    public AdminDashboardView(Stage stage, User currentUser) {
        this.primaryStage = stage;
        this.currentUser = currentUser;
        initialize();
    }
    
    public void initialize() {
        primaryStage.setTitle("GoVlash Laundry - Admin Dashboard");
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        
        Label welcomeLabel = new Label("Welcome, Admin " + currentUser.getUserName() + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        Button manageEmployeeButton = new Button("Manage Employees");
        Button manageServiceButton = new Button("Manage Services");
        Button viewTransactionButton = new Button("View All Transactions");
        Button sendNotificationsButton = new Button("Send Notifications");
        Button logoutButton = new Button("Logout");
        
        // Set button widths
        manageEmployeeButton.setPrefWidth(200);
        manageServiceButton.setPrefWidth(200);
        viewTransactionButton.setPrefWidth(200);
        sendNotificationsButton.setPrefWidth(200); 
        logoutButton.setPrefWidth(200);

        // Button actions
        manageEmployeeButton.setOnAction(e -> onManageEmployees.run());
        manageServiceButton.setOnAction(e -> onManageServices.run());
        viewTransactionButton.setOnAction(e -> onViewTransactions.run());
        sendNotificationsButton.setOnAction(e -> {
            if (onSendNotifications != null) onSendNotifications.run();
        }); // new
        logoutButton.setOnAction(e -> onLogout.run());
        
        root.getChildren().addAll(
            welcomeLabel, 
            manageEmployeeButton, 
            manageServiceButton, 
            viewTransactionButton, 
            sendNotificationsButton,
            logoutButton
        );
        
        Scene scene = new Scene(root, 400, 350); 
        primaryStage.setScene(scene);
    }
    
    public void show() {
        primaryStage.show();
    }
    
    public void setOnManageEmployees(Runnable handler) {
        this.onManageEmployees = handler;
    }

    public void setOnManageServices(Runnable handler) {
        this.onManageServices = handler;
    }

    public void setOnViewTransactions(Runnable handler) {
        this.onViewTransactions = handler;
    }

    public void setOnSendNotifications(Runnable handler) { 
        this.onSendNotifications = handler;
    }

    public void setOnLogout(Runnable handler) {
        this.onLogout = handler;
    }
}
