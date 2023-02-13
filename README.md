# Berlin Termin Bot

This application uses Selenium library to automate the process of getting an appointment in Berlin Ausländerbehörde.
Instead of notifying the person like other solutions, this application automatically **books** for you the requested *Termin* 

## How to run
- [ ] Create   

## How to deploy using AWS EC2 

[ - ] *t3.nano* failed.   Price: *$0.0052*

- [-] Running the selenium in *t2.medium* worked! Price  *$0.0464*
- t3a.micro worked only for selenium and finder. Not for elastic

- [x] **t3a.small**: Works

- t4g.micro was not available


Other recommandations
- t4g.small
- t4g.medium

![](doc/ec2_price.png)

docker exec -it 4c85e0506977 /bin/bash

## How to dockerize

`docker build --tag 'yilmaznaslan/berlinterminfinder:latest' .`

docker build --tag yilmaznaslan/berlinterminfinder:latest --file DockerFileForDebian

docker build -t yilmaznaslan/berlinterminfinder:pi -f DockerfileForDebian .

`docker push yilmaznaslan/berlinterminfinder:latest`

`docker push yilmaznaslan/berlinterminfinder:pi`


`sudo docker run --name termifinder --network=termin yilmaznaslan/berlinterminfinder:latest`


docker exec -ti 18b3d6e1415b sh
