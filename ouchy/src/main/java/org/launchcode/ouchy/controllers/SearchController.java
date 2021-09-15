package org.launchcode.ouchy.controllers;

import org.launchcode.ouchy.models.Data.ProviderRepository;
import org.launchcode.ouchy.models.Data.ServiceRepository;
import org.launchcode.ouchy.models.Provider;
import org.launchcode.ouchy.models.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    ProviderRepository providerRepository;

    @Autowired
    ServiceRepository serviceRepository;

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
        for (Provider provider : providers) {
            if (provider.getProviderName().toLowerCase().contains(term.toLowerCase())) {
                suggestions.add(provider.getProviderName());
            }
        }

        /**  Checking to see if the service name contains the search term */
        for (Service service : services) {
            if (service.getServiceName().toLowerCase().contains(term.toLowerCase())) {
                suggestions.add(service.getServiceName());
            }
        }

        /**  Sorting possible results alphabetically  */
        Collections.sort(suggestions);

        return suggestions;
    }

    @PostMapping("Search")
    public String processSearchTerm(Model model, @RequestParam String searchTerm) {
        model.addAttribute("title", "Search Results");
        model.addAttribute("providers", Provider.userSearch(searchTerm, providerRepository.findAll()));
        model.addAttribute("services", Service.userSearch(searchTerm,serviceRepository.findAll()));
        model.addAttribute("searchTerm", searchTerm);

        return "search";
    }
}
