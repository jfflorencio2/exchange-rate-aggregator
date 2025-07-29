package com.example.infrastructure.api2;

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
public class Api2ExchangeRateAdapter implements ExchangeRateGateway {

    private static final Logger LOG = Logger.getLogger(Api2ExchangeRateAdapter.class);

    @Inject
    @RestClient
    Api2RestClient api2RestClient;

    @Override
    public Uni<BigDecimal> getExchangeRate(String sourceCurrency, String targetCurrency, BigDecimal amount) {
        Api2Request request = new Api2Request(sourceCurrency, targetCurrency, amount);
        return api2RestClient.getExchangeRate(request)
                .onItem().transform(response -> response.result)
                .onFailure().recoverWithUni(throwable -> {
                    LOG.errorf("Error calling API2 for %s to %s with amount %s: %s", sourceCurrency, targetCurrency, amount, throwable.getMessage());
                    return Uni.createFrom().failure(new RuntimeException("API2 call failed", throwable));
                })
                .onFailure().retry().withBackOff(Duration.ofSeconds(1), Duration.ofSeconds(5)).atMost(3)
                .onFailure(TimeoutException.class).recoverWithUni(() ->
                        Uni.createFrom().failure(new RuntimeException("Timeout while calling API2"))
                );
    }
}