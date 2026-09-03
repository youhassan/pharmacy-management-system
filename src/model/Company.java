package model;

public class Company {
    private int compID;
    private String compName;

    public Company(int compID, String compName)
    {
        this.compID = compID;
        this.compName = compName;
    }

    public int getCompID() {return compID;}
    public void setCompID(int compID) {this.compID = compID;}

    public String getCompName() {return compName;}
    public void setCompName(String compName) {this.compName = compName;}
}
