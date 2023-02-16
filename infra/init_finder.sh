docker run \
  --name terminfinder \
  --net termin \
  -e SELENIUM_GRID_HOST='selenium' \
  -e ELASTICSEARCH_HOST='elasticsearch' \
  -d \
  -t yilmaznaslan/berlinterminfinder:release-1
