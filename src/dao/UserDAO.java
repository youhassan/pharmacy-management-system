package dao;

import database.DBConnection;
import model.Role;
import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class UserDAO {
    public void addUser(User user) throws SQLException
    {
        String sql = "INSERT INTO User (username, password, role) VALUES (?, ?, ?)";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().name());
            ps.executeUpdate();
        }
    }


    public User getUserByUsername(String username) throws SQLException
    {
        String sql = "SELECT username, role FROM User WHERE username = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                User user = new User(
                        rs.getString("username"),
                        Role.valueOf(rs.getString("role"))
                );
                return user;
            }
            return null;
        }
    }


    public List<User> getAllUsers() throws SQLException
    {
        List<User> users = new ArrayList<>();

        String sql = "SELECT username, role FROM User";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                User user = new User(
                        rs.getString("username"),
                        Role.valueOf(rs.getString("role"))
                );
                users.add(user);
            }
            return users;
        }
    }


    public void updateUser(String oldUsername, User user) throws SQLException
    {
        String sql = "UPDATE User SET username = ?, password = ?, role = ? WHERE username = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().name());
            ps.setString(4, oldUsername);
            ps.executeUpdate();
        }
    }


    public void deleteUser(User user) throws SQLException
    {
        String sql = "DELETE FROM User WHERE username = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        )
        {
            ps.setString(1, user.getUsername());
            ps.executeUpdate();
        }
    }


    public User login(String username, String password) throws SQLException
    {
        String sql = "SELECT username, role FROM User WHERE username = ? AND password = ?";

        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                User user = new User(
                        rs.getString("username"),
                        Role.valueOf(rs.getString("role"))
                );
                return user;
            }
            return null;
        }
    }
}
