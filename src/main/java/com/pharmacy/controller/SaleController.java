package com.pharmacy.controller;

import com.pharmacy.dto.SaleItemEntryDTO;
import com.pharmacy.dto.SaleRequestDTO;
import com.pharmacy.model.Sale;
import com.pharmacy.model.User;
import com.pharmacy.repository.UserRepository;
import com.pharmacy.service.SaleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;
    private final UserRepository userRepository;

    public SaleController(SaleService saleService, UserRepository userRepository) {
        this.saleService = saleService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<String> addSale(@RequestParam(required = false) String currentUsername, @RequestBody SaleRequestDTO request) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;

        List<SaleService.SaleItemEntry> serviceItems = new ArrayList<>();
        if (request.getItems() != null) {
            for (SaleItemEntryDTO dto : request.getItems()) {
                serviceItems.add(new SaleService.SaleItemEntry(dto.getMedicineName(), dto.getQuantitySold(), dto.getUnitPrice()));
            }
        }

        saleService.addSale(currentUser, request.getSaleDate(), serviceItems);
        return ResponseEntity.status(HttpStatus.CREATED).body("Sale recorded successfully");
    }

    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales(@RequestParam(required = false) String currentUsername) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        return ResponseEntity.ok(saleService.getAllSales(currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable int id, @RequestParam(required = false) String currentUsername) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        Sale s = new Sale();
        s.setSaleID(id);
        Sale found = saleService.getSaleByID(currentUser, s);
        return found != null ? ResponseEntity.ok(found) : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Sale>> getSalesByUser(@PathVariable String username, @RequestParam(required = false) String currentUsername) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        User targetUser = new User();
        targetUser.setUsername(username);
        return ResponseEntity.ok(saleService.getSalesByUser(currentUser, targetUser));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Sale>> getSalesByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam(required = false) String currentUsername) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        return ResponseEntity.ok(saleService.getSalesByDate(currentUser, date));
    }

    @GetMapping("/between")
    public ResponseEntity<List<Sale>> getSalesBetweenDates(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate, @RequestParam(required = false) String currentUsername) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        return ResponseEntity.ok(saleService.getSalesBetweenDates(currentUser, fromDate, toDate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSale(@PathVariable int id, @RequestParam(required = false) String currentUsername) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        Sale s = new Sale();
        s.setSaleID(id);
        saleService.deleteSale(currentUser, s);
        return ResponseEntity.ok("Sale deleted successfully");
    }
}