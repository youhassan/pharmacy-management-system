package com.pharmacy.repository;

import com.pharmacy.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Integer>{
    Medicine findByMedName(String medName);
    List<Medicine> findByCompID(int companyID);
    @Query("SELECT m FROM Medicine m WHERE m.quantity <= m.minimumStock")
    List<Medicine> findLowStockMedicines();
    @Query("SELECT m FROM Medicine m WHERE m.expiryDate < :today")
    List<Medicine> findExpiredMedicines(@Param("today") LocalDate today);
    @Query("SELECT m FROM Medicine m WHERE m.expiryDate > :today AND m.expiryDate <= :thirtyDaysLater")
    List<Medicine> findExpiringSoonMedicines(@Param("today") LocalDate today, @Param("thirtyDaysLater") LocalDate thirtyDaysLater);
}