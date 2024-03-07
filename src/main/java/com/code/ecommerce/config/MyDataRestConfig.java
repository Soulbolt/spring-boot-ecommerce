package com.code.ecommerce.config;

import com.code.ecommerce.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ExposureConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    @Value("${allowed.origins}")
    private String[] allowedOrigins;

    private final EntityManager entityManager;
    @Autowired
    public MyDataRestConfig(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
//        RepositoryRestConfigure.super.configureRepositoryRestConfiguration(config, cors);
        HttpMethod[] theUnsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PATCH};

        //disable HTTP methods for Products: PUT, POST, DELETE
        disableHttpMethods(config.getExposureConfiguration().forDomainType(Product.class), theUnsupportedActions);
        disableHttpMethods(config.getExposureConfiguration().forDomainType(ProductCategory.class), theUnsupportedActions);
        disableHttpMethods(config.getExposureConfiguration().forDomainType(Country.class), theUnsupportedActions);
        disableHttpMethods(config.getExposureConfiguration().forDomainType(State.class), theUnsupportedActions);
        disableHttpMethods(config.getExposureConfiguration().forDomainType(Order.class), theUnsupportedActions);

        // Call an internal helper method
        exposeIds(config);

        // Configure cors mapping
        cors.addMapping(config.getBasePath() + "/**").allowedOrigins(allowedOrigins);
    }

    private static void disableHttpMethods(ExposureConfigurer config, HttpMethod[] theUnsupportedActions) {
        config
                .withItemExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        // expose entity ids
        // Get a list of all entity classes from the entity manager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        // Create an array of the entity types
        List<Class> entityClasses = new ArrayList<>();

        // Get the entity types for the entities
        for (EntityType tempEntityType: entities) {
            entityClasses.add(tempEntityType.getJavaType());
        }

        // Expose the entity ids for the array of entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}
