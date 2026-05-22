package com.ark.construction.service;

import com.ark.construction.entity.Project;
import com.ark.construction.entity.ProjectProgress;
import com.ark.construction.entity.WorkStage;
import com.ark.construction.repository.ProjectProgressRepository;
import com.ark.construction.repository.WorkStageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectProgressService {

    private final WorkStageRepository workStageRepository;
    private final ProjectProgressRepository projectProgressRepository;

    public ProjectProgressService(WorkStageRepository workStageRepository,
                                  ProjectProgressRepository projectProgressRepository) {
        this.workStageRepository = workStageRepository;
        this.projectProgressRepository = projectProgressRepository;
    }

    public void createDefaultProgressForProject(Project project) {

        if (projectProgressRepository.existsByProject_Id(project.getId())) {
            return;
        }

        List<WorkStage> stages =
                workStageRepository
                        .findByProjectType_IdAndActiveTrueOrderByDisplayOrderAsc(
                                project.getProjectType().getId()
                        );

        Integer totalFloors = project.getNumberOfFloors();

        if (totalFloors == null || totalFloors < 1) {
            totalFloors = 1;
        }

        for (WorkStage stage : stages) {

            // NON FLOOR BASED
            if (Boolean.FALSE.equals(stage.getFloorBased())) {

                ProjectProgress progress = ProjectProgress.builder()
                        .project(project)
                        .workStage(stage)
                        .progressPercentage(0)
                        .status("PENDING")
                        .build();

                projectProgressRepository.save(progress);

            }

            // FLOOR BASED
            else {

                for (int floor = 1; floor <= totalFloors; floor++) {

                    String floorName;

                    if (floor == 1) {
                        floorName = "Ground Floor";
                    } else {
                        floorName = floor - 1 + " Floor";
                    }

                    ProjectProgress progress = ProjectProgress.builder()
                            .project(project)
                            .workStage(stage)
                            .floorNumber(floor)
                            .floorName(floorName)
                            .progressPercentage(0)
                            .status("PENDING")
                            .build();

                    projectProgressRepository.save(progress);
                }
            }
        }
    }
    //

    public List<ProjectProgress> getProgressByProject(Long projectId) {
        return projectProgressRepository
                .findByProject_IdOrderByWorkStage_DisplayOrderAsc(projectId);
    }
    //

    public Long updateStageProgress(Long progressId,
                                    Integer progressPercentage,
                                    String status,
                                    String remarks) {

        ProjectProgress progress = projectProgressRepository.findById(progressId)
                .orElseThrow(() -> new RuntimeException("Progress not found"));

        if (progressPercentage == null || progressPercentage < 0 || progressPercentage > 100) {
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        }

        progress.setProgressPercentage(progressPercentage);
        progress.setStatus(status);
        progress.setRemarks(remarks);

        projectProgressRepository.save(progress);

        return progress.getProject().getId();
    }
}