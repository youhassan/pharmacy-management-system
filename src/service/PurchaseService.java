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

    public static class PurchaseItemEntry {
        private String medicineName;
        private int quantityPur;
        private double unitCost;
        private boolean isNewMedicine;
        private LocalDate expiryDate;
        private int minimumStock;

        public PurchaseItemEntry(String medicineName, int quantityPur, double unitCost) {
            this.medicineName = medicineName;
            this.quantityPur = quantityPur;
            this.unitCost = unitCost;
            this.isNewMedicine = false;
        }

        public PurchaseItemEntry(String medicineName, int quantityPur, double unitCost, LocalDate expiryDate, int minimumStock) {
            this.medicineName = medicineName;
            this.quantityPur = quantityPur;
            this.unitCost = unitCost;
            this.isNewMedicine = true;
            this.expiryDate = expiryDate;
            this.minimumStock = minimumStock;
        }

        public String getMedicineName() {
            return medicineName;
        }

        public int getQuantityPur() {
            return quantityPur;
        }

        public double getUnitCost() {
            return unitCost;
        }

        public boolean isNewMedicine() {
            return isNewMedicine;
        }

        public LocalDate getExpiryDate() {
            return expiryDate;
        }

        public int getMinimumStock() {
            return minimumStock;
        }
    }

    public void addPurchase(User currentUser, String companyName, LocalDate purDate, List<PurchaseItemEntry> items) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (companyName == null || companyName.isBlank())
        {
            throw new IllegalArgumentException("Company name can't be empty");
        }
        if (purDate == null)
        {
            throw new IllegalArgumentException("Purchase date can't be empty");
        }
        if (items == null || items.isEmpty())
        {
            throw new IllegalArgumentException("Purchase must contain at least one item");
        }

        Company company = companyDAO.getCompanyByName(companyName);
        if (company == null)
        {
            throw new IllegalArgumentException("Company not found");
        }

        for (PurchaseItemEntry itemEntry : items)
        {
            if (itemEntry == null)
            {
                throw new IllegalArgumentException("Purchase item can't be empty");
            }
            if (itemEntry.getMedicineName() == null || itemEntry.getMedicineName().isBlank())
            {
                throw new IllegalArgumentException("Medicine name can't be empty");
            }
            if (itemEntry.getQuantityPur() <= 0)
            {
                throw new IllegalArgumentException("Purchase quantity should be greater than 0");
            }
            if (itemEntry.getUnitCost() <= 0)
            {
                throw new IllegalArgumentException("Unit cost should be greater than 0");
            }

            Medicine medicine = medicineDAO.getMedicineByName(itemEntry.getMedicineName());
            if (medicine == null)
            {
                if (itemEntry.getExpiryDate() == null)
                {
                    throw new IllegalArgumentException("Expiry date is required for new medicine '" + itemEntry.getMedicineName() + "'");
                }
                if (itemEntry.getExpiryDate().isBefore(LocalDate.now()) || itemEntry.getExpiryDate().isEqual(LocalDate.now()))
                {
                    throw new IllegalArgumentException("Medicine Expiration date should be after today");
                }
                if (itemEntry.getMinimumStock() < 0)
                {
                    throw new IllegalArgumentException("Medicine minimum stock should be greater than or equal to 0");
                }

                Medicine newMed = new Medicine(0, itemEntry.getMedicineName(), 0, itemEntry.getExpiryDate(), itemEntry.getMinimumStock(), company.getCompID());
                medicineDAO.addMedicine(newMed);
            }
        }

        Purchase purchase = new Purchase(0, company.getCompID(), purDate);
        purchaseDAO.addPurchase(purchase);

        for (PurchaseItemEntry itemEntry : items)
        {
            Medicine medicine = medicineDAO.getMedicineByName(itemEntry.getMedicineName());
            PurchaseItem pi = new PurchaseItem(0, purchase.getPurID(), medicine.getMedicineID(), itemEntry.getQuantityPur(), itemEntry.getUnitCost());
            purchaseItemDAO.addPurchaseItem(pi);

            medicine.setQuantity(medicine.getQuantity() + itemEntry.getQuantityPur());
            medicineDAO.updateMedicine(medicine.getMedName(), medicine);
        }
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

    public List<Purchase> getPurchasesByCompanyName(User currentUser, String companyName) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (companyName == null || companyName.isBlank())
        {
            throw new IllegalArgumentException("Company name can't be empty");
        }

        Company company = companyDAO.getCompanyByName(companyName);
        if (company == null)
        {
            throw new IllegalArgumentException("Company not found");
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

    public List<Purchase> getPurchasesBetweenDates(User currentUser, LocalDate fromDate, LocalDate toDate) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (fromDate == null || toDate == null)
        {
            throw new IllegalArgumentException("Date cannot be empty");
        }
        if (fromDate.isAfter(toDate))
        {
            throw new IllegalArgumentException("From date cannot be after To date");
        }
        return purchaseDAO.getPurchasesBetweenDates(fromDate, toDate);
    }

    public void updatePurchase(User currentUser, int purID, String companyName, LocalDate updateDate) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (purID <= 0)
        {
            throw new IllegalArgumentException("Invalid purchase ID");
        }
        if (companyName == null || companyName.isBlank())
        {
            throw new IllegalArgumentException("Company name can't be empty");
        }
        if (updateDate == null)
        {
            throw new IllegalArgumentException("Purchase date can't be empty");
        }

        Purchase existPurchase = purchaseDAO.getPurchaseByID(purID);
        if (existPurchase == null)
        {
            throw new IllegalArgumentException("Purchase not found");
        }

        Company company = companyDAO.getCompanyByName(companyName);
        if (company == null)
        {
            throw new IllegalArgumentException("Company not found");
        }

        Purchase updatedPurchase = new Purchase(purID, company.getCompID(), updateDate);
        purchaseDAO.updatePurchase(updatedPurchase);
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