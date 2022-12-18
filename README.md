# Berlin Termin Bot


## How to setup
### 1. Install Chromedriver

#### MacOs

`wget https://chromedriver.storage.googleapis.com/108.0.5359.71/chromedriver_mac64.zip`

curl https://chromedriver.storage.googleapis.com/108.0.5359.71/chromedriver_mac64.zip --output chromedriver_mac64.zip

unzip chromedriver_mac64.zip

rm chromedriver_mac64.zip

#### Linux

## How to dockerize
`docker build --tag 'berlinterminfinder:latest' .` 

``

# Setup
#COPY build/libs/berlinTerminFinder-1.0-SNAPSHOT-all.jar /opt/hello/

#EXPOSE 80
#WORKDIR /opt/hello
#CMD ["java", "-jar", "berlinTerminFinder-1.0-SNAPSHOT-all.jar"]


## ToDo's

- java.util.concurrent.TimeoutException error


# Root logger option
log4j.rootLogger=info, stdout, FILE
log=/logs

# Redirect log messages to console
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.out
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-2p  %c{1} - %m%n

# Write log messages to log files
log4j.appender.FILE=org.apache.log4j.rolling.RollingFileAppender
log4j.appender.FILE.RollingPolicy=org.apache.log4j.rolling.TimeBasedRollingPolicy
log4j.appender.FILE.RollingPolicy.ActiveFileName=${log}/log-0.log
log4j.appender.FILE.RollingPolicy.FileNamePattern=${log}/log-%d{yyyy-MM-dd-HH_mm_ss}.log
log4j.appender.FILE.RollingPolicy.maxIndex=10000000
log4j.appender.FILE.TriggeringPolicy=org.apache.log4j.rolling.SizeBasedTriggeringPolicy
log4j.appender.FILE.TriggeringPolicy.MaxFileSize=1000000
log4j.appender.FILE.layout=org.apache.log4j.PatternLayout
log4j.appender.FILE.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1} - %m%n

