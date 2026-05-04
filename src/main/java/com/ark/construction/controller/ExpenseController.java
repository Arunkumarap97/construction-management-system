package com.ark.construction.controller;

import com.ark.construction.entity.Expense;
import com.ark.construction.service.ExpenseService;
import com.ark.construction.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final ProjectService projectService;

    public ExpenseController(ExpenseService expenseService,
                             ProjectService projectService) {
        this.expenseService = expenseService;
        this.projectService = projectService;
    }

    // LIST
    @GetMapping
    public String listExpenses(Model model) {
        model.addAttribute("expenses", expenseService.getAllExpenses());
        model.addAttribute("totalExpense", expenseService.totalExpenseAll());
        return "expense/expenses";
    }

    // NEW FORM
    @GetMapping("/new")
    public String showExpenseForm(Model model) {
        model.addAttribute("expense", new Expense());
        model.addAttribute("projects", projectService.getAllProjects());
        return "expense/expense-form";
    }

    // SAVE
    @PostMapping
    public String saveExpense(@ModelAttribute Expense expense) {
        expenseService.saveExpense(expense);
        return "redirect:/expenses";
    }

    // EDIT FORM
    @GetMapping("/edit/{id}")
    public String editExpense(@PathVariable Long id, Model model) {
        Expense expense = expenseService.getExpenseById(id);
        if (expense.getProject() != null) {
            expense.setProjectId(expense.getProject().getId());
        }
        model.addAttribute("expense", expense);
        model.addAttribute("projects", projectService.getAllProjects());

        return "expense/expense-form";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return "redirect:/expenses";
    }
}