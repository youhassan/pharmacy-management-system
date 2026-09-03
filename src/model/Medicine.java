package model;
import java.time.LocalDate;

public class Medicine {
    private int medicineID;
    private String medName;
    private int quantity;
    private LocalDate expiryDate;
    private int minimumStock;
    private int compID;


    public Medicine(int medicineID, String medName, int quantity, LocalDate expiryDate,  int minimumStock, int compID)
    {
        this.medicineID = medicineID;
        this.medName = medName;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.minimumStock = minimumStock;
        this.compID = compID;
    }

    public int getMedicineID() {return this.medicineID;}
    public void setMedicineID(int medicineID)
    {
        this.medicineID = medicineID;
    }

    public String getMedName() {return this.medName;}
    public void setMedName(String medName)
    {
        this.medName = medName;
    }

    public int getQuantity() {return this.quantity;}
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public LocalDate getExpiryDate() {return this.expiryDate;}
    public void setExpiryDate(LocalDate expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    public int getMinimumStock() {return this.minimumStock;}
    public void setMinimumStock(int minimumStock)
    {
        this.minimumStock = minimumStock;
    }

    public int getCompID() {return this.compID;}
    public void setCompID(int compID)
    {
        this.compID = compID;
    }
}
