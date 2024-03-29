package org.launchcode.ouchy.controllers;


import org.launchcode.ouchy.models.Data.ProviderRepository;
import org.launchcode.ouchy.models.Data.ProviderServiceDTORepository;
import org.launchcode.ouchy.models.Data.UserRepository;
import org.launchcode.ouchy.models.Provider;
import org.launchcode.ouchy.models.ProviderServiceDTO;
import org.launchcode.ouchy.models.User.LoginFormDTO;
import org.launchcode.ouchy.models.User.RegisterFormDTO;
import org.launchcode.ouchy.models.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProviderRepository providerRepository;

    @Autowired
    ProviderServiceDTORepository providerServiceDTORepository;

    private static final String userSessionKey = "user";

    public User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);
        if (userId == null) {
            return null;
        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }

    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
    }

    @GetMapping("/register")
    public String displayRegistrationForm(Model model, HttpServletRequest request) {
        model.addAttribute(new RegisterFormDTO());
        model.addAttribute("title", "Register");
        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute @Valid RegisterFormDTO registerFormDTO,
                                          Errors errors, HttpServletRequest request,
                                          Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Register");
            return "register";
        }

        User existingUser = userRepository.findByUsername(registerFormDTO.getUsername());

        if (existingUser != null) {
            errors.rejectValue("username", "username.alreadyexists", "A user with that username already exists");
            model.addAttribute("title", "Register");
            return "register";
        }

        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match");
            model.addAttribute("title", "Register");
            return "register";
        }

        User newUser = new User(registerFormDTO.getUsername(), registerFormDTO.getPassword(), registerFormDTO.getProvider());
        userRepository.save(newUser);
        setUserInSession(request.getSession(), newUser);

        /** Forward to add provider form if user is a provider */
        if(newUser.getProvider()) {
            String path = "redirect:/AddProvider/" + newUser.getId();

            return path;
        }
        return "redirect:/myAccount";
    }

    @GetMapping("/login")
    public String displayLoginForm(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();
        User user = getUserFromSession(session);

        // The user is logged in
        if (user != null) {
            model.addAttribute("title", "My Account");
            return "redirect:";
        }
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Log In");
        return "login";
    }

    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO,
                                   Errors errors, HttpServletRequest request,
                                   Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Log In");
            return "login";
        }

        User theUser = userRepository.findByUsername(loginFormDTO.getUsername());

        if (theUser == null) {
            errors.rejectValue("username", "user.invalid", "The given username does not exist");
            model.addAttribute("title", "Log In");
            return "login";
        }

        String password = loginFormDTO.getPassword();

        if (!theUser.isMatchingPassword(password)) {
            errors.rejectValue("password", "password.invalid", "Invalid password");
            model.addAttribute("title", "Log In");
            return "login";
        }

        setUserInSession(request.getSession(), theUser);

        return "redirect:/myAccount";
    }

    @GetMapping("/Logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();

        return "redirect:/login";
    }

    @GetMapping("myAccount")
    public String myAccountHome(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = getUserFromSession(session);

        // The user is logged in
        if(user != null) {
            model.addAttribute("title", "My Account");
            model.addAttribute("user", user);

            //  User is a provider
            if(user.getProviderId() >= 0) {
                Optional optional = providerRepository.findById(user.getProviderId());
                Provider provider = (Provider) optional.get();

                Iterable<ProviderServiceDTO> services = providerServiceDTORepository.findAllByProviderID(user.getProviderId());

                model.addAttribute("provider", provider);
                model.addAttribute("services", services);

                return "myAccount_Provider";
            }
            return "myAccount_User";
        }

        // The user is NOT logged in
        return "redirect:/login";
    }
}
