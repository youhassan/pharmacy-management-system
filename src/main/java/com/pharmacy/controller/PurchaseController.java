package com.pharmacy.controller;

import com.pharmacy.dto.PurchaseItemEntryDTO;
import com.pharmacy.dto.PurchaseRequestDTO;
import com.pharmacy.model.Company;
import com.pharmacy.model.Purchase;
import com.pharmacy.model.User;
import com.pharmacy.repository.UserRepository;
import com.pharmacy.service.PurchaseService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final UserRepository userRepository;

    public PurchaseController(PurchaseService purchaseService, UserRepository userRepository) {
        this.purchaseService = purchaseService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<String> addPurchase(@RequestParam(required = false) String currentUsername, @RequestBody PurchaseRequestDTO request) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;

        List<PurchaseService.PurchaseItemEntry> serviceItems = new ArrayList<>();
        if (request.getItems() != null) {
            for (PurchaseItemEntryDTO dto : request.getItems()) {
                if (dto.isNewMedicine() || dto.getExpiryDate() != null) {
                    serviceItems.add(new PurchaseService.PurchaseItemEntry(dto.getMedicineName(), dto.getQuantityPur(), dto.getUnitCost(), dto.getExpiryDate(), dto.getMinimumStock()));
                } else {
                    serviceItems.add(new PurchaseService.PurchaseItemEntry(dto.getMedicineName(), dto.getQuantityPur(), dto.getUnitCost()));
                }
            }
        }

        purchaseService.addPurchase(currentUser, request.getCompanyName(), request.getPurDate(), serviceItems);
        return ResponseEntity.status(HttpStatus.CREATED).body("Purchase added successfully");
    }

    @GetMapping
    public ResponseEntity<List<Purchase>> getAllPurchases(@RequestParam(required = false) String currentUsername) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        return ResponseEntity.ok(purchaseService.getAllPurchases(currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable int id, @RequestParam(required = false) String currentUsername) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        Purchase p = new Purchase();
        p.setPurID(id);
        Purchase found = purchaseService.getPurchaseByID(currentUser, p);
        return found != null ? ResponseEntity.ok(found) : ResponseEntity.notFound().build();
    }

    @GetMapping("/company/name/{companyName}")
    public ResponseEntity<List<Purchase>> getPurchasesByCompanyName(@PathVariable String companyName, @RequestParam(required = false) String currentUsername) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        return ResponseEntity.ok(purchaseService.getPurchasesByCompanyName(currentUser, companyName));
    }

    @GetMapping("/company/id/{companyId}")
    public ResponseEntity<List<Purchase>> getPurchasesByCompanyId(@PathVariable int companyId, @RequestParam(required = false) String currentUsername) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        Company comp = new Company();
        comp.setCompID(companyId);
        return ResponseEntity.ok(purchaseService.getPurchasesByCompany(currentUser, comp));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Purchase>> getPurchasesByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam(required = false) String currentUsername) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        return ResponseEntity.ok(purchaseService.getPurchasesByDate(currentUser, date));
    }

    @GetMapping("/between")
    public ResponseEntity<List<Purchase>> getPurchasesBetweenDates(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate, @RequestParam(required = false) String currentUsername) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        return ResponseEntity.ok(purchaseService.getPurchasesBetweenDates(currentUser, fromDate, toDate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePurchase(@PathVariable int id, @RequestParam String companyName, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate updateDate, @RequestParam(required = false) String currentUsername) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        purchaseService.updatePurchase(currentUser, id, companyName, updateDate);
        return ResponseEntity.ok("Purchase updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePurchase(@PathVariable int id, @RequestParam(required = false) String currentUsername) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        Purchase p = new Purchase();
        p.setPurID(id);
        purchaseService.deletePurchase(currentUser, p);
        return ResponseEntity.ok("Purchase deleted successfully");
    }
}