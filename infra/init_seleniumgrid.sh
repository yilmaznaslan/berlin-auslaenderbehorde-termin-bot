sudo docker run \
    --name selenium \
    --net termin \
    -p 4444:4444 -p 7900:7900 \
    --shm-size="1g" \
    -d \
    -e SE_NODE_MAX_SESSIONS=5 \
    -e SE_NODE_OVERRIDE_MAX_SESSIONS=true \
    -e SE_NODE_SESSION_TIMEOUT=120 \
    -t selenium/standalone-chrome:latest