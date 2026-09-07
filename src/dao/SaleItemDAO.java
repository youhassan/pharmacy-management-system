package dao;

import database.DBConnection;
import model.SaleItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class SaleItemDAO {

    public void addSaleItem(SaleItem saleItem) throws SQLException
    {
        String sql = "INSERT INTO SaleItem (saleID, medicineID, quantitySold, unitPrice) VALUES (?, ?, ?, ?)";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, saleItem.getSaleID());
            ps.setInt(2, saleItem.getMedicineID());
            ps.setInt(3, saleItem.getQuantitySold());
            ps.setDouble(4, saleItem.getUnitPrice());
            ps.executeUpdate();
        }
    }


    public SaleItem getSaleItemByID(int itemID) throws SQLException
    {
        String sql = "SELECT * FROM SaleItem WHERE itemID = ?";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, itemID);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                SaleItem saleItem = new SaleItem(
                        rs.getInt("itemID"),
                        rs.getInt("saleID"),
                        rs.getInt("medicineID"),
                        rs.getInt("quantitySold"),
                        rs.getDouble("unitPrice")
                );
                return saleItem;
            }
            return null;
        }
    }


    public List<SaleItem> getAllSaleItems() throws SQLException
    {
        List<SaleItem> saleItems = new ArrayList<>();

        String sql = "SELECT * FROM SaleItem";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                SaleItem saleItem = new SaleItem(
                        rs.getInt("itemID"),
                        rs.getInt("saleID"),
                        rs.getInt("medicineID"),
                        rs.getInt("quantitySold"),
                        rs.getDouble("unitPrice")
                );
                saleItems.add(saleItem);
            }
            return saleItems;
        }
    }


    public void updateSaleItem(SaleItem saleItem) throws SQLException
    {
        String sql = "UPDATE SaleItem SET saleID = ?, medicineID = ?, quantitySold = ?, unitPrice = ? WHERE itemID = ?";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, saleItem.getSaleID());
            ps.setInt(2, saleItem.getMedicineID());
            ps.setInt(3, saleItem.getQuantitySold());
            ps.setDouble(4, saleItem.getUnitPrice());
            ps.setInt(5, saleItem.getItemID());
            ps.executeUpdate();
        }
    }


    public void deleteSaleItem(SaleItem saleItem) throws SQLException
    {
        String sql = "DELETE FROM SaleItem WHERE itemID = ?";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, saleItem.getItemID());
            ps.executeUpdate();
        }
    }


    public List<SaleItem> getSaleItemsBySale(int saleID) throws SQLException
    {
        List<SaleItem> saleItems = new ArrayList<>();

        String sql = "SELECT * FROM SaleItem WHERE saleID = ?";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, saleID);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                SaleItem saleItem = new SaleItem(
                        rs.getInt("itemID"),
                        rs.getInt("saleID"),
                        rs.getInt("medicineID"),
                        rs.getInt("quantitySold"),
                        rs.getDouble("unitPrice")
                );
                saleItems.add(saleItem);
            }
            return saleItems;
        }
    }


    public List<SaleItem> getSaleItemsByMedicine(int medicineID) throws SQLException
    {
        List<SaleItem> saleItems = new ArrayList<>();

        String sql = "SELECT * FROM SaleItem WHERE medicineID = ?";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, medicineID);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                SaleItem saleItem = new SaleItem(
                        rs.getInt("itemID"),
                        rs.getInt("saleID"),
                        rs.getInt("medicineID"),
                        rs.getInt("quantitySold"),
                        rs.getDouble("unitPrice")
                );
                saleItems.add(saleItem);
            }
            return saleItems;
        }
    }
}