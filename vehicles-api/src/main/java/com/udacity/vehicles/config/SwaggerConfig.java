package com.udacity.vehicles.config;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.client.JsonPathLinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.hal.HalLinkDiscoverer;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.hateoas.server.LinkRelationProvider.LookupContext;
import org.springframework.hateoas.server.core.DelegatingLinkRelationProvider;
import org.springframework.hateoas.server.core.EvoInflectorLinkRelationProvider;
import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.plugin.core.support.PluginRegistryFactoryBean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ApiResponses(value = {
        @ApiResponse(code = 400, message = "BadRequest, check all the parameters and header content type"),
        @ApiResponse(code = 404, message = "NotFound, check if the car exists with that id"),
        @ApiResponse(code = 500, message = "Server id down, check if you are running all the required services before")
})
public class SwaggerConfig {

    @Bean
    public LinkDiscoverers discoverers() {
        List<JsonPathLinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new HalLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
    }

    @Bean
    public LinkRelationProvider provider() {
        return new EvoInflectorLinkRelationProvider();
    }

    @Bean
    @Primary
    public PluginRegistryFactoryBean<LinkRelationProvider, LookupContext>
    myPluginRegistryProvider(){

        PluginRegistryFactoryBean<LinkRelationProvider, LinkRelationProvider.LookupContext> factory
                = new PluginRegistryFactoryBean<>();

        factory.setType(LinkRelationProvider.class);
        Class<?> classes[] = new Class<?>[1];
        classes[0] = DelegatingLinkRelationProvider.class;
        factory.setExclusions(classes);

        return factory;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Vehicle API",
                "This API returns cars according to their specifications. Price and Location are services consumed by Vehicle API",
                "1.0",
                "",
                new Contact("Filipe Costa", "https://github.com/filipecosta01", "s.costa.filipe@gmail.com"),
                "",
                "",
                Collections.emptyList()
        );
    }
}
