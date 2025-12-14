package view;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import model.User;

import controller.UserController;
import controller.ServiceController;
import controller.TransactionController;
import model.Employee;
import model.Service;
import model.Transaction;

import java.util.List;

public class MainView extends Application {

	private Stage primaryStage;
	private UserController userController = new UserController();
	private ServiceController serviceController = new ServiceController();
	private TransactionController transactionController = new TransactionController();
	private static User currentUser;
	
	public void launchApplication(String[] args) {
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
			    case "receptionist":
			    case "laundry staff":
						showAdminDashboard(currentUser);
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
		dashboard.setOnSendNotifications(() -> showNotificationView(user));
		
		dashboard.show();
	}
	
	private void showManageEmployeeView() {
		ObservableList<Employee> employeeList = userController.getAllEmployees();
		ManageEmployeeView view = new ManageEmployeeView(primaryStage);


	    view.setBackAction(() -> {
	        showLoginView();
	    });
		
		view.show();
		
	}
	
	private void showManageServiceView() {
		ManageServiceView manageServiceView = new ManageServiceView();
		manageServiceView.setBackButtonAction(() -> showAdminDashboard(getCurrentUser()));
		manageServiceView.show();
	}
	
	private void showAllTransactionsView() {
	    AllTransactionView view = new AllTransactionView(
	    		primaryStage
	    		,getCurrentUser().getUserRole()
	    		,getCurrentUser().getUserID()
	    		);

	    view.setBackAction(() -> {
	        showAdminDashboard(getCurrentUser());
	    });

	    view.show();
	}

	
	private void showCustomerDashboard(User user) {
		CustomerDashboardView dashboard = new CustomerDashboardView(primaryStage);
		
		dashboard.setOnLogout(() -> showLoginView());
		dashboard.setOnViewMyOrders(() -> showCustomerOrders(user));
		dashboard.setOnViewServices(() -> showServiceCatalogForCustomer(user));
		dashboard.setWelcomeMessage("Welcome, " + user.getUserName() + "!");
		dashboard.setOnViewNotifications(() -> showNotificationView(user));
		
		dashboard.show();
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
		List<Transaction> transactionList = transactionController.getTransactionsByCustomer(user.getUserID());
		List<Service> services = serviceController.getAllServices();
		
		
		CustomerOrderView customerOrderView = new CustomerOrderView(primaryStage, transactionList, services);
		customerOrderView.setBackAction(() -> showCustomerDashboard(user));
		
		customerOrderView.show();
	}
	
	private void showNotificationView(User user) {
		NotificationView view = new NotificationView(primaryStage, user);
		view.setBackAction(() -> {
			if(user.getUserRole().toLowerCase().equals("customer")) {
				showCustomerDashboard(user);
			}else {
				showAdminDashboard(user);
			}
		});
		view.show();
	}
	
	private User getCurrentUser() {
		return currentUser;
	}
	
	public static void setCurrentUser(User user) {
		currentUser = user;
	}
}
