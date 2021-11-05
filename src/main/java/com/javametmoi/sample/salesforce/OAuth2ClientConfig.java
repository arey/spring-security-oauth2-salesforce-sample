package com.javametmoi.sample.salesforce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;

@Configuration
public class OAuth2ClientConfig {

    private static final String SALESFORCE_CLIENT_NAME = "salesforce";

    private static final String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";

    @Autowired
    private Environment env;

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService) {
        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .password()
                        .build();

        // Using AuthorizedClientServiceOAuth2AuthorizedClientManager instead of the DefaultOAuth2AuthorizedClientManager
        // to support asynchrone execution through the @Async annotation
        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        authorizedClientManager.setContextAttributesMapper(oAuth2AuthorizeRequest -> {
                    if (SALESFORCE_CLIENT_NAME.equals(oAuth2AuthorizeRequest.getClientRegistrationId())) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put(OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME, getProperty(SALESFORCE_CLIENT_NAME, "username"));
                        map.put(OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME, getProperty(SALESFORCE_CLIENT_NAME, "password"));
                        return map;
                    }
                    return null;
                }
        );

        return authorizedClientManager;
    }

    @Bean
    public WebClient salesforceWebClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        // May use a ServerAuth2AuthorizedClientExchangeFilterFunction in a reactive stack
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth2Client.setDefaultClientRegistrationId(SALESFORCE_CLIENT_NAME);
        return WebClient.builder()
                .baseUrl(env.getProperty("myapp.salesforce.base-path"))
                .apply(oauth2Client.oauth2Configuration())
                .build();
    }

    private String getProperty(String client, String property) {
        return env.getProperty(CLIENT_PROPERTY_KEY + client + "." + property);
    }

}
