package dao;

import database.DBConnection;
import model.PurchaseItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class PurchaseItemDAO {
    public void addPurchaseItem(PurchaseItem purchaseItem) throws SQLException
    {
        String sql = "INSERT INTO PurchaseItem (purID, medicineID, quantityPur, unitCost) VALUES (?, ?, ?, ?)";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, purchaseItem.getPurID());
            ps.setInt(2, purchaseItem.getMedicineID());
            ps.setInt(3, purchaseItem.getQuantityPur());
            ps.setDouble(4, purchaseItem.getUnitCost());
            ps.executeUpdate();
        }
    }


    public PurchaseItem getPurchaseItemByID(int purItemID) throws SQLException
    {
        String sql = "SELECT * FROM PurchaseItem WHERE purItemID = ?";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, purItemID);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                PurchaseItem purchaseItem = new PurchaseItem(
                        rs.getInt("purItemID"),
                        rs.getInt("purID"),
                        rs.getInt("medicineID"),
                        rs.getInt("quantityPur"),
                        rs.getDouble("unitCost")
                );
                return purchaseItem;
            }
            return null;
        }
    }


    public List<PurchaseItem> getAllPurchaseItems() throws SQLException
    {
        List<PurchaseItem> purchaseItems = new ArrayList<>();

        String sql = "SELECT * FROM PurchaseItem";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                PurchaseItem purchaseItem = new PurchaseItem(
                        rs.getInt("purItemID"),
                        rs.getInt("purID"),
                        rs.getInt("medicineID"),
                        rs.getInt("quantityPur"),
                        rs.getDouble("unitCost")
                );
                purchaseItems.add(purchaseItem);
            }
            return purchaseItems;
        }
    }


    public void updatePurchaseItem(PurchaseItem purchaseItem) throws SQLException
    {
        String sql = "UPDATE PurchaseItem SET purID = ?, medicineID = ?, quantityPur = ?, unitCost = ? WHERE purItemID = ?";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, purchaseItem.getPurID());
            ps.setInt(2, purchaseItem.getMedicineID());
            ps.setInt(3, purchaseItem.getQuantityPur());
            ps.setDouble(4, purchaseItem.getUnitCost());
            ps.setInt(5, purchaseItem.getPurItemID());
            ps.executeUpdate();
        }
    }


    public void deletePurchaseItem(PurchaseItem purchaseItem) throws SQLException
    {
        String sql = "DELETE FROM PurchaseItem WHERE purItemID = ?";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, purchaseItem.getPurItemID());
            ps.executeUpdate();
        }
    }


    public List<PurchaseItem> getPurchaseItemsByPurchase(int purID) throws SQLException
    {
        List<PurchaseItem> purchaseItems = new ArrayList<>();

        String sql = "SELECT * FROM PurchaseItem WHERE purID = ?";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, purID);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                PurchaseItem purchaseItem = new PurchaseItem(
                        rs.getInt("purItemID"),
                        rs.getInt("purID"),
                        rs.getInt("medicineID"),
                        rs.getInt("quantityPur"),
                        rs.getDouble("unitCost")
                );
                purchaseItems.add(purchaseItem);
            }
            return purchaseItems;
        }
    }


    public List<PurchaseItem> getPurchaseItemsByMedicine(int medicineID) throws SQLException
    {
        List<PurchaseItem> purchaseItems = new ArrayList<>();

        String sql = "SELECT * FROM PurchaseItem WHERE medicineID = ?";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, medicineID);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                PurchaseItem purchaseItem = new PurchaseItem(
                        rs.getInt("purItemID"),
                        rs.getInt("purID"),
                        rs.getInt("medicineID"),
                        rs.getInt("quantityPur"),
                        rs.getDouble("unitCost")
                );
                purchaseItems.add(purchaseItem);
            }
            return purchaseItems;
        }
    }
}
