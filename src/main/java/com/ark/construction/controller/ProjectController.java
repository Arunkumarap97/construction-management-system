package com.ark.construction.controller;

import com.ark.construction.entity.Expense;
import com.ark.construction.entity.Payment;
import com.ark.construction.entity.Project;
import com.ark.construction.entity.ProjectProgress;
import com.ark.construction.repository.ClientRepository;
import com.ark.construction.repository.ProjectTypeRepository;
import com.ark.construction.service.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final PaymentService paymentService;
    private final ClientRepository clientRepo;
    private final ExpenseService expenseService;
    private final PdfService pdfService;
    private final ProjectProgressService projectProgressService;
    private final ProjectTypeRepository projectTypeRepository;

    public ProjectController(ProjectService projectService,
                             PaymentService paymentService,
                             ClientRepository clientRepo, ExpenseService expenseService, PdfService pdfService,
                             ProjectProgressService projectProgressService, ProjectTypeRepository projectTypeRepository) {
        this.projectService = projectService;
        this.paymentService = paymentService;
        this.clientRepo = clientRepo;
        this.expenseService = expenseService;
        this.pdfService = pdfService;
        this.projectProgressService = projectProgressService;
        this.projectTypeRepository = projectTypeRepository;
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
        Double totalExpense = expenseService.getTotalExpenseByProject(id);

        Double pending = project.getTotalCost() - totalPaid;
        Double siteBalance = totalPaid - totalExpense;
        Double estimatedProfit = project.getTotalCost() - totalExpense;

        model.addAttribute("project", project);
        model.addAttribute("payments", paymentService.getPaymentsByProject(id));
        model.addAttribute("expenses", expenseService.getExpensesByProject(id));

        model.addAttribute("totalPaid", totalPaid);
        model.addAttribute("totalExpense", totalExpense);
        model.addAttribute("pending", pending);
        model.addAttribute("siteBalance", siteBalance);
        model.addAttribute("estimatedProfit", estimatedProfit);

        model.addAttribute("newPayment", new Payment());
        model.addAttribute("newExpense", new Expense());

        if (error != null) {
            model.addAttribute("error", error);
        }

        List<ProjectProgress> progressList =
                projectProgressService.getProgressByProject(id);
        model.addAttribute("progressList", progressList);


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
        model.addAttribute("projectTypes",
                projectTypeRepository.findByActiveTrueOrderByTypeNameAsc());
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
        model.addAttribute("projectTypes",
                projectTypeRepository.findByActiveTrueOrderByTypeNameAsc());

        return "project/project-form"; // reuse same form
    }

    @PostMapping("/{id}/progress")
    public String updateProgress(@PathVariable Long id,
                                 @RequestParam Integer progress) {

        projectService.updateProgress(id, progress);

        return "redirect:/projects/" + id;
    }

    @PostMapping("/{id}/expense")
    public String addExpenseToProject(@PathVariable Long id,
                                      @ModelAttribute("newExpense") Expense expense,
                                      RedirectAttributes redirectAttributes) {

        expense.setProjectId(id);
        expenseService.saveExpense(expense);

        redirectAttributes.addFlashAttribute("success", "Expense added successfully!");

        return "redirect:/projects/" + id;
    }

    @GetMapping("/payments/{paymentId}/receipt")
    @ResponseBody
    public ResponseEntity<byte[]> downloadReceipt(@PathVariable Long paymentId) {

        Payment payment = paymentService.getPaymentById(paymentId);

        byte[] pdf = pdfService.generatePaymentReceipt(payment);

        String projectName = payment.getProject().getProjectName();

// 🔥 sanitize filename
        projectName = projectName.replaceAll("[^a-zA-Z0-9]", "_");

// optional: shorten
        if (projectName.length() > 30) {
            projectName = projectName.substring(0, 30);
        }

        String fileName = projectName + "_Payment_" + payment.getPaymentDate() + ".pdf";

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    //

    @PostMapping("/progress/{progressId}/update")
    public String updateStageProgress(@PathVariable Long progressId,
                                      @RequestParam Integer progressPercentage,
                                      @RequestParam String status,
                                      @RequestParam(required = false) String remarks) {
        Long projectId = projectProgressService.updateStageProgress(
                progressId,
                progressPercentage,
                status,
                remarks
        );
        return "redirect:/projects/" + projectId;
    }
}