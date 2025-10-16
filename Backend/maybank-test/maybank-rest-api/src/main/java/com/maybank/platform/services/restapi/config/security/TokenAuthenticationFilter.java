package com.maybank.platform.services.restapi.config.security;

import com.maybank.platform.services.restapi.services.impl.UserDetailsServiceImpl;
import com.maybank.platform.services.util.RedisDbKeyUtils;
import com.maybank.platform.services.util.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
  private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Autowired
  private RedisUtil redisUtil;

  @Override
  protected void doFilterInternal(
          HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = getJwtFromRequest(request);
      if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
        String key = RedisDbKeyUtils.getRedisCacheRestApiTokenKey(jwt);
        boolean isValidToken = redisUtil.exists(key);
        if(isValidToken) {
          String username = tokenProvider.getUserNameFromToken(jwt);

          UserDetails userDetails = userDetailsService.loadUserByUsername(username);
          UsernamePasswordAuthenticationToken authentication =
                  new UsernamePasswordAuthenticationToken(
                          userDetails, null, userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
          response.sendError(HttpStatus.UNAUTHORIZED.value(), "Token has been invalidated.");
          return;
        }
      }
    } catch (Exception ex) {
      logger.error("Could not set user authentication in security context", ex);
    }

    filterChain.doFilter(request, response);
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }

    return null;
  }
}
