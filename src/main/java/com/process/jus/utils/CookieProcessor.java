
package com.process.jus.utils;

import org.apache.catalina.Context;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CookieProcessor {

    @Bean
    WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
        return (TomcatServletWebServerFactory tomcatServletWebServerFactory) -> {
            tomcatServletWebServerFactory.addContextCustomizers((TomcatContextCustomizer) (Context context1) -> {
                context1.setCookieProcessor(new LegacyCookieProcessor());
            });
        };
    }
    
}
