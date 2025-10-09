package com.pomostudy.security;

import com.pomostudy.config.security.AuthenticatedUser;
import com.pomostudy.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

/**
 * A factory for creating a {@link SecurityContext} that holds a mock {@link AuthenticatedUser} as the principal.
 * <p>
 * This factory is the mechanism behind the custom {@code @WithMockAuthenticatedUser} annotation. Its primary purpose
 * is to simulate a logged-in user with our specific, custom principal type ({@code AuthenticatedUser}) during
 * integration tests with {@code MockMvc}.
 * <p>
 * When a test method is annotated with {@code @WithMockAuthenticatedUser}, the Spring Test framework invokes the
 * {@link #createSecurityContext(WithMockAuthenticatedUser)} method. This method then:
 * <ol>
 * <li>Builds a mock domain {@code User} object.</li>
 * <li>Wraps it in our custom {@code AuthenticatedUser} principal.</li>
 * <li>Creates an {@code Authentication} token (specifically {@code UsernamePasswordAuthenticationToken}).</li>
 * <li>Places this token into a new {@code SecurityContext}.</li>
 * </ol>
 * The resulting context is then used for the execution of the test, allowing the {@code @AuthenticationPrincipal}
 * annotation in controllers to be resolved correctly with our mock user.
 *
 * @see WithMockAuthenticatedUser
 * @see WithSecurityContextFactory
 */

public class WithMockAuthenticatedUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockAuthenticatedUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockAuthenticatedUser annotation) {

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");

        AuthenticatedUser principal = new AuthenticatedUser(mockUser);

        var authentication = new UsernamePasswordAuthenticationToken(
                principal,
                "password",
                principal.getAuthorities()
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        return context;
    }
}