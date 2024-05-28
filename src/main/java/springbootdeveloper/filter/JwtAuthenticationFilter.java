package springbootdeveloper.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import springbootdeveloper.provider.JwtTokenProvider;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider JwtTokenProvider;

    public JwtAuthenticationFilter(springbootdeveloper.provider.JwtTokenProvider jwtTokenProvider) {
        JwtTokenProvider = jwtTokenProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        try {
            String token = parseBearerToken(request);

            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            String email = JwtTokenProvider.validate(token);
            if (email == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // context 등록 부분
            AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,null,
                    AuthorityUtils.NO_AUTHORITIES);

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authenticationToken);

            SecurityContextHolder.setContext(securityContext);


        }catch (Exception e){
            e.printStackTrace();
        }


        filterChain.doFilter(request, response);
    }


    private String parseBearerToken(HttpServletRequest request){
        String authorization = request.getHeader("Authorization"); // 요청 Header값 검증

        boolean hasAuthorization = StringUtils.hasText(authorization); // hasText : null or length -0 or 공백 false 반환

        if (!hasAuthorization)
            return null;

        boolean isBearer = authorization.startsWith("Bearer ");
        if (!isBearer)
            return null;

        String token = authorization.substring(7);

        return token;
    }

}
