package com.ark.construction.entity;

import com.ark.construction.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkStage extends BaseEntity {

    private String stageName;

    private String description;

    private Integer displayOrder;

    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "project_type_id")
    private ProjectType projectType;

    private Boolean floorBased = false;
}