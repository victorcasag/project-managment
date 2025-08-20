package br.edu.infnet.victorapi.modules.area.repository;

import br.edu.infnet.victorapi.modules.area.entity.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAreaRepository extends JpaRepository<Area, Integer> {

    @Query("SELECT a FROM Area a WHERE a.isActive = true")
    List<Area> findAllActive();

    @Query("SELECT a FROM Area a WHERE a.isActive = true")
    Page<Area> findAllActive(Pageable pageable);

    @Query("SELECT a FROM Area a WHERE a.id = :id AND a.isActive = true")
    Optional<Area> findByIdAndActive(@Param("id") Integer id);

    @Query("SELECT a FROM Area a WHERE a.code = :code AND a.isActive = true")
    Optional<Area> findByCodeAndActive(@Param("code") String code);

    @Query("SELECT a FROM Area a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%')) AND a.isActive = true")
    List<Area> findByNameContainingIgnoreCaseAndActive(@Param("name") String name);

    boolean existsByCodeAndIsActiveTrue(String code);

    boolean existsByCodeAndIdNotAndIsActiveTrue(String code, Integer id);
}
