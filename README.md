# wallet-service 🎩💰

Este repositório contém o código-fonte e os recursos relacionados ao Wallet-Service, um microserviço dedicado a gerenciar carteiras digitais de usuários.

## Principal recurso 🚀
✨ Gerenciamento de carteiras: O Wallet-Service permite aos usuários criar, atualizar e excluir carteiras digitais. Ele também oferece recursos de consulta para recuperar informações específicas de uma carteira, como saldo atual e histórico de transações.

# Setup da aplicação (local)
## Pré-requisitos
Antes de executar a aplicação, certifique-se de ter as seguintes dependências instaladas corretamente em seu ambiente de desenvolvimento:
```
- Java 17
- Maven 3.9.2
- MySQL 8.0.27
- RabbitMQ 3.11.5
```

**Observação:** Para fins de avaliação ou execução em um ambiente local de teste, o projeto já está configurado com o banco de dados **H2** e **RabbitMQ**. No entanto, se você deseja usar seu próprio servidor **MySQL** e/ou **RabbitMQ**, siga as seguintes instruções:

### MySQL
No arquivo de configuração `application.yml`, adicione as seguintes configurações para o MySQL:


```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: sua_url
    username: seu_usuario
    password: sua_senha
```

Certifique-se de substituir `sua_url`, `seu_usuario` e `sua_senha` pelas informações corretas do seu servidor MySQL.

### RabbitMQ
No arquivo de configuração  `application.yml`, adicione as seguintes configurações para o RabbitMQ:



```yaml
spring:
  rabbitmq:
    host: seu_host
    port: sua_porta
    username: seu_usuario
    password: sua_senha
```

Certifique-se de substituir `seu_host`, `sua_porta`, `seu_usuario` e `sua_senha` pelas informações corretas do seu servidor RabbitMQ.

## Instalação e execução da aplicação

1. Clone o repositório:
```
git clone git@github.com:Geovane33/wallet-service.git
```

2. Acesse o diretório do projeto:
```
cd wallet-service
```

3. Compile o código e baixe as dependências do projeto:
```
mvn clean package
```

4. Inicie a aplicação:
```
mvn spring-boot:run
```

- Após executar esses passos, a aplicação estará disponível em http://localhost:8080.
- O projeto está utilizando FlywayDB e nesse caso, para facilitar os testes locais, o banco de dados já terá 4 Wallets.
- Acesse a documentação da API em http://localhost:8080/swagger-ui/index.html para obter mais detalhes sobre os endpoints disponíveis.

# Setup da aplicação usando Docker

## Pré-requisitos
Antes de executar a aplicação, considerando que o projeto ja vem com H2 e RabbitMQ configurados, certifique-se de ter as seguintes dependências instaladas corretamente em seu ambiente de desenvolvimento:
```
- Java 17
- Docker 24.0.2
- Maven 3.9.2
```
1. Clone o repositório:
```
git clone git@github.com:Geovane33/wallet-service.git
```

2. Acesse o diretório do projeto:
```
cd wallet-service
```

3. Compile o código e baixe as dependências do projeto:
```
mvn clean package
```

4. Construa a imagem Docker:
```
docker build -t wallet-service .
```

5. O serviço forcene informaçoes via feignClient para [payment-transfer-service](https://github.com/Geovane33/payment-transfer-service) então caso não tenha criado, crie uma rede para conectar os containers:
```
docker network create local
```

6. Execute o contêiner Docker conectado à rede local:
```
docker run -d -p 8080:8080 --network local --name wallet-service wallet-service
```

- Após executar esses passos, a aplicação estará disponível em http://localhost:8080.
- Acesse a documentação da API em http://localhost:8080/swagger-ui/index.html para obter mais detalhes sobre os endpoints disponíveis.

Observações:
- A opção `-p 8080:8080` mapeia a porta 8080 do contêiner para a porta 8080 do host. Você pode alterar a porta do host, se desejar.
- O parâmetro `--network local` conecta o contêiner à rede local que foi criada.
- O parâmetro `--name wallet-service` define o nome do contêiner como "payment-transfer-service". Você pode escolher um nome diferente, se desejar.
