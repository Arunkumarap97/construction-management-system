package com.ark.construction.controller;

import com.ark.construction.entity.BankAccount;
import org.springframework.ui.Model;
import com.ark.construction.service.BankAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile/banks")
public class BankAccountController {

    private final BankAccountService service;

    public BankAccountController(BankAccountService service) {
        this.service = service;
    }

    // LIST PAGE
    @GetMapping
    public String list(Model model) {
        model.addAttribute("banks", service.getAll());
        return "profile/bank-list";
    }

    // FORM
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("bank", new BankAccount());
        return "profile/bank-form";
    }

    // SAVE
    @PostMapping
    public String save(@ModelAttribute BankAccount bank) {
        service.save(bank);
        return "redirect:/profile/banks";
    }

    // EDIT
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("bank", service.get(id));
        return "profile/bank-form";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/profile/banks";
    }
}
