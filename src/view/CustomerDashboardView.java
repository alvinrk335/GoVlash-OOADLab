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

public class CustomerDashboardView {
    private Stage primaryStage;
    
    private Label welcomeLabel;
    private Button viewServicesButton;
    private Button viewMyOrdersButton;
    private Button viewNotificationsButton;
    private Button logoutButton;

    public CustomerDashboardView(Stage stage) {
        this.primaryStage = stage;
        initComponents();
    }
    
    private void initComponents() {
        primaryStage.setTitle("GoVlash Laundry - Customer Dashboard");
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        
        welcomeLabel = new Label();
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        viewServicesButton = new Button("View Services & Place Order");
        viewMyOrdersButton = new Button("View My Orders");
        viewNotificationsButton = new Button("View Notifications");
        logoutButton = new Button("Logout");
        
        // Button styling
        viewServicesButton.setPrefWidth(200);
        viewMyOrdersButton.setPrefWidth(200);
        viewNotificationsButton.setPrefWidth(200); 
        logoutButton.setPrefWidth(200);
        
        root.getChildren().addAll(welcomeLabel, viewServicesButton, viewMyOrdersButton, viewNotificationsButton, logoutButton);
        
        Scene scene = new Scene(root, 400, 350); 
        primaryStage.setScene(scene);
    }


    public void setWelcomeMessage(String userName) {
        welcomeLabel.setText("Welcome, " + userName + "!");
    }

    public void setOnViewServices(Runnable action) {
        viewServicesButton.setOnAction(e -> action.run());
    }

    public void setOnViewMyOrders(Runnable action) {
        viewMyOrdersButton.setOnAction(e -> action.run());
    }

    public void setOnViewNotifications(Runnable action) { // new
        viewNotificationsButton.setOnAction(e -> action.run());
    }

    public void setOnLogout(Runnable action) {
        logoutButton.setOnAction(e -> action.run());
    }
    
    public void show() {
        primaryStage.show();
    }
}
