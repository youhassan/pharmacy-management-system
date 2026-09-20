package com.pharmacy.repository;

import com.pharmacy.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Integer> {

    List<Sale> findByUsername(String username);

    List<Sale> findBySaleDate(LocalDate saleDate);

    List<Sale> findBySaleDateBetweenOrderBySaleDateAsc(LocalDate fromDate, LocalDate toDate);

    List<Sale> findBySaleDateBetween(LocalDate fromDate, LocalDate toDate);

}
