package service;

import dao.CompanyDAO;
import dao.UserDAO;
import model.Purchase;
import model.Role;
import model.Company;
import model.User;
import java.sql.SQLException;
import java.util.List;
import dao.PurchaseDAO;
import dao.MedicineDAO;

public class CompanyService {
    private CompanyDAO companyDAO;
    private PurchaseDAO purchaseDAO;
    private MedicineDAO medicineDAO;

    public CompanyService()
    {
        companyDAO = new CompanyDAO();
        purchaseDAO = new PurchaseDAO();
        medicineDAO = new MedicineDAO();
    }


    public void addCompany(User currentUser, Company company) throws SQLException
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
        if (companyDAO.getCompanyByName(company.getCompName()) != null)
        {
            throw new IllegalArgumentException("Company already exists");
        }
        companyDAO.addCompany(company);
    }


    public Company getCompanyByName(User currentUser, Company company) throws SQLException
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
        return companyDAO.getCompanyByName(company.getCompName());
    }


    public Company getCompanyByID(User currentUser, Company company) throws SQLException
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
        return companyDAO.getCompanyByID(company.getCompID());
    }


    public List<Company> getAllCompanies(User currentUser) throws SQLException
    {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN)
        {
            throw new IllegalArgumentException("Only Admin can view companies");
        }
        return companyDAO.getAllCompanies();
    }


    public void updateCompany(User currentUser, String oldCompanyName, Company company) throws SQLException
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

        Company existCompany = companyDAO.getCompanyByName(oldCompanyName);
        if (existCompany == null)
        {
            throw new IllegalArgumentException("Company not found");
        }
        if (!oldCompanyName.equals(company.getCompName()) && companyDAO.getCompanyByName(company.getCompName()) != null)
        {
            throw new IllegalArgumentException("Company already exists");
        }
        companyDAO.updateCompany(oldCompanyName, company);
    }


    public void deleteCompany(User currentUser, Company company) throws SQLException
    {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN)
        {
            throw new IllegalArgumentException("Only Admin can delete companies");
        }
        if (company == null)
        {
            throw new IllegalArgumentException("Company can't be empty");
        }

        Company existCompany = companyDAO.getCompanyByName(company.getCompName());
        if (existCompany == null)
        {
            throw new IllegalArgumentException("Company not found");
        }
        if (!purchaseDAO.getPurchasesByCompany(company.getCompID()).isEmpty())
        {
            throw new IllegalArgumentException("Can't delete this company because it has purchase history");
        }
        if (!medicineDAO.getMedicineByCompany(company.getCompID()).isEmpty())
        {
            throw new IllegalArgumentException("Can't delete this company because it has Existing Medicine");
        }
        companyDAO.deleteCompany(company);
    }
}
