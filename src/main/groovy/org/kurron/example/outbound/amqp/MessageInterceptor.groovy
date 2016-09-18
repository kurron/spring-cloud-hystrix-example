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
import org.kurron.example.shared.Constants
import org.kurron.traits.GenerationAbility
import org.springframework.amqp.AmqpException
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessagePostProcessor

/**
 * Called just prior to sending the message out onto the wire.  We can ensure required
 * headers and properties are in place.
 */
class MessageInterceptor implements MessagePostProcessor, GenerationAbility {

    /**
     * Holds the application's configuration settings.
     */
    private final ApplicationProperties configuration

    MessageInterceptor( final ApplicationProperties aConfiguration ) {
        configuration = aConfiguration
    }

    @Override
    Message postProcessMessage( final Message message ) throws AmqpException {
        //TODO: we should grab this from the MDC for propagation
        message.messageProperties.headers.putIfAbsent( Constants.CORRELATION_ID_HEADER, randomUUID() as String )
        message.messageProperties.appId = configuration.logging.serviceCode
        message
    }
}
