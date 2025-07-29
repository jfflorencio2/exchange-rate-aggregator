package com.example.infrastructure.api3;

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
public class Api3ExchangeRateAdapter implements ExchangeRateGateway {

    private static final Logger LOG = Logger.getLogger(Api3ExchangeRateAdapter.class);

    @Inject
    @RestClient
    Api3RestClient api3RestClient;

    @Override
    public Uni<BigDecimal> getExchangeRate(String sourceCurrency, String targetCurrency, BigDecimal amount) {
        Api3Request request = new Api3Request(sourceCurrency, targetCurrency, amount);
        return api3RestClient.getExchangeRate(request)
                .onItem().transform(response -> {
                    if (response.statusCode == 200 && response.data != null && response.data.total != null) {
                        return response.data.total;
                    } else {
                        LOG.warnf("API3 returned non-success status or invalid data: %d - %s", response.statusCode, response.message);
                        throw new RuntimeException("API3 returned invalid response");
                    }
                })
                .onFailure().recoverWithUni(throwable -> {
                    LOG.errorf("Error calling API3 for %s to %s with amount %s: %s", sourceCurrency, targetCurrency, amount, throwable.getMessage());
                    return Uni.createFrom().failure(new RuntimeException("API3 call failed", throwable));
                })
                .onFailure().retry().withBackOff(Duration.ofSeconds(1), Duration.ofSeconds(5)).atMost(3)
                .onFailure(TimeoutException.class).recoverWithUni(() ->
                        Uni.createFrom().failure(new RuntimeException("Timeout while calling API3"))
                );
    }
}