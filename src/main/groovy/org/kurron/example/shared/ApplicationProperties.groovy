/*
 * Copyright (c) 2016. Ronald D. Kurr kurr@jvmguy.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kurron.example.shared

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Custom configuration properties that are driven by Spring Boot and its application.yml file.
 */
@ConfigurationProperties( value = 'example', ignoreUnknownFields = false )
class ApplicationProperties {

    /**
     * Contains the logging related properties.
     */
    Logging logging

    /**
     * Contains the dead letter exchange settings.
     */
    DeadLetter deadletter

    /**
     * Contains the inbound AMQP settings.
     */
    Inbound inbound

    /**
     * Contains the outbound AMQP settings.
     */
    Outbound outbound

    static class Logging {
        /**
         * Identifies this type of service. Used in logging.
         */
        String serviceCode

        /**
         * Identifies this instance of the service. Used in logging.
         */
        String serviceInstance

        /**
         * Logically groups a collection of services. Used in logging.
         */
        String realm
    }

    static class DeadLetter {
        /**
         * This property controls the name of the exchange used to publish poison messages to.
         */
        String exchangeName

        /**
         * This property controls the queue that is bound to the poison messages exchange.
         */
        String queueName

        /**
         * This property controls the routing key that is used when publishing poison messages.
         */
        String routingKey

        /**
         * This property controls how many times a message will be processed before being declared a poison message.
         */
        int messageRetryAttempts
    }

    static class Inbound {
        /**
         * This property controls the name of the exchange that the outbound gateway publishes to.
         */
        String exchangeName

        /**
         * This property controls the routing key that the outbound gateway publishes to.
         */
        String routingKey

        /**
         * This property controls the queue that is bound to the inbound exchange.
         */
        String queueName
    }

    static class Outbound {
        /**
         * This property controls the name of the exchange that the outbound gateway publishes to.
         */
        String exchangeName

        /**
         * This property controls the routing key that the outbound gateway publishes to.
         */
        String routingKey
    }

    /**
     * This property controls...
     */
    String foo

}
