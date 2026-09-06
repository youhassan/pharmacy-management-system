package dao;

import database.DBConnection;
import model.Sale;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

public class SaleDAO {
    public void addSale(Sale sale) throws SQLException
    {
        String sql = "INSERT INTO Sale (saleDate, username) VALUES (?, ?)";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setDate(1, java.sql.Date.valueOf(sale.getSaleDate()));
            ps.setString(2, sale.getUsername());
            ps.executeUpdate();
        }
    }


    public Sale getSaleByID(int saleID) throws SQLException
    {
        String sql = "SELECT * FROM Sale WHERE saleID = ?";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, saleID);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                Sale sale = new Sale(
                        rs.getInt("saleID"),
                        rs.getDate("saleDate").toLocalDate(),
                        rs.getString("username")
                );
                return sale;
            }
            return null;
        }
    }


    public List<Sale> getAllSales() throws SQLException
    {
        List<Sale> sales = new ArrayList<>();

        String sql = "SELECT * FROM Sale";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Sale sale = new Sale(
                        rs.getInt("saleID"),
                        rs.getDate("saleDate").toLocalDate(),
                        rs.getString("username")
                );
                sales.add(sale);
            }
            return sales;
        }
    }


    public void updateSale(Sale sale) throws SQLException
    {
        String sql = "UPDATE Sale SET saleDate = ?, username = ? WHERE saleID = ?";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setDate(1, java.sql.Date.valueOf(sale.getSaleDate()));
            ps.setString(2, sale.getUsername());
            ps.setInt(3, sale.getSaleID());
            ps.executeUpdate();
        }
    }


    public void deleteSale(Sale sale) throws SQLException
    {
        String sql = "DELETE FROM Sale WHERE saleID = ?";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, sale.getSaleID());
            ps.executeUpdate();
        }
    }


    public List<Sale> getSalesByUser(String username) throws SQLException
    {
        List<Sale> sales = new ArrayList<>();

        String sql = "SELECT * FROM Sale WHERE username = ?";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Sale sale = new Sale(
                        rs.getInt("saleID"),
                        rs.getDate("saleDate").toLocalDate(),
                        rs.getString("username")
                );
                sales.add(sale);
            }
            return sales;
        }
    }


    public List<Sale> getSalesByDate(LocalDate date) throws SQLException
    {
        List<Sale> sales = new ArrayList<>();

        String sql = "SELECT * FROM Sale WHERE saleDate = ?";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setDate(1, java.sql.Date.valueOf(date));
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Sale sale = new Sale(
                        rs.getInt("saleID"),
                        rs.getDate("saleDate").toLocalDate(),
                        rs.getString("username")
                );
                sales.add(sale);
            }
            return sales;
        }
    }
}