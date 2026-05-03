package com.ark.construction.controller;

import com.ark.construction.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("totalClients", service.totalClients());
        model.addAttribute("activeClients", service.activeClients());

        model.addAttribute("totalProjects", service.totalProjects());
        model.addAttribute("activeProjects", service.activeProjects());

        model.addAttribute("revenue", service.totalRevenue());
        model.addAttribute("expense", service.totalExpense());
        model.addAttribute("pending", service.pendingAmount());

        return "dashboard/dashboard";
    }
}