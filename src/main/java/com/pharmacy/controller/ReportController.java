package com.pharmacy.controller;

import com.pharmacy.model.FinancialReport;
import com.pharmacy.model.User;
import com.pharmacy.repository.UserRepository;
import com.pharmacy.service.SaleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final SaleService saleService;
    private final UserRepository userRepository;

    public ReportController(SaleService saleService, UserRepository userRepository) {
        this.saleService = saleService;
        this.userRepository = userRepository;
    }

    @GetMapping("/financial")
    public ResponseEntity<FinancialReport> getFinancialReport(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate, @RequestParam(required = false) String currentUsername) {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        return ResponseEntity.ok(saleService.getFinancialReport(currentUser, fromDate, toDate));
    }
}
