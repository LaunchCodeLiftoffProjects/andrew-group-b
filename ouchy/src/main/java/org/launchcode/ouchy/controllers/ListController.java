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
    public String providersListHome(Model model, @RequestParam(required = false) String sort) {
        model.addAttribute("title", "List of all providers");

        if(sort != null && sort.equals("Desc")) {
            model.addAttribute("providers", providerRepository.findByOrderByProviderNameDesc());
        } else {
            model.addAttribute("providers", providerRepository.findByOrderByProviderNameAsc());
        }

        return "listProviders";
    }

    @GetMapping("Services")
    public String servicesListHome(Model model, @RequestParam(required = false) String sort) {
        model.addAttribute("title", "List of all Services");
        model.addAttribute("providers",providerRepository.findByOrderByProviderNameAsc());

        if(sort != null && sort.equals("Desc")) {
            model.addAttribute("services", serviceRepository.findByOrderByServiceNameDesc());
        } else {
            model.addAttribute("services", serviceRepository.findByOrderByServiceNameAsc());
        }
        return "listServices";
    }

    @PostMapping("Search")
    public String processSearchTerm(Model model, @RequestParam String searchTerm) {
        model.addAttribute("title", "Search Results");
        model.addAttribute("providers", Provider.userSearch(searchTerm, providerRepository.findAll()));

        return "search";
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

        for(Provider provider : providers) {
            if(provider.getProviderName().toLowerCase().startsWith(term.toLowerCase())) {
                suggestions.add(provider.getProviderName());
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
