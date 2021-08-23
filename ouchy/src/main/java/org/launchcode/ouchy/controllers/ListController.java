package org.launchcode.ouchy.controllers;

import org.launchcode.ouchy.models.Data.ProviderRepository;
import org.launchcode.ouchy.models.Data.ProviderServiceDTORepository;
import org.launchcode.ouchy.models.Data.ServiceRepository;
import org.launchcode.ouchy.models.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ListController {

    @Autowired
    ProviderRepository providerRepository;

    @Autowired
    ProviderServiceDTORepository providerServiceDTORepository;

    @Autowired
    ServiceRepository serviceRepository;

    @GetMapping("Providers")
    public String providersListHome(Model model) {
        model.addAttribute("title", "List of all providers");
        model.addAttribute("providers", providerRepository.findAll());

        return "listProviders";
    }

    @GetMapping("Services")
    public String servicesListHome(Model model) {
        model.addAttribute("title", "List of all Services");
        model.addAttribute("services", serviceRepository.findAll());
        model.addAttribute("providers",providerRepository.findAll());

        return "listServices";
    }

    @PostMapping("Search")
    public String processSearchTerm(Model model, @RequestParam String searchTerm) {
        model.addAttribute("title", "Search Results");
        model.addAttribute("providers", Provider.userSearch(searchTerm, providerRepository.findAll()));

        return "listProviders";
    }

    @GetMapping("Mission")
    public String missionImpossible(Model model) {
        model.addAttribute("title", "Our Mission");

        return "mission";
    }
}
