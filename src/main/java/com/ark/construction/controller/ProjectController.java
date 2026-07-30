package com.ark.construction.controller;

import com.ark.construction.entity.Expense;
import com.ark.construction.entity.Payment;
import com.ark.construction.entity.Project;
import com.ark.construction.repository.ClientRepository;
import com.ark.construction.service.ExpenseService;
import com.ark.construction.service.PaymentService;
import com.ark.construction.service.PdfService;
import com.ark.construction.service.ProjectService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private final ExpenseService expenseService;
    private final PdfService pdfService;

    public ProjectController(ProjectService projectService, PaymentService paymentService, ClientRepository clientRepo, ExpenseService expenseService, PdfService pdfService) {
        this.projectService = projectService;
        this.paymentService = paymentService;
        this.clientRepo = clientRepo;
        this.expenseService = expenseService;
        this.pdfService = pdfService;
    }

    // 🔹 LIST PROJECTS
    @GetMapping
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        return "project/projects";
    }

    // 🔹 PROJECT DETAIL PAGE
    @GetMapping("/{guid}")
    public String projectDetail(@PathVariable String guid, Model model, @RequestParam(required = false) String error) {

        Project project = projectService.getProject(guid);

        Long projectId = project.getId();

        Double totalPaid = paymentService.getTotalPaid(projectId);
        Double totalExpense = expenseService.getTotalExpenseByProject(projectId);

        Double pending = project.getTotalCost() - totalPaid;
        Double siteBalance = totalPaid - totalExpense;
        Double estimatedProfit = project.getTotalCost() - totalExpense;

        model.addAttribute("project", project);
        model.addAttribute("payments", paymentService.getPaymentsByProject(projectId));
        model.addAttribute("expenses", expenseService.getExpensesByProject(projectId));

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

        return "project/project-detail";
    }

    // 🔹 ADD PAYMENT
    @PostMapping("/{guid}/payment")
    public String addPayment(@PathVariable String guid, @ModelAttribute Payment payment, RedirectAttributes redirectAttributes) {

        payment.setId(null);

        Project project = projectService.getProject(guid);

        String result = paymentService.addPayment(project.getId(), payment);

        if (!result.equals("success")) {
            return "redirect:/projects/" + guid + "?error=" + result;
        }

        redirectAttributes.addFlashAttribute("success", "Payment added successfully!");

        return "redirect:/projects/" + guid;
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
            project.setClient(clientRepo.findById(project.getClientId()).orElse(null));
        }
        projectService.saveProject(project);
        return "redirect:/projects";
    }

    @GetMapping("/edit/{guid}")
    public String editProject(@PathVariable String guid, Model model) {

        Project project = projectService.getProject(guid);

        if (project.getClient() != null) {
            project.setClientId(project.getClient().getId());
        }

        model.addAttribute("project", project);
        model.addAttribute("clients", clientRepo.findByActiveTrue());

        return "project/project-form";
    }

    @PostMapping("/{guid}/progress")
    public String updateProgress(@PathVariable String guid, @RequestParam Integer progress) {

        projectService.updateProgress(guid, progress);

        return "redirect:/projects/" + guid;
    }

    @PostMapping("/{guid}/expense")
    public String addExpenseToProject(@PathVariable String guid, @ModelAttribute("newExpense") Expense expense, RedirectAttributes redirectAttributes) {

        Project project = projectService.getProject(guid);

        expense.setProjectId(project.getId());

        expenseService.saveExpense(expense);

        redirectAttributes.addFlashAttribute("success", "Expense added successfully!");

        return "redirect:/projects/" + guid;
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

        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=\"" + fileName + "\"").contentType(MediaType.APPLICATION_PDF).body(pdf);
    }

    //
    @GetMapping("/delete/{guid}")
    public String deleteProject(@PathVariable String guid) {

        projectService.deleteProject(guid);

        return "redirect:/projects";
    }
}