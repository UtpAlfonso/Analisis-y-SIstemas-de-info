# --- Etapa 1: Construcción (Usa un contenedor con Maven y Java para compilar) ---
FROM maven:3.8.5-openjdk-17 AS build

# Crea una carpeta /app dentro del contenedor para trabajar
WORKDIR /app

# Copia solo el pom.xml para descargar las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia el resto del código fuente
COPY src ./src

# Compila y empaqueta la aplicación en un archivo .jar, omitiendo los tests
RUN mvn package -DskipTests

# --- Etapa 2: Ejecución (Usa un contenedor ligero solo con Java para correr la app) ---
FROM openjdk:17-jdk-slim

# Crea la carpeta de trabajo
WORKDIR /app

# Copia el archivo .jar creado en la etapa anterior y renómbralo a app.jar
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto 8080 para que Render pueda comunicarse con tu aplicación
EXPOSE 8080

# Comando que se ejecutará cuando el contenedor inicie
ENTRYPOINT ["java", "-jar", "app.jar"]