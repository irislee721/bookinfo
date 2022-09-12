FROM image-registry.openshift-image-registry.svc:5000/openshift/ubi8-openjdk-8:1.3

#FROM registry.access.redhat.com/ubi8/openjdk-8

USER 0

RUN mkdir /APP
WORKDIR /APP
ADD ./target/*.jar /APP/aaa.jar

CMD java -jar aaa.jar
