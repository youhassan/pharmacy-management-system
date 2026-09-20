package com.pharmacy.repository;

import com.pharmacy.model.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Integer> {

    List<PurchaseItem> findByPurID(int purID);

    List<PurchaseItem> findByMedicineID(int medicineID);

}
