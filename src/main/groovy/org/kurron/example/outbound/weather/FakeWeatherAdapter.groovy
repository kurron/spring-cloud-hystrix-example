/*
 * Copyright (c) 2016 Ronald D. Kurr kurr@jvmguy.com
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

package org.kurron.example.outbound.weather

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand

/**
 * A fake gateway that we can use to simulate timeouts and error conditions.
 **/
class FakeWeatherAdapter implements WeatherPort {

    @HystrixCommand( fallbackMethod = 'cachedConditions' )
    @Override
    String currentWeather( final String location ) {
        // ugly because of early return but works
        if ( location == 'Boston' ) {
            return 'The weather in Boston is Sunny'
        }
        else {
            throw new RuntimeException( 'Failing on purpose' )
        }
    }

    // NOTE: signature of the fallback method must match the primary method or Hystrix gets cranky
    @SuppressWarnings( 'GroovyUnusedDeclaration' )
    static String cachedConditions( String location ) {
        "Weather service is currently off-line but the last known conditions for ${location} were: Sunny"
    }
}
