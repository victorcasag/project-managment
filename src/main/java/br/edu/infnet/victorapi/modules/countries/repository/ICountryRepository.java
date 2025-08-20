package br.edu.infnet.victorapi.modules.countries.repository;

import br.edu.infnet.victorapi.modules.countries.entity.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICountryRepository extends JpaRepository<Country, Integer> {

    Optional<Country> findByCode2(String code2);

    Optional<Country> findByCode3(String code3);

    boolean existsByCode2(String code2);

    boolean existsByCode3(String code3);

    @Query("SELECT c FROM Country c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY c.name")
    List<Country> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT c FROM Country c WHERE c.isActive = true ORDER BY c.name")
    List<Country> findAllActive();

    @Query("SELECT c FROM Country c WHERE c.isActive = true ORDER BY c.name")
    Page<Country> findAllActive(Pageable pageable);

    @Query("SELECT c FROM Country c ORDER BY c.name")
    List<Country> findAllOrderByName();

    @Query("SELECT c FROM Country c ORDER BY c.name")
    Page<Country> findAllOrderByName(Pageable pageable);

    @Query("SELECT COUNT(c) FROM Country c WHERE c.isActive = true")
    Long countActive();

    Long countByName(String name);

    @Query("SELECT c FROM Country c WHERE c.currencyCode = :currencyCode ORDER BY c.name")
    List<Country> findByCurrencyCode(@Param("currencyCode") String currencyCode);
}
