package com.shopme.client.config.security;

import com.shopme.client.service.CustomerContextService;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.CustomerStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class CustomerStatusAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final CustomerContextService customerContextService;


    @Override
    public AuthorizationDecision check( Supplier<Authentication> authentication, RequestAuthorizationContext context
    ) {
//        Customer customer = ((UserPrincipal) authentication.get().getPrincipal()).getCustomer();
//        TEMPORARY SOLUTION, WILL USE USER PRINCIPAL LATER
        Customer customer = customerContextService.getCurrentCustomer();

        if (!customer.isEnabled())
            return new AuthorizationDecision(false);

        String endpoint = context.getRequest().getRequestURI();

        boolean isAllowed = isAllowedEndpoint(customer.getStatus(), endpoint);

        return new AuthorizationDecision(isAllowed);
    }

    private boolean isAllowedEndpoint(CustomerStatus status, String endpoint) {
        return switch (status) {
            case VERIFIED -> true;
            case UNVERIFIED -> endpoint.startsWith("profile") || endpoint.startsWith("verify") ||
                    endpoint.startsWith("resend-verification");
            case NEED_INFO -> endpoint.startsWith("profile");
        };
    }
}