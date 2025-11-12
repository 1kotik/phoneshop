package service;

import com.es.core.model.AuthenticationRequest;
import com.es.core.service.DefaultAuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultAuthenticationServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private DefaultAuthenticationService defaultAuthenticationService;

    @Test
    void shouldDoLogin() {
        AuthenticationRequest authRequest = new AuthenticationRequest("username", "password");
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
        when(authenticationManager
                .authenticate(authentication)).thenReturn(any());
        assertDoesNotThrow(() -> defaultAuthenticationService.login(authRequest));
    }

    @Test
    void shouldThrowAuthenticationException() {
        AuthenticationRequest authRequest = new AuthenticationRequest("username", "password");
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
        doThrow(BadCredentialsException.class).when(authenticationManager).authenticate(authentication);
        assertThrows(BadCredentialsException.class, () -> defaultAuthenticationService.login(authRequest));
    }
}
