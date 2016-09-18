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

    @HystrixCommand( fallbackMethod = 'fallbackData' )
    @Override
    String currentWeather( final String location ) {
        'foo'
    }

    @SuppressWarnings( "GroovyUnusedDeclaration" )
    String fallbackData() {
        'Weather service is currently off-line but the last known conditions were: Sunny'
    }
}