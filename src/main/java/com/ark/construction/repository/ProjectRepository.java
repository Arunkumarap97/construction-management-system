package com.ark.construction.repository;

import com.ark.construction.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Long countByStatus(String status);

    @Query("""
            SELECT (p.totalCost - COALESCE(SUM(pay.amount),0))
            FROM Project p
            LEFT JOIN Payment pay ON pay.project.id = p.id
            WHERE p.id = :projectId
            GROUP BY p.totalCost
            """)
    Double pendingByProject(Long projectId);

    // Active Projects
    List<Project> findByActiveTrue();

    // Find by GUID (NEW)
    Optional<Project> findByGuidAndActiveTrue(String guid);

    // Optional: Find by GUID irrespective of active status
    Optional<Project> findByGuid(String guid);

}