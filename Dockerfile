FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y maven

COPY . /app/repo

# Combinando cd e mvn clean install em uma única instrução RUN
RUN cd /app/repo && mvn clean install

# Ajustando o caminho para copiar o JAR construído para o diretório correto
RUN mkdir -p /app/jar && cp /app/repo/target/*.jar /app/jar/myapp.jar

RUN apt-get remove --purge -y maven && \
    apt-get autoremove -y && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /app/repo

WORKDIR /app/jar

# Expondo a porta 8080
EXPOSE 8080

CMD ["java", "-jar", "myapp.jar"]