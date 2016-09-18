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

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.Canonical

/**
 * Describes how a descriptor has changed.
 */
@Canonical
class ExampleEvent {

    /**
     * UUID of the event.
     */
    @JsonProperty( 'id' )
    String id

    /**
     * ISO-8601 timestamp of when the event was emitted.
     */
    @JsonProperty( 'timestamp' )
    String timestamp

    /**
     * ISO-8601 timestamp of when the event is to be applied. Used in scenarios
     * where we have to adjust the event stream and inject a compensating event.
     */
    @JsonProperty( 'effective-timestamp' )
    String effectiveTimestamp

    /**
     * The non-volatile portion of the event.
     */
    @JsonProperty( 'data' )
    ExampleData data
}
