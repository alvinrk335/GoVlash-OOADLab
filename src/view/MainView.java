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

/**
 * Kelas utama untuk menjalankan aplikasi GoVlash Laundry.
 * Bertanggung jawab untuk mengatur navigasi antar view sesuai role user.
 * Memulai aplikasi, menampilkan login, dashboard, dan view lain sesuai interaksi pengguna.
 */
public class MainView extends Application {

    private Stage primaryStage;
    private UserController userController = new UserController();
    private ServiceController serviceController = new ServiceController();
    private TransactionController transactionController = new TransactionController();
    private static User currentUser;

    /**
     * Metode untuk meluncurkan aplikasi JavaFX
     */
    public void launchApplication(String[] args) {
        launch(args);
    }

    /**
     * Metode start utama JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoginView();
    }

    /**
     * Menampilkan tampilan login
     */
    private void showLoginView() {
        LoginView loginView = new LoginView(primaryStage);

        // Aksi tombol login
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

                // Navigasi berdasarkan role
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

        // Aksi tombol register
        loginView.setRegisterButtonAction(() -> showRegisterView());

        loginView.show();
    }

    /**
     * Menampilkan view register untuk customer baru
     */
    private void showRegisterView() {
        RegisterView registerView = new RegisterView(primaryStage);
        registerView.setBackButtonAction(() -> showLoginView());
        registerView.show();
    }

    /**
     * Menampilkan dashboard admin/receptionist/laundry staff
     */
    private void showAdminDashboard(User user) {
        AdminDashboardView dashboard = new AdminDashboardView(primaryStage, user);
        dashboard.setOnManageEmployees(() -> showManageEmployeeView());
        dashboard.setOnManageServices(() -> showManageServiceView());
        dashboard.setOnViewTransactions(() -> showAllTransactionsView());
        dashboard.setOnLogout(() -> showLoginView());
        dashboard.setOnSendNotifications(() -> showNotificationView(user));

        dashboard.show();
    }

    /**
     * Menampilkan view untuk manajemen employee
     */
    private void showManageEmployeeView() {
        ManageEmployeeView view = new ManageEmployeeView(primaryStage, currentUser);
        view.setBackAction(() -> showLoginView());
        view.show();
    }

    /**
     * Menampilkan view untuk manajemen service
     */
    private void showManageServiceView() {
        ManageServiceView manageServiceView = new ManageServiceView();
        manageServiceView.setBackButtonAction(() -> showAdminDashboard(getCurrentUser()));
        manageServiceView.show();
    }

    /**
     * Menampilkan semua transaksi untuk admin/receptionist/laundry staff
     */
    private void showAllTransactionsView() {
        AllTransactionView view = new AllTransactionView(
                primaryStage,
                getCurrentUser().getUserRole(),
                getCurrentUser().getUserID()
        );

        view.setBackAction(() -> showAdminDashboard(getCurrentUser()));
        view.show();
    }

    /**
     * Menampilkan dashboard customer
     */
    private void showCustomerDashboard(User user) {
        CustomerDashboardView dashboard = new CustomerDashboardView(primaryStage);

        dashboard.setOnLogout(() -> showLoginView());
        dashboard.setOnViewMyOrders(() -> showCustomerOrders(user));
        dashboard.setOnViewServices(() -> showServiceCatalogForCustomer(user));
        dashboard.setWelcomeMessage("Welcome, " + user.getUserName() + "!");
        dashboard.setOnViewNotifications(() -> showNotificationView(user));

        dashboard.show();
    }

    /**
     * Menampilkan katalog layanan untuk customer
     */
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

    /**
     * Menampilkan form untuk membuat transaksi laundry baru
     */
    private void showCreateTransactionView(User user, Service service) {
        CreateTransactionView createView = new CreateTransactionView(primaryStage, user, service);
        createView.setBackButtonAction(() -> showServiceCatalogForCustomer(user));
        createView.show();
    }

    /**
     * Menampilkan daftar order customer
     */
    private void showCustomerOrders(User user) {
        List<Transaction> transactionList = transactionController.getTransactionsByCustomer(user.getUserID());
        List<Service> services = serviceController.getAllServices();

        CustomerOrderView customerOrderView = new CustomerOrderView(primaryStage, transactionList, services);
        customerOrderView.setBackAction(() -> showCustomerDashboard(user));
        customerOrderView.show();
    }

    /**
     * Menampilkan view notifikasi sesuai role
     */
    private void showNotificationView(User user) {
        NotificationView view = new NotificationView(primaryStage, user);
        view.setBackAction(() -> {
            if(user.getUserRole().toLowerCase().equals("customer")) {
                showCustomerDashboard(user);
            } else {
                showAdminDashboard(user);
            }
        });
        view.show();
    }

    /**
     * Mengambil user saat ini
     */
    private User getCurrentUser() {
        return currentUser;
    }

    /**
     * Menetapkan user saat ini
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}
