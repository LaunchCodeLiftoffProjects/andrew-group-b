package org.launchcode.ouchy.controllers;

import org.launchcode.ouchy.models.Data.ServiceRepository;
import org.launchcode.ouchy.models.Provider;
import org.launchcode.ouchy.models.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping("AddService")
    public String AddServiceHome(Model model) {
        model.addAttribute("title", "Add New Service");
        model.addAttribute("service", new Service());

        return "addService";
    }

    @PostMapping("AddService")
    public String processAddService(Model model, @RequestParam String serviceName, @RequestParam String serviceDescription) {

        if(!serviceName.isEmpty() || !serviceDescription.isEmpty()) {
            Service newService = new Service(serviceName, serviceDescription);
            serviceRepository.save(newService);
        }

        model.addAttribute("title", "Add New Service");
        model.addAttribute("service", new Service());

        return "redirect:AddService";
    }
}
