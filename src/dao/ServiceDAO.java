package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Service;
import util.Connect;

/**
 * DAO untuk operasi CRUD Service di database
 */
public class ServiceDAO {

    private final String table_name = "MsService";
    private final Connect db = Connect.getInstance();

    /**
     * Menambahkan service baru ke database
     * @param serviceName nama service
     * @param serviceDescription deskripsi service
     * @param servicePrice harga service
     * @param serviceDuration durasi service (hari)
     */
    public void addService(String serviceName, String serviceDescription, Double servicePrice, Integer serviceDuration) {
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
            e.printStackTrace();
        }
    }

    /**
     * Mengupdate data service
     * @param service objek service yang sudah dimodifikasi
     */
    public void updateService(Service service) {
        String query = "UPDATE " + table_name
                + " SET serviceName=?, serviceDescription=?, servicePrice=?, serviceDuration=? WHERE serviceID=?";
        Connection conn = db.getConnection();

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, service.getServiceName());
            ps.setString(2, service.getServiceDescription());
            ps.setDouble(3, service.getServicePrice());
            ps.setInt(4, service.getServiceDuration());
            ps.setInt(5, service.getServiceID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Menghapus service berdasarkan ID
     * @param serviceID id service
     */
    public void deleteService(Integer serviceID) {
        String query = "DELETE FROM " + table_name + " WHERE serviceID=" + serviceID;
        db.execUpdate(query);
    }

    /**
     * Mengambil semua service
     * @return ObservableList<Service>
     */
    public ObservableList<Service> getAllServices() {
        ObservableList<Service> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + table_name + " ORDER BY serviceID ASC";

        try {
            ResultSet rs = db.execQuery(query);
            while (rs.next()) {
                list.add(new Service(
                        rs.getInt("serviceID"),
                        rs.getString("serviceName"),
                        rs.getString("serviceDescription"),
                        rs.getDouble("servicePrice"),
                        rs.getInt("serviceDuration")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Mengambil service berdasarkan ID
     * @param id id service
     * @return objek Service atau null jika tidak ditemukan
     */
    public Service getServiceById(Integer id) {
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

    /**
     * Mengecek keberadaan service berdasarkan ID
     * @param id id service
     * @return true jika ada, false jika tidak
     */
    public boolean searchService(Integer id) {
        String query = "SELECT * FROM " + table_name + " WHERE serviceID=" + id;
        ResultSet rs = db.execQuery(query);

        try {
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
