docker run \
  --name terminfinder \
  --net termin \
  -e SELENIUM_GRID_HOST='selenium' \
  -d \
  -t yilmaznaslan/berlinterminfinder:latest
