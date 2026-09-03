package model;

public class PurchaseItem {
    private int purItemID;
    private int purID;
    private int medicineID;
    private int quantityPur;
    private double unitCost;

    public PurchaseItem(int purItemID, int purID, int medicineID, int quantityPur, double unitCost)
    {
        this.purItemID = purItemID;
        this.purID = purID;
        this.medicineID = medicineID;
        this.quantityPur = quantityPur;
        this.unitCost = unitCost;
    }

    public int getPurItemID() {return this.purItemID;}
    public void setPurItemID(int purItemID) {this.purItemID = purItemID;}

    public int getPurID() {return this.purID;}
    public void setPurID(int purID) {this.purID = purID;}

    public int getMedicineID() {return this.medicineID;}
    public void setMedicineID(int medicineID) {this.medicineID = medicineID;}

    public int getQuantityPur() {return this.quantityPur;}
    public void setQuantityPur(int quantityPur) {this.quantityPur = quantityPur;}

    public double getUnitCost() {return this.unitCost;}
    public void setUnitCost(double unitCost) {this.unitCost = unitCost;}
}
