package controller;

import com.es.core.model.AuthenticationRequest;
import com.es.core.model.Cart;
import com.es.core.service.AuthenticationService;
import com.es.core.service.CartService;
import com.es.core.util.AppConstants;
import com.es.phoneshop.web.controller.pages.AuthenticationController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private CartService cartService;
    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void shouldGetLoginPage() {
        Model model = new ExtendedModelMap();
        String view = authenticationController.getLoginPage(model);
        assertEquals(AppConstants.Pages.LOGIN, view);
        assertTrue(model.containsAttribute(AppConstants.PageAttributes.AUTHENTICATION_REQUEST));
    }

    @Test
    void shouldDoLogin() {
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        AuthenticationRequest authRequest = new AuthenticationRequest("username", "password");
        Cart cart = new Cart();
        doNothing().when(authenticationService).login(authRequest);
        when(cartService.getCart()).thenReturn(cart);
        String view = authenticationController.login(authRequest, redirectAttributes);
        assertEquals(AppConstants.Pages.REDIRECT_PRODUCT_LIST, view);
        assertTrue(redirectAttributes.getFlashAttributes()
                .containsValue(cart));
    }

    @Test
    void shouldRedirectToLoginPageWhenLogin() {
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        AuthenticationRequest authRequest = new AuthenticationRequest("username", "password");
        doThrow(BadCredentialsException.class).when(authenticationService).login(authRequest);
        String view = authenticationController.login(authRequest, redirectAttributes);
        assertEquals(AppConstants.Pages.REDIRECT_LOGIN, view);
        assertTrue(redirectAttributes.getFlashAttributes().containsValue(authRequest));
        assertTrue(redirectAttributes.getFlashAttributes().containsKey(AppConstants.PageAttributes.ERROR));
    }

    @Test
    void shouldDoLogout() {
        doNothing().when(authenticationService).logout();
        String view = authenticationController.logout();
        assertEquals(AppConstants.Pages.REDIRECT_LOGIN, view);
    }
}
