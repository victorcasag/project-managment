package br.edu.infnet.victorapi.modules.contract.repository;

import br.edu.infnet.victorapi.modules.contract.entity.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IContractRepository extends JpaRepository<Contract, Integer> {

    Optional<Contract> findByContractNumber(String contractNumber);

    boolean existsByContractNumber(String contractNumber);

    @Query("SELECT c FROM Contract c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY c.name")
    List<Contract> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT c FROM Contract c WHERE c.isActive = true ORDER BY c.name")
    List<Contract> findAllActive();

    @Query("SELECT c FROM Contract c WHERE c.isActive = true ORDER BY c.name")
    Page<Contract> findAllActive(Pageable pageable);

    @Query("SELECT c FROM Contract c ORDER BY c.name")
    List<Contract> findAllOrderByName();

    @Query("SELECT c FROM Contract c ORDER BY c.name")
    Page<Contract> findAllOrderByName(Pageable pageable);

    @Query("SELECT COUNT(c) FROM Contract c WHERE c.isActive = true")
    Long countActive();

    Long countByName(String name);

    @Query("SELECT c FROM Contract c WHERE c.clientSupplierId = :clientSupplierId ORDER BY c.name")
    List<Contract> findByClientSupplierId(@Param("clientSupplierId") Integer clientSupplierId);

    @Query("SELECT c FROM Contract c WHERE c.coinTypeId = :coinTypeId ORDER BY c.name")
    List<Contract> findByCoinTypeId(@Param("coinTypeId") Integer coinTypeId);

    @Query("SELECT c FROM Contract c WHERE c.startDate >= :startDate ORDER BY c.startDate")
    List<Contract> findByStartDateAfter(@Param("startDate") LocalDate startDate);

    @Query("SELECT c FROM Contract c WHERE c.endDate <= :endDate ORDER BY c.endDate")
    List<Contract> findByEndDateBefore(@Param("endDate") LocalDate endDate);

    @Query("SELECT c FROM Contract c WHERE c.value BETWEEN :minValue AND :maxValue ORDER BY c.value")
    List<Contract> findByValueBetween(@Param("minValue") BigDecimal minValue, @Param("maxValue") BigDecimal maxValue);

    @Query("SELECT SUM(c.value) FROM Contract c WHERE c.isActive = true")
    BigDecimal sumTotalActiveValue();
}
