FROM openjdk:17-alpine

COPY build/libs/berlinTerminFinder-1.0-SNAPSHOT-all.jar /berlinTerminFinder-1.0-SNAPSHOT-all.jar

CMD ["java", "-jar", "berlinTerminFinder-1.0-SNAPSHOT-all.jar"]