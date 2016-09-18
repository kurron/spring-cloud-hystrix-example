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

import org.junit.experimental.categories.Category
import org.kurron.categories.InboundIntegrationTest
import org.kurron.example.Application
import org.kurron.example.inbound.rest.RestCapable
import org.kurron.example.outbound.amqp.ExtendedRabbitOperations
import org.kurron.example.shared.ApplicationProperties
import org.kurron.traits.GenerationAbility
import org.springframework.amqp.core.MessageDeliveryMode
import org.springframework.amqp.core.MessagePropertiesBuilder
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 * An integration-level test of the RabbitGateway object.
 **/
@Category( InboundIntegrationTest )
@IntegrationTest
@ContextConfiguration( classes = [Application, TestConfiguration], loader = SpringApplicationContextLoader )
class RabbitGatewayIntegrationTest extends Specification implements GenerationAbility, RestCapable {

    @Autowired
    ApplicationProperties configuration

    @Autowired
    RabbitAdmin administrator

    @Autowired
    ExtendedRabbitOperations template

    @Autowired
    Jackson2JsonMessageConverter converter

    def setup() {
        // clear the queue before each test
        assert configuration
        assert administrator
        administrator.purgeQueue( configuration.inbound.queueName, false )
    }

    def 'exercise publishing happy path'() {
        given: 'a proper testing environment'
        assert template
        assert converter

        and: 'a valid message'
        def properties = MessagePropertiesBuilder.newInstance()
                .setAppIdIfAbsent( 'integration test' )
                .setContentTypeIfAbsentOrDefault( 'test/plain' )
                .setDeliveryModeIfAbsentOrDefault( MessageDeliveryMode.NON_PERSISTENT )
                .setMessageIdIfAbsent( randomUUID() as String )
                .setTimestampIfAbsent( Calendar.instance.time )
                .setTypeIfAbsent( 'sample command' )
                .build()
        def message = converter.toMessage( new SampleRequest( status: randomPositiveInteger(), timestamp: randomHexString() ), properties )

        when: 'message is sent'
        template.send( configuration.inbound.exchangeName, configuration.inbound.queueName, message )

        then:
        Thread.sleep( 1000 )
    }

}

