# Define a imagem base
FROM adoptopenjdk:17-jdk-hotspot

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiando o arquivo JAR da aplicação para o diretório de trabalho
COPY target/wallet-service.jar .

# Comando a ser executado quando o contêiner for iniciado
CMD ["java", "-jar", "wallet-service.jar"]
