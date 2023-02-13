docker run \
    --name selenium-test-junit5 \
    --net elastic \
    -p 4445:4444 -p 7905:7905 \
    --shm-size="2g" \
    -d \
    -t selenium/standalone-chrome:latest