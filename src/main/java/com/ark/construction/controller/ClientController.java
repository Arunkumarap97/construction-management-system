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

        // LIST ALL CLIENTS
        @GetMapping
        public String listClients(Model model) {
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

        @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Working";
    }
    }