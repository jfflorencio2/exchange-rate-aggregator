package com.example.application;

import com.example.domain.ExchangeRateGateway;
import com.example.domain.ExchangeRateRequest;
import com.example.domain.ExchangeRateResponse;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
public class ExchangeRateService {

    private static final Logger LOG = Logger.getLogger(ExchangeRateService.class);

    @Inject
    @Any // Inyecta todas las implementaciones de ExchangeRateGateway
    Instance<ExchangeRateGateway> exchangeRateGateways;

    public Uni<ExchangeRateResponse> getBestExchangeRate(ExchangeRateRequest request) {
        List<Uni<ExchangeRateResponse>> unis = exchangeRateGateways.stream()
                .map(gateway -> {
                    String apiName = gateway.getClass().getSimpleName();
                    // Eliminar 'Adapter' del nombre para un nombre de API más limpio
                    if (apiName.endsWith("Adapter")) {
                        apiName = apiName.substring(0, apiName.length() - "Adapter".length());
                    }
                    final String finalApiName = apiName;

                    return gateway.getExchangeRate(request.getSourceCurrency(), request.getTargetCurrency(), request.getAmount())
                            .onItem().transform(convertedAmount -> {
                                LOG.infof("API %s returned converted amount: %s", finalApiName, convertedAmount);
                                return new ExchangeRateResponse(convertedAmount, finalApiName);
                            })
                            .onFailure().recoverWithItem(throwable -> {
                                LOG.errorf("API %s failed: %s", finalApiName, throwable.getMessage());
                                return null; // Retorna null para indicar que esta API falló y no debe ser considerada
                            });
                })
                .toList();

        // Combina todos los Uni y espera a que todos se completen (o fallen)
        return Uni.join().all(unis).andCollectFailures()
                .onItem().transform(responses -> {
                    // Filtra las respuestas nulas (fallidas) y encuentra la mejor oferta
                    return responses.stream()
                            .filter(response -> response != null && response.getConvertedAmount() != null)
                            .max(Comparator.comparing(ExchangeRateResponse::getConvertedAmount))
                            .orElseGet(() -> {
                                LOG.warn("No successful exchange rate responses received from any API.");
                                return new ExchangeRateResponse(BigDecimal.ZERO, "N/A"); // O lanzar una excepción si no hay ofertas
                            });
                });
    }
}