package com.pomostudy.security;

import org.springframework.security.test.context.support.WithSecurityContext;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A custom meta-annotation that provides a convenient way to run a test with a mock,
 * authenticated user. The principal in the security context will be an instance of
 * {@code AuthenticatedUser}.
 * <p>
 * This annotation should be placed on a test method. When present, it automatically
 * configures the {@link org.springframework.security.core.context.SecurityContext}
 * for that test's execution, simulating a logged-in user. This is a declarative
 * shortcut that avoids boilerplate setup code in tests requiring an authenticated principal.
 * <p>
 * The actual creation of the security context is handled by the
 * {@link WithMockAuthenticatedUserSecurityContextFactory}.
 *
 * <h3>Example Usage:</h3>
 * <pre>
 * {@code
 * @Test
 * @WithMockAuthenticatedUser
 * public void getCategories_whenAuthenticated_shouldReturnOk() {
 * // The SecurityContext is now populated with a mock AuthenticatedUser.
 * // The @AuthenticationPrincipal in the controller will be correctly injected.
 * mockMvc.perform(get("/api/categories"))
 * .andExpect(status().isOk());
 * }
 * }
 * </pre>
 *
 * @see WithMockAuthenticatedUserSecurityContextFactory
 * @see WithSecurityContext
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockAuthenticatedUserSecurityContextFactory.class)
public @interface WithMockAuthenticatedUser {

}
