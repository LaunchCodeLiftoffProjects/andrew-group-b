package org.launchcode.ouchy;


import org.launchcode.ouchy.controllers.AuthenticationController;
import org.launchcode.ouchy.models.Data.UserRepository;
import org.launchcode.ouchy.models.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthenticationFilter extends HandlerInterceptorAdapter {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationController authenticationController;

    private static final List<String> whitelist = Arrays.asList("/login", "/register", "/", "/logout", "/Services", "/Providers", "/Distance", "/searchTermAutocomplete",
            "/styles.css", "/Images/project_ouchy.png");

    private static boolean isWhitelisted(String path) {
        System.out.println("Path:  " + path);
        for (String pathRoot : whitelist) {
            /** This pathRoot enables the whitelist filter to function */
            //if (path.equals(pathRoot)) {

            /** This pathRoot whitelists every page so you don't have to login each time you run bootRun */
            if (path.startsWith(pathRoot)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {

        // Don't require sign-in for whitelisted pages
        if (isWhitelisted(request.getRequestURI())) {
            // returning true indicates that the request may proceed
            return true;
        }

        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);

        // The user is logged in
        if (user != null) {
            return true;
        }

        // The user is NOT logged in
        response.sendRedirect("/login");
        return false;
    }
}
