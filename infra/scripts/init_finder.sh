sudo docker run \
    -d \
    --name terminfinder \
    --net termin \
    -e SELENIUM_GRID_HOST='selenium'\
    -t yilmaznaslan/berlinterminfinder:latest

