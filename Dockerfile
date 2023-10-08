FROM openjdk:17-alpine

COPY build/libs/berlin-auslaenderbehorde-termin-bot-dev-all.jar /berlin-auslaenderbehorde-termin-bot-dev-all.jar

CMD ["java", "-jar", "berlin-auslaenderbehorde-termin-bot-dev-all.jar"]