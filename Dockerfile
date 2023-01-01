FROM openjdk:17-alpine

# copy the packaged jar file into our docker image
COPY build/libs/berlinTerminFinder-1.0-SNAPSHOT-all.jar /berlinTerminFinder-1.0-SNAPSHOT-all.jar

EXPOSE 80
CMD ["java", "-jar", "berlinTerminFinder-1.0-SNAPSHOT-all.jar"]