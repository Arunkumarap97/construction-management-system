package com.ark.construction.service;

import com.ark.construction.entity.Client;
import com.ark.construction.entity.Project;
import com.ark.construction.repository.ClientRepository;
import com.ark.construction.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepo;
    private final ClientRepository clientRepo;

    public ProjectService(ProjectRepository projectRepo, ClientRepository clientRepo) {
        this.projectRepo = projectRepo;
        this.clientRepo = clientRepo;
    }

    // Active Projects Only
    public List<Project> getAllProjects() {
        return projectRepo.findByActiveTrue();
    }

    // Find Project by GUID
    public Project getProject(String guid) {
        return projectRepo.findByGuidAndActiveTrue(guid).orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public Project saveProject(Project project) {

        if (project.getClientId() != null) {
            Client client = clientRepo.findById(project.getClientId()).orElse(null);
            project.setClient(client);
        }

        return projectRepo.save(project);
    }

    // Update Progress using GUID
    public void updateProgress(String guid, Integer progress) {

        Project project = getProject(guid);

        if (progress == null) {
            throw new IllegalArgumentException("Progress cannot be null");
        }

        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        }

        project.setProgressPercent(progress);

        projectRepo.save(project);
    }

    // Soft Delete using GUID
    public void deleteProject(String guid) {

        Project project = getProject(guid);

        project.setActive(false);

        projectRepo.save(project);
    }

    //
    public Project getActiveProject(String guid) {
        return projectRepo.findByGuidAndActiveTrue(guid).orElseThrow(() -> new RuntimeException("Project not found"));
    }

}