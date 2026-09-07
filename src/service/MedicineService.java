package service;

import dao.*;
import model.*;
import java.time.LocalDate;
import java.sql.SQLException;
import java.util.List;

public class MedicineService {
    private MedicineDAO medicineDAO;
    private PurchaseItemDAO purchaseItemDAO;
    private SaleItemDAO saleItemDAO;
    private CompanyDAO companyDAO;

    public MedicineService()
    {
        purchaseItemDAO = new PurchaseItemDAO();
        medicineDAO = new MedicineDAO();
        saleItemDAO = new SaleItemDAO();
        companyDAO = new CompanyDAO();
    }


    public void addMedicine(User currentUser, Medicine medicine) throws SQLException
    {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN)
        {
            throw new IllegalArgumentException("Only Admins can add medicines");
        }
        if (medicine == null)
        {
            throw new IllegalArgumentException("Medicine can't be empty");
        }
        if (medicine.getMedName() == null || medicine.getMedName().isBlank())
        {
            throw new IllegalArgumentException("Medicine name can't be empty");
        }
        if (medicineDAO.getMedicineByName(medicine.getMedName()) != null)
        {
            throw new IllegalArgumentException("Medicine already exists");
        }
        if (medicine.getQuantity() < 0)
        {
            throw new IllegalArgumentException("Medicine quantity should be greater than or equal to 0");
        }
        if (medicine.getMinimumStock() < 0)
        {
            throw new IllegalArgumentException("Medicine minimum stock should be greater than or equal to 0");
        }
        if (medicine.getExpiryDate().isBefore(LocalDate.now()) || medicine.getExpiryDate().isEqual(LocalDate.now()))
        {
            throw new IllegalArgumentException("Medicine Expiration date should be after today");
        }

        Company company = companyDAO.getCompanyByID(medicine.getCompID());
        if (company == null)
        {
            throw new IllegalArgumentException("Company not found");
        }

        medicineDAO.addMedicine(medicine);
    }


    public Medicine getMedicineByName(Medicine medicine) throws SQLException
    {
        if (medicine == null)
        {
            throw new IllegalArgumentException("Medicine can't be empty");
        }
        if (medicine.getMedName() == null || medicine.getMedName().isBlank())
        {
            throw new IllegalArgumentException("Medicine name can't be empty");
        }
        return medicineDAO.getMedicineByName(medicine.getMedName());
    }


    public Medicine getMedicineByID(Medicine medicine) throws SQLException
    {
        if (medicine == null)
        {
            throw new IllegalArgumentException("Medicine can't be empty");
        }
        if (medicine.getMedicineID() == 0)
        {
            throw new IllegalArgumentException("Medicine ID can't be empty");
        }
        return medicineDAO.getMedicineByID(medicine.getMedicineID());
    }


    public List<Medicine> getAllMedicines() throws SQLException
    {
        return medicineDAO.getAllMedicines();
    }


    public List<Medicine> getMedicinesByCompany(Company company) throws SQLException
    {
        if (company == null)
        {
            throw new IllegalArgumentException("Company can't be empty");
        }
        if (company.getCompID() == 0)
        {
            throw new IllegalArgumentException("Company ID can't be empty");
        }
        return medicineDAO.getMedicineByCompany(company.getCompID());
    }


    public void updateMedicine(User currentUser, String oldMedicineName, Medicine medicine) throws SQLException
    {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN)
        {
            throw new IllegalArgumentException("Only Admin can update medicines");
        }
        if (oldMedicineName == null || oldMedicineName.isBlank())
        {
            throw new IllegalArgumentException("Old medicine name can't be empty");
        }
        if (medicine == null)
        {
            throw new IllegalArgumentException("Medicine can't be empty");
        }
        if (medicine.getMedName() == null || medicine.getMedName().isBlank())
        {
            throw new IllegalArgumentException("Medicine name can't be empty");
        }

        Medicine existMedicine = medicineDAO.getMedicineByName(oldMedicineName);
        if (existMedicine == null)
        {
            throw new IllegalArgumentException("Medicine not found");
        }
        if (!oldMedicineName.equals(medicine.getMedName()) && medicineDAO.getMedicineByName(medicine.getMedName()) != null)
        {
            throw new IllegalArgumentException("Medicine already exists");
        }
        if (medicine.getQuantity() < 0)
        {
            throw new IllegalArgumentException("Medicine quantity should be greater than or equal to 0");
        }
        if (medicine.getMinimumStock() < 0)
        {
            throw new IllegalArgumentException("Medicine minimum stock should be greater than or equal to 0");
        }
        if (medicine.getExpiryDate().isBefore(LocalDate.now()) || medicine.getExpiryDate().isEqual(LocalDate.now()))
        {
            throw new IllegalArgumentException("Medicine Expiration date should be after today");
        }

        Company company = companyDAO.getCompanyByID(medicine.getCompID());
        if (company == null)
        {
            throw new IllegalArgumentException("Company not found");
        }
        medicine.setMedicineID(existMedicine.getMedicineID());
        medicineDAO.updateMedicine(oldMedicineName, medicine);
        medicineDAO.updateMedicine(oldMedicineName, medicine);
    }


    public void deleteMedicine(User currentUser, Medicine medicine) throws SQLException
    {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN)
        {
            throw new IllegalArgumentException("Only Admin can delete medicines");
        }
        if (medicine == null)
        {
            throw new IllegalArgumentException("Medicine can't be empty");
        }

        Medicine existMedicine = medicineDAO.getMedicineByName(medicine.getMedName());
        if (existMedicine == null)
        {
            throw new IllegalArgumentException("Medicine not found");
        }
        if (!purchaseItemDAO.getPurchaseItemsByMedicine(existMedicine.getMedicineID()).isEmpty())
        {
            throw new IllegalArgumentException("Can't delete this medicine because it has purchase history");
        }
        if (!saleItemDAO.getSaleItemsByMedicine(existMedicine.getMedicineID()).isEmpty())
        {
            throw new IllegalArgumentException("Can't delete this medicine because it has sale history");
        }

        medicineDAO.deleteMedicine(existMedicine);
    }

    public List<Medicine> getLowStockMedicines(User currentUser) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }

        return medicineDAO.getLowStockMedicines();
    }

    public List<Medicine> getExpiringSoonMedicines(User currentUser) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }

        return medicineDAO.getExpiringSoonMedicines();
    }

    public List<Medicine> getExpiredMedicines(User currentUser) throws SQLException
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }

        return medicineDAO.getExpiredMedicines();
    }
}
