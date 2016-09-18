package org.kurron.example.inbound.rest

import org.springframework.boot.test.TestRestTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Test-specific beans.
 **/
@Configuration
class RestGatewayIntegrationTestConfiguration {

    @Bean
    TestRestTemplate TestRestTemplate() {
        new TestRestTemplate()
    }
}
