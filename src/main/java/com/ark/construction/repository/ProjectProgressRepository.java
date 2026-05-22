package com.ark.construction.repository;

import com.ark.construction.entity.ProjectProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectProgressRepository extends JpaRepository<ProjectProgress, Long> {

    List<ProjectProgress> findByProject_IdOrderByWorkStage_DisplayOrderAsc(Long projectId);
    boolean existsByProject_Id(Long projectId);
}