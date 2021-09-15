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
import java.util.Optional;

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

    @GetMapping("Provider/{providerId}")
    public String displayProviderDetails(Model model, @PathVariable int providerId) {
        Optional optional= providerRepository.findById(providerId);
        if(!optional.isEmpty()) {
            Provider provider = (Provider) optional.get();

            model.addAttribute("title", provider.getProviderName());
            model.addAttribute("provider", provider);

            return "providerDetails";
        } else {
            return "redirect:.";
        }


    }

}
