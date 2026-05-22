package com.ark.construction.entity;

import com.ark.construction.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectProgress extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "work_stage_id")
    private WorkStage workStage;

    private Integer progressPercentage = 0;

    private String status;
    // PENDING, IN_PROGRESS, COMPLETED, ON_HOLD

    private LocalDate startDate;

    private LocalDate completedDate;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    private Integer floorNumber;
    private String floorName;
}