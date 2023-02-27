sudo yum update -y &&\
sudo yum install vim -y &&\
sudo yum install docker -y &&\
sudo usermod -a -G docker ec2-user &&\
sudo service docker start &&\
sudo docker network create termin


sudo docker run \
  --name selenium \
  --net termin \
  -p 4444:4444 -p 7900:7900 -p 5900:5900 \
  --shm-size="1g" \
  -d \
  -e SE_NODE_MAX_SESSIONS=5 \
  -e SE_NODE_OVERRIDE_MAX_SESSIONS=true \
  -e SE_NODE_SESSION_TIMEOUT=120 \
  -t selenium/standalone-chrome:latest


echo "starting the elasticsearch"
sudo docker run \
  --name elasticsearch \
  --net termin \
  -p 9200:9200 \
  -p 9300:9300 \
  -e discovery.type=single-node \
  -e ES_JAVA_OPTS="-Xms1g -Xmx2g" \
  -e xpack.security.enabled=false \
  -d \
  -t docker.elastic.co/elasticsearch/elasticsearch:8.5.3

echo "Starting the kibana"
sudo docker run \
  --name kibana \
  --net termin \
  -p 5601:5601 \
  -e xpack.security.enabled=false \
  -d \
  -t docker.elastic.co/kibana/kibana:8.5.3

sudo docker run \
  --name terminfinder \
  --net termin \
  -e SELENIUM_GRID_HOST='selenium' \
  -e ELASTICSEARCH_HOST='elasticsearch' \
  -d \
  -t yilmaznaslan/berlinterminfinder:release-1
