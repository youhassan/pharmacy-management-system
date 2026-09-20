package com.pharmacy.repository;

import com.pharmacy.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.*;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    List<Purchase> findByCompID(int compID);

    List<Purchase> findByPurDate(LocalDate purDate);

    List<Purchase> findByPurDateBetween(LocalDate fromDate, LocalDate toDate);

}
