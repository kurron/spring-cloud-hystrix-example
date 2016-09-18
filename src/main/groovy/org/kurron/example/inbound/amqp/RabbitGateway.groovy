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
package org.kurron.example.inbound.amqp

import org.kurron.example.shared.MessagingContext
import org.kurron.feedback.AbstractFeedbackAware
import org.kurron.stereotype.InboundGateway
import org.springframework.amqp.rabbit.annotation.RabbitListener

/**
 * Gateway that handles incoming messages from a message queue.
 */
@SuppressWarnings( 'GroovyUnusedDeclaration' )
@InboundGateway
class RabbitGateway  extends AbstractFeedbackAware {

    @SuppressWarnings(['GrMethodMayBeStatic', 'GroovyUnusedDeclaration'])
    @RabbitListener( queues = '${example.inbound.queueName}' )
    void processMessage( SampleRequest  request ) {
        feedbackProvider.sendFeedback( MessagingContext.INTENTIONAL_ERROR, 'Triggering poison message handling.' )
        throw new UnsupportedOperationException( "forced to fail: ${request.timestamp}" )
    }
}
