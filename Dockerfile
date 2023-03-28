FROM openjdk:17-alpine

COPY build/libs/berlin-auslaenderbehorde-termin-bot-1.0-SNAPSHOT-all.jar /berlin-auslaenderbehorde-termin-bot-1.0-SNAPSHOT-all.jar

CMD ["java", "-jar", "berlinTerminFinder-1.0-SNAPSHOT-all.jar"]