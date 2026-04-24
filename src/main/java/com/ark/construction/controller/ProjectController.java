package com.ark.construction.controller;

import com.ark.construction.entity.Payment;
import com.ark.construction.entity.Project;
import com.ark.construction.repository.ClientRepository;
import com.ark.construction.service.PaymentService;
import com.ark.construction.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final PaymentService paymentService;
    private final ClientRepository clientRepo;

    public ProjectController(ProjectService projectService,
                             PaymentService paymentService,
                             ClientRepository clientRepo) {
        this.projectService = projectService;
        this.paymentService = paymentService;
        this.clientRepo = clientRepo;
    }

    // 🔹 LIST PROJECTS
    @GetMapping
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        return "project/projects";
    }

    // 🔹 PROJECT DETAIL PAGE
    @GetMapping("/{id}")
    public String projectDetail(@PathVariable Long id,
                                Model model,
                                @RequestParam(required = false) String error) {

        Project project = projectService.getProject(id);

        Double totalPaid = paymentService.getTotalPaid(id);
        Double pending = project.getTotalCost() - totalPaid;

        model.addAttribute("project", project);
        model.addAttribute("payments", paymentService.getPaymentsByProject(id));
        model.addAttribute("totalPaid", totalPaid);
        model.addAttribute("pending", pending);
        model.addAttribute("newPayment", new Payment());

        // 🔴 error handling
        if (error != null) {
            model.addAttribute("error", error);
        }

        return "project/project-detail";
    }

    // 🔹 ADD PAYMENT
    @PostMapping("/{id}/payment")
    public String addPayment(@PathVariable Long id,
                             @ModelAttribute Payment payment, RedirectAttributes redirectAttributes) {

        // 🔥 VERY IMPORTANT FIX (prevents stale entity error)
        payment.setId(null);
        String result = paymentService.addPayment(id, payment);

        if (!result.equals("success")) {
            return "redirect:/projects/" + id + "?error=" + result;
        }

        // ✅ SUCCESS MESSAGE
        redirectAttributes.addFlashAttribute("success", "Payment added successfully!");

        return "redirect:/projects/" + id;
    }

    // 🔹 SHOW PROJECT FORM
    @GetMapping("/new")
    public String showProjectForm(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("clients", clientRepo.findByActiveTrue());
        return "project/project-form";
    }

    // 🔹 SAVE PROJECT
    @PostMapping
    public String saveProject(@ModelAttribute Project project) {

        // 🔥 set client from ID
        if (project.getClientId() != null) {
            project.setClient(
                    clientRepo.findById(project.getClientId()).orElse(null)
            );
        }
        projectService.saveProject(project);
        return "redirect:/projects";
    }

    @GetMapping("/edit/{id}")
    public String editProject(@PathVariable Long id, Model model) {

        Project project = projectService.getProject(id);

        // 🔥 IMPORTANT
        if (project.getClient() != null) {
            project.setClientId(project.getClient().getId());
        }

        model.addAttribute("project", project);
        model.addAttribute("clients", clientRepo.findByActiveTrue());

        return "project/project-form"; // reuse same form
    }

    @PostMapping("/{id}/progress")
    public String updateProgress(@PathVariable Long id,
                                 @RequestParam Integer progress) {

        projectService.updateProgress(id, progress);

        return "redirect:/projects/" + id;
    }
}