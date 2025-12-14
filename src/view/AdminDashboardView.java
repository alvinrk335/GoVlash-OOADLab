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

/**
 * Tampilan Dashboard untuk Admin, Receptionist, atau Laundry Staff.
 * Menampilkan tombol sesuai role user saat login.
 */
public class AdminDashboardView {
    private Stage primaryStage;
    private User currentUser;

    // Handler aksi tombol
    private Runnable onManageEmployees;
    private Runnable onManageServices;
    private Runnable onViewTransactions;
    private Runnable onSendNotifications;
    private Runnable onLogout;

    /**
     * Konstruktor menerima stage dan user yang sedang login
     */
    public AdminDashboardView(Stage stage, User currentUser) {
        this.primaryStage = stage;
        this.currentUser = currentUser;
        initialize();
    }

    /**
     * Inisialisasi UI dan menambahkan tombol sesuai role user
     */
    private void initialize() {
        primaryStage.setTitle("GoVlash Laundry - Dashboard");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // Label welcome dengan role dan nama user
        Label welcomeLabel = new Label("Welcome, " + currentUser.getUserRole() + " " + currentUser.getUserName() + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        root.getChildren().add(welcomeLabel);

        // Tambahkan tombol berdasarkan role
        String role = currentUser.getUserRole().toLowerCase();

        // Jika admin, tampilkan tombol Manage Employees dan Manage Services
        if (role.equals("admin")) {
            Button manageEmployeeButton = new Button("Manage Employees");
            Button manageServiceButton = new Button("Manage Services");
            manageEmployeeButton.setPrefWidth(200);
            manageServiceButton.setPrefWidth(200);
            manageEmployeeButton.setOnAction(e -> { if (onManageEmployees != null) onManageEmployees.run(); });
            manageServiceButton.setOnAction(e -> { if (onManageServices != null) onManageServices.run(); });
            root.getChildren().addAll(manageEmployeeButton, manageServiceButton);
        }

        // Jika receptionist, laundry staff, atau admin, tampilkan tombol View Transactions
        if (role.equals("receptionist") || role.equals("laundry staff") || role.equals("admin")) {
            Button viewTransactionButton = new Button("View Transactions");
            viewTransactionButton.setPrefWidth(200);
            viewTransactionButton.setOnAction(e -> { if (onViewTransactions != null) onViewTransactions.run(); });
            root.getChildren().add(viewTransactionButton);
        }

        // Semua role punya tombol Notifications
        Button sendNotificationsButton = new Button("Notifications");
        sendNotificationsButton.setPrefWidth(200);
        sendNotificationsButton.setOnAction(e -> { if (onSendNotifications != null) onSendNotifications.run(); });
        root.getChildren().add(sendNotificationsButton);

        // Semua role punya tombol Logout / Back
        Button logoutButton = new Button("Logout");
        logoutButton.setPrefWidth(200);
        logoutButton.setOnAction(e -> { if (onLogout != null) onLogout.run(); });
        root.getChildren().add(logoutButton);

        Scene scene = new Scene(root, 400, 350);
        primaryStage.setScene(scene);
    }

    /**
     * Menampilkan stage
     */
    public void show() {
        primaryStage.show();
    }

    // Setter untuk handler aksi tombol
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
