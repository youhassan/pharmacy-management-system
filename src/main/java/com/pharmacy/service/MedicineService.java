package com.pharmacy.service;

import com.pharmacy.model.Company;
import com.pharmacy.model.Medicine;
import com.pharmacy.model.Role;
import com.pharmacy.model.User;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import com.pharmacy.repository.CompanyRepository;
import com.pharmacy.repository.MedicineRepository;
import com.pharmacy.repository.PurchaseItemRepository;
import com.pharmacy.repository.SaleItemRepository;

@Service
public class MedicineService {
    private MedicineRepository medicineRepository;
    private CompanyRepository companyRepository;
    private PurchaseItemRepository purchaseItemRepository;
    private SaleItemRepository saleItemRepository;

    public MedicineService(MedicineRepository medicineRepository,
                           CompanyRepository companyRepository,
                           PurchaseItemRepository purchaseItemRepository,
                           SaleItemRepository saleItemRepository)
    {
        this.medicineRepository = medicineRepository;
        this.companyRepository = companyRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.saleItemRepository = saleItemRepository;
    }


    public void addMedicine(User currentUser, Medicine medicine, String companyName)
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
        if (companyName == null || companyName.isBlank())
        {
            throw new IllegalArgumentException("Company name can't be empty");
        }
        if (medicineRepository.findByMedName(medicine.getMedName()) != null)
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
        if (medicine.getExpiryDate() == null)
        {
            throw new IllegalArgumentException("Expiry date can't be empty");
        }
        if (medicine.getExpiryDate().isBefore(LocalDate.now()) || medicine.getExpiryDate().isEqual(LocalDate.now()))
        {
            throw new IllegalArgumentException("Medicine Expiration date should be after today");
        }

        Company company = companyRepository.findByCompNameIgnoreCase(companyName);
        if (company == null)
        {
            throw new IllegalArgumentException("Company not found");
        }

