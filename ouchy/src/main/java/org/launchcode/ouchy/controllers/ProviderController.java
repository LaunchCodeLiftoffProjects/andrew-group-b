package org.launchcode.ouchy.controllers;

import org.launchcode.ouchy.models.Data.ProviderRepository;
import org.launchcode.ouchy.models.Data.UserRepository;
import org.launchcode.ouchy.models.Provider;
import org.launchcode.ouchy.models.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ProviderController {

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("AddProvider/{userID}")
    public String addProviderHome(Model model, @PathVariable int userID) {
        model.addAttribute("title", "Add New Provider");
        model.addAttribute("provider", new Provider());

        Optional optional = userRepository.findById(userID);
        if(optional.isEmpty()) {
            return "redirect:";
        } else {
            User user = (User) optional.get();
            model.addAttribute("user", user);
        }

        return "addProvider";
    }

    @PostMapping("AddProvider/{userID}")
    public String processAddProvider(Model model, @RequestParam String providerName, @RequestParam String providerAddress,
                                     @RequestParam String providerEmail, @RequestParam String providerPhone,
                                     @PathVariable int userID) {

        /**  Saves new provider if all data is available and assigns provider ID to the applicable user */

        if(!providerAddress.isEmpty() || !providerEmail.isEmpty() || !providerName.isEmpty() || !providerPhone.isEmpty()) {
            Provider newProvider = new Provider(providerName, providerAddress, providerPhone, providerEmail);
            providerRepository.save(newProvider);
            Optional optional = userRepository.findById(userID);
            User user = (User) optional.get();
            user.setProviderId(newProvider.getProviderId());
            userRepository.save(user);
        }

        /**  Redirect to My Account page after creating provider info
         *     Update to redirect to Add Provider Services eventually
         */

        return "redirect:../myAccount";
    }
}
