# Étape 1 : Builder avec Maven + Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copier tout le code dans l'image
COPY . .

# Compiler le projet (crée un .jar dans target/)
RUN mvn clean package -DskipTests

# Étape 2 : Image finale légère avec juste le JAR
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copier le .jar généré depuis l'étape précédente
COPY --from=build /app/target/*.jar app.jar

# Exposer le port (à adapter si ton app écoute sur un autre)
EXPOSE 8080

# Commande de lancement
ENTRYPOINT ["java", "-jar", "app.jar"]
