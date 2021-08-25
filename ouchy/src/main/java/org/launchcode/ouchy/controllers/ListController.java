package org.launchcode.ouchy.controllers;

import org.launchcode.ouchy.models.Data.ProviderRepository;
import org.launchcode.ouchy.models.Data.ProviderServiceDTORepository;
import org.launchcode.ouchy.models.Data.ServiceRepository;
import org.launchcode.ouchy.models.Provider;
import org.launchcode.ouchy.models.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping("searchTermAutocomplete")
    @ResponseBody
    public List<String> searchTermAutocomplete(@RequestParam String term) {
        List<String> suggestions = new ArrayList<String>();

        Iterable<Provider> providers = providerRepository.findAll();
        Iterable<Service> services = serviceRepository.findAll();

        System.out.println("Search Term = " + term);

        for(Provider provider : providers) {
            if(provider.getProviderName().toLowerCase().startsWith(term.toLowerCase())) {
                suggestions.add(provider.getProviderName());
                System.out.println("Suggestion Added:  " + provider.getProviderName());
            }
        }

        for(Service service : services) {
            if(service.getServiceName().toLowerCase().startsWith(term.toLowerCase())) {
                suggestions.add(service.getServiceName());
            }
        }

        return suggestions;
    }
}
