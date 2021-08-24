package org.launchcode.ouchy.controllers;

import org.launchcode.ouchy.models.Data.ProviderRepository;
import org.launchcode.ouchy.models.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProviderController {

    @Autowired
    private ProviderRepository providerRepository;

    @GetMapping("AddProvider")
    public String addProviderHome(Model model) {
        model.addAttribute("title", "Add New Provider");
        model.addAttribute("provider", new Provider());

        return "addProvider";
    }

    @PostMapping("AddProvider")
    public String processAddProvider(Model model, @RequestParam String providerName, @RequestParam String providerAddress,
                                     @RequestParam String providerEmail, @RequestParam String providerPhone) {

        if(!providerAddress.isEmpty() || !providerEmail.isEmpty() || !providerName.isEmpty() || !providerPhone.isEmpty()) {
            Provider newProvider = new Provider(providerName, providerAddress, providerPhone, providerEmail);
            providerRepository.save(newProvider);
        }

        model.addAttribute("title", "Add New Provider");

        return "redirect:AddProvider";
    }
}
