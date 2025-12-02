package view;

import controller.ServiceController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Service;

public class ManageServiceView {

	private TableView<Service> table;
	private TextField tfName, tfDescription, tfPrice, tfDuration;
	private Button btnAdd, btnUpdate, btnDelete, btnClear, btnBack;
	private ServiceController controller = new ServiceController();

	private Runnable backAction; // <- BACK CALLBACK

	public void setBackButtonAction(Runnable backAction) {
		this.backAction = backAction;
	}

	public void show() {
		Stage stage = new Stage();
		start(stage);
	}

	public void start(Stage stage) {
		stage.setTitle("Service Management");

		//left side
		Label lblTitle = new Label("Service Form");
		lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		tfName = new TextField();
		tfName.setPromptText("Service Name");

		tfDescription = new TextField();
		tfDescription.setPromptText("Service Description");

		tfPrice = new TextField();
		tfPrice.setPromptText("Price");

		tfDuration = new TextField();
		tfDuration.setPromptText("Duration (1-30)");

		btnAdd = new Button("Add");
		btnUpdate = new Button("Update");
		btnDelete = new Button("Delete");
		btnClear = new Button("Clear");
		btnBack = new Button("Back"); // <- BUTTON BACK

		btnAdd.setPrefWidth(120);
		btnUpdate.setPrefWidth(120);
		btnDelete.setPrefWidth(120);
		btnClear.setPrefWidth(120);
		btnBack.setPrefWidth(120);

		VBox formBox = new VBox(10, lblTitle, tfName, tfDescription, tfPrice, tfDuration,
				btnAdd, btnUpdate, btnDelete, btnClear, btnBack // <- ADD BACK BUTTON
		);

		formBox.setPadding(new Insets(20));
		formBox.setPrefWidth(300);
		formBox.setAlignment(Pos.TOP_CENTER);
		formBox.setStyle("-fx-background-color: #f1f1f1;");

		// right side
		table = new TableView<>();
		table.setItems(controller.getAllServices());

		TableColumn<Service, Integer> colID = new TableColumn<>("ID");
		colID.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getServiceID()).asObject());

		TableColumn<Service, String> colName = new TableColumn<>("Name");
		colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getServiceName()));

		TableColumn<Service, String> colDesc = new TableColumn<>("Description");
		colDesc.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getServiceDescription()));

		TableColumn<Service, Double> colPrice = new TableColumn<>("Price");
		colPrice.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getServicePrice()).asObject());

		TableColumn<Service, Integer> colDur = new TableColumn<>("Duration");
		colDur.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getServiceDuration()).asObject());

		table.getColumns().addAll(colID, colName, colDesc, colPrice, colDur);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		table.setOnMouseClicked(e -> fillFormFromTable());

		// BUTTON EVENTS
		btnAdd.setOnAction(e -> addService());
		btnUpdate.setOnAction(e -> updateService());
		btnDelete.setOnAction(e -> deleteService());
		btnClear.setOnAction(e -> clearForm());

		// ---- BACK BUTTON ACTION ----
		btnBack.setOnAction(e -> {
			if (backAction != null) {
				Stage stg = (Stage) btnBack.getScene().getWindow();
				stg.close();     // tutup window sekarang
				backAction.run(); // kembali ke dashboard admin
			}
		});

		HBox root = new HBox(formBox, table);
		HBox.setHgrow(table, Priority.ALWAYS);

		Scene scene = new Scene(root, 900, 500);
		stage.setScene(scene);
		stage.show();
	}

	// ------------- LOGIC AREA -----------------

	private void fillFormFromTable() {
		Service s = table.getSelectionModel().getSelectedItem();
		if (s == null) return;

		tfName.setText(s.getServiceName());
		tfDescription.setText(s.getServiceDescription());
		tfPrice.setText(String.valueOf(s.getServicePrice()));
		tfDuration.setText(String.valueOf(s.getServiceDuration()));
	}

	private void addService() {
		try {
			String msg = controller.validateAddService(
					tfName.getText(),
					tfDescription.getText(),
					Double.parseDouble(tfPrice.getText()),
					Integer.parseInt(tfDuration.getText())
			);

			if (msg.equalsIgnoreCase("service valid")) {
				controller.addService(
						tfName.getText(),
						tfDescription.getText(),
						Double.parseDouble(tfPrice.getText()),
						Integer.parseInt(tfDuration.getText())
				);
			}

			alert(msg);
			refresh();
			clearForm();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateService() {
		Service s = table.getSelectionModel().getSelectedItem();
		if (s == null) {
			alert("Select a service first!");
			return;
		}

		try {
			String msg = controller.validateEditService(
					s.getServiceID(),
					tfName.getText(),
					tfDescription.getText(),
					Double.parseDouble(tfPrice.getText()),
					Integer.parseInt(tfDuration.getText())
			);

			if (msg.equalsIgnoreCase("service valid")) {
				controller.editService(
						s.getServiceID(),
						tfName.getText(),
						tfDescription.getText(),
						Double.parseDouble(tfPrice.getText()),
						Integer.parseInt(tfDuration.getText())
				);
			}

			alert(msg);
			refresh();

		} catch (Exception e) {
			alert("Please fill price/duration with numbers!");
		}
	}

	private void deleteService() {
		Service s = table.getSelectionModel().getSelectedItem();
		if (s == null) {
			alert("Select a service to delete!");
			return;
		}

		s.deleteService();
		alert("Service deleted!");
		refresh();
		clearForm();
	}

	private void refresh() {
		table.setItems(controller.getAllServices());
	}

	private void clearForm() {
		tfName.clear();
		tfDescription.clear();
		tfPrice.clear();
		tfDuration.clear();
		table.getSelectionModel().clearSelection();
	}

	private void alert(String msg) {
		Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
		a.show();
	}
}
