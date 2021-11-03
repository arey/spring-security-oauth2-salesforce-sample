package com.javametmoi.sample.salesforce;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        OAuth2ClientConfig.class,
        SalesforceClient.class,
        OAuth2ClientAutoConfiguration.class,
        SecurityAutoConfiguration.class})
@EnableConfigurationProperties(OAuth2ClientProperties.class)
class SalesforceClientIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(SalesforceClientIntegrationTest.class);

    @Autowired
    private SalesforceClient salesforceClient;

    @Test
    void should_updateResource() {
        // Given
        String resourceId = "123";
        String request = "{your json request}";
        LOG.info("input={}", request);

        // When
        String response = salesforceClient.upsertResource(resourceId, request);

        // Then
        LOG.info("output={}", response);
        assertThat(response).contains("\"success\":true");
    }

}
