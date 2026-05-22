package com.ark.construction.service;

import com.ark.construction.entity.Client;
import com.ark.construction.entity.Project;
import com.ark.construction.entity.ProjectType;
import com.ark.construction.repository.ClientRepository;
import com.ark.construction.repository.ProjectRepository;
import com.ark.construction.repository.ProjectTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepo;
    private final ClientRepository clientRepo;
    private final ProjectProgressService projectProgressService;
    private final ProjectTypeRepository projectTypeRepository;

    public ProjectService(ProjectRepository projectRepo,
                          ClientRepository clientRepo, ProjectProgressService projectProgressService, ProjectTypeRepository projectTypeRepository) {
        this.projectRepo = projectRepo;
        this.clientRepo = clientRepo;
        this.projectProgressService = projectProgressService;
        this.projectTypeRepository = projectTypeRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepo.findAll();
    }

    public Project getProject(Long id) {
        return projectRepo.findById(id).orElseThrow();
    }

    public Project saveProject(Project project) {

        // 🔥 map clientId → client
        if (project.getClientId() != null) {
            Client client = clientRepo.findById(project.getClientId()).orElse(null);
            project.setClient(client);
        }
        if (project.getProjectType() != null &&
                project.getProjectType().getId() != null) {

            ProjectType type = projectTypeRepository
                    .findById(project.getProjectType().getId())
                    .orElseThrow(() -> new RuntimeException("Project type not found"));

            project.setProjectType(type);
        }

        // save project
        Project savedProject = projectRepo.save(project);
        // create default progress stages
        projectProgressService.createDefaultProgressForProject(savedProject);
        return savedProject;
    }

    public void updateProgress(Long projectId, Integer progress) {

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // ✅ validation
        if (progress == null) {
            throw new IllegalArgumentException("Progress cannot be null");
        }

        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        }

        project.setProgressPercent(progress);

        projectRepo.save(project);
    }
}