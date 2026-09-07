package service;

import dao.*;
import model.*;
import java.time.LocalDate;
import java.sql.SQLException;
import java.util.List;

public class SaleService {
    private SaleDAO saleDAO;
    private SaleItemDAO saleItemDAO;
    private MedicineDAO medicineDAO;
    private UserDAO userDAO;

    public SaleService()
    {
        saleDAO = new SaleDAO();
        saleItemDAO = new SaleItemDAO();
        medicineDAO = new MedicineDAO();
        userDAO = new UserDAO();
    }

    public void addSale(User currentUser, Sale sale, List<SaleItem> saleItems) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (sale == null)
        {
            throw new IllegalArgumentException("Sale can't be empty");
        }
        if (saleItems == null || saleItems.isEmpty())
        {
            throw new IllegalArgumentException("Sale must contain at least one item");
        }

        User user = userDAO.getUserByUsername(sale.getUsername());
        if (user == null)
        {
            throw new IllegalArgumentException("User not found");
        }

        if (!currentUser.getUsername().equals(sale.getUsername()))
        {
            throw new IllegalArgumentException("Sale must belong to the current user");
        }

        for (SaleItem item : saleItems)
        {
            if (item == null)
            {
                throw new IllegalArgumentException("Sale item can't be empty");
            }

            Medicine medicine = medicineDAO.getMedicineByID(item.getMedicineID());
            if (medicine == null)
            {
                throw new IllegalArgumentException("Medicine not found");
            }

            if (item.getQuantitySold() <= 0)
            {
                throw new IllegalArgumentException("Sale quantity should be greater than 0");
            }

            if (item.getUnitPrice() <= 0)
            {
                throw new IllegalArgumentException("Unit price should be greater than 0");
            }

            if (medicine.getExpiryDate().isBefore(LocalDate.now()))
            {
                throw new IllegalArgumentException("Medicine Expired");
            }

            if (item.getQuantitySold() > medicine.getQuantity())
            {
                throw new IllegalArgumentException(
                        "Only " + medicine.getQuantity() + " Available"
                );
            }
        }

        saleDAO.addSale(sale);

        for (SaleItem item : saleItems)
        {
            item.setSaleID(sale.getSaleID());

            saleItemDAO.addSaleItem(item);

            Medicine medicine = medicineDAO.getMedicineByID(item.getMedicineID());

            medicine.setQuantity(
                    medicine.getQuantity() - item.getQuantitySold()
            );

            medicineDAO.updateMedicine(medicine.getMedName(), medicine);
        }
    }


    public Sale getSaleByID(User currentUser, Sale sale) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (sale == null)
        {
            throw new IllegalArgumentException("Sale can't be empty");
        }
        if (sale.getSaleID() == 0)
        {
            throw new IllegalArgumentException("Sale ID can't be empty");
        }

        return saleDAO.getSaleByID(sale.getSaleID());
    }


    public List<Sale> getAllSales(User currentUser) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }

        return saleDAO.getAllSales();
    }


    public List<Sale> getSalesByUser(User currentUser, User user) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (user == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (user.getUsername() == null || user.getUsername().isBlank())
        {
            throw new IllegalArgumentException("Username can't be empty");
        }

        return saleDAO.getSalesByUser(user.getUsername());
    }


    public List<Sale> getSalesByDate(User currentUser, LocalDate date) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (date == null)
        {
            throw new IllegalArgumentException("Date can't be empty");
        }

        return saleDAO.getSalesByDate(date);
    }


    public void updateSale(User currentUser, Sale sale) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (sale == null)
        {
            throw new IllegalArgumentException("Sale can't be empty");
        }
        if (sale.getSaleID() == 0)
        {
            throw new IllegalArgumentException("Sale ID can't be empty");
        }

        Sale existSale = saleDAO.getSaleByID(sale.getSaleID());
        if (existSale == null)
        {
            throw new IllegalArgumentException("Sale not found");
        }

        User user = userDAO.getUserByUsername(sale.getUsername());
        if (user == null)
        {
            throw new IllegalArgumentException("User not found");
        }

        saleDAO.updateSale(sale);
    }


    public void deleteSale(User currentUser, Sale sale) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (sale == null)
        {
            throw new IllegalArgumentException("Sale can't be empty");
        }
        if (sale.getSaleID() == 0)
        {
            throw new IllegalArgumentException("Sale ID can't be empty");
        }

        Sale existSale = saleDAO.getSaleByID(sale.getSaleID());
        if (existSale == null)
        {
            throw new IllegalArgumentException("Sale not found");
        }

        if (!saleItemDAO.getSaleItemsBySale(existSale.getSaleID()).isEmpty())
        {
            throw new IllegalArgumentException("Can't delete this sale because it has sale items");
        }

        saleDAO.deleteSale(existSale);
    }
}