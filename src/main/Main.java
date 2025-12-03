package main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.User;
import model.Customer;
import util.Connect;
import controller.UserController;
import controller.ServiceController;
import controller.TransactionController;
import model.Service;
import model.Transaction;
import view.LoginView;
import view.RegisterView;
import view.ManageServiceView;
import view.ServiceCatalogView;
import view.AdminDashboardView;
import view.CreateTransactionView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Main extends Application {

	private Stage primaryStage;
	private Connect connect = Connect.getInstance();
	private UserController userController = new UserController();
	private ServiceController serviceController = new ServiceController();
	private TransactionController transactionController = new TransactionController();
	private static User currentUser;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		showLoginView();
	}
	
	private void showLoginView() {
		LoginView loginView = new LoginView(primaryStage);
		loginView.setLoginButtonAction(() -> {
	    	String userName = loginView.getUsername();
	    	String password = loginView.getPassword();
	    	
	    	
	    	User currentUser = userController.login(userName, password);
	    	setCurrentUser(currentUser);
	    	
	    	if (userName.isEmpty() || password.isEmpty()) {
				loginView.setMessage("Please fill in all fields!", true);
				return;
			}
			
			if (currentUser != null) {
				loginView.setMessage("Login successful!", false);
			
				switch (currentUser.getUserRole().toLowerCase()) {
					case "admin":
						showAdminDashboard(currentUser);
						break;
					case "receptionist":
						showReceptionistDashboard(currentUser);
						break;
					case "laundry staff":
						showLaundryStaffDashboard(currentUser);
						break;
					case "customer":
						showCustomerDashboard(currentUser);
						break;
					default:
						loginView.setMessage("Invalid user role!", true);
				}
			} else {
				loginView.setMessage("Invalid username or password!", true);
			}
		});
		
		loginView.setRegisterButtonAction(() -> showRegisterView());
		
		loginView.show();
	}
	
	private void showRegisterView() {
		RegisterView registerView = new RegisterView(primaryStage);
		registerView.setBackButtonAction(() -> showLoginView());
		registerView.show();
	}
	
	private void showAdminDashboard(User user) {
		AdminDashboardView dashboard = new AdminDashboardView(primaryStage, user);
		dashboard.setOnManageEmployees(() -> showManageEmployeeView());
		dashboard.setOnManageServices(() -> showManageServiceView());
		dashboard.setOnViewTransactions(() -> showAllTransactionsView());
		dashboard.setOnLogout(() -> showLoginView());
		
		dashboard.show();
	}
	
	private void showManageEmployeeView() {
		primaryStage.setTitle("GoVlash Laundry - Manage Employees");
		
		VBox root = new VBox(10);
		root.setPadding(new Insets(20));
		
		Label titleLabel = new Label("Employee Management");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		
		// Employee list (TableView will be added here)
		Label employeeListLabel = new Label("Employee List:");
		TextArea employeeListArea = new TextArea();
		employeeListArea.setEditable(false);
		employeeListArea.setPrefHeight(200);
		loadEmployeeList(employeeListArea);
		
		// Buttons
		HBox buttonBox = new HBox(10);
		buttonBox.setAlignment(Pos.CENTER);
		
		Button addEmployeeButton = new Button("Add New Employee");
		Button refreshButton = new Button("Refresh List");
		Button backButton = new Button("Back to Dashboard");
		
		buttonBox.getChildren().addAll(addEmployeeButton, refreshButton, backButton);
		
		// Event handlers
		addEmployeeButton.setOnAction(e -> showAddEmployeeView());
		refreshButton.setOnAction(e -> loadEmployeeList(employeeListArea));
		backButton.setOnAction(e -> {
			// Navigate back to admin dashboard (need to pass user object)
			showLoginView(); // Temporary - should go back to admin dashboard
		});
		
		root.getChildren().addAll(titleLabel, employeeListLabel, employeeListArea, buttonBox);
		
		Scene scene = new Scene(root, 600, 400);
		primaryStage.setScene(scene);
	}
	
	private void loadEmployeeList(TextArea textArea) {
		try {
			String query = "SELECT * FROM MsUser WHERE userRole IN ('Admin', 'Receptionist', 'Laundry Staff')";
			ResultSet rs = connect.execQuery(query);
			
			StringBuilder sb = new StringBuilder();
			sb.append("ID\tName\t\tEmail\t\t\tRole\n");
			sb.append("--------------------------------------------------------\n");
			
			while (rs.next()) {
				sb.append(rs.getInt("userID")).append("\t");
				sb.append(rs.getString("userName")).append("\t\t");
				sb.append(rs.getString("userEmail")).append("\t\t");
				sb.append(rs.getString("userRole")).append("\n");
			}
			
			textArea.setText(sb.toString());
		} catch (SQLException e) {
			textArea.setText("Error loading employee data: " + e.getMessage());
		}
	}
	
	private void showAddEmployeeView() {
		primaryStage.setTitle("GoVlash Laundry - Add New Employee");
		
		VBox root = new VBox(10);
		root.setPadding(new Insets(20));
		root.setAlignment(Pos.CENTER);
		
		Label titleLabel = new Label("Add New Employee");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		
		// Form (similar to registration but for employees)
		GridPane form = new GridPane();
		form.setHgap(10);
		form.setVgap(10);
		form.setAlignment(Pos.CENTER);
		
		// Add employee form fields here
		Label messageLabel = new Label("Add Employee functionality coming soon!");
		messageLabel.setStyle("-fx-text-fill: blue;");
		
		Button backButton = new Button("Back to Employee Management");
		backButton.setOnAction(e -> showManageEmployeeView());
		
		root.getChildren().addAll(titleLabel, form, messageLabel, backButton);
		
		Scene scene = new Scene(root, 500, 400);
		primaryStage.setScene(scene);
	}
	
	private void showReceptionistDashboard(User user) {
		Label label = new Label("Receptionist Dashboard - Coming Soon!");
		Scene scene = new Scene(new VBox(label), 400, 300);
		primaryStage.setScene(scene);
	}
	
	private void showLaundryStaffDashboard(User user) {
		Label label = new Label("Laundry Staff Dashboard - Coming Soon!");
		Scene scene = new Scene(new VBox(label), 400, 300);
		primaryStage.setScene(scene);
	}
	
	private void showManageServiceView() {
		ManageServiceView manageServiceView = new ManageServiceView();
		manageServiceView.setBackButtonAction(() -> showAdminDashboard(getCurrentUser()));
		manageServiceView.show();
	}
	
	private void showAllTransactionsView() {
		primaryStage.setTitle("GoVlash Laundry - All Transactions");
		
		VBox root = new VBox(10);
		root.setPadding(new Insets(20));
		
		Label titleLabel = new Label("All Transactions");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		
		TextArea transactionArea = new TextArea();
		transactionArea.setPrefRowCount(15);
		transactionArea.setEditable(false);
		
		// Load all transactions
		List<Transaction> transactions = transactionController.getAllTransactions();
		StringBuilder content = new StringBuilder();
		
		if (transactions.isEmpty()) {
			content.append("No transactions found.");
		} else {
			content.append("TRANSACTION HISTORY:\n");
			content.append("===================\n\n");
			
			for (Transaction transaction : transactions) {
				content.append("Transaction ID: ").append(transaction.getTransactionID()).append("\n");
				content.append("Customer ID: ").append(transaction.getCustomerID()).append("\n");
				content.append("Service ID: ").append(transaction.getServiceID()).append("\n");
				content.append("Weight: ").append(transaction.getTotalWeight()).append(" kg\n");
				content.append("Status: ").append(transaction.getTransactionStatus()).append("\n");
				content.append("Date: ").append(transaction.getTransactionDate()).append("\n");
				if (transaction.getLaundryStaffID() != null) {
					content.append("Assigned Staff: ").append(transaction.getLaundryStaffID()).append("\n");
				}
				content.append("Notes: ").append(transaction.getTransactionNotes()).append("\n");
				content.append("------------------------\n\n");
			}
		}
		
		transactionArea.setText(content.toString());
		
		Button backButton = new Button("Back to Admin Dashboard");
		backButton.setOnAction(e -> showAdminDashboard(getCurrentUser()));
		
		root.getChildren().addAll(titleLabel, transactionArea, backButton);
		
		Scene scene = new Scene(root, 600, 500);
		primaryStage.setScene(scene);
	}
	
	private void showCustomerDashboard(User user) {
		primaryStage.setTitle("GoVlash Laundry - Customer Dashboard");
		
		VBox root = new VBox(10);
		root.setPadding(new Insets(20));
		root.setAlignment(Pos.CENTER);
		
		Label welcomeLabel = new Label("Welcome, " + user.getUserName() + "!");
		welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		
		Button viewServicesButton = new Button("View Services & Place Order");
		Button viewMyOrdersButton = new Button("View My Orders");
		Button logoutButton = new Button("Logout");
		
		// Button styling
		viewServicesButton.setPrefWidth(200);
		viewMyOrdersButton.setPrefWidth(200);
		logoutButton.setPrefWidth(200);
		
		// Event handlers
		viewServicesButton.setOnAction(e -> showServiceCatalogForCustomer(user));
		viewMyOrdersButton.setOnAction(e -> showCustomerOrders(user));
		logoutButton.setOnAction(e -> showLoginView());
		
		root.getChildren().addAll(welcomeLabel, viewServicesButton, viewMyOrdersButton, logoutButton);
		
		Scene scene = new Scene(root, 400, 300);
		primaryStage.setScene(scene);
	}
	
	private void showServiceCatalogForCustomer(User user) {
		ServiceCatalogView catalogView = new ServiceCatalogView(primaryStage);
		catalogView.setOnServiceSelected(() -> {
			Service selectedService = catalogView.getSelectedService();
			if (selectedService != null) {
				showCreateTransactionView(user, selectedService);
			}
		});
		catalogView.setBackButtonAction(() -> showCustomerDashboard(user));
		catalogView.show();
	}
	
	private void showCreateTransactionView(User user, Service service) {
		CreateTransactionView createView = new CreateTransactionView(primaryStage, user, service);
		createView.setBackButtonAction(() -> showServiceCatalogForCustomer(user));
		createView.show();
	}
	
	private void showCustomerOrders(User user) {
		primaryStage.setTitle("GoVlash Laundry - My Orders");
		
		VBox root = new VBox(10);
		root.setPadding(new Insets(20));
		
		Label titleLabel = new Label("My Laundry Orders");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		
		// Create TableView for transactions
		TableView<Transaction> ordersTable = new TableView<>();
		ordersTable.setPrefHeight(400);
		
		// Create columns
		TableColumn<Transaction, Integer> idColumn = new TableColumn<>("Order ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
		idColumn.setPrefWidth(80);
		
		TableColumn<Transaction, Double> weightColumn = new TableColumn<>("Weight (kg)");
		weightColumn.setCellValueFactory(new PropertyValueFactory<>("totalWeight"));
		weightColumn.setPrefWidth(100);
		
		TableColumn<Transaction, String> statusColumn = new TableColumn<>("Status");
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("transactionStatus"));
		statusColumn.setPrefWidth(100);
		
		TableColumn<Transaction, String> dateColumn = new TableColumn<>("Order Date");
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
		dateColumn.setPrefWidth(120);
		
		TableColumn<Transaction, String> serviceColumn = new TableColumn<>("Service");
		serviceColumn.setCellValueFactory(cellData -> {
			Service service = serviceController.getServiceById(cellData.getValue().getServiceID());
			return new javafx.beans.property.SimpleStringProperty(service != null ? service.getServiceName() : "Unknown");
		});
		serviceColumn.setPrefWidth(120);
		
		TableColumn<Transaction, String> priceColumn = new TableColumn<>("Estimated Price");
		priceColumn.setCellValueFactory(cellData -> {
			Transaction transaction = cellData.getValue();
			Service service = serviceController.getServiceById(transaction.getServiceID());
			if (service != null) {
				double estimatedPrice = transaction.getTotalWeight() * service.getServicePrice();
				return new javafx.beans.property.SimpleStringProperty("Rp " + String.format("%.0f", estimatedPrice));
			}
			return new javafx.beans.property.SimpleStringProperty("N/A");
		});
		priceColumn.setPrefWidth(120);
		
		TableColumn<Transaction, String> notesColumn = new TableColumn<>("Notes");
		notesColumn.setCellValueFactory(new PropertyValueFactory<>("transactionNotes"));
		notesColumn.setPrefWidth(150);
		
		// Add columns to table
		ordersTable.getColumns().addAll(idColumn, weightColumn, statusColumn, dateColumn, serviceColumn, priceColumn, notesColumn);
		
		// Load customer's transactions
		List<Transaction> transactions = transactionController.getTransactionsByCustomer(user.getUserID());
		
		Label noOrdersLabel = new Label("You haven't placed any orders yet.\nClick 'View Services & Place Order' to get started!");
		noOrdersLabel.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;");
		
		if (!transactions.isEmpty()) {
			ordersTable.getItems().addAll(transactions);
		}
		
		Button backButton = new Button("Back to Dashboard");
		backButton.setOnAction(e -> showCustomerDashboard(user));
		
		if (transactions.isEmpty()) {
			root.getChildren().addAll(titleLabel, noOrdersLabel, backButton);
		} else {
			root.getChildren().addAll(titleLabel, ordersTable, backButton);
		}
		
		Scene scene = new Scene(root, 800, 600);
		primaryStage.setScene(scene);
	}
	
	private User getCurrentUser() {
		// This would normally be stored in a session or application state
		// For now, return a dummy admin user
		User admin = new User();
		admin.setUserID(1);
		admin.setUserName("Admin");
		admin.setUserRole("Admin");
		return admin;
	}
	
	public static void setCurrentUser(User user) {
		currentUser = user;
	}
}
