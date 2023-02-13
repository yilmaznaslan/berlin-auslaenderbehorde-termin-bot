echo "Starting the kibana"
sudo docker run \
  --name kibana \
  --net termin \
  -p 5601:5601 \
  -e xpack.security.enabled=false \
  -d \
  -t docker.elastic.co/kibana/kibana:8.5.3
