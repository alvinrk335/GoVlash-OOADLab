package model;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.Connect;

public class Service {
	private Integer serviceID;
	private String serviceName;
	private String serviceDescription;
	private Double servicePrice;
	private Integer serviceDuration;
	private static String table_name = "MsService";

	private static final Connect db = Connect.getInstance();

	public Service(Integer serviceID, String serviceName, String serviceDescription, Double servicePrice,
			Integer serviceDuration) {
		super();
		this.serviceID = serviceID;
		this.serviceName = serviceName;
		this.serviceDescription = serviceDescription;
		this.servicePrice = servicePrice;
		this.serviceDuration = serviceDuration;
	}

	public Integer getServiceID() {
		return serviceID;
	}

	public void setServiceID(Integer serviceID) {
		this.serviceID = serviceID;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	public Double getServicePrice() {
		return servicePrice;
	}

	public void setServicePrice(Double servicePrice) {
		this.servicePrice = servicePrice;
	}

	public Integer getServiceDuration() {
		return serviceDuration;
	}

	public void setServiceDuration(Integer serviceDuration) {
		this.serviceDuration = serviceDuration;
	}

	// Add Service
	public void addService() {
		String query = "INSERT INTO " + table_name
				+ " (serviceName, serviceDescription, servicePrice, serviceDuration) VALUES (?, ?, ?, ?)";
		Connection conn = db.getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement(query);

			ps.setString(1, serviceName);
			ps.setString(2, serviceDescription);
			ps.setDouble(3, servicePrice);
			ps.setInt(4, serviceDuration);

			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// update
	public void updateService() {
		String query = "UPDATE " + table_name
				+ " SET serviceName=? , serviceDescription=?, servicePrice=?, serviceDuration=?" + " WHERE serviceID=?";
		Connection conn = db.getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement(query);

			ps.setString(1, serviceName);
			ps.setString(2, serviceDescription);
			ps.setDouble(3, servicePrice);
			ps.setInt(4, serviceDuration);
			ps.setInt(5, serviceID);

			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// delete
	public void deleteService() {
		String query = "DELETE FROM " + table_name + " WHERE serviceID=" + serviceID;
		db.execUpdate(query);
	}

	// read all
	public static ObservableList<Service> getAllServices() {
		ObservableList<Service> list = FXCollections.observableArrayList();
		String query = "SELECT * FROM " + table_name + " ORDER BY serviceID ASC";

		try (ResultSet rs = db.execQuery(query)) {
			while (rs.next()) {
				list.add(new Service(rs.getInt("serviceID"), rs.getString("serviceName"),
						rs.getString("serviceDescription"), (double) rs.getInt("servicePrice"),
						rs.getInt("serviceDuration")));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	
	
	//read by id
	public static Service getServiceById(Integer id) {
	    String query = "SELECT * FROM " + table_name + " WHERE serviceID=" + id;
	    ResultSet rs = db.execQuery(query);

	    Service service = null;

	    try {
	        if (rs.next()) {
	            service = new Service(
	                rs.getInt("serviceID"),
	                rs.getString("serviceName"),
	                rs.getString("serviceDescription"),
	                rs.getDouble("servicePrice"),
	                rs.getInt("serviceDuration")
	            );
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return service;
	}


}
