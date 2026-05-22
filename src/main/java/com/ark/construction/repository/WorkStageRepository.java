package com.ark.construction.repository;

import com.ark.construction.entity.WorkStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkStageRepository extends JpaRepository<WorkStage, Long> {

    List<WorkStage> findByProjectType_IdAndActiveTrueOrderByDisplayOrderAsc(Long projectTypeId);
}