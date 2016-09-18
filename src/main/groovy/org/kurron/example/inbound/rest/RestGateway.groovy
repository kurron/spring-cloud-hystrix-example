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

import java.time.Instant
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import org.kurron.example.core.TimeComponent
import org.kurron.example.shared.MessagingContext
import org.kurron.feedback.AbstractFeedbackAware
import org.kurron.stereotype.InboundRestGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.mvc.ControllerLinkBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.HandlerMapping

/**
 * Inbound HTTP gateway that supports the FIXME resource.
 **/
@SuppressWarnings( 'GroovyUnusedDeclaration' )
@InboundRestGateway
@RequestMapping
class RestGateway extends AbstractFeedbackAware {

    /**
     * Knows how to get the most accurate time.
     */
    private final TimeComponent theComponent

    @Autowired
    RestGateway( final TimeComponent aComponent ) {
        theComponent = aComponent
    }

    @RequestMapping( path = '/descriptor/application', method = [RequestMethod.GET], produces = [HypermediaControl.MIME_TYPE] )
    ResponseEntity<HypermediaControl> fetchApplicationList( HttpServletRequest request ) {
        def control = defaultControl( request )
        control.add( ControllerLinkBuilder.linkTo( RestGateway ).withSelfRel() )
        control.time = theComponent.currentTime().toString()
        ResponseEntity.ok( control )
    }

    @SuppressWarnings( 'ThrowRuntimeException' )
    @RequestMapping( path = '/descriptor/fail', method = [RequestMethod.GET], produces = [HypermediaControl.MIME_TYPE] )
    ResponseEntity<HypermediaControl> failApplicationList() {
        throw new RuntimeException( 'Failure -- system exception' )
    }

    @RequestMapping( path = '/descriptor/fail/application', method = [RequestMethod.GET], produces = [HypermediaControl.MIME_TYPE] )
    ResponseEntity<HypermediaControl> failSystemApplicationList() {
        throw new ForcedApplicationError( MessagingContext.FORCED_ERROR )
    }

    @RequestMapping( path = '/descriptor/application', method = [RequestMethod.PUT],  consumes = [HypermediaControl.MIME_TYPE], produces = [HypermediaControl.MIME_TYPE] )
    ResponseEntity<HypermediaControl> showcaseValidationHandling( @RequestBody @Valid HypermediaControl input,
                                                                                      Errors errors,
                                                                                      HttpServletRequest request ) {
        final ResponseEntity<HypermediaControl> response
        def control = defaultControl( request )
        control.add( ControllerLinkBuilder.linkTo( RestGateway ).withSelfRel() )

        ResponseEntity.ok( control )

        if ( errors.hasErrors() ) {
            addErrors( errors, control )
            response = ResponseEntity.badRequest().body( control )
        }
        else {
            control.time = theComponent.currentTime().toString()
            response = ResponseEntity.ok( control )
            //TODO: do some real work
            input.fragment
        }

        response
    }

    private HypermediaControl addErrors( Errors errors, HypermediaControl toAugment ) {
        toAugment.status = HttpStatus.BAD_REQUEST.value()
        def validationErrors = errors.fieldErrors.collect {
            feedbackProvider.sendFeedback( MessagingContext.VALIDATION_ERROR, it.field, it.defaultMessage )
            new ValidationError( field: it.field, reason: it.defaultMessage )
        }
        toAugment.errorBlock = new ErrorBlock( code: MessagingContext.VALIDATION_ERROR.code,
                                               message: 'The uploaded descriptor is invalid.  Please correct the issues and try again.',
                                               developerMessage: 'Certain properties in the payload are invalid.',
                                               validationErrors: validationErrors )
        toAugment
    }

    private static HypermediaControl defaultControl( HttpServletRequest request ) {
        def path = request.getAttribute( HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE ) as String
        new HypermediaControl( status: HttpStatus.OK.value(), timestamp: Instant.now().toString(), path: path )
    }

}
