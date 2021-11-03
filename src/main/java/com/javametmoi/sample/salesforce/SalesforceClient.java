package com.javametmoi.sample.salesforce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class SalesforceClient {

    private static final Logger LOG = LoggerFactory.getLogger(SalesforceClient.class);

    @Autowired
    private WebClient salesforceWebClient;

    @Value("${myapp.salesforce.resource-path}")
    private String resourcePath;

    public String upsertResource(String resourceId, String jsonRequest) {
        Mono<String> response = salesforceWebClient
                .patch()
                .uri(uriBuilder -> uriBuilder
                        .path(resourcePath)
                        .build(resourceId))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonRequest)
                .retrieve()
                .onStatus(HttpStatus::isError, errorResponse -> {
                    logErrorBody(errorResponse);
                    return Mono.error(new RuntimeException(String.format("Salesforce request on error status=%s, headers=%s",
                            errorResponse.statusCode(), errorResponse.headers().asHttpHeaders())));
                })
                .bodyToMono(String.class);

        return response.block();
    }

    public static void logErrorBody(ClientResponse response) {
        if (LOG.isErrorEnabled()) {
            response.bodyToMono(String.class)
                    .publishOn(Schedulers.boundedElastic())
                    .subscribe(body -> LOG.error("Body of the #Salesforce error response: {}", body));
        }
    }

}
