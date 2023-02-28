docker run \
  --name terminfinder \
  --net termin \
  -e SELENIUM_GRID_HOST='selenium' \
  -e ELASTICSEARCH_HOST='elasticsearch' \
  -e AWS_ACCESS_KEY_ID='AKIATUWIY5SL44BVYTX6' \
  -e AWS_SECRET_ACCESS_KEY='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' \
  -d \
  -t yilmaznaslan/berlinterminfinder:add_tests
