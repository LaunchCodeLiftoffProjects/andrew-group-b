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
import java.util.Collections;
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

    @GetMapping("FAQ")
    public String missionImpossible(Model model) {
        model.addAttribute("title", "Frequently asked questions");

        return "faq";
    }

    @GetMapping("AboutUs")
    public String displayAboutUs(Model model) {
        model.addAttribute("title", "About Us!");

        return "aboutus";
    }

    /** Generates the JSON data for the search box autocomplete
     * Currently searches for any Provider/Service name that contains the search term.  Could update later to list those
     * that start with the term first then add those that contain the term second.  May be more useful to users.
     */

    @RequestMapping("searchTermAutocomplete")
    @ResponseBody
    public List<String> searchTermAutocomplete(@RequestParam String term) {
        List<String> suggestions = new ArrayList<String>();

        Iterable<Provider> providers = providerRepository.findAll();
        Iterable<Service> services = serviceRepository.findAll();

        /**  Checking to see if the provider name contains the search term */
        for(Provider provider : providers) {
            if(provider.getProviderName().toLowerCase().contains(term.toLowerCase())) {
                suggestions.add(provider.getProviderName());
            }
        }

        /**  Checking to see if the service name contains the search term */
        for(Service service : services) {
            if(service.getServiceName().toLowerCase().contains(term.toLowerCase())){
                suggestions.add(service.getServiceName());
            }
        }

        /**  Sorting possible results alphabetically  */
        Collections.sort(suggestions);

        return suggestions;
    }
}
