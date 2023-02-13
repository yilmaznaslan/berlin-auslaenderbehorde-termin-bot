sudo yum update -y &&\
sudo yum install vim -y &&\
sudo yum install docker -y &&\
sudo usermod -a -G docker ec2-user &&\
sudo service docker start &&\
sudo docker network create termin