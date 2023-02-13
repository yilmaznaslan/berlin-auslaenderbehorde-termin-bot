echo "starting the elasticsearch"
docker run \
  --name elasticsearch \
  --net termin \
  -p 9200:9200 \
  -p 9300:9300 \
  -e discovery.type=single-node \
  -e ES_JAVA_OPTS="-Xms1g -Xmx2g" \
  -e xpack.security.enabled=false \
  -d \
  -t docker.elastic.co/elasticsearch/elasticsearch:8.5.3
