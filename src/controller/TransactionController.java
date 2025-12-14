package controller;

import model.Transaction;
import dao.TransactionDAO;

import java.util.List;

/**
 * Controller untuk mengelola logika terkait transaksi.
 * Menghubungkan View dengan DAO Transaction.
 */
public class TransactionController {

    private final TransactionDAO transactionDAO = new TransactionDAO();

    /**
     * Membuat transaksi baru.
     * Validasi dilakukan sebelum insert:
     * - transaksi tidak null
     * - transaksi valid
     * - customer dan service ID valid
     * @param transaction objek Transaction
     * @return true jika berhasil, false jika gagal
     */
    public boolean createTransaction(Transaction transaction) {
        if (transaction == null || !transaction.isValidTransaction()) return false;
        if (!transactionDAO.isValidCustomer(transaction.getCustomerID()) || !transactionDAO.isValidService(transaction.getServiceID())) return false;
        return transactionDAO.insert(transaction);
    }

    /**
     * Mengambil semua transaksi.
     * @return list semua transaksi
     */
    public List<Transaction> getAllTransactions() {
        return transactionDAO.getAll();
    }

    /**
     * Mengambil transaksi berdasarkan customer ID.
     * @param customerId ID customer
     * @return list transaksi customer tersebut
     */
    public List<Transaction> getTransactionsByCustomer(int customerId) {
        return transactionDAO.getByCustomer(customerId);
    }

    /**
     * Memperbarui status transaksi.
     * @param transactionId ID transaksi
     * @param status status baru (misal: Pending, Finished)
     * @return true jika update berhasil
     */
    public boolean updateTransactionStatus(int transactionId, String status) {
        return transactionDAO.updateStatus(transactionId, status);
    }

    /**
     * Menugaskan transaksi ke staff tertentu.
     * @param transactionId ID transaksi
     * @param staffId ID staff
     * @return true jika assignment berhasil
     */
    public boolean assignTransactionToStaff(int transactionId, int staffId) {
        return transactionDAO.assignToStaff(transactionId, staffId);
    }

    /**
     * Mengambil transaksi berdasarkan status tertentu.
     * @param status status transaksi (Pending/Finished)
     * @return list transaksi dengan status tersebut
     */
    public List<Transaction> getTransactionsByStatus(String status) {
        return transactionDAO.getByStatus(status);
    }
}
