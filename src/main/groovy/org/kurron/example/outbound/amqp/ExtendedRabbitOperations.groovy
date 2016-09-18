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

import org.springframework.amqp.AmqpException
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.core.RabbitOperations
import org.springframework.amqp.rabbit.support.CorrelationData

/**
 * Creates an interface for the functionality exposed in RabbitTemplate, which is not
 * an interface.  This interface will make unit-testing more convenient.
 */
interface ExtendedRabbitOperations extends RabbitOperations {

    /**
     * Sends a message to the broker, not waiting for a response.
     * @param exchange the exchange to publish to.
     * @param routingKey the routing key to use.
     * @param message the message to send.
     * @param correlationData unique identifier to associate with the message -- used for confirmations and returns.
     * @throws AmqpException if there is a problem getting the message to the broker.
     */
    void send( String exchange, String routingKey, Message message, CorrelationData correlationData ) throws AmqpException
}
