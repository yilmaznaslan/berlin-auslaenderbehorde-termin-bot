# elasticsearch_setup

## Docker
docker exec -it 5bb35835a984 bash

Setup docker things in raspberry pi


sudo usermod -aG docker $USER
sudo reboot


docker inspect selenium | grep -i shm
`docker container ls`
## Installation



## Kubernetes setup

Deployment
`kubectl create -f selenium-hub-deployment.yaml`



### Setting up selenium grid
java -jar selenium-server-4.7.0.jar standalone