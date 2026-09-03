package model;
import java.time.LocalDate;

public class Purchase {
    private int purID;
    private int compID;
    private LocalDate purDate;

    public Purchase(int purID, int compID, LocalDate purDate)
    {
        this.purID = purID;
        this.compID = compID;
        this.purDate = purDate;
    }

    public int getPurID() {return this.purID;}
    public void setPurID(int purID) {this.purID = purID;}

    public int getCompID() {return this.compID;}
    public void setCompID(int compID) {this.compID = compID;}

    public LocalDate getPurDate() {return this.purDate;}
    public void setPurDate(LocalDate purDate) {this.purDate = purDate;}
}
