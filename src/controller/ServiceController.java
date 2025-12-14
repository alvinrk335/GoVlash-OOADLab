package controller;

import java.util.List;
import dao.ServiceDAO;
import model.Service;

/**
 * Controller untuk mengelola logika terkait Service.
 * Menghubungkan View dengan DAO Service.
 */
public class ServiceController {
    private ServiceDAO dao = new ServiceDAO();
    
    /**
     * Menambahkan service baru ke database.
     * @param name nama service
     * @param description deskripsi service
     * @param price harga service
     * @param duration durasi service dalam hari
     */
    public void addService(String name, String description, double price, int duration) {
        dao.addService(name, description, price, duration);        
    }
    
    /**
     * Mengubah/memperbarui data service yang sudah ada.
     * @param serviceId ID service yang akan diubah
     * @param name nama baru
     * @param description deskripsi baru
     * @param price harga baru
     * @param duration durasi baru dalam hari
     */
    public void editService(int serviceId, String name, String description, double price, int duration) {
        Service service = new Service(serviceId, name, description, price, duration);
        dao.updateService(service);
    }
    
    /**
     * Mengambil semua service dari database.
     * @return List semua service
     */
    public List<Service> getAllServices(){
        List<Service> services = dao.getAllServices();
        return services;
    }
    
    /**
     * Menghapus service berdasarkan ID.
     * @param serviceID ID service yang akan dihapus
     */
    public void deleteService(Integer serviceID) {
        dao.deleteService(serviceID);
    }
    
    /**
     * Validasi data sebelum menambahkan service baru.
     * @param name nama service
     * @param description deskripsi service
     * @param price harga service
     * @param duration durasi service dalam hari
     * @return pesan validasi, "service valid" jika semua valid
     */
    public String validateAddService(String name, String description, double price, int duration) {
        // name validation
        if(name.isEmpty()) {
            return "name cannot be empty";
        }
        if(name.length() > 50) {
            return "name must be less than or equals than 50 (inclusive)";
        }
        
        // description validation
        if(description.isEmpty()) {
            return "description cannot be empty";
        }
        if(description.length() > 250) {
            return "description must be less than or equals than 250 (inclusive)";
        }
        
        // price validation
        if(price <= 0) {
            return "price must be greater than 0 (exclusive)";
        }
        
        // duration validation
        if(duration < 1 || duration > 30) {
            return "duration must be between 1 and 30 days (inclusive)";
        }
        
        return "service valid";
    }
    
    /**
     * Validasi data sebelum mengedit service.
     * @param serviceId ID service yang akan diubah
     * @param name nama baru
     * @param description deskripsi baru
     * @param price harga baru
     * @param duration durasi baru
     * @return pesan validasi
     */
    public String validateEditService(Integer serviceId, String name, String description, double price, int duration) {
        boolean found = dao.searchService(serviceId);
        
        if(!found) {
            return "service not found";
        }
        
        return validateAddService(name, description, price, duration);
    }
    
    /**
     * Mengambil service berdasarkan ID.
     * @param id ID service
     * @return objek Service jika ditemukan, null jika tidak
     */
    public Service getServiceById(Integer id) {
        return dao.getServiceById(id);
    }
}
