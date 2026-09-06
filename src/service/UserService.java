package service;

import dao.UserDAO;
import model.Role;
import model.User;
import java.sql.SQLException;
import java.util.List;
import dao.SaleDAO;

public class UserService {
    private UserDAO userDAO;
    private SaleDAO saleDAO;


    public UserService()
    {
        userDAO = new UserDAO();
        saleDAO = new SaleDAO();
    }

    public User login(String username, String password) throws SQLException
    {
        if (username == null || username.isBlank())
        {
            throw new IllegalArgumentException("Username can't be empty");
        }
        if (password == null || password.isBlank())
        {
            throw new IllegalArgumentException("Password can't be empty");
        }
        User user = userDAO.login(username, password);
        if (user == null)
        {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return user;
    }


    public void addUser(User currentUser, User user) throws SQLException
    {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN)
        {
            throw new IllegalArgumentException("Only Admins can add users");
        }
        if (user == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (user.getUsername() == null || user.getUsername().isBlank())
        {
            throw new IllegalArgumentException("Username can't be empty");
        }
        if (user.getPassword() == null || user.getPassword().isBlank())
        {
            throw new IllegalArgumentException("Password can't be empty");
        }
        if (user.getRole() == null)
        {
            throw new IllegalArgumentException("Role can't be empty");
        }
        if (userDAO.getUserByUsername(user.getUsername()) != null)
        {
            throw new IllegalArgumentException("Username already exists");
        }
        userDAO.addUser(user);
    }

    public List<User> getAllUsers(User currentUser) throws SQLException
    {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN)
        {
            throw new IllegalArgumentException("Only Admin can view users");
        }
        return userDAO.getAllUsers();
    }


    public void updateUser(User currentUser, String oldUsername, User user) throws SQLException
    {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN)
        {
            throw new IllegalArgumentException("Only Admin can update users");
        }
        if (oldUsername == null || oldUsername.isBlank())
        {
            throw new IllegalArgumentException("Old username can't be empty");
        }
        if (user == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (user.getUsername() == null || user.getUsername().isBlank())
        {
            throw new IllegalArgumentException("Username can't be empty");
        }
        if (user.getPassword() == null || user.getPassword().isBlank())
        {
            throw new IllegalArgumentException("Password can't be empty");
        }
        if (user.getRole() == null)
        {
            throw new IllegalArgumentException("Role can't be empty");
        }

        User existUser = userDAO.getUserByUsername(oldUsername);
        if (existUser == null)
        {
            throw new IllegalArgumentException("User not found");
        }
        if (!oldUsername.equals(user.getUsername()) && userDAO.getUserByUsername(user.getUsername()) != null)
        {
            throw new IllegalArgumentException("Username already exists");
        }
        userDAO.updateUser(oldUsername, user);
    }

    public void deleteUser(User currentUser, User user) throws SQLException
    {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN)
        {
            throw new IllegalArgumentException("Only Admin can delete users");
        }
        if (user == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }

        User existUser = userDAO.getUserByUsername(user.getUsername());
        if (existUser == null)
        {
            throw new IllegalArgumentException("User not found");
        }
        if (!saleDAO.getSalesByUser(user.getUsername()).isEmpty())
        {
            throw new IllegalArgumentException("Can't delete the user because he has sales history");
        }
        userDAO.deleteUser(user);
    }
}
