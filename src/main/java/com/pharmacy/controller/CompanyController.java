package com.pharmacy.controller;

import com.pharmacy.model.Company;
import com.pharmacy.model.User;
import com.pharmacy.repository.UserRepository;
import com.pharmacy.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final UserRepository userRepository;

    public CompanyController(CompanyService companyService, UserRepository userRepository) {
        this.companyService = companyService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies(@RequestParam(required = false) String currentUsername) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        return ResponseEntity.ok(companyService.getAllCompanies(currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable int id, @RequestParam(required = false) String currentUsername) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        Company comp = new Company();
        comp.setCompID(id);
        Company found = companyService.getCompanyByID(currentUser, comp);
        return found != null ? ResponseEntity.ok(found) : ResponseEntity.notFound().build();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Company> getCompanyByName(@PathVariable String name, @RequestParam(required = false) String currentUsername) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        Company comp = new Company();
        comp.setCompName(name);
        Company found = companyService.getCompanyByName(currentUser, comp);
        return found != null ? ResponseEntity.ok(found) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<String> addCompany(@RequestParam(required = false) String currentUsername, @RequestBody Company company) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        companyService.addCompany(currentUser, company);
        return ResponseEntity.status(HttpStatus.CREATED).body("Company added successfully");
    }

    @PutMapping
    public ResponseEntity<String> updateCompany(@RequestParam(required = false) String currentUsername, @RequestParam String oldCompanyName, @RequestBody Company company) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        companyService.updateCompany(currentUser, oldCompanyName, company);
        return ResponseEntity.ok("Company updated successfully");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCompany(@RequestParam(required = false) String currentUsername, @RequestParam String companyName) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        Company company = new Company();
        company.setCompName(companyName);
        companyService.deleteCompany(currentUser, company);
        return ResponseEntity.ok("Company deleted successfully");
    }
}
