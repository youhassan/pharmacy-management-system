package com.pharmacy.service;

import com.pharmacy.model.*;
import com.pharmacy.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class SaleService {
    private SaleRepository saleRepository;
    private SaleItemRepository saleItemRepository;
    private MedicineRepository medicineRepository;
    private UserRepository userRepository;
    private PurchaseRepository purchaseRepository;
    private PurchaseItemRepository purchaseItemRepository;
    private CompanyRepository companyRepository;

    public SaleService(SaleRepository saleRepository,
                       SaleItemRepository saleItemRepository,
                       MedicineRepository medicineRepository,
                       UserRepository userRepository,
                       PurchaseRepository purchaseRepository,
                       PurchaseItemRepository purchaseItemRepository,
                       CompanyRepository companyRepository)
    {
        this.saleRepository = saleRepository;
        this.saleItemRepository = saleItemRepository;
        this.medicineRepository = medicineRepository;
        this.userRepository = userRepository;
        this.purchaseRepository = purchaseRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.companyRepository = companyRepository;
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

    public void addSale(User currentUser, LocalDate saleDate, List<SaleItemEntry> items)
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

        User user = userRepository.findById(currentUser.getUsername()).orElse(null);
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

            Medicine medicine = medicineRepository.findByMedName(itemEntry.getMedicineName());
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
        saleRepository.save(sale);

        for (SaleItem item : saleItems)
        {
            item.setSaleID(sale.getSaleID());
            saleItemRepository.save(item);
            Medicine medicine = medicineRepository.findById(item.getMedicineID()).orElse(null);
            medicine.setQuantity(medicine.getQuantity() - item.getQuantitySold());
            medicineRepository.save(medicine);
        }
    }

    public void addSale(User currentUser, Sale sale, List<SaleItem> saleItems)
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

        User user = userRepository.findById(sale.getUsername()).orElse(null);
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

            Medicine medicine = medicineRepository.findById(item.getMedicineID()).orElse(null);
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

        saleRepository.save(sale);

        for (SaleItem item : saleItems)
        {
            item.setSaleID(sale.getSaleID());

            saleItemRepository.save(item);

            Medicine medicine = medicineRepository.findById(item.getMedicineID()).orElse(null);

            medicine.setQuantity(
                    medicine.getQuantity() - item.getQuantitySold()
            );

            medicineRepository.save(medicine);
        }
    }


    public Sale getSaleByID(User currentUser, Sale sale)
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

        return saleRepository.findById(sale.getSaleID()).orElse(null);
    }


    public List<Sale> getAllSales(User currentUser)
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }

        return saleRepository.findAll();
    }


    public List<Sale> getSalesByUser(User currentUser, User user)
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

        return saleRepository.findByUsername(user.getUsername());
    }


    public List<Sale> getSalesByDate(User currentUser, LocalDate date)
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (date == null)
        {
            throw new IllegalArgumentException("Date can't be empty");
        }

        return saleRepository.findBySaleDate(date);
    }

    public List<Sale> getSalesBetweenDates(User currentUser, LocalDate fromDate, LocalDate toDate)
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

        return saleRepository.findBySaleDateBetweenOrderBySaleDateAsc(fromDate, toDate);
    }

    public FinancialReport getFinancialReport(User currentUser, LocalDate fromDate, LocalDate toDate)
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

        List<Sale> sales = saleRepository.findBySaleDateBetweenOrderBySaleDateAsc(fromDate, toDate);
        double totalSalesRevenue = 0.0;
        List<FinancialReport.SoldItemDetail> soldItems = new java.util.ArrayList<>();
        for (Sale s : sales)
        {
            List<SaleItem> items = saleItemRepository.findBySaleID(s.getSaleID());
            for (SaleItem item : items)
            {
                totalSalesRevenue += (item.getQuantitySold() * item.getUnitPrice());
                Medicine med = medicineRepository.findById(item.getMedicineID()).orElse(null);
                String medName = med != null ? med.getMedName() : "ID: " + item.getMedicineID();
                soldItems.add(new FinancialReport.SoldItemDetail(s.getSaleID(), s.getSaleDate(), s.getUsername(), medName, item.getQuantitySold(), item.getUnitPrice()));
            }
        }

        List<Purchase> purchases = purchaseRepository.findByPurDateBetween(fromDate, toDate);
        double totalPurchaseCost = 0.0;
        List<FinancialReport.PurchasedItemDetail> purchasedItems = new java.util.ArrayList<>();
        for (Purchase p : purchases)
        {
            Company comp = companyRepository.findById(p.getCompID()).orElse(null);
            String compName = comp != null ? comp.getCompName() : "ID: " + p.getCompID();
            List<PurchaseItem> items = purchaseItemRepository.findByPurID(p.getPurID());
            for (PurchaseItem item : items)
            {
                totalPurchaseCost += (item.getQuantityPur() * item.getUnitCost());
                Medicine med = medicineRepository.findById(item.getMedicineID()).orElse(null);
                String medName = med != null ? med.getMedName() : "ID: " + item.getMedicineID();
                purchasedItems.add(new FinancialReport.PurchasedItemDetail(p.getPurID(), p.getPurDate(), compName, medName, item.getQuantityPur(), item.getUnitCost()));
            }
        }

        return new FinancialReport(fromDate, toDate, sales.size(), totalSalesRevenue, purchases.size(), totalPurchaseCost, soldItems, purchasedItems);
    }

    public void updateSale(User currentUser, Sale sale)
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

        Sale existSale = saleRepository.findById(sale.getSaleID()).orElse(null);
        if (existSale == null)
        {
            throw new IllegalArgumentException("Sale not found");
        }

        User user = userRepository.findById(sale.getUsername()).orElse(null);
        if (user == null)
        {
            throw new IllegalArgumentException("User not found");
        }

        saleRepository.save(sale);
    }


    public void deleteSale(User currentUser, Sale sale)
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

        Sale existSale = saleRepository.findById(sale.getSaleID()).orElse(null);
        if (existSale == null)
        {
            throw new IllegalArgumentException("Sale not found");
        }

        if (!saleItemRepository.findBySaleID(existSale.getSaleID()).isEmpty())
        {
            throw new IllegalArgumentException("Can't delete this sale because it has sale items");
        }

        saleRepository.delete(existSale);
    }
}