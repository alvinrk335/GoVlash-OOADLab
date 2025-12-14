package controller;


import java.util.List;

import dao.ServiceDAO;
import javafx.collections.ObservableList;
import model.Service;

public class ServiceController {
	private ServiceDAO dao = new ServiceDAO();
	
	
	public void addService(String name, String description, double price, int duration) {
		dao.addService(name, description, price, duration);		
	}
	
	public void editService(int serviceId, String name, String description, double price, int duration) {
		Service service = new Service(serviceId, name, description, price, duration);
		dao.updateService(service);
	}
	
	public List<Service> getAllServices(){
		List<Service> services = dao.getAllServices();
		return services;
	}
	
	public void deleteService(Integer serviceID) {
		dao.deleteService(serviceID);
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
		boolean found = dao.searchService(serviceId);
		
		if(!found) {
			return "service not found";
		}
		
		return validateAddService(name, description, price, duration);
	}
	
	public Service getServiceById(Integer id) {
		return dao.getServiceById(id);
	}
}

