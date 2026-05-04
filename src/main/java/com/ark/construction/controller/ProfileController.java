package com.ark.construction.controller;

import com.ark.construction.service.AppUserService;
import com.ark.construction.service.BankAccountService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final BankAccountService bankService;
    private final AppUserService appUserService;

    public ProfileController(BankAccountService bankService, AppUserService appUserService) {
        this.bankService = bankService;
        this.appUserService = appUserService;
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {

        model.addAttribute("username", authentication.getName());
        model.addAttribute("banks", bankService.getAll());

        return "profile/profile";
    }

    @GetMapping("/profile/change-password")
    public String changePasswordPage() {
        return "profile/change-password";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {

        String result = appUserService.changePassword(
                authentication.getName(),
                currentPassword,
                newPassword,
                confirmPassword
        );

        if (!result.equals("success")) {
            redirectAttributes.addFlashAttribute("error", result);
            return "redirect:/profile/change-password";
        }

        redirectAttributes.addFlashAttribute("success", "Password changed successfully. Please login again.");
        return "redirect:/logout";
    }
}