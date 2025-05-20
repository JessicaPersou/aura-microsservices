# AURA MICROSSERVICES

Repositório do Sistema de E-commerce em Microsserviços Para o Tech Challenge - Fase 4: 
- Pedido Recebedor (Order Receiver)
- Pedido (Order) 
- Produto (Product) 
- Cliente (Client)
- Estoque (Stock)
- Pagamento (Payment)

## Pré-requisitos

- Docker e Docker Compose instalados no seu sistema
- Git (para clonar o repositório)
- JDK 21
- Maven 

## Estrutura do Projeto

Cada serviço tem seu próprio banco de dados (PostgreSQL) e utiliza Flyway para migrações de banco de dados.

## Como Iniciar

### 1. Clone o Repositório

```bash
git clone https://github.com/seuusuario/aura-microsservicos.git
cd aura-microsservicos
```

### 2. Configure o Ambiente

Certifique-se de que todos os scripts de migração estejam em seus locais adequados:
- Migrações de Pedido: `./aura-ms-order/src/main/resources/db/migration/`
- Migrações de Produto: `./aura-ms-product/src/main/resources/db/migration/`
- Migrações de Cliente: `./aura-ms-client/src/main/resources/db/migration/`
- Migrações de Estoque: `./aura-ms-stock/src/main/resources/db/migration/`
- Migrações de Pagamento: `./aura-ms-payment/src/main/resources/db/migration/`

### 3. Inicie a Infraestrutura

Inicie todos os serviços usando Docker Compose:

```bash
docker-compose up -d
```

Este comando irá:
- Iniciar bancos de dados PostgreSQL para cada microsserviço
- Executar migrações Flyway para cada serviço
- Iniciar o LocalStack para emulação de serviços AWS (SQS)
- Criar as filas SQS necessárias

### 4. Verifique os Serviços

Verifique se todos os contêineres estão em execução:

```bash
docker-compose ps
```

Você deve ver todos os contêineres em execução sem erros.

### 5. Acesso aos Bancos de Dados

Os bancos de dados estão acessíveis com as seguintes credenciais:

| Serviço  | Host     | Porta | Banco de Dados   | Usuário  | Senha    |
|----------|----------|-------|------------------|----------|----------|
| Order    | localhost| 5432  | AURA_MS_ORDER    | postgres | postgres |
| Product  | localhost| 5433  | AURA_MS_PRODUCT  | postgres | postgres |
| Client   | localhost| 5434  | AURA_MS_CLIENT   | postgres | postgres |
| Stock    | localhost| 5435  | AURA_MS_STOCK    | postgres | postgres |
| Payment  | localhost| 5436  | AURA_MS_PAYMENT  | postgres | postgres |

### 6. Acesso ao AWS SQS

O LocalStack fornece emulação de serviços AWS. A fila SQS pode ser acessada em:

```
http://localhost:4566/000000000000/new-order-queue
```

Para interagir com o SQS usando AWS CLI:

```bash
# Listar filas
aws --endpoint-url=http://localhost:4566 sqs list-queues

# Enviar uma mensagem
aws --endpoint-url=http://localhost:4566 sqs send-message --queue-url http://localhost:4566/000000000000/new-order-queue --message-body '{"orderId":"123"}'

# Receber mensagens
aws --endpoint-url=http://localhost:4566 sqs receive-message --queue-url http://localhost:4566/000000000000/new-order-queue
```

## Fluxo de Desenvolvimento

1. Cada microsserviço deve se conectar ao seu respectivo banco de dados usando os mapeamentos de porta definidos no arquivo Docker Compose
2. Os serviços devem ser configurados para usar o endpoint do LocalStack para comunicação SQS
3. Ao desenvolver e testar localmente, use `localhost` como host para todos os serviços

## Parando o Ambiente

Para parar todos os contêineres:

```bash
docker-compose down
```

Para parar e remover todos os volumes (isso excluirá todos os dados):

```bash
docker-compose down -v
```

## Solução de Problemas

### Problemas com Migrações Flyway

Se você encontrar problemas com as migrações Flyway:

1. Verifique os logs para mensagens de erro específicas:

```bash
docker logs order_flyway
```

2. Certifique-se de que os arquivos de migração seguem as convenções de nomenclatura do Flyway: `V1__Descricao.sql`

### Problemas de Conexão com Banco de Dados

Se os serviços não conseguirem se conectar aos bancos de dados:

1. Certifique-se de que as portas corretas estão sendo usadas nas suas strings de conexão
2. Verifique se os bancos de dados estão em execução:

```bash
docker ps | grep postgres
```

### Problemas com SQS

Se houver problemas com o SQS:

1. Verifique os logs do LocalStack:

```bash
docker logs localstack
```

2. Verifique se a fila foi criada:
```bash
aws --endpoint-url=http://localhost:4566 sqs list-queues
```

## Informações Adicionais

- A configuração do Docker Compose usa rede bridge, então todos os serviços podem se comunicar entre si
- Volumes persistentes são usados para armazenamento de banco de dados
- O LocalStack está configurado para emular o AWS SQS para mensagens entre serviços