        medicine.setCompID(company.getCompID());
        medicineRepository.save(medicine);
    }

    public void addMedicine(User currentUser, Medicine medicine)
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
        if (medicineRepository.findByMedName(medicine.getMedName()) != null)
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
        if (medicine.getExpiryDate() == null)
        {
            throw new IllegalArgumentException("Expiry date can't be empty");
        }
        if (medicine.getExpiryDate().isBefore(LocalDate.now()) || medicine.getExpiryDate().isEqual(LocalDate.now()))
        {
            throw new IllegalArgumentException("Medicine Expiration date should be after today");
        }

        Company company = companyRepository.findById(medicine.getCompID()).orElse(null);
        if (company == null)
        {
            throw new IllegalArgumentException("Company not found");
        }

        medicineRepository.save(medicine);
    }


    public Medicine getMedicineByName(Medicine medicine)
    {
        if (medicine == null)
        {
            throw new IllegalArgumentException("Medicine can't be empty");
        }
        if (medicine.getMedName() == null || medicine.getMedName().isBlank())
        {
            throw new IllegalArgumentException("Medicine name can't be empty");
        }
        return medicineRepository.findByMedName(medicine.getMedName());
    }


    public Medicine getMedicineByID(Medicine medicine)
    {
        if (medicine == null)
        {
            throw new IllegalArgumentException("Medicine can't be empty");
        }
        if (medicine.getMedicineID() == 0)
        {
            throw new IllegalArgumentException("Medicine ID can't be empty");
        }
        return medicineRepository.findById(medicine.getMedicineID()).orElse(null);
    }


    public List<Medicine> getAllMedicines()
    {
        return medicineRepository.findAll();
    }


    public List<Medicine> getMedicinesByCompany(Company company)
    {
        if (company == null)
        {
            throw new IllegalArgumentException("Company can't be empty");
        }
        if (company.getCompID() == 0)
        {
            throw new IllegalArgumentException("Company ID can't be empty");
        }
        return medicineRepository.findByCompID(company.getCompID());
    }

    public List<Medicine> getMedicinesByCompanyName(String companyName)
    {
        if (companyName == null || companyName.isBlank())
        {
            throw new IllegalArgumentException("Company name can't be empty");
        }
        Company company = companyRepository.findByCompNameIgnoreCase(companyName);
        if (company == null)
        {
            throw new IllegalArgumentException("Company not found");
        }
        return medicineRepository.findByCompID(company.getCompID());
    }


    public void updateMedicine(User currentUser, String oldMedicineName, Medicine medicine, String companyName)
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
        if (companyName == null || companyName.isBlank())
        {
            throw new IllegalArgumentException("Company name can't be empty");
        }

        Medicine existMedicine = medicineRepository.findByMedName(oldMedicineName);
        if (existMedicine == null)
        {
            throw new IllegalArgumentException("Medicine not found");
        }
        if (!oldMedicineName.equalsIgnoreCase(medicine.getMedName()) && medicineRepository.findByMedName(medicine.getMedName()) != null)
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
        if (medicine.getExpiryDate() == null)
        {
            throw new IllegalArgumentException("Expiry date can't be empty");
        }
        if (medicine.getExpiryDate().isBefore(LocalDate.now()) || medicine.getExpiryDate().isEqual(LocalDate.now()))
        {
            throw new IllegalArgumentException("Medicine Expiration date should be after today");
        }

        Company company = companyRepository.findByCompNameIgnoreCase(companyName);
        if (company == null)
        {
            throw new IllegalArgumentException("Company not found");
        }
        medicine.setMedicineID(existMedicine.getMedicineID());
        medicine.setCompID(company.getCompID());
        medicineRepository.save(medicine);
    }

    public void updateMedicine(User currentUser, String oldMedicineName, Medicine medicine)
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

        Medicine existMedicine = medicineRepository.findByMedName(oldMedicineName);
        if (existMedicine == null)
        {
            throw new IllegalArgumentException("Medicine not found");
        }
        if (!oldMedicineName.equalsIgnoreCase(medicine.getMedName()) && medicineRepository.findByMedName(medicine.getMedName()) != null)
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
        if (medicine.getExpiryDate() == null)
        {
            throw new IllegalArgumentException("Expiry date can't be empty");
        }
        if (medicine.getExpiryDate().isBefore(LocalDate.now()) || medicine.getExpiryDate().isEqual(LocalDate.now()))
        {
            throw new IllegalArgumentException("Medicine Expiration date should be after today");
        }

        Company company = companyRepository.findById(medicine.getCompID()).orElse(null);
        if (company == null)
        {
            throw new IllegalArgumentException("Company not found");
        }
        medicine.setMedicineID(existMedicine.getMedicineID());
        medicineRepository.save(medicine);
    }


    public void deleteMedicine(User currentUser, Medicine medicine)
    {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN)
        {
            throw new IllegalArgumentException("Only Admin can delete medicines");
        }
        if (medicine == null)
        {
            throw new IllegalArgumentException("Medicine can't be empty");
        }

        Medicine existMedicine = medicineRepository.findByMedName(medicine.getMedName());
        if (existMedicine == null)
        {
            throw new IllegalArgumentException("Medicine not found");
        }
        if (!purchaseItemRepository.findByMedicineID(existMedicine.getMedicineID()).isEmpty())
        {
            throw new IllegalArgumentException("Can't delete this medicine because it has purchase history");
        }
        if (!saleItemRepository.findByMedicineID(existMedicine.getMedicineID()).isEmpty())
        {
            throw new IllegalArgumentException("Can't delete this medicine because it has sale history");
        }

        medicineRepository.delete(existMedicine);
    }

    public List<Medicine> getLowStockMedicines(User currentUser)
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }

        return medicineRepository.findLowStockMedicines();
    }

    public List<Medicine> getExpiringSoonMedicines(User currentUser)
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }

        return medicineRepository.findExpiringSoonMedicines(LocalDate.now(), LocalDate.now().plusDays(30));
    }

    public List<Medicine> getExpiredMedicines(User currentUser)
    {
        if (currentUser == null)
        {
            throw new IllegalArgumentException("User can't be empty");
        }

        return medicineRepository.findExpiredMedicines(LocalDate.now());
    }
}
