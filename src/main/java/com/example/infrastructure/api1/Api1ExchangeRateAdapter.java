package com.example.infrastructure.api1;

import com.example.domain.ExchangeRateGateway;
import io.smallrye.mutiny.TimeoutException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.time.Duration;

@ApplicationScoped
public class Api1ExchangeRateAdapter implements ExchangeRateGateway {

    private static final Logger LOG = Logger.getLogger(Api1ExchangeRateAdapter.class);

    @Inject
    @RestClient
    Api1RestClient api1RestClient;

    @Override
    public Uni<BigDecimal> getExchangeRate(String sourceCurrency, String targetCurrency, BigDecimal amount) {
        Api1Request request = new Api1Request(sourceCurrency, targetCurrency, amount);
        return api1RestClient.getExchangeRate(request)
                .onItem().transform(response -> amount.multiply(response.rate))
                .onFailure().recoverWithUni(throwable -> {
                    LOG.errorf("Error calling API1 for %s to %s with amount %s: %s", sourceCurrency, targetCurrency, amount, throwable.getMessage());
                    return Uni.createFrom().failure(new RuntimeException("API1 call failed", throwable));
                })
                .onFailure().retry().withBackOff(Duration.ofSeconds(1), Duration.ofSeconds(5)).atMost(3)
                .onFailure(TimeoutException.class).recoverWithUni(() ->
                        Uni.createFrom().failure(new RuntimeException("Timeout while calling API1"))
                );
    }
}