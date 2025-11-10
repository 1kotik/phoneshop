package com.es.phoneshop.web.controller.pages;

import com.es.core.model.AuthenticationRequest;
import com.es.core.service.AuthenticationService;
import com.es.core.service.CartService;
import com.es.core.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final CartService cartService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, CartService cartService) {
        this.authenticationService = authenticationService;
        this.cartService = cartService;
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        if (!model.containsAttribute(AppConstants.PageAttributes.AUTHENTICATION_REQUEST)) {
            model.addAttribute(AppConstants.PageAttributes.AUTHENTICATION_REQUEST, new AuthenticationRequest());
        }
        return AppConstants.Pages.LOGIN;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute(AppConstants.PageAttributes.AUTHENTICATION_REQUEST)
                        AuthenticationRequest authenticationRequest,
                        RedirectAttributes redirectAttributes) {
        try {
            authenticationService.login(authenticationRequest);
        } catch (AuthenticationException e) {
            redirectAttributes.addFlashAttribute(
                    AppConstants.PageAttributes.AUTHENTICATION_REQUEST,
                    authenticationRequest);
            redirectAttributes.addFlashAttribute(
                    AppConstants.PageAttributes.ERROR,
                    "Authentication Failed");
            return AppConstants.Pages.REDIRECT_LOGIN;
        }
        redirectAttributes.addFlashAttribute(AppConstants.PageAttributes.CART, cartService.getCart());
        return AppConstants.Pages.REDIRECT_PRODUCT_LIST;
    }

    @PostMapping("/logout")
    public String logout() {
        authenticationService.logout();
        return AppConstants.Pages.REDIRECT_LOGIN;
    }
}
