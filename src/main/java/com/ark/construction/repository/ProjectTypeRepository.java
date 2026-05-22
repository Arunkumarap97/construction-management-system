package com.ark.construction.repository;

import com.ark.construction.entity.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectTypeRepository extends JpaRepository<ProjectType, Long> {

    List<ProjectType> findByActiveTrueOrderByTypeNameAsc();
}