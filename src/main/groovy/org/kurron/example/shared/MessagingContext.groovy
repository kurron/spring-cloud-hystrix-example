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

import org.kurron.feedback.Audience
import org.kurron.feedback.FeedbackContext
import org.kurron.feedback.FeedbackLevel

/**
 * The enumeration of all logged messages produced by the system.
 **/
@SuppressWarnings( 'LineLength' )
enum MessagingContext implements FeedbackContext {

    CURRENT_TIME( 1000, FeedbackLevel.WARN, Audience.QA, 'The current time is {}.' ),
    FORCED_ERROR( 1001, FeedbackLevel.ERROR, Audience.OPERATIONS, 'I was forced to fail!' ),
    GENERATED_TRACING_HEADER( 1002, FeedbackLevel.DEBUG, Audience.DEVELOPMENT, 'Generated trace id for OPTIONS call: {} = {}' ),
    MISSING_HTTP_HEADER_ERROR( 1003, FeedbackLevel.WARN, Audience.QA, 'Required {} header is missing!' ),
    GENERIC_ERROR( 1007, FeedbackLevel.ERROR, Audience.QA, 'The following error has occurred and was caught by the global error handler: {}'  ),
    INTENTIONAL_ERROR( 1008, FeedbackLevel.ERROR, Audience.QA, 'Failing on purpose for this reason: {}'  ),
    VALIDATION_ERROR( 1009, FeedbackLevel.INFO, Audience.QA, 'The property {} is invalid. Cause: {}' ),
    PUBLICATION_FAILURE( 1010, FeedbackLevel.ERROR, Audience.SUPPORT, 'The descriptor could not be published because of this error: {}' ),
    RETRY_UNROUTABLE( 1011, FeedbackLevel.ERROR, Audience.QA, 'A message could not be routed, likely due to a configuration error. code: {}, text: {}, exchange: {}, routing key: {}' ),
    RETRY_ACKNOWLEDGE( 1012, FeedbackLevel.DEBUG, Audience.DEVELOPMENT, 'The descriptor {} has been acknowledged by RabbitMQ.' ),
    RETRY_UNACKNOWLEDGED( 1013, FeedbackLevel.WARN, Audience.QA, 'The descriptor {} has not been acknowledged by RabbitMQ due to {}. Will retry later.' ),

    private final int code
    private final String formatString
    private final FeedbackLevel feedbackLevel
    private final Audience audience

    MessagingContext( final int aCode,
                      final FeedbackLevel aFeedbackLevel,
                      final Audience aAudience,
                      final String aFormatString ) {
        code = aCode
        formatString = aFormatString
        feedbackLevel = aFeedbackLevel
        audience = aAudience
    }

    @Override
    int getCode() {
        code
    }

    @Override
    String getFormatString() {
        formatString
    }

    @Override
    FeedbackLevel getFeedbackLevel() {
        feedbackLevel
    }

    @Override
    Audience getAudience() {
        audience
    }
}
