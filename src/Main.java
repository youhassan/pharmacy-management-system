import model.*;
import service.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        UserService userService = new UserService();
        CompanyService companyService = new CompanyService();
        MedicineService medicineService = new MedicineService();
        PurchaseService purchaseService = new PurchaseService();
        SaleService saleService = new SaleService();

        boolean programRunning = true;

        while (programRunning)
        {
            User currentUser = null;
            while (currentUser == null && programRunning)
            {
                try
                {
                    System.out.println();
                    System.out.println("===== Pharmacy Management System =====");
                    System.out.println("1. Login");
                    System.out.println("0. Exit");
                    int choice = readInt(sc, "Choose: ");

                    switch (choice)
                    {
                        case 1:
                            while (currentUser == null)
                            {
                                String username = readNonEmptyString(sc, "Username: ", "Username");
                                String password = readNonEmptyString(sc, "Password: ", "Password");

                                try
                                {
                                    currentUser = userService.login(username, password);
                                    System.out.println("Login successful");
                                    System.out.println("Welcome " + currentUser.getUsername());
                                }
                                catch (Exception e)
                                {
                                    System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid credentials");
                                    System.out.println("Please try again.");
                                }
                            }
                            break;

                        case 0:
                            programRunning = false;
                            System.out.println("Closing..");
                            break;

                        default:
                            System.out.println("Invalid choice");
                    }
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
                }
            }

            if (!programRunning)
            {
                break;
            }
            boolean loggedIn = true;

            while (loggedIn)
            {
                try
                {
                    System.out.println();
                    System.out.println("===== Main Menu =====");

                    if (currentUser.getRole() == Role.ADMIN)
                    {
                        System.out.println("1. User Management");
                        System.out.println("2. Company Management");
                        System.out.println("3. Medicine Management");
                        System.out.println("4. Purchase Management");
                        System.out.println("5. Sale Management");
                        System.out.println("6. Inventory Reports");
                        System.out.println("7. Financial Reports");
                        System.out.println("8. Logout");
                        System.out.println("0. Exit");
                        int choice = readInt(sc, "Choose: ");

                        switch (choice)
                        {
                            case 1:
                                userMenu(sc, userService, saleService, currentUser);
                                break;

                            case 2:
                                companyMenu(sc, companyService, medicineService, purchaseService, currentUser);
                                break;

                            case 3:
                                medicineMenu(sc, medicineService, companyService, currentUser);
                                break;

                            case 4:
                                purchaseMenu(sc, purchaseService, companyService, medicineService, currentUser);
                                break;

                            case 5:
                                saleMenu(sc, saleService, medicineService, currentUser);
                                break;

                            case 6:
                                inventoryMenu(sc, medicineService, companyService, currentUser);
                                break;

                            case 7:
                                financialReportMenu(sc, saleService, currentUser);
                                break;

                            case 8:
                                loggedIn = false;
                                System.out.println("Logged out successfully");
                                break;

                            case 0:
                                loggedIn = false;
                                programRunning = false;
                                System.out.println("Closing..");
                                break;

                            default:
                                System.out.println("Invalid choice");
                        }
                    }
                    else
                    {
                        System.out.println("1. View/Search Medicine");
                        System.out.println("2. Purchase Management");
                        System.out.println("3. Sale Management");
                        System.out.println("4. Inventory Reports");
                        System.out.println("5. Logout");
                        System.out.println("0. Exit");
                        int choice = readInt(sc, "Choose: ");

                        switch (choice)
                        {
                            case 1:
                                medicineMenu(sc, medicineService, companyService, currentUser);
                                break;

                            case 2:
                                purchaseMenu(sc, purchaseService, companyService, medicineService, currentUser);
                                break;

                            case 3:
                                saleMenu(sc, saleService, medicineService, currentUser);
                                break;

                            case 4:
                                inventoryMenu(sc, medicineService, companyService, currentUser);
                                break;

                            case 5:
                                loggedIn = false;
                                System.out.println("Logged out successfully");
                                break;

                            case 0:
                                loggedIn = false;
                                programRunning = false;
                                System.out.println("Goodbye!");
                                break;

                            default:
                                System.out.println("Invalid choice");
                        }
                    }
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
                }
            }
        }
        sc.close();
    }

    public static void userMenu(Scanner sc, UserService userService, SaleService saleService, User currentUser)
    {
        boolean running = true;

        while (running)
        {
            try
            {
                System.out.println();
                System.out.println("===== User Management =====");
                System.out.println("1. Add User");
                System.out.println("2. View All Users");
                System.out.println("3. Update User");
                System.out.println("4. Delete User");
                System.out.println("0. Back");
                int choice = readInt(sc, "Choose: ");

                switch (choice)
                {
                    case 1:
                        while (true)
                        {
                            try
                            {
                                String username = readNonEmptyString(sc, "Username: ", "Username");
                                String password = readNonEmptyString(sc, "Password: ", "Password");
                                Role role = readRole(sc, "Role (ADMIN / PHARMACIST): ");

                                User user = new User(username, password, role);
                                userService.addUser(currentUser, user);
                                System.out.println("User added successfully");
                                break;
                            }
                            catch (Exception e)
                            {
                                System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
                                System.out.println("Please try again.");
                            }
                        }
                        break;

                    case 2:
                        List<User> users = userService.getAllUsers(currentUser);

                        for (User u : users)
                        {
                            System.out.println("Username: " + u.getUsername() + " | Role: " + u.getRole());
                        }
                        break;

                    case 3:
                        while (true)
                        {
                            try
                            {
                                String oldUsername = readNonEmptyString(sc, "Old Username: ", "Old Username");
                                String newUsername = readNonEmptyString(sc, "New Username: ", "New Username");
                                String newPassword = readNonEmptyString(sc, "New Password: ", "New Password");
                                Role newRole = readRole(sc, "New Role (ADMIN / PHARMACIST): ");

                                User updatedUser = new User(newUsername, newPassword, newRole);
                                userService.updateUser(currentUser, oldUsername, updatedUser);

                                System.out.println("User updated successfully");
                                break;
                            }
                            catch (Exception e)
                            {
                                System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
                                System.out.println("Please try again.");
                            }
                        }
                        break;

                    case 4:
                        while (true)
                        {
                            try
                            {
                                String deleteUsername = readNonEmptyString(sc, "Username to delete: ", "Username");
                                if (currentUser.getUsername().equalsIgnoreCase(deleteUsername))
                                {
                                    System.out.println("Admin cannot delete their own account");
                                    System.out.println("Please enter another username.");
                                    continue;
                                }
                                User deleteUser = new User(deleteUsername, null);

                                userService.deleteUser(currentUser, deleteUser);
                                System.out.println("User deleted successfully");
                                break;
                            }
                            catch (Exception e)
                            {
                                System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
                                System.out.println("Please try again.");
                            }
                        }
                        break;

                    case 0:
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid choice");
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
            }
        }
    }

    public static void companyMenu(Scanner sc,
                                   CompanyService companyService,
                                   MedicineService medicineService,
                                   PurchaseService purchaseService,
                                   User currentUser)
    {
        boolean running = true;

        while (running)
        {
            try
            {
                System.out.println();
                System.out.println("===== Company Management =====");
                System.out.println("1. Add Company");
                System.out.println("2. View Company");
                System.out.println("3. View All Companies");
                System.out.println("4. Update Company");
                System.out.println("5. Delete Company");
                System.out.println("0. Back");
                int choice = readInt(sc, "Choose: ");

                switch (choice)
                {
                    case 1:
                        while (true)
                        {
                            try
                            {
                                String companyName = readNonEmptyString(sc, "Company Name: ", "Company Name");
                                Company company = new Company(0, companyName);

                                companyService.addCompany(currentUser, company);
                                System.out.println("Company added successfully");
                                break;
                            }
                            catch (Exception e)
                            {
                                System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
                                System.out.println("Please try again.");
                            }
                        }
                        break;

                    case 2:
                        String searchName = readNonEmptyString(sc, "Company Name: ", "Company Name");
                        Company searchCompany = new Company(0, searchName);
                        Company foundCompany = companyService.getCompanyByName(currentUser, searchCompany);

                        if (foundCompany == null)
                        {
                            System.out.println("Company not found");
                        }
                        else
                        {
                            System.out.println("ID: " + foundCompany.getCompID() + " | Name: " + foundCompany.getCompName());
                        }
                        break;

                    case 3:
                        List<Company> companies = companyService.getAllCompanies(currentUser);

                        for (Company c : companies)
                        {
                            System.out.println("ID: " + c.getCompID() + " | Name: " + c.getCompName());
                        }
                        break;

                    case 4:
                        while (true)
                        {
                            try
                            {
                                String oldCompanyName = readNonEmptyString(sc, "Old Company Name: ", "Old Company Name");
                                String newCompanyName = readNonEmptyString(sc, "New Company Name: ", "New Company Name");

                                Company updatedCompany = new Company(0, newCompanyName);
                                companyService.updateCompany(currentUser, oldCompanyName, updatedCompany);

                                System.out.println("Company updated successfully");
                                break;
                            }
                            catch (Exception e)
                            {
                                System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
                                System.out.println("Please try again.");
                            }
                        }
                        break;

                    case 5:
                        while (true)
                        {
                            try
                            {
                                String deleteCompanyName = readNonEmptyString(sc, "Company Name to delete: ", "Company Name");
                                Company deleteCompany = new Company(0, deleteCompanyName);
                                companyService.deleteCompany(currentUser, deleteCompany);

                                System.out.println("Company deleted successfully");
                                break;
                            }
                            catch (Exception e)
                            {
                                System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
                                System.out.println("Please try again.");
                            }
                        }
                        break;

                    case 0:
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid choice");
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
            }
        }
    }

    public static void medicineMenu(Scanner sc, MedicineService medicineService, CompanyService companyService, User currentUser)
    {
        boolean running = true;

        while (running)
        {
            try
            {
                System.out.println();
                System.out.println("===== Medicine Management =====");

                if (currentUser.getRole() == Role.ADMIN)
                {
                    System.out.println("1. Add Medicine");
                    System.out.println("2. Search Medicine by Name");
                    System.out.println("3. View All Medicines");
                    System.out.println("4. View Medicines by Company");
                    System.out.println("5. Update Medicine");
                    System.out.println("6. Delete Medicine");
                    System.out.println("0. Back");
                }
                else
                {
                    System.out.println("1. Search Medicine by Name");
                    System.out.println("2. View All Medicines");
                    System.out.println("3. View Medicines by Company");
                    System.out.println("0. Back");
                }

                int choice = readInt(sc, "Choose: ");

                if (currentUser.getRole() == Role.ADMIN)
                {
                    switch (choice)
                    {
                        case 1:
                            while (true)
                            {
                                try
                                {
                                    String medName = readNonEmptyString(sc, "Medicine Name: ", "Medicine Name");
                                    int quantity = readNonNegativeInt(sc, "Quantity: ", "Quantity cannot be negative.");
                                    LocalDate expiryDate = readExpiryDate(sc, "Expiry Date (YYYY-MM-DD): ");
                                    int minimumStock = readNonNegativeInt(sc, "Minimum Stock: ", "Minimum stock cannot be negative.");
                                    String compName = readNonEmptyString(sc, "Company Name: ", "Company Name");

                                    Medicine medicine = new Medicine(0, medName, quantity, expiryDate, minimumStock, 0);
                                    medicineService.addMedicine(currentUser, medicine, compName);
                                    System.out.println("Medicine added successfully");
                                    break;
                                }
                                catch (Exception e)
                                {
                                    System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
                                    System.out.println("Please try again.");
                                }
                            }
                            break;

                        case 2:
                            String searchMedName = readNonEmptyString(sc, "Medicine Name: ", "Medicine Name");
                            Medicine searchMedicine = new Medicine(0, searchMedName, 0, LocalDate.now().plusDays(1), 0, 0);
                            Medicine foundMedicine = medicineService.getMedicineByName(searchMedicine);

                            if (foundMedicine == null)
                            {
                                System.out.println("Medicine not found");
                            }
                            else
                            {
                                printMedicine(foundMedicine, companyService, currentUser);
                            }
                            break;

                        case 3:
                            List<Medicine> medicines = medicineService.getAllMedicines();

                            for (Medicine m : medicines)
                            {
                                printMedicine(m, companyService, currentUser);
                            }
                            break;

                        case 4:
                            String viewCompName = readNonEmptyString(sc, "Company Name: ", "Company Name");
                            try
                            {
                                List<Medicine> companyMedicines = medicineService.getMedicinesByCompanyName(viewCompName);

                                for (Medicine m : companyMedicines)
                                {
                                    printMedicine(m, companyService, currentUser);
                                }
                            }
                            catch (Exception e)
                            {
                                System.out.println(e.getMessage() != null ? e.getMessage() : "Company not found");
                            }
                            break;

                        case 5:
                            while (true)
                            {
                                try
                                {
                                    String oldMedicineName = readNonEmptyString(sc, "Old Medicine Name: ", "Old Medicine Name");
                                    String newMedicineName = readNonEmptyString(sc, "New Medicine Name: ", "New Medicine Name");
                                    int newQuantity = readNonNegativeInt(sc, "Quantity: ", "Quantity cannot be negative.");
                                    LocalDate newExpiryDate = readExpiryDate(sc, "Expiry Date (YYYY-MM-DD): ");
                                    int newMinimumStock = readNonNegativeInt(sc, "Minimum Stock: ", "Minimum stock cannot be negative.");
                                    String newCompName = readNonEmptyString(sc, "Company Name: ", "Company Name");

                                    Medicine updatedMedicine = new Medicine(0, newMedicineName, newQuantity, newExpiryDate, newMinimumStock, 0);
                                    medicineService.updateMedicine(currentUser, oldMedicineName, updatedMedicine, newCompName);
                                    System.out.println("Medicine updated successfully");
                                    break;
                                }
                                catch (Exception e)
                                {
                                    System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
                                    System.out.println("Please try again.");
                                }
                            }
                            break;

                        case 6:
                            while (true)
                            {
                                try
                                {
                                    String deleteMedicineName = readNonEmptyString(sc, "Medicine Name to delete: ", "Medicine Name");
                                    Medicine deleteMedicine = new Medicine(0, deleteMedicineName, 0, LocalDate.now().plusDays(1), 0, 0);
                                    medicineService.deleteMedicine(currentUser, deleteMedicine);

                                    System.out.println("Medicine deleted successfully");
                                    break;
                                }
                                catch (Exception e)
                                {
                                    System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
                                    System.out.println("Please try again.");
                                }
                            }
                            break;

                        case 0:
                            running = false;
                            break;

                        default:
                            System.out.println("Invalid choice");
                    }
                }
                else
                {
                    switch (choice)
                    {
                        case 1:
                            String searchMedName = readNonEmptyString(sc, "Medicine Name: ", "Medicine Name");
                            Medicine searchMedicine = new Medicine(0, searchMedName, 0, LocalDate.now().plusDays(1), 0, 0);
                            Medicine foundMedicine = medicineService.getMedicineByName(searchMedicine);

                            if (foundMedicine == null)
                            {
                                System.out.println("Medicine not found");
                            }
                            else
                            {
                                printMedicine(foundMedicine, companyService, currentUser);
                            }
                            break;

                        case 2:
                            List<Medicine> medicines = medicineService.getAllMedicines();

                            for (Medicine m : medicines)
                            {
                                printMedicine(m, companyService, currentUser);
                            }
                            break;

                        case 3:
                            String viewCompName = readNonEmptyString(sc, "Company Name: ", "Company Name");
                            try
                            {
                                List<Medicine> companyMedicines = medicineService.getMedicinesByCompanyName(viewCompName);

                                for (Medicine m : companyMedicines)
                                {
                                    printMedicine(m, companyService, currentUser);
                                }
                            }
                            catch (Exception e)
                            {
                                System.out.println(e.getMessage() != null ? e.getMessage() : "Company not found");
                            }
                            break;

                        case 0:
                            running = false;
                            break;

                        default:
                            System.out.println("Invalid choice");
                    }
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
            }
        }
    }

    public static void purchaseMenu(Scanner sc, PurchaseService purchaseService, CompanyService companyService, MedicineService medicineService, User currentUser)
    {
        boolean running = true;

        while (running)
        {
            try
            {
                System.out.println();
                System.out.println("===== Purchase Management =====");
                System.out.println("1. Add Purchase");
                System.out.println("2. Search Purchases by Company");
                System.out.println("3. View All Purchases");
                System.out.println("4. View Purchases by Date");
                System.out.println("5. Update Purchase");
                System.out.println("6. Delete Purchase");
                System.out.println("0. Back");
                int choice = readInt(sc, "Choose: ");

                switch (choice)
                {
                    case 1:
                        while (true)
                        {
                            try
                            {
                                String compName = readNonEmptyString(sc, "Company Name: ", "Company Name");
                                LocalDate purDate = readDate(sc, "Purchase Date (YYYY-MM-DD): ");
                                int itemCount = readPositiveInt(sc, "Number of items: ", "Number of items must be greater than 0.");

                                List<PurchaseService.PurchaseItemEntry> purchaseItems = new ArrayList<>();

                                for (int i = 0; i < itemCount; i++)
                                {
                                    System.out.println("Item " + (i + 1));
                                    String medName = readNonEmptyString(sc, "Medicine Name: ", "Medicine Name");

                                    Medicine existingMed = medicineService.getMedicineByName(new Medicine(0, medName, 0, LocalDate.now().plusDays(1), 0, 0));

                                    if (existingMed == null)
                                    {
                                        LocalDate expiryDate = readExpiryDate(sc, "Expiry Date (YYYY-MM-DD): ");
                                        int minimumStock = readNonNegativeInt(sc, "Minimum Stock: ", "Minimum stock cannot be negative.");
                                        int quantityPur = readPositiveInt(sc, "Quantity: ", "Quantity must be greater than 0.");
                                        double unitCost = readPositiveDouble(sc, "Unit Cost: ", "Unit cost must be greater than 0.");

                                        purchaseItems.add(new PurchaseService.PurchaseItemEntry(medName, quantityPur, unitCost, expiryDate, minimumStock));
                                    }
                                    else
                                    {
                                        int quantityPur = readPositiveInt(sc, "Quantity: ", "Quantity must be greater than 0.");
                                        double unitCost = readPositiveDouble(sc, "Unit Cost: ", "Unit cost must be greater than 0.");

                                        purchaseItems.add(new PurchaseService.PurchaseItemEntry(medName, quantityPur, unitCost));
                                    }
                                }
                                purchaseService.addPurchase(currentUser, compName, purDate, purchaseItems);
                                System.out.println("Purchase added successfully");
                                break;
                            }
                            catch (Exception e)
                            {
                                System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
                                System.out.println("Please try again.");
                            }
                        }
                        break;

                    case 2:
                        String searchCompName = readNonEmptyString(sc, "Company Name: ", "Company Name");
                        List<Purchase> compPurchases = purchaseService.getPurchasesByCompanyName(currentUser, searchCompName);

                        if (compPurchases.isEmpty())
                        {
                            System.out.println("No purchases found for company: " + searchCompName);
                        }
                        else
                        {
                            for (Purchase p : compPurchases)
                            {
                                printPurchase(p, companyService, currentUser);
                            }
                        }
                        break;

                    case 3:
                        List<Purchase> purchases = purchaseService.getAllPurchases(currentUser);

                        for (Purchase p : purchases)
                        {
                            printPurchase(p, companyService, currentUser);
                        }
                        break;

                    case 4:
                        LocalDate date = readDate(sc, "Date (YYYY-MM-DD): ");
                        List<Purchase> datePurchases = purchaseService.getPurchasesByDate(currentUser, date);

                        for (Purchase p : datePurchases)
                        {
                            printPurchase(p, companyService, currentUser);
                        }
                        break;

                    case 5:
                        while (true)
                        {
                            try
                            {
                                int updatePurID = readPositiveInt(sc, "Purchase ID: ", "Purchase ID must be a positive number.");
                                String updateCompName = readNonEmptyString(sc, "New Company Name: ", "Company Name");
                                LocalDate updateDate = readDate(sc, "New Purchase Date (YYYY-MM-DD): ");

                                purchaseService.updatePurchase(currentUser, updatePurID, updateCompName, updateDate);
                                System.out.println("Purchase updated successfully");
                                break;
                            }
                            catch (Exception e)
                            {
                                System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
                                System.out.println("Please try again.");
                            }
                        }
                        break;

                    case 6:
                        while (true)
                        {
                            try
                            {
                                int deletePurID = readPositiveInt(sc, "Purchase ID to delete: ", "Purchase ID must be a positive number.");
                                Purchase deletePurchase = new Purchase(deletePurID, 0, LocalDate.now());
                                purchaseService.deletePurchase(currentUser, deletePurchase);
                                System.out.println("Purchase deleted successfully");
                                break;
                            }
                            catch (Exception e)
                            {
                                System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
                                System.out.println("Please try again.");
                            }
                        }
                        break;

                    case 0:
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid choice");
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
            }
        }
    }

    public static void saleMenu(Scanner sc, SaleService saleService, MedicineService medicineService, User currentUser)
    {
        boolean running = true;

        while (running)
        {
            try
            {
                System.out.println();
                System.out.println("===== Sale Management =====");
                System.out.println("1. Add Sale");
                System.out.println("2. Search Sales by Username");
                System.out.println("3. View All Sales");
                System.out.println("4. View Sales by Date");
                System.out.println("5. Update Sale");
                System.out.println("6. Delete Sale");
                System.out.println("0. Back");
                int choice = readInt(sc, "Choose: ");

                switch (choice)
                {
                    case 1:
                        while (true)
                        {
                            try
                            {
                                LocalDate saleDate = readDate(sc, "Sale Date (YYYY-MM-DD): ");
                                int itemCount = readPositiveInt(sc, "Number of items: ", "Number of items must be greater than 0.");

                                List<SaleService.SaleItemEntry> saleItems = new ArrayList<>();

                                for (int i = 0; i < itemCount; i++)
                                {
                                    System.out.println("Item " + (i + 1));
                                    String medName;
                                    Medicine med;
                                    while (true)
                                    {
                                        medName = readNonEmptyString(sc, "Medicine Name: ", "Medicine Name");
                                        med = medicineService.getMedicineByName(new Medicine(0, medName, 0, LocalDate.now().plusDays(1), 0, 0));
                                        if (med == null)
                                        {
                                            System.out.println("Medicine '" + medName + "' not found. Please enter an existing medicine name.");
                                            continue;
                                        }
                                        if (med.getExpiryDate().isBefore(LocalDate.now()))
                                        {
                                            System.out.println("Medicine '" + med.getMedName() + "' is expired. Cannot be sold.");
                                            continue;
                                        }
                                        if (med.getQuantity() <= 0)
                                        {
                                            System.out.println("Medicine '" + med.getMedName() + "' is out of stock.");
                                            continue;
                                        }
                                        break;
                                    }

                                    int quantitySold;
                                    while (true)
                                    {
                                        quantitySold = readPositiveInt(sc, "Quantity Sold: ", "Quantity must be greater than 0.");
                                        if (quantitySold > med.getQuantity())
                                        {
                                            System.out.println("Only " + med.getQuantity() + " available. Please enter a valid quantity.");
                                            continue;
                                        }
                                        break;
                                    }

                                    double unitPrice = readPositiveDouble(sc, "Unit Price: ", "Unit price must be greater than 0.");

                                    saleItems.add(new SaleService.SaleItemEntry(medName, quantitySold, unitPrice));
                                }

                                saleService.addSale(currentUser, saleDate, saleItems);
                                System.out.println("Sale added successfully");
                                break;
                            }
                            catch (Exception e)
                            {
                                System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
                                System.out.println("Please try again.");
                            }
                        }
                        break;

                    case 2:
                        String searchUsername = readNonEmptyString(sc, "Username: ", "Username");
                        User searchUser = new User(searchUsername, null);

                        List<Sale> userSales = saleService.getSalesByUser(currentUser, searchUser);

                        if (userSales.isEmpty())
                        {
                            System.out.println("No sales found for user: " + searchUsername);
                        }
                        else
                        {
                            for (Sale s : userSales)
                            {
                                System.out.println("ID: " + s.getSaleID() + " | Date: " + s.getSaleDate() + " | Username: " + s.getUsername());
                            }
                        }
                        break;

                    case 3:
                        List<Sale> sales = saleService.getAllSales(currentUser);

                        for (Sale s : sales)
                        {
                            System.out.println("ID: " + s.getSaleID() + " | Date: " + s.getSaleDate() + " | Username: " + s.getUsername());
                        }
                        break;

                    case 4:
                        LocalDate date = readDate(sc, "Date (YYYY-MM-DD): ");
                        List<Sale> dateSales = saleService.getSalesByDate(currentUser, date);

                        for (Sale s : dateSales)
                        {
                            System.out.println("ID: " + s.getSaleID() + " | Date: " + s.getSaleDate() + " | Username: " + s.getUsername());
                        }
                        break;

                    case 5:
                        while (true)
                        {
                            try
                            {
                                int updateSaleID = readPositiveInt(sc, "Sale ID: ", "Sale ID must be a positive number.");
                                LocalDate updateSaleDate = readDate(sc, "New Sale Date (YYYY-MM-DD): ");
                                Sale updatedSale = new Sale(updateSaleID, updateSaleDate, currentUser.getUsername());
                                saleService.updateSale(currentUser, updatedSale);
                                System.out.println("Sale updated successfully");
                                break;
                            }
                            catch (Exception e)
                            {
                                System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
                                System.out.println("Please try again.");
                            }
                        }
                        break;

                    case 6:
                        while (true)
                        {
                            try
                            {
                                int deleteSaleID = readPositiveInt(sc, "Sale ID to delete: ", "Sale ID must be a positive number.");
                                Sale deleteSale = new Sale(deleteSaleID, LocalDate.now(), currentUser.getUsername());
                                saleService.deleteSale(currentUser, deleteSale);
                                System.out.println("Sale deleted successfully");
                                break;
                            }
                            catch (Exception e)
                            {
                                System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
                                System.out.println("Please try again.");
                            }
                        }
                        break;

                    case 0:
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid choice");
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
            }
        }
    }

    public static void inventoryMenu(Scanner sc, MedicineService medicineService, CompanyService companyService, User currentUser)
    {
        boolean running = true;

        while (running)
        {
            try
            {
                System.out.println();
                System.out.println("===== Inventory Reports =====");
                System.out.println("1. Low Stock Medicines");
                System.out.println("2. Expired Medicines");
                System.out.println("3. Expiring Soon Medicines");
                System.out.println("0. Back");
                int choice = readInt(sc, "Choose: ");

                switch (choice)
                {
                    case 1:
                        List<Medicine> lowStock = medicineService.getLowStockMedicines(currentUser);

                        for (Medicine medicine : lowStock)
                        {
                            printMedicine(medicine, companyService, currentUser);
                        }
                        break;

                    case 2:
                        List<Medicine> expired = medicineService.getExpiredMedicines(currentUser);

                        for (Medicine medicine : expired)
                        {
                            printMedicine(medicine, companyService, currentUser);
                        }
                        break;

                    case 3:
                        List<Medicine> expiringSoon = medicineService.getExpiringSoonMedicines(currentUser);

                        for (Medicine medicine : expiringSoon)
                        {
                            printMedicine(medicine, companyService, currentUser);
                        }
                        break;

                    case 0:
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid choice");
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
            }
        }
    }

    public static void financialReportMenu(Scanner sc, SaleService saleService, User currentUser)
    {
        boolean running = true;

        while (running)
        {
            try
            {
                System.out.println();
                System.out.println("===== Financial Reports =====");
                System.out.println("1. Generate Financial Report");
                System.out.println("0. Back");
                int choice = readInt(sc, "Choose: ");

                switch (choice)
                {
                    case 1:
                        while (true)
                        {
                            LocalDate fromDate = readDate(sc, "From Date (YYYY-MM-DD): ");
                            LocalDate toDate = readDate(sc, "To Date (YYYY-MM-DD): ");

                            if (fromDate.isAfter(toDate))
                            {
                                System.out.println("From Date cannot be after To Date. Please enter a valid date range.");
                                continue;
                            }

                            FinancialReport report = saleService.getFinancialReport(currentUser, fromDate, toDate);

                            System.out.println();
                            System.out.println("===== Financial Report (" + report.getFromDate() + " to " + report.getToDate() + ") =====");
                            System.out.println("Total Sales: " + report.getTotalSalesCount() + " transactions | Total Revenue: " + String.format("%.2f", report.getTotalSalesRevenue()) + " EGP");
                            System.out.println("Total Purchases: " + report.getTotalPurchasesCount() + " transactions | Total Expenses: " + String.format("%.2f", report.getTotalPurchaseCost()) + " EGP");
                            System.out.println("----------------------------------------");
                            System.out.println("Net Profit: " + String.format("%.2f", report.getNetProfit()) + " EGP");
                            System.out.println("========================================");

                            System.out.println();
                            System.out.println("----- Sold Items -----");
                            if (report.getSoldItems().isEmpty())
                            {
                                System.out.println("No sold items in this period.");
                            }
                            else
                            {
                                for (FinancialReport.SoldItemDetail item : report.getSoldItems())
                                {
                                    System.out.println("Sale ID: " + item.getSaleID() + " | Date: " + item.getSaleDate() + " | User: " + item.getUsername() + " | Medicine: " + item.getMedicineName() + " | Quantity: " + item.getQuantitySold() + " | Unit Price: " + String.format("%.2f", item.getUnitPrice()) + " EGP | Total: " + String.format("%.2f", item.getSubtotal()) + " EGP" );
                                }
                            }

                            System.out.println();
                            System.out.println("----- Purchased Items -----");
                            if (report.getPurchasedItems().isEmpty())
                            {
                                System.out.println("No purchased items in this period.");
                            }
                            else
                            {
                                for (FinancialReport.PurchasedItemDetail item : report.getPurchasedItems())
                                {
                                    System.out.println("Purchase ID: " + item.getPurID() + " | Date: " + item.getPurDate() + " | Company: " + item.getCompanyName() + " | Medicine: " + item.getMedicineName() + " | Quantity: " + item.getQuantityPur() + " | Unit Cost: " + String.format("%.2f", item.getUnitCost()) + " EGP | Total: " + String.format("%.2f", item.getSubtotal()) + " EGP" );
                                }
                            }
                            break;
                        }
                        break;

                    case 0:
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid choice");
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage() != null ? e.getMessage() : "Invalid input");
            }
        }
    }

    public static void printMedicine(Medicine medicine, CompanyService companyService, User currentUser)
    {
        String companyName = "ID: " + medicine.getCompID();
        if (companyService != null && currentUser != null && currentUser.getRole() == Role.ADMIN)
        {
            try
            {
                Company c = companyService.getCompanyByID(currentUser, new Company(medicine.getCompID(), null));
                if (c != null)
                {
                    companyName = c.getCompName();
                }
            }
            catch (Exception ignored) {}
        }

        System.out.println("ID: " + medicine.getMedicineID() + " | Name: " + medicine.getMedName() + " | Quantity: " + medicine.getQuantity() + " | Expiry: " + medicine.getExpiryDate() + " | Minimum Stock: " + medicine.getMinimumStock() + " | Company: " + companyName);
    }

    public static void printPurchase(Purchase purchase, CompanyService companyService, User currentUser)
    {
        String companyName = "ID: " + purchase.getCompID();
        if (companyService != null && currentUser != null && currentUser.getRole() == Role.ADMIN)
        {
            try
            {
                Company c = companyService.getCompanyByID(currentUser, new Company(purchase.getCompID(), null));
                if (c != null)
                {
                    companyName = c.getCompName();
                }
            }
            catch (Exception ignored) {}
        }

        System.out.println("ID: " + purchase.getPurID() + " | Company: " + companyName + " | Date: " + purchase.getPurDate());
    }

    public static int readInt(Scanner sc, String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.isEmpty())
            {
                System.out.println("Input cannot be empty. Please enter a valid number.");
                continue;
            }
            try
            {
                return Integer.parseInt(input);
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static int readPositiveInt(Scanner sc, String prompt, String errorMsg)
    {
        while (true)
        {
            int val = readInt(sc, prompt);
            if (val > 0)
            {
                return val;
            }
            System.out.println(errorMsg != null ? errorMsg : "Value must be greater than 0.");
        }
    }

    public static int readNonNegativeInt(Scanner sc, String prompt, String errorMsg)
    {
        while (true)
        {
            int val = readInt(sc, prompt);
            if (val >= 0)
            {
                return val;
            }
            System.out.println(errorMsg != null ? errorMsg : "Value cannot be negative.");
        }
    }

    public static double readDouble(Scanner sc, String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.isEmpty())
            {
                System.out.println("Input cannot be empty. Please enter a valid number.");
                continue;
            }
            try
            {
                return Double.parseDouble(input);
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static double readPositiveDouble(Scanner sc, String prompt, String errorMsg)
    {
        while (true)
        {
            double val = readDouble(sc, prompt);
            if (val > 0)
            {
                return val;
            }
            System.out.println(errorMsg != null ? errorMsg : "Value must be greater than 0.");
        }
    }

    public static LocalDate readDate(Scanner sc, String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.isEmpty())
            {
                System.out.println("Date cannot be empty. Please enter a valid date (YYYY-MM-DD).");
                continue;
            }
            try
            {
                return LocalDate.parse(input);
            }
            catch (DateTimeParseException e)
            {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }

    public static LocalDate readExpiryDate(Scanner sc, String prompt)
    {
        while (true)
        {
            LocalDate date = readDate(sc, prompt);
            if (!date.isAfter(LocalDate.now()))
            {
                System.out.println("Medicine expiration date should be after today.");
                continue;
            }
            return date;
        }
    }

    public static Role readRole(Scanner sc, String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            String input = sc.nextLine().trim().toUpperCase();
            if (input.isEmpty())
            {
                System.out.println("Role cannot be empty. Must be ADMIN or PHARMACIST.");
                continue;
            }
            try
            {
                return Role.valueOf(input);
            }
            catch (IllegalArgumentException e)
            {
                System.out.println("Invalid role. Role must be ADMIN or PHARMACIST.");
            }
        }
    }

    public static String readNonEmptyString(Scanner sc, String prompt, String fieldName)
    {
        while (true)
        {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (!input.isEmpty())
            {
                return input;
            }
            System.out.println(fieldName + " cannot be empty. Please enter a valid value.");
        }
    }
}