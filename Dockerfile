FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw || true

RUN ./mvnw package -DskipTests || mvn package -DskipTests

EXPOSE 8080

CMD ["java","-jar","target/gse-render-simple-1.0.0.jar"]