# Usamos a imagem completa do Maven com Java 21
FROM maven:3.9.6-eclipse-temurin-21
WORKDIR /app

# Copiamos o projeto inteiro para dentro do container
COPY . .

# Expomos a porta do Spring
EXPOSE 8080

# O comando que você usa na sua máquina
CMD ["mvn", "spring-boot:run"]