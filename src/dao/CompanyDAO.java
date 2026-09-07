package dao;

import database.DBConnection;
import model.Company;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class CompanyDAO {
    public void addCompany(Company company) throws SQLException
    {

        String sql = "INSERT INTO Company (compName) VALUES (?)";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setString(1, company.getCompName());
            ps.executeUpdate();
        }
    }


    public Company getCompanyByID(int compID) throws SQLException
    {
        String sql = "SELECT * FROM Company WHERE compID = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, compID);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                Company company = new Company(
                        rs.getInt("compID"),
                        rs.getString("compName")
                );
                return company;
            }
            return null;
        }
    }


    public Company getCompanyByName(String compName) throws SQLException
    {
        String sql = "SELECT * FROM Company WHERE compName = ?";

        try (
             Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setString(1, compName);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                Company company = new Company(
                        rs.getInt("compID"),
                        rs.getString("compName")
                );
                return company;
            }
            return null;
        }
    }


    public List<Company> getAllCompanies() throws SQLException
    {
        List<Company> companies = new ArrayList<>();

        String sql = "SELECT * FROM Company";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ){
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Company company = new Company(
                        rs.getInt("compID"),
                        rs.getString("compName")
                );
                companies.add(company);
            }
            return companies;
        }
    }


    public void updateCompany(String oldCompanyName, Company company) throws SQLException
    {
        String sql = "UPDATE Company SET compName = ? WHERE compName = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setString(1, company.getCompName());
            ps.setString(2, oldCompanyName);
            ps.executeUpdate();
        }
    }

    public void deleteCompany(Company company) throws SQLException
    {
        String sql = "DELETE FROM Company WHERE compID = ?";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, company.getCompID());
            ps.executeUpdate();
        }
    }
}
