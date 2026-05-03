    package com.ark.construction.controller;

    import com.ark.construction.entity.Client;
    import com.ark.construction.service.ClientService;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    @Controller
    @RequestMapping("/clients")
    public class ClientController {

        private final ClientService service;

        public ClientController(ClientService service) {
            this.service = service;
        }

        // LIST ALL ACTIVE CLIENTS
        @GetMapping
        public String listActiveClients(Model model) {
            model.addAttribute("clients", service.getAllActiveClients());
            return "clients";
        }

        // LIST ALL CLIENTS
        @GetMapping("/all")
        public String listAllClients(Model model) {
            model.addAttribute("clients", service.getAllClients());
            return "clients";
        }

        // SHOW FORM
        @GetMapping("/new")
        public String showForm(Model model) {
            model.addAttribute("client", new Client());
            return "client-form";
        }

        // SAVE CLIENT
        @PostMapping
        public String saveClient(@ModelAttribute Client client) {
            service.saveClient(client);
            return "redirect:/clients";
        }

        // DELETE CLIENT
        @GetMapping("/delete/{id}")
        public String deleteClient(@PathVariable Long id) {
            service.deleteClient(id);
            return "redirect:/clients";
        }

        @GetMapping("/{id}")
        public String clientDetail(@PathVariable Long id, Model model) {

            Client client = service.getClientById(id);

            model.addAttribute("client", client);
            model.addAttribute("projects", service.getProjectsOfClient(id));
            model.addAttribute("totalProjects", service.totalProjects(id));
            model.addAttribute("totalProjectCost", service.totalProjectCost(id));
            model.addAttribute("totalReceived", service.totalReceived(id));
            model.addAttribute("pendingAmount", service.pendingAmount(id));
            model.addAttribute("payments", service.getAllPaymentsOfClient(id));

            return "client/client-detail";
        }

    }