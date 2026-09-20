package com.pharmacy.repository;

import com.pharmacy.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface CompanyRepository extends JpaRepository<Company,Integer> {
    Company findByCompNameIgnoreCase(String compName);
}
