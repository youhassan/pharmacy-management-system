package com.pharmacy.repository;

import com.pharmacy.model.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SaleItemRepository extends JpaRepository<SaleItem, Integer> {

    List<SaleItem> findBySaleID(int saleID);

    List<SaleItem> findByMedicineID(int medicineID);

}
