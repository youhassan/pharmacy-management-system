package model;
import java.time.LocalDate;

public class Sale {
    private int saleID;
    private LocalDate saleDate;
    private String username;

    public Sale(int saleID, LocalDate saleDate, String username)
    {
        this.saleID = saleID;
        this.saleDate = saleDate;
        this.username = username;
    }

    public int getSaleID() {return this.saleID;}
    public void setSaleID(int saleID)
    {
        this.saleID = saleID;
    }

    public LocalDate getSaleDate() {return this.saleDate;}
    public void setSaleDate(LocalDate saleDate)
    {
        this.saleDate = saleDate;
    }

    public String getUsername() {return this.username;}
    public void setUsername(String username)
    {
        this.username = username;
    }

}
