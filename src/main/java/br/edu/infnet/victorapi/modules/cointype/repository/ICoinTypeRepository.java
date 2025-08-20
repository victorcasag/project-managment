package br.edu.infnet.victorapi.modules.cointype.repository;

import br.edu.infnet.victorapi.modules.cointype.entity.CoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICoinTypeRepository extends JpaRepository<CoinType, Integer> {

    Optional<CoinType> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT c FROM CoinType c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY c.name")
    List<CoinType> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT c FROM CoinType c WHERE c.isActive = true ORDER BY c.name")
    List<CoinType> findAllActive();

    @Query("SELECT c FROM CoinType c WHERE c.isActive = true ORDER BY c.name")
    Page<CoinType> findAllActive(Pageable pageable);

    @Query("SELECT c FROM CoinType c ORDER BY c.name")
    List<CoinType> findAllOrderByName();

    @Query("SELECT c FROM CoinType c ORDER BY c.name")
    Page<CoinType> findAllOrderByName(Pageable pageable);

    @Query("SELECT COUNT(c) FROM CoinType c WHERE c.isActive = true")
    Long countActive();

    Long countByName(String name);

    @Query("SELECT c FROM CoinType c WHERE c.symbol = :symbol ORDER BY c.name")
    List<CoinType> findBySymbol(@Param("symbol") String symbol);
}
