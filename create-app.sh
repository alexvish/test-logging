#!/bin/sh

set -e

APP_NAME=test-logging
DOCKER_FROM=openjdk:8

oc new-build --name $APP_NAME --strategy docker --binary --docker-image $DOCKER_FROM
oc start-build -F $APP_NAME --from-dir .
oc new-app $APP_NAME
