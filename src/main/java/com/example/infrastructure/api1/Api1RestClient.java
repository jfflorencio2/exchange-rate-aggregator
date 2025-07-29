package com.example.infrastructure.api1;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


@RegisterRestClient(configKey="exchange-api.api1")
@Path("/exchange")
public interface Api1RestClient {

    @POST
    Uni<Api1Response> getExchangeRate(Api1Request request);
}




