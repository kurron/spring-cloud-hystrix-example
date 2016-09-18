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
package org.kurron.example.inbound.rest

import cucumber.api.PendingException
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import groovy.util.logging.Slf4j
import org.kurron.example.Application
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration

/**
 * Step definitions geared towards Enchantment's acceptance test but remember, all steps are used
 * by Cucumber unless special care is taken. If you word your features in a consistent manner, then
 * steps will automatically get reused and you won't have to keep writing the same test code.
 **/
@SuppressWarnings( ['MethodCount'] )
@Slf4j
// ====> this is the configuration to use when hitting a Docker container
@IntegrationTest
@ContextConfiguration( classes = [Application, AcceptanceTestConfiguration], loader = SpringApplicationContextLoader )

// ====> this is the configuration to use when hitting an embedded application
// @WebIntegrationTest( randomPort = true )
// @ContextConfiguration( classes = [Application, AcceptanceTestConfiguration], loader = SpringApplicationContextLoader )
class TestSteps {
    /**
     * This is state shared between steps and can be setup and torn down by the hooks.
     **/
    class MyWorld {
        ResponseEntity<HypermediaControl> uploadEntity
        ResponseEntity<byte[]> downloadEntity
        byte[] bytes = new byte[0]
        def headers = new HttpHeaders()
        def mediaType  = MediaType.APPLICATION_JSON
        URI uri
        HttpStatus statusCode = HttpStatus.I_AM_A_TEAPOT
    }

    /**
     * Shared between hooks and steps.
     **/
    MyWorld sharedState

    @Before
    void assembleSharedState() {
        log.info( 'Creating shared state' )
        sharedState = new MyWorld()
    }

    @After
    void destroySharedState() {
        log.info( 'Destroying shared state' )
        sharedState = null
    }

    @When( '^a successful conversion has been executed$' )
    void 'a successful conversion has been executed'() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException()
    }

    @Then( '^the event is emitted$' )
    void 'the event is emitted'() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException()
    }
/*
    @Given( '^an X-Correlation-Id header filled in with a unique request identifier$' )
    void 'an X-Correlation-Id header filled in with a unique request identifier'() {
    }

    @When( '^a PUT request is made with the B4U in the body$' )
    void 'a PUT request is made with the B4U in the body'() {
    }

    @Then( '^a response with a (\\d+) HTTP status code is returned$' )
    void 'a response with an HTTP status code is returned'( int statusCode ) {
    }
*/

}

