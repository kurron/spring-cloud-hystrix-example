#!/bin/bash

export COMPOSE_HTTP_TIMEOUT=120
export COMPOSE_PROJECT_NAME=build-test
# all-in-one
/usr/local/bin/docker-compose --file build-via-docker/docker-compose-all-in-one-step.yml up --no-color --remove-orphans
/usr/local/bin/docker-compose --file build-via-docker/docker-compose-all-in-one-step.yml down --remove-orphans --volumes --rmi all