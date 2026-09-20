package com.pharmacy.service;

import com.pharmacy.model.Role;
import com.pharmacy.model.User;
import com.pharmacy.repository.SaleRepository;
import com.pharmacy.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    private SaleRepository saleRepository;


    public UserService(UserRepository userRepository, SaleRepository saleRepository)
    {
        this.userRepository = userRepository;
        this.saleRepository = saleRepository;
    }

    public User login(String username, String password)
    {
        if (username == null || username.isBlank())
        {
            throw new IllegalArgumentException("Username can't be empty");
        }
        if (password == null || password.isBlank())
        {
            throw new IllegalArgumentException("Password can't be empty");
        }
        User user = userRepository.findByUsernameAndPassword(username, password);
        if (user == null)
        {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return user;
    }


    public void addUser(User currentUser, User user)
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
        if (userRepository.findById(user.getUsername()).orElse(null) != null)
        {
            throw new IllegalArgumentException("Username already exists");
        }
        userRepository.save(user);
    }

    public List<User> getAllUsers(User currentUser)
    {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN)
        {
            throw new IllegalArgumentException("Only Admin can view users");
        }
        return userRepository.findAll();
    }


    public void updateUser(User currentUser, String oldUsername, User user)
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

        User existUser = userRepository.findById(oldUsername).orElse(null);
        if (existUser == null)
        {
            throw new IllegalArgumentException("User not found");
        }
        if (!oldUsername.equals(user.getUsername()) && userRepository.findById(user.getUsername()).orElse(null) != null)
        {
            throw new IllegalArgumentException("Username already exists");
        }
        if (!oldUsername.equals(user.getUsername()))
        {
            userRepository.delete(existUser);
        }
        userRepository.save(user);
    }

    public void deleteUser(User currentUser, User user)
    {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN)
        {
            throw new IllegalArgumentException("Only Admin can delete users");
        }
        if (user == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (user.getUsername() == null || user.getUsername().isBlank())
        {
            throw new IllegalArgumentException("Username can't be empty");
        }
        if (currentUser.getUsername().equalsIgnoreCase(user.getUsername()))
        {
            throw new IllegalArgumentException("Admin cannot delete their own account");
        }

        User existUser = userRepository.findById(user.getUsername()).orElse(null);
        if (existUser == null)
        {
            throw new IllegalArgumentException("User not found");
        }
        if (!saleRepository.findByUsername(user.getUsername()).isEmpty())
        {
            throw new IllegalArgumentException("Can't delete the user because he has sales history");
        }
        userRepository.delete(existUser);
    }
}
