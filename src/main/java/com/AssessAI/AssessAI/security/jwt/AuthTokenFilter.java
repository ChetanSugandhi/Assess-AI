package com.AssessAI.AssessAI.security.jwt;

import com.AssessAI.AssessAI.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {

        try {
            // Step 1: Token extract
//            String jwt = jwtUtils.getJwtFromHeader(request);
//            String jwt = jwtUtils.getJwtFromCookie(request);     // because we are using cookies so that we wrote these.. token ko extract kra from cookie...
            String jwt = parseJwt(request);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // Step 2: Username extract
                String username = jwtUtils.getUsernameFromJWTToken(jwt);

                // Step 3: UserDetails load
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Step 4: Spring Security authentication set
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Step 5: Store in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            System.out.println("Cannot set user authentication: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    // this is for swagger for authentication in swagger...
    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromCookie(request);
        if(jwt != null) {
            return jwt;
        }

        String jwt2 = jwtUtils.getJwtFromHeader(request);
        if(jwt2 != null) {
            return jwt2;
        }

        return null;
    }
}
