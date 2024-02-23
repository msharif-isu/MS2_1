package harmonize.Security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint{
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException)
            throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
    }
}