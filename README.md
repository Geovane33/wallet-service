# wallet-service 🎩💰
Este repositório contém o código-fonte e os recursos relacionados ao Wallet-Service, um microserviço dedicado a gerenciar carteiras digitais de usuários.

## Principal recurso 🚀
✨ Gerenciamento de carteiras: O Wallet-Service permite aos usuários criar, atualizar e excluir carteiras digitais. Ele também oferece recursos de consulta para recuperar informações específicas de uma carteira, como saldo atual e histórico de transações.

## Como começar 🏁

1. Clone este repositório em sua máquina local.


# Setup da aplicação (local)
## Pré-requisito
Antes de rodar a aplicação é preciso garantir que as seguintes dependências estejam corretamente instaladas:
```
Java 17
Maven 3.9.2
Mysql 8.0.27
```

Observação: Para fins de avaliação ou execução em um ambiente local de teste, o projeto está configurado com o banco de dados H2 por conveniência.
Se você deseja usar um servidor MySQL, siga as instruções as seguintes para configurar corretamente a conexão com o MySQL.
```
spring:
  datasource:
    drive-class-name: com.mysql.cj.jdbc.Driver
    url: coloque_aqui_a_url
    username: coloque_aqui_o_username
    password: coloque_aqui_o_password
```


## Instalação da aplicação

Primeiramente, faça o clone do repositório:
```
https://github.com/emmanuelneri/productivity-with-spring.git
```
Feito isso, acesse o projeto:
```
cd productivity-with-spring
```
É preciso compilar o código e baixar as dependências do projeto:
```
mvn clean package
```
Finalizado esse passo, vamos iniciar a aplicação:
```
mvn spring-boot:run
```
Pronto. A aplicação está disponível em http://localhost:8080
```
Tomcat started on port(s): 8080 (http)
Started AppConfig in xxxx seconds (JVM running for xxxx)