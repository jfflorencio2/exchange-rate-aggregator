package com.example.infrastructure.api3;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey="exchange-api.api3")
@Path("/convert")
public interface Api3RestClient {

    @POST
    Uni<Api3Response> getExchangeRate(Api3Request request);
}