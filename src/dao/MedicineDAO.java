package dao;

import database.DBConnection;
import model.Medicine;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class MedicineDAO {
    public void addMedicine(Medicine medicine) throws SQLException
    {

        String sql = "INSERT INTO Medicine (medName, quantity, expiryDate, minimumStock, compID) VALUES (?, ?, ?, ?, ?)";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setString(1, medicine.getMedName());
            ps.setInt(2, medicine.getQuantity());
            ps.setDate(3, java.sql.Date.valueOf(medicine.getExpiryDate()));
            ps.setInt(4, medicine.getMinimumStock());
            ps.setInt(5, medicine.getCompID());
            ps.executeUpdate();
        }
    }


    public Medicine getMedicineByID(int medicineID) throws SQLException
    {
        String sql = "SELECT * FROM Medicine WHERE medicineID = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, medicineID);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                Medicine medicine  = new Medicine(
                        rs.getInt("medicineID"),
                        rs.getString("medName"),
                        rs.getInt("quantity"),
                        rs.getDate("expiryDate").toLocalDate(),
                        rs.getInt("minimumStock"),
                        rs.getInt("compID")
                );
                return medicine;
            }
            return null;
        }
    }


    public Medicine getMedicineByName(String medName) throws SQLException
    {
        String sql = "SELECT * FROM Medicine WHERE medName = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setString(1, medName);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                Medicine medicine  = new Medicine(
                        rs.getInt("medicineID"),
                        rs.getString("medName"),
                        rs.getInt("quantity"),
                        rs.getDate("expiryDate").toLocalDate(),
                        rs.getInt("minimumStock"),
                        rs.getInt("compID")
                );
                return medicine;
            }
            return null;
        }
    }


    public List<Medicine> getAllMedicines() throws SQLException
    {
        List<Medicine> medicines = new ArrayList<>();

        String sql = "SELECT * FROM Medicine";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Medicine medicine = new Medicine(
                        rs.getInt("medicineID"),
                        rs.getString("medName"),
                        rs.getInt("quantity"),
                        rs.getDate("expiryDate").toLocalDate(),
                        rs.getInt("minimumStock"),
                        rs.getInt("compID")
                );
                medicines.add(medicine);
            }
            return medicines;
        }
    }


    public void updateMedicine(Medicine medicine) throws SQLException
    {
        String sql = "UPDATE Medicine SET medName = ?, quantity = ?, expiryDate = ?, minimumStock = ?, compID = ? WHERE medicineID = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setString(1, medicine.getMedName());
            ps.setInt(2, medicine.getQuantity());
            ps.setDate(3, java.sql.Date.valueOf(medicine.getExpiryDate()));
            ps.setInt(4, medicine.getMinimumStock());
            ps.setInt(5, medicine.getCompID());
            ps.setInt(6, medicine.getMedicineID());
            ps.executeUpdate();
        }
    }

    public void deleteMedicine(Medicine medicine) throws SQLException
    {
        String sql = "DELETE FROM Medicine WHERE MedicineID = ?";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, medicine.getMedicineID());
            ps.executeUpdate();
        }
    }


    public List<Medicine> getLowStockMedicines() throws SQLException
    {
        List<Medicine> medicines = new ArrayList<>();

        String sql = "SELECT * FROM Medicine WHERE quantity <= minimumStock";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Medicine medicine = new Medicine(
                        rs.getInt("medicineID"),
                        rs.getString("medName"),
                        rs.getInt("quantity"),
                        rs.getDate("expiryDate").toLocalDate(),
                        rs.getInt("minimumStock"),
                        rs.getInt("compID")
                );
                medicines.add(medicine);
            }
            return medicines;
        }
    }


    public List<Medicine> getExpiredMedicines() throws SQLException
    {
        List<Medicine> medicines = new ArrayList<>();

        String sql = "SELECT * FROM Medicine WHERE expiryDate < CURRENT_DATE";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Medicine medicine = new Medicine(
                        rs.getInt("medicineID"),
                        rs.getString("medName"),
                        rs.getInt("quantity"),
                        rs.getDate("expiryDate").toLocalDate(),
                        rs.getInt("minimumStock"),
                        rs.getInt("compID")
                );
                medicines.add(medicine);
            }
            return medicines;
        }
    }



    public List<Medicine> getExpiringSoonMedicines() throws SQLException
    {
        List<Medicine> medicines = new ArrayList<>();

        String sql = "SELECT * FROM Medicine WHERE expiryDate BETWEEN CURRENT_DATE AND DATE_ADD(CURRENT_DATE, INTERVAL 30 DAY)";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Medicine medicine = new Medicine(
                        rs.getInt("medicineID"),
                        rs.getString("medName"),
                        rs.getInt("quantity"),
                        rs.getDate("expiryDate").toLocalDate(),
                        rs.getInt("minimumStock"),
                        rs.getInt("compID")
                );
                medicines.add(medicine);
            }
            return medicines;
        }
    }
}
