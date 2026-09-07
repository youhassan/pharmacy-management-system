package service;

import dao.*;
import model.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PurchaseService {
    private PurchaseDAO purchaseDAO;
    private PurchaseItemDAO purchaseItemDAO;
    private MedicineDAO medicineDAO;
    private CompanyDAO companyDAO;

    public PurchaseService()
    {
        purchaseDAO = new PurchaseDAO();
        purchaseItemDAO = new PurchaseItemDAO();
        medicineDAO = new MedicineDAO();
        companyDAO = new CompanyDAO();
    }

    public void addPurchase(User currentUser, Purchase purchase, List<PurchaseItem> purchaseItems) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (purchase == null)
        {
            throw new IllegalArgumentException("Purchase can't be empty");
        }
        if (purchaseItems == null || purchaseItems.isEmpty())
        {
            throw new IllegalArgumentException("Purchase must contain at least one item");
        }

        Company company = companyDAO.getCompanyByID(purchase.getCompID());
        if (company == null)
        {
            throw new IllegalArgumentException("Company not found");
        }

        for (PurchaseItem item : purchaseItems)
        {
            if (item == null)
            {
                throw new IllegalArgumentException("Purchase item can't be empty");
            }

            Medicine medicine = medicineDAO.getMedicineByID(item.getMedicineID());
            if (medicine == null)
            {
                throw new IllegalArgumentException("Medicine not found");
            }

            if (item.getQuantityPur() <= 0)
            {
                throw new IllegalArgumentException("Purchase quantity should be greater than 0");
            }

            if (item.getUnitCost() <= 0)
            {
                throw new IllegalArgumentException("Unit cost should be greater than 0");
            }
        }

        purchaseDAO.addPurchase(purchase);

        for (PurchaseItem item : purchaseItems)
        {
            item.setPurID(purchase.getPurID());

            purchaseItemDAO.addPurchaseItem(item);

            Medicine medicine = medicineDAO.getMedicineByID(item.getMedicineID());
            medicine.setQuantity(medicine.getQuantity() + item.getQuantityPur());

            medicineDAO.updateMedicine(medicine.getMedName(), medicine);
        }
    }

    public Purchase getPurchaseByID(User currentUser, Purchase purchase) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (purchase == null)
        {
            throw new IllegalArgumentException("Purchase can't be empty");
        }
        if (purchase.getPurID() == 0)
        {
            throw new IllegalArgumentException("Purchase ID can't be empty");
        }

        return purchaseDAO.getPurchaseByID(purchase.getPurID());
    }

    public List<Purchase> getAllPurchases(User currentUser) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }

        return purchaseDAO.getAllPurchases();
    }

    public List<Purchase> getPurchasesByCompany(User currentUser, Company company) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (company == null)
        {
            throw new IllegalArgumentException("Company can't be empty");
        }
        if (company.getCompID() == 0)
        {
            throw new IllegalArgumentException("Company ID can't be empty");
        }

        return purchaseDAO.getPurchasesByCompany(company.getCompID());
    }

    public List<Purchase> getPurchasesByDate(User currentUser, LocalDate date) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (date == null)
        {
            throw new IllegalArgumentException("Date can't be empty");
        }

        return purchaseDAO.getPurchasesByDate(date);
    }

    public void updatePurchase(User currentUser, Purchase purchase) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (purchase == null)
        {
            throw new IllegalArgumentException("Purchase can't be empty");
        }
        if (purchase.getPurID() == 0)
        {
            throw new IllegalArgumentException("Purchase ID can't be empty");
        }

        Purchase existPurchase = purchaseDAO.getPurchaseByID(purchase.getPurID());
        if (existPurchase == null)
        {
            throw new IllegalArgumentException("Purchase not found");
        }

        Company company = companyDAO.getCompanyByID(purchase.getCompID());
        if (company == null)
        {
            throw new IllegalArgumentException("Company not found");
        }

        purchaseDAO.updatePurchase(purchase);
    }

    public void deletePurchase(User currentUser, Purchase purchase) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (purchase == null)
        {
            throw new IllegalArgumentException("Purchase can't be empty");
        }
        if (purchase.getPurID() == 0)
        {
            throw new IllegalArgumentException("Purchase ID can't be empty");
        }

        Purchase existPurchase = purchaseDAO.getPurchaseByID(purchase.getPurID());
        if (existPurchase == null)
        {
            throw new IllegalArgumentException("Purchase not found");
        }

        if (!purchaseItemDAO.getPurchaseItemsByPurchase(existPurchase.getPurID()).isEmpty())
        {
            throw new IllegalArgumentException("Can't delete this purchase because it has purchase items");
        }

        purchaseDAO.deletePurchase(existPurchase);
    }
}