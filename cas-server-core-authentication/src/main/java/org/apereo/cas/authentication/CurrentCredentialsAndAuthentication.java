package org.apereo.cas.authentication;

import java.util.Arrays;
import java.util.Collection;

import static java.util.stream.Collectors.joining;

/**
 * ThreadLocal based holder for current set of credentials and/or authentication object for any current
 * CAS authentication transaction. Useful for making this information available to all the interested CAS
 * components that are not tightly coupled with core CAS APIs, for example audit principal resolver component, etc.
 * <p>
 * The thread local state carried by this class should be set by core CAS components processing core authentication and
 * CAS protocol events e.g. <code>AbstractAuthenticationManager</code>, <code>CentralAuthenticationServiceImpl</code>, etc.
 * <p>
 * The clearing of this state at the end of a thread execution path is the responsibility
 * of <code>CurrentCredentialsAndAuthenticationClearingFilter</code>
 *
 * @author Dmitriy Kopylenko
 * @since 5.0.0
 */
public class CurrentCredentialsAndAuthentication {

    private static final ThreadLocal<Authentication> CURRENT_AUTHENTICATION = new ThreadLocal<>();

    private static final ThreadLocal<String[]> CURRENT_CREDENTIAL_IDS = new ThreadLocal<>();

    protected CurrentCredentialsAndAuthentication() {
    }


    /**
     * Bind credentials to ThreadLocal.
     *
     * @param credentials the credentials
     */
    public static void bindCurrent(final Collection<Credential> credentials) {
        bindCurrent(credentials.toArray(new Credential[0]));
    }

    /**
     * Bind credentials to ThreadLocal.
     *
     * @param credentials the credentials
     */
    public static void bindCurrent(final Credential... credentials) {
        CURRENT_CREDENTIAL_IDS.set(Arrays.stream(credentials).map(Credential::getId).toArray(String[]::new));
    }

    /**
     * Get credential ids from ThreadLocal.
     *
     * @return credential ids
     */
    public static String[] getCurrentCredentialIds() {
        return CURRENT_CREDENTIAL_IDS.get();
    }

    /**
     * Get credential ids String representation from ThreadLocal.
     *
     * @return credential ids String representation
     */
    public static String getCurrentCredentialIdsAsString() {
        return (getCurrentCredentialIds() != null) ? Arrays.stream(getCurrentCredentialIds()).collect(joining(", ")) : null;
    }

    /**
     * Bind Authentication to ThreadLocal.
     *
     * @param authentication the authentication
     */
    public static void bindCurrent(final Authentication authentication) {
        CURRENT_AUTHENTICATION.set(authentication);
    }

    /**
     * Get Authentication from ThreadLocal.
     *
     * @return authentication
     */
    public static Authentication getCurrentAuthentication() {
        return CURRENT_AUTHENTICATION.get();
    }

    /**
     * Clear ThreadLocal state.
     */
    public static void clear() {
        CURRENT_CREDENTIAL_IDS.remove();
        CURRENT_AUTHENTICATION.remove();
    }
}
