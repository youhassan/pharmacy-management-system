package com.pharmacy.controller;

import com.pharmacy.model.Company;
import com.pharmacy.model.Medicine;
import com.pharmacy.model.User;
import com.pharmacy.repository.UserRepository;
import com.pharmacy.service.MedicineService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {
    private MedicineService medicineService;
    private UserRepository userRepository;

    public MedicineController(MedicineService medicineService, UserRepository userRepository)
    {
        this.medicineService = medicineService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Medicine> getAllMedicines()
    {
        return medicineService.getAllMedicines();
    }

    @GetMapping("/{id}")
    public Medicine getMedicineByID(@PathVariable int id)
    {
        Medicine medicine = new Medicine();
        medicine.setMedicineID(id);
        return medicineService.getMedicineByID(medicine);
    }

    @GetMapping("/name/{name}")
    public Medicine getMedicineByName(@PathVariable String name)
    {
        Medicine medicine = new Medicine();
        medicine.setMedName(name);
        return medicineService.getMedicineByName(medicine);
    }

    @GetMapping("/company/{companyID}")
    public List<Medicine> getMedicinesByCompany(@PathVariable int companyID)
    {
        Company company = new Company();
        company.setCompID(companyID);

        return medicineService.getMedicinesByCompany(company);
    }

    @GetMapping("/company/name/{companyName}")
    public List<Medicine> getMedicinesByCompanyName(@PathVariable String companyName)
    {
        return medicineService.getMedicinesByCompanyName(companyName);
    }

    @PostMapping
    public void addMedicine(@RequestParam(required = false) String currentUsername, @RequestParam String companyName, @RequestBody Medicine medicine)
    {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        medicineService.addMedicine(currentUser, medicine, companyName);
    }

    @PutMapping
    public void updateMedicine(@RequestParam(required = false) String currentUsername, @RequestParam String oldMedicineName, @RequestParam String companyName, @RequestBody Medicine medicine)
    {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        medicineService.updateMedicine(currentUser, oldMedicineName, medicine, companyName);
    }

    @DeleteMapping
    public void deleteMedicine(@RequestParam(required = false) String currentUsername, @RequestBody Medicine medicine)
    {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        medicineService.deleteMedicine(currentUser, medicine);
    }

    @GetMapping("/low-stock")
    public List<Medicine> getLowStockMedicines(@RequestParam(required = false) String currentUsername)
    {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        return medicineService.getLowStockMedicines(currentUser);
    }

    @GetMapping("/expired")
    public List<Medicine> getExpiredMedicines(@RequestParam(required = false) String currentUsername)
    {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        return medicineService.getExpiredMedicines(currentUser);
    }

    @GetMapping("/expiring-soon")
    public List<Medicine> getExpiringSoonMedicines(@RequestParam(required = false) String currentUsername)
    {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        return medicineService.getExpiringSoonMedicines(currentUser);
    }
}
