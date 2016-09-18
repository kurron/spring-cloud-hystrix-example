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
package org.kurron.example.core

import org.kurron.example.outbound.amqp.AcknowledgmentManager
import org.kurron.example.outbound.amqp.EventGateway
import org.kurron.example.shared.ExampleData

/**
 * Showcases some orchestration.
 */
class ExampleManager {

    /**
     * Handles message acknowledgments.
     */
    private final AcknowledgmentManager acknowledgmentManager

    /**
     * Handles putting messages on the event bus.
     */
    private final EventGateway gateway

    ExampleManager( final AcknowledgmentManager aAcknowledgmentManager, final EventGateway aGateway ) {
        acknowledgmentManager = aAcknowledgmentManager
        gateway = aGateway
    }

    void manage( ExampleData data ) {
        def key = acknowledgmentManager.manage( data )
        gateway.sendEvent( data, key )
    }
}
