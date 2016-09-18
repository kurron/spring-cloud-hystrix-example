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
package org.kurron.example.outbound.amqp

import org.kurron.example.shared.ApplicationProperties
import org.kurron.example.shared.ExampleData
import org.kurron.example.shared.MessagingContext
import org.kurron.feedback.AbstractFeedbackAware
import org.springframework.amqp.AmqpException
import org.springframework.amqp.core.MessagePropertiesBuilder
import org.springframework.amqp.rabbit.support.CorrelationData
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.http.MediaType

/**
 * Puts events on the message bus.
 */
class RabbitEventGateway extends AbstractFeedbackAware implements EventGateway {

    /**
     * Knows how to interact with the message bus.  The reason we need the concrete class is that the special
     * send() method that uses the CorrelationData is there and not in the interface.  This will make unit testing harder.
     */
    ExtendedRabbitOperations template

    /**
     * Contains the application's configuration settings.
     */
    ApplicationProperties configuration

    /**
     * An object-to-JSON converter.
     */
    Jackson2JsonMessageConverter converter

    RabbitEventGateway( final ExtendedRabbitOperations aTemplate,
                        final ApplicationProperties aConfiguration,
                        final Jackson2JsonMessageConverter aConverter ) {
        template = aTemplate
        configuration = aConfiguration
        converter = aConverter
    }

    @Override
    void sendEvent( final ExampleData data, CorrelationData key ) {
        def properties = MessagePropertiesBuilder.newInstance()
                                                 .setContentTypeIfAbsentOrDefault( MediaType.APPLICATION_JSON_VALUE )
                                                 .setTypeIfAbsent( ExampleData.name )
                                                 .build()
        def message = converter.toMessage( data, properties )
        try {
            template.send( configuration.outbound.exchangeName, configuration.outbound.routingKey, message, key )
        }
        catch ( AmqpException e ) {
            //REMINDER: the data is held by the acknowledgment manager so there is nothing more to do here.
            feedbackProvider.sendFeedback( MessagingContext.PUBLICATION_FAILURE, e.message )
        }
    }
}
