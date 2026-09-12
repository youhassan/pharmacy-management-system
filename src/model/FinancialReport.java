package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FinancialReport {
    public static class SoldItemDetail {
        private int saleID;
        private LocalDate saleDate;
        private String username;
        private String medicineName;
        private int quantitySold;
        private double unitPrice;
        private double subtotal;

        public SoldItemDetail(int saleID, LocalDate saleDate, String username, String medicineName, int quantitySold, double unitPrice) {
            this.saleID = saleID;
            this.saleDate = saleDate;
            this.username = username;
            this.medicineName = medicineName;
            this.quantitySold = quantitySold;
            this.unitPrice = unitPrice;
            this.subtotal = quantitySold * unitPrice;
        }

        public int getSaleID() {
            return saleID;
        }

        public LocalDate getSaleDate() {
            return saleDate;
        }

        public String getUsername() {
            return username;
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

        public double getSubtotal() {
            return subtotal;
        }
    }

    public static class PurchasedItemDetail {
        private int purID;
        private LocalDate purDate;
        private String companyName;
        private String medicineName;
        private int quantityPur;
        private double unitCost;
        private double subtotal;

        public PurchasedItemDetail(int purID, LocalDate purDate, String companyName, String medicineName, int quantityPur, double unitCost) {
            this.purID = purID;
            this.purDate = purDate;
            this.companyName = companyName;
            this.medicineName = medicineName;
            this.quantityPur = quantityPur;
            this.unitCost = unitCost;
            this.subtotal = quantityPur * unitCost;
        }

        public int getPurID() {
            return purID;
        }

        public LocalDate getPurDate() {
            return purDate;
        }

        public String getCompanyName() {
            return companyName;
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

        public double getSubtotal() {
            return subtotal;
        }
    }

    private LocalDate fromDate;
    private LocalDate toDate;
    private int totalSalesCount;
    private double totalSalesRevenue;
    private int totalPurchasesCount;
    private double totalPurchaseCost;
    private double netProfit;
    private List<SoldItemDetail> soldItems;
    private List<PurchasedItemDetail> purchasedItems;

    public FinancialReport(LocalDate fromDate, LocalDate toDate, int totalSalesCount, double totalSalesRevenue, int totalPurchasesCount, double totalPurchaseCost, List<SoldItemDetail> soldItems, List<PurchasedItemDetail> purchasedItems)
    {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.totalSalesCount = totalSalesCount;
        this.totalSalesRevenue = totalSalesRevenue;
        this.totalPurchasesCount = totalPurchasesCount;
        this.totalPurchaseCost = totalPurchaseCost;
        this.netProfit = totalSalesRevenue - totalPurchaseCost;
        this.soldItems = soldItems != null ? soldItems : new ArrayList<>();
        this.purchasedItems = purchasedItems != null ? purchasedItems : new ArrayList<>();
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public int getTotalSalesCount() {
        return totalSalesCount;
    }

    public void setTotalSalesCount(int totalSalesCount) {
        this.totalSalesCount = totalSalesCount;
    }

    public double getTotalSalesRevenue() {
        return totalSalesRevenue;
    }

    public void setTotalSalesRevenue(double totalSalesRevenue) {
        this.totalSalesRevenue = totalSalesRevenue;
    }

    public int getTotalPurchasesCount() {
        return totalPurchasesCount;
    }

    public void setTotalPurchasesCount(int totalPurchasesCount) {
        this.totalPurchasesCount = totalPurchasesCount;
    }

    public double getTotalPurchaseCost() {
        return totalPurchaseCost;
    }

    public void setTotalPurchaseCost(double totalPurchaseCost) {
        this.totalPurchaseCost = totalPurchaseCost;
    }

    public double getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(double netProfit) {
        this.netProfit = netProfit;
    }

    public List<SoldItemDetail> getSoldItems() {
        return soldItems;
    }

    public void setSoldItems(List<SoldItemDetail> soldItems) {
        this.soldItems = soldItems;
    }

    public List<PurchasedItemDetail> getPurchasedItems() {
        return purchasedItems;
    }

    public void setPurchasedItems(List<PurchasedItemDetail> purchasedItems) {
        this.purchasedItems = purchasedItems;
    }
}
