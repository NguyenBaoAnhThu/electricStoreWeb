package org.example.electricstore.repository;

import org.example.electricstore.model.Product;
import org.example.electricstore.model.WareHouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface IWareHouseRepository extends JpaRepository<WareHouse, Integer> {
    @Query("""
    SELECT w FROM WareHouse w
    WHERE (:importDate IS NULL OR w.importDate = :importDate)
      AND (:brand IS NULL OR :brand = '' OR LOWER(w.product.brand.brandName) LIKE LOWER(CONCAT('%', :brand, '%')))
      AND (:statusStock IS NULL OR :statusStock = 0 OR 
           (:statusStock = 1 AND w.product.stock = 0) OR 
           (:statusStock = 2 AND w.product.stock > 0 AND w.product.stock <= 5) OR 
           (:statusStock = 3 AND w.product.stock > 5))
      AND (:productCode IS NULL OR :productCode = '' OR LOWER(w.product.productCode) LIKE LOWER(CONCAT('%', :productCode, '%')))
      AND (:productName IS NULL OR :productName = '' OR LOWER(w.product.name) LIKE LOWER(CONCAT('%', :productName, '%')))
""")
    Page<WareHouse> findAllWithFilters(@Param("importDate") LocalDate importDate,
                                       @Param("brand") String brand,
                                       @Param("statusStock") Integer statusStock,
                                       @Param("productCode") String productCode,
                                       @Param("productName") String productName,
                                       Pageable pageable);


    @Query("SELECT w FROM WareHouse w WHERE w.product.productID = :productId ORDER BY w.importDate DESC")
    List<WareHouse> findByProductIdOrderByImportDateDesc(@Param("productId") Integer productId);
}
