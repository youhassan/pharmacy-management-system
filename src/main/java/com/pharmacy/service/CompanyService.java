package com.pharmacy.service;

import com.pharmacy.model.Role;
import com.pharmacy.model.Company;
import com.pharmacy.model.User;
import java.util.List;
import com.pharmacy.repository.CompanyRepository;
import com.pharmacy.repository.MedicineRepository;
import com.pharmacy.repository.PurchaseRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    private CompanyRepository companyRepository;
    private PurchaseRepository purchaseRepository;
    private MedicineRepository medicineRepository;

    public CompanyService(CompanyRepository companyRepository,
                          PurchaseRepository purchaseRepository,
                          MedicineRepository medicineRepository)
    {
        this.companyRepository = companyRepository;
        this.purchaseRepository = purchaseRepository;
        this.medicineRepository = medicineRepository;
    }


    public void addCompany(User currentUser, Company company)
    {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN)
        {
            throw new IllegalArgumentException("Only Admins can add companies");
        }
        if (company == null)
        {
            throw new IllegalArgumentException("Company can't be empty");
        }
        if (company.getCompName() == null || company.getCompName().isBlank())
        {
            throw new IllegalArgumentException("Company name can't be empty");
        }
        if (companyRepository.findByCompNameIgnoreCase(company.getCompName()) != null)
        {
            throw new IllegalArgumentException("Company already exists");
        }
        companyRepository.save(company);
    }


    public Company getCompanyByName(User currentUser, Company company)
    {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN)
        {
            throw new IllegalArgumentException("Only Admin can view companies");
        }
        if (company == null)
        {
            throw new IllegalArgumentException("Company can't be empty");
        }
        if (company.getCompName() == null || company.getCompName().isBlank())
        {
            throw new IllegalArgumentException("Company name can't be empty");
        }
        return companyRepository.findByCompNameIgnoreCase(company.getCompName());
    }


    public Company getCompanyByID(User currentUser, Company company)
    {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN)
        {
            throw new IllegalArgumentException("Only Admin can view companies");
        }
        if (company == null)
        {
            throw new IllegalArgumentException("Company can't be empty");
        }
        if (company.getCompID() == 0)
        {
            throw new IllegalArgumentException("Company ID can't be empty");
        }
        return companyRepository.findById(company.getCompID()).orElse(null);
    }


    public List<Company> getAllCompanies(User currentUser)
    {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN)
        {
            throw new IllegalArgumentException("Only Admin can view companies");
        }
        return companyRepository.findAll();
    }


    public void updateCompany(User currentUser, String oldCompanyName, Company company)
    {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN)
        {
            throw new IllegalArgumentException("Only Admin can update companies");
        }
        if (oldCompanyName == null || oldCompanyName.isBlank())
        {
            throw new IllegalArgumentException("Old company name can't be empty");
        }
        if (company == null)
        {
            throw new IllegalArgumentException("Company can't be empty");
        }
        if (company.getCompName() == null || company.getCompName().isBlank())
        {
            throw new IllegalArgumentException("Company name can't be empty");
        }

        Company existCompany = companyRepository.findByCompNameIgnoreCase(oldCompanyName);
        if (existCompany == null)
        {
            throw new IllegalArgumentException("Company not found");
        }
        if (!oldCompanyName.equalsIgnoreCase(company.getCompName()) && companyRepository.findByCompNameIgnoreCase(company.getCompName()) != null)
        {
            throw new IllegalArgumentException("Company already exists");
        }
        company.setCompID(existCompany.getCompID());
        companyRepository.save(company);
    }


    public void deleteCompany(User currentUser, Company company)
    {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN)
        {
            throw new IllegalArgumentException("Only Admin can delete companies");
        }
        if (company == null)
        {
            throw new IllegalArgumentException("Company can't be empty");
        }
        if (company.getCompName() == null || company.getCompName().isBlank())
        {
            throw new IllegalArgumentException("Company name can't be empty");
        }

        Company existCompany = companyRepository.findByCompNameIgnoreCase(company.getCompName());
        if (existCompany == null)
        {
            throw new IllegalArgumentException("Company not found");
        }
        if (!purchaseRepository.findByCompID(existCompany.getCompID()).isEmpty())
        {
            throw new IllegalArgumentException("Can't delete this company because it has purchase history");
        }
        if (!medicineRepository.findByCompID(existCompany.getCompID()).isEmpty())
        {
            throw new IllegalArgumentException("Can't delete this company because it has Existing Medicine");
        }
        companyRepository.delete(existCompany);
    }
}
