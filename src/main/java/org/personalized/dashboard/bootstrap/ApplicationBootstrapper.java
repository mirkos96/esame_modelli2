package org.personalized.dashboard.bootstrap;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.bson.Document;
import org.personalized.dashboard.auth.*;
import org.personalized.dashboard.utils.stopwords.StopwordsRemover;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sudan on 5/4/15.
 */
public class ApplicationBootstrapper extends GuiceServletContextListener {

    private final Logger LOGGER = LoggerFactory.getLogger(ApplicationBootstrapper.class);

    @Override
    protected Injector getInjector() {

        try {
            ConfigManager.init();
            MongoBootstrap.init();
            MongoBootstrap.getMongoDatabase().runCommand(new Document("ping", 1));
            QueueBootstrap.init();
            ESBootstrap.init();
            StopwordsRemover.init();
            OAuthBootstrap.init();

            LOGGER.info("Application initialization has been successful");
        } catch (Exception e) {
            LOGGER.error("Application initialization failed with exception  ", e);
            System.exit(1);
        }
        return Guice.createInjector(new ServletModule() {

            @Override
            protected void configureServlets() {
                super.configureServlets();
                serve("/login").with(LoginServlet.class);
                serve("/google-login").with(GoogleLoginServlet.class);
                serve("/google-redirect").with(GoogleLoginCallbackServlet.class);
                serve("/dashboard").with(DashboardServlet.class);
                serve("/").with(DashboardServlet.class);
                serve("/logout").with(LogoutServlet.class);

                ResourceConfig resourceConfig = new PackagesResourceConfig("org.personalized" +
                        ".dashboard.controller");
                for (Class<?> resource : resourceConfig.getClasses()) {
                    bind(resource);
                }
                serve("/dashboard/*").with(GuiceContainer.class);

            }
        }, new DIModule());
    }
}
