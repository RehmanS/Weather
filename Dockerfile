FROM openjdk:19 AS build
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw package
# Bu aşamanın sonucunda, derlenmiş uygulama JAR dosyası mevcut olacak.

FROM openjdk:19
WORKDIR weather
COPY --from=build target/*.jar weather.jar
ENTRYPOINT ["java","-jar","weather.jar"]
# Bu aşamada ise, önceki "build" aşamasının sonucunda oluşan derlenmiş JAR dosyasını
# alıp Docker imajınızın son hâlini oluşturuyorsunuz.