package controller;


import javafx.collections.ObservableList;
import model.Service;

public class ServiceController {
	public void addService(String name, String description, double price, int duration) {
		Service service = new Service(0, name, description, price, duration);
		service.addService();
		
	}
	
	public void editService(int serviceId, String name, String description, double price, int duration) {
		Service service = new Service(serviceId, name, description, price, duration);
		service.updateService();
	}
	
	public ObservableList<Service> getAllServices(){
		ObservableList<Service> services = Service.getAllServices();
		
		return services;
	}
	
	public String validateAddService(String name, String description, double price, int duration) {
		//name validation
		if(name.isEmpty()) {
			return "name cannot be empty";
		}
		if(name.length() > 50) {
			return "name must be less than or equals than 50 (inclusive)";
		}
		
		//description validation
		if(description.isEmpty()) {
			return "description cannot be empty";
		}
		if(description.length() > 250) {
			return "description must be less than or equals than 250 (inclusive)";
		}
		
		//price
		if(price <= 0) {
			return "price must be greater than 0 (exclusive)";
		}
		
		//duration
		if(duration < 1 && duration > 30) {
			return "duration must be between 1 and 30 days (inclusive)";
		}
		
		return "service valid";
	}
	
	public String validateEditService(Integer serviceId, String name, String description, double price, int duration) {

		return validateAddService(name, description, price, duration);
	}
	
	public Service getServiceById(Integer id) {
		return Service.getServiceById(id);
	}
}

