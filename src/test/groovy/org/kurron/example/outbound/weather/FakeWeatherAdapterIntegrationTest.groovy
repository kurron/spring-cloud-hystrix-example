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

import groovy.util.logging.Slf4j
import org.kurron.example.Application
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 * Put the FakeWeatherAdapter through its paces.
 **/
@Slf4j
@IntegrationTest
@ContextConfiguration( classes = Application, loader = SpringApplicationContextLoader )
class FakeWeatherAdapterIntegrationTest extends Specification {

    // Since Spring is generating proxies for us, we have autowire by the interface or the bean won't be found for autowiring. The proxy type won't be FakeWeatherAdapter.
    @Autowired
    WeatherPort sut

    def 'happy path'() {
        given: 'a city name'
        def city = 'Boston'

        when: 'the weather is asked for'
        def weather = sut.currentWeather( city )

        then: 'the city is part of the current conditions'
        log.info( '{}', weather )
        weather.contains( city )
    }

    def 'sad path'() {
        given: 'a city name'
        def city = 'Miami'

        when: 'the weather is asked for'
        def weather = sut.currentWeather( city )

        then: 'the cached results are used'
        log.info( '{}', weather )
        weather.contains( 'off-line' )
    }

    // TODO: put in tests that showcase the breaker transitioning between closed, open, half-open and closed states
}
