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

import org.kurron.example.shared.ExampleData
import org.kurron.example.shared.MessagingContext
import org.kurron.feedback.AbstractFeedbackAware
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.support.CorrelationData

/**
 * Deals with acknowledgments using in-memory data structures.  Suitable only for an example application.
 * Production code probably uses Redis or something similar.
 */
class InMemoryAcknowledgmentManager extends AbstractFeedbackAware implements AcknowledgmentManager {

    /**
     * Holds the outstanding messages.  NOT THREAD SAFE!
     */
    private final Map<CorrelationData, ExampleData> outstanding = [:]

    //TOD: not thread-safe! Use Redis instead?
    private final List<Message> unacknowledged = []

    @Override
    CorrelationData manage( final ExampleData data ) {
        def key = new CorrelationData( UUID.randomUUID() as String )
        outstanding[key] = data
        key
    }

    @Override
    void confirm( final CorrelationData correlationData, final boolean ack, final String cause ) {
        if ( ack ) {
            outstanding.remove( correlationData )
            feedbackProvider.sendFeedback( MessagingContext.RETRY_ACKNOWLEDGE, correlationData.id )
        }
        else {
            //TODO: a production implementation might retry unacknowledged messages at a later time
            feedbackProvider.sendFeedback( MessagingContext.RETRY_UNACKNOWLEDGED, correlationData.id, cause ?: 'No cause given.' )
        }
    }

    @Override
    void returnedMessage( final Message message,
                          final int replyCode,
                          final String replyText,
                          final String exchange,
                          final String routingKey ) {
        feedbackProvider.sendFeedback( MessagingContext.RETRY_UNROUTABLE, replyCode, replyText, exchange, routingKey )
        // REMINDER: an unroutable message STILL gets acknowledged so we'll need keep it in a separate retry list
        unacknowledged.add( message )
    }
}
