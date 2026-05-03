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

    public ProjectService(ProjectRepository projectRepo,
                          ClientRepository clientRepo) {
        this.projectRepo = projectRepo;
        this.clientRepo = clientRepo;
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

        return projectRepo.save(project);
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