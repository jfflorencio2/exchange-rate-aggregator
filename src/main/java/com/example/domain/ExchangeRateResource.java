package com.example.domain;

import com.example.application.ExchangeRateService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/exchange")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExchangeRateResource {

    @Inject
    ExchangeRateService service;

    @POST
    @Path("/best-rate")
    public Uni<Response> getBestRate(ExchangeRateRequest request) {
        System.out.println("LLEGO AL ENDPOINT: " + request);
        return service.getBestExchangeRate(request)
                .onItem().transform(result -> Response.ok(result).build());
    }
}