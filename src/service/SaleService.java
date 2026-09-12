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
    private PurchaseDAO purchaseDAO;
    private PurchaseItemDAO purchaseItemDAO;
    private CompanyDAO companyDAO;

    public SaleService()
    {
        saleDAO = new SaleDAO();
        saleItemDAO = new SaleItemDAO();
        medicineDAO = new MedicineDAO();
        userDAO = new UserDAO();
        purchaseDAO = new PurchaseDAO();
        purchaseItemDAO = new PurchaseItemDAO();
        companyDAO = new CompanyDAO();
    }

    public static class SaleItemEntry {
        private String medicineName;
        private int quantitySold;
        private double unitPrice;

        public SaleItemEntry(String medicineName, int quantitySold, double unitPrice) {
            this.medicineName = medicineName;
            this.quantitySold = quantitySold;
            this.unitPrice = unitPrice;
        }

        public String getMedicineName() {
            return medicineName;
        }

        public int getQuantitySold() {
            return quantitySold;
        }

        public double getUnitPrice() {
            return unitPrice;
        }
    }

    public void addSale(User currentUser, LocalDate saleDate, List<SaleItemEntry> items) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (saleDate == null)
        {
            throw new IllegalArgumentException("Sale date can't be empty");
        }
        if (items == null || items.isEmpty())
        {
            throw new IllegalArgumentException("Sale must contain at least one item");
        }

        User user = userDAO.getUserByUsername(currentUser.getUsername());
        if (user == null)
        {
            throw new IllegalArgumentException("User not found");
        }

        List<SaleItem> saleItems = new java.util.ArrayList<>();
        for (SaleItemEntry itemEntry : items)
        {
            if (itemEntry == null)
            {
                throw new IllegalArgumentException("Sale item can't be empty");
            }
            if (itemEntry.getMedicineName() == null || itemEntry.getMedicineName().isBlank())
            {
                throw new IllegalArgumentException("Medicine name can't be empty");
            }

            Medicine medicine = medicineDAO.getMedicineByName(itemEntry.getMedicineName());
            if (medicine == null)
            {
                throw new IllegalArgumentException("Medicine '" + itemEntry.getMedicineName() + "' not found");
            }

            if (itemEntry.getQuantitySold() <= 0)
            {
                throw new IllegalArgumentException("Sale quantity should be greater than 0");
            }

            if (itemEntry.getUnitPrice() <= 0)
            {
                throw new IllegalArgumentException("Unit price should be greater than 0");
            }

            if (medicine.getExpiryDate().isBefore(LocalDate.now()))
            {
                throw new IllegalArgumentException("Medicine '" + medicine.getMedName() + "' is expired");
            }

            if (itemEntry.getQuantitySold() > medicine.getQuantity())
            {
                throw new IllegalArgumentException(
                        "Only " + medicine.getQuantity() + " available for medicine '" + medicine.getMedName() + "'"
                );
            }

            SaleItem si = new SaleItem(0, 0, medicine.getMedicineID(), itemEntry.getQuantitySold(), itemEntry.getUnitPrice());
            saleItems.add(si);
        }

        Sale sale = new Sale(0, saleDate, currentUser.getUsername());
        saleDAO.addSale(sale);

        for (SaleItem item : saleItems)
        {
            item.setSaleID(sale.getSaleID());
            saleItemDAO.addSaleItem(item);

            Medicine medicine = medicineDAO.getMedicineByID(item.getMedicineID());
            medicine.setQuantity(medicine.getQuantity() - item.getQuantitySold());
            medicineDAO.updateMedicine(medicine.getMedName(), medicine);
        }
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

    public List<Sale> getSalesBetweenDates(User currentUser, LocalDate fromDate, LocalDate toDate) throws SQLException
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

        return saleDAO.getSalesBetweenDates(fromDate, toDate);
    }

    public FinancialReport getFinancialReport(User currentUser, LocalDate fromDate, LocalDate toDate) throws SQLException
    {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN)
        {
            throw new IllegalArgumentException("Only Admins can access financial reports");
        }
        if (fromDate == null || toDate == null)
        {
            throw new IllegalArgumentException("Date cannot be empty");
        }
        if (fromDate.isAfter(toDate))
        {
            throw new IllegalArgumentException("From date cannot be after To date");
        }

        List<Sale> sales = saleDAO.getSalesBetweenDates(fromDate, toDate);
        double totalSalesRevenue = 0.0;
        List<FinancialReport.SoldItemDetail> soldItems = new java.util.ArrayList<>();
        for (Sale s : sales)
        {
            List<SaleItem> items = saleItemDAO.getSaleItemsBySale(s.getSaleID());
            for (SaleItem item : items)
            {
                totalSalesRevenue += (item.getQuantitySold() * item.getUnitPrice());
                Medicine med = medicineDAO.getMedicineByID(item.getMedicineID());
                String medName = med != null ? med.getMedName() : "ID: " + item.getMedicineID();
                soldItems.add(new FinancialReport.SoldItemDetail(
                        s.getSaleID(),
                        s.getSaleDate(),
                        s.getUsername(),
                        medName,
                        item.getQuantitySold(),
                        item.getUnitPrice()
                ));
            }
        }

        List<Purchase> purchases = purchaseDAO.getPurchasesBetweenDates(fromDate, toDate);
        double totalPurchaseCost = 0.0;
        List<FinancialReport.PurchasedItemDetail> purchasedItems = new java.util.ArrayList<>();
        for (Purchase p : purchases)
        {
            Company comp = companyDAO.getCompanyByID(p.getCompID());
            String compName = comp != null ? comp.getCompName() : "ID: " + p.getCompID();
            List<PurchaseItem> items = purchaseItemDAO.getPurchaseItemsByPurchase(p.getPurID());
            for (PurchaseItem item : items)
            {
                totalPurchaseCost += (item.getQuantityPur() * item.getUnitCost());
                Medicine med = medicineDAO.getMedicineByID(item.getMedicineID());
                String medName = med != null ? med.getMedName() : "ID: " + item.getMedicineID();
                purchasedItems.add(new FinancialReport.PurchasedItemDetail(
                        p.getPurID(),
                        p.getPurDate(),
                        compName,
                        medName,
                        item.getQuantityPur(),
                        item.getUnitCost()
                ));
            }
        }

        return new FinancialReport(
                fromDate,
                toDate,
                sales.size(),
                totalSalesRevenue,
                purchases.size(),
                totalPurchaseCost,
                soldItems,
                purchasedItems
        );
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