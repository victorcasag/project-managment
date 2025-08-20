package br.edu.infnet.victorapi.modules.departments.repository;

import br.edu.infnet.victorapi.modules.departments.entity.Departments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDepartmentRepository extends JpaRepository<Departments, Integer> {

    Optional<Departments> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT d FROM Departments d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY d.name")
    List<Departments> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT d FROM Departments d WHERE d.isActive = true ORDER BY d.name")
    List<Departments> findAllActive();

    @Query("SELECT d FROM Departments d WHERE d.isActive = true ORDER BY d.name")
    Page<Departments> findAllActive(Pageable pageable);

    @Query("SELECT d FROM Departments d ORDER BY d.name")
    List<Departments> findAllOrderByName();

    @Query("SELECT d FROM Departments d ORDER BY d.name")
    Page<Departments> findAllOrderByName(Pageable pageable);

    @Query("SELECT COUNT(d) FROM Departments d WHERE d.isActive = true")
    Long countActive();

    Long countByName(String name);
}
