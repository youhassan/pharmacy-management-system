package dao;

import database.DBConnection;
import model.Purchase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class PurchaseDAO {

    public void addPurchase(Purchase purchase) throws SQLException
    {
        String sql = "INSERT INTO Purchase (compID, purDate) VALUES (?, ?)";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
        ){
            ps.setInt(1, purchase.getCompID());
            ps.setDate(2, java.sql.Date.valueOf(purchase.getPurDate()));
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next())
            {
                purchase.setPurID(rs.getInt(1));
            }
        }
    }


    public Purchase getPurchaseByID(int purID) throws SQLException
    {
        String sql = "SELECT * FROM Purchase WHERE purID = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, purID);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                Purchase purchase = new Purchase(
                        rs.getInt("purID"),
                        rs.getInt("compID"),
                        rs.getDate("purDate").toLocalDate()
                );
                return purchase;
            }
            return null;
        }
    }


    public List<Purchase> getAllPurchases() throws SQLException
    {
        List<Purchase> purchases = new ArrayList<>();

        String sql = "SELECT * FROM Purchase";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Purchase purchase = new Purchase(
                        rs.getInt("purID"),
                        rs.getInt("compID"),
                        rs.getDate("purDate").toLocalDate()
                );
                purchases.add(purchase);
            }
            return purchases;
        }
    }


    public void updatePurchase(Purchase purchase) throws SQLException
    {
        String sql = "UPDATE Purchase SET compID = ?, purDate = ? WHERE purID = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, purchase.getCompID());
            ps.setDate(2, java.sql.Date.valueOf(purchase.getPurDate()));
            ps.setInt(3, purchase.getPurID());
            ps.executeUpdate();
        }
    }


    public void deletePurchase(Purchase purchase) throws SQLException
    {
        String sql = "DELETE FROM Purchase WHERE purID = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, purchase.getPurID());
            ps.executeUpdate();
        }
    }


    public List<Purchase> getPurchasesByCompany(int compID) throws SQLException
    {
        List<Purchase> purchases = new ArrayList<>();

        String sql = "SELECT * FROM Purchase WHERE compID = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, compID);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Purchase purchase = new Purchase(
                        rs.getInt("purID"),
                        rs.getInt("compID"),
                        rs.getDate("purDate").toLocalDate()
                );
                purchases.add(purchase);
            }
            return purchases;
        }
    }


    public List<Purchase> getPurchasesByDate(java.time.LocalDate date) throws SQLException
    {
        List<Purchase> purchases = new ArrayList<>();

        String sql = "SELECT * FROM Purchase WHERE purDate = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setDate(1, java.sql.Date.valueOf(date));
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Purchase purchase = new Purchase(
                        rs.getInt("purID"),
                        rs.getInt("compID"),
                        rs.getDate("purDate").toLocalDate()
                );
                purchases.add(purchase);
            }
            return purchases;
        }
    }
}