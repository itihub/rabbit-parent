#!/bin/sh

docker run -d --name some-rabbit \
    -p 15672:15672  -p  5672:5672 \
    -e RABBITMQ_DEFAULT_USER=admin \
    -e RABBITMQ_DEFAULT_PASS=admin \
    rabbitmq:3.8.3-management-alpine

