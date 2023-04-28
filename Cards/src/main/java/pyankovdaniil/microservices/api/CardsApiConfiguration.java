package pyankovdaniil.microservices.api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class CardsApiConfiguration {
    @Value("${api.documentation.cardsMicroserviceUrl}")
    private String cardsMicroserviceUrl;

    @Bean
    public OpenAPI cardsApiConfig() {
        Server cardsServer = new Server();
        cardsServer.setUrl(cardsMicroserviceUrl);
        cardsServer.setDescription("Cards microservice");

        String apiDescription = "This API shows the endpoints to interact with cards microservice";
        Info info = new Info()
                .title("Cards microservice API")
                .version("1.0")
                .description(apiDescription);

        return new OpenAPI().info(info).servers(Collections.emptyList());
    }
}
