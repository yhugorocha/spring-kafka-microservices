# iCompras | Microservicos com Spring Boot e Kafka

Projeto de estudo com arquitetura de microservicos para simular o fluxo de compras de um e-commerce. A solucao combina comunicacao sincrona via REST entre servicos de dominio e comunicacao assincrona via Kafka para orquestrar faturamento e logistica.

O fluxo principal cobre cadastro de clientes e produtos, criacao de pedidos, simulacao de pagamento, geracao de nota fiscal em PDF e atualizacao do pedido com URL da nota e codigo de rastreio.

## Diagrama da arquitetura

Use esta secao para adicionar a imagem da arquitetura no GitHub.

Exemplo de como referenciar a imagem:

```md
![Arquitetura da solucao](./docs/arquitetura.png)
```

Sugestao:
- salve a imagem em `docs/arquitetura.png`
- substitua o bloco acima pela tag Markdown definitiva quando quiser exibir a imagem no README

## Visao geral da arquitetura

O projeto esta dividido em cinco modulos principais:

| Modulo | Porta | Papel na arquitetura | Dependencias principais |
| --- | --- | --- | --- |
| `product` | `8081` | Catalogo de produtos e validacao de IDs enviados no pedido. | PostgreSQL |
| `clients` | `8082` | Cadastro e consulta de clientes. | PostgreSQL |
| `orders` | `8083` | Servico central do fluxo. Cria pedidos, valida cliente/produtos, recebe callback de pagamento e consolida status. | PostgreSQL, OpenFeign, Kafka |
| `invoicing` | `8084` | Consome pedidos pagos, gera PDF da nota fiscal com JasperReports, envia o arquivo ao MinIO e publica o evento de faturamento. | Kafka, MinIO, JasperReports |
| `logistics` | `8085` | Consome pedidos faturados, gera codigo de rastreio e publica o evento de envio. | Kafka |

O repositorio tambem possui o modulo `icompras-services`, usado como apoio local para infraestrutura e bootstrap:

- `icompras-services/database`: `docker-compose.yml` do PostgreSQL e arquivo de referencia do schema.
- `icompras-services/broker`: `docker-compose.yml` do Kafka e Kafka UI.
- `icompras-services/bucket`: `docker-compose.yml` do MinIO.

## Fluxo de negocio

1. O cliente e os produtos sao cadastrados pelos servicos `clients` e `product`.
2. Um pedido e criado no servico `orders`.
3. O servico `orders` valida cliente e produtos de forma concorrente usando OpenFeign.
4. O pagamento e simulado no proprio `orders`, que gera um `paymentKey`.
5. Um callback externo confirma ou reprova o pagamento em `/api/v1/callback-payment`.
6. Quando o pagamento e aprovado, `orders` publica um evento no topico `icompras.paid-orders`.
7. O servico `invoicing` consome o evento, gera a nota fiscal em PDF, envia o arquivo para o MinIO e publica um novo evento em `icompras.invoiced-orders`.
8. O proprio `orders` consome esse evento e grava a URL da nota fiscal no pedido.
9. O servico `logistics` tambem consome `icompras.invoiced-orders`, gera um codigo de rastreio e publica em `icompras.shipped-orders`.
10. O servico `orders` consome o evento final e atualiza o status para `SHIPPED`.

## Caracteristicas tecnicas

- Java 21
- Spring Boot 3.5.11
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring Cloud OpenFeign
- Spring Kafka
- PostgreSQL
- Apache Kafka
- Kafka UI
- MinIO
- JasperReports
- Maven Wrapper
- Lombok

## Comunicacao entre servicos

### REST sincrono

O servico `orders` depende diretamente de:

- `product` para validar os produtos do pedido em `GET /api/v1/products/validate`
- `clients` para validar e consultar o cliente em `GET /api/v1/clients/{id}`

### Eventos Kafka

Topicos usados no fluxo:

| Topico | Publicador | Consumidor |
| --- | --- | --- |
| `icompras.paid-orders` | `orders` | `invoicing` |
| `icompras.invoiced-orders` | `invoicing` | `orders`, `logistics` |
| `icompras.shipped-orders` | `logistics` | `orders` |

## Estrutura do repositorio

```text
spring-microservice-kafka/
|-- clients/
|-- icompras-services/
|   |-- broker/
|   |-- bucket/
|   `-- database/
|-- invoicing/
|-- logistics/
|-- orders/
`-- product/
```

## Infraestrutura local

Suba os servicos de apoio antes de iniciar os microservicos:

```bash
docker compose -f icompras-services/database/docker-compose.yml up -d
docker compose -f icompras-services/broker/docker-compose.yml up -d
docker compose -f icompras-services/bucket/docker-compose.yml up -d
```

### Portas da infraestrutura

| Componente | Porta | Observacao |
| --- | --- | --- |
| PostgreSQL | `5432` | Bancos `products`, `clients` e `orders` |
| Kafka | `9092` | Broker para troca de eventos |
| Kafka UI | `8080` | Interface web para acompanhar topicos e mensagens |
| MinIO API | `9000` | Endpoint S3 compativel |
| MinIO Console | `9001` | Painel web do bucket |

## Preparacao do banco de dados

Os servicos `clients`, `product` e `orders` usam `spring.jpa.hibernate.ddl-auto=validate`. Isso significa que as tabelas precisam existir antes da aplicacao subir.

O arquivo [icompras-services/database/schema.sql](./icompras-services/database/schema.sql) funciona como referencia do modelo, mas a criacao dos tres bancos precisa ser feita separadamente.

### 1. Crie os bancos

```sql
CREATE DATABASE products;
CREATE DATABASE clients;
CREATE DATABASE orders;
```

### 2. Crie as tabelas do banco `products`

```sql
CREATE TABLE products (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    unit_price DECIMAL(16,2) NOT NULL
);
```

### 3. Crie as tabelas do banco `clients`

```sql
CREATE TABLE clients (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    street VARCHAR(100),
    number VARCHAR(10),
    neighborhood VARCHAR(100),
    email VARCHAR(150),
    phone VARCHAR(20)
);
```

### 4. Crie as tabelas do banco `orders`

```sql
CREATE TABLE orders (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    client_id BIGINT NOT NULL,
    order_date TIMESTAMP NOT NULL DEFAULT NOW(),
    payment_key TEXT,
    observations TEXT,
    status VARCHAR(20) CHECK (
        status IN ('CREATED', 'PAID', 'INVOICED', 'SHIPPED', 'PAYMENT_ERROR', 'PREPARING_SHIPMENT')
    ),
    total DECIMAL(16,2) NOT NULL,
    tracking_code VARCHAR(255),
    invoice_url TEXT
);

CREATE TABLE order_items (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders (id),
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(16,2) NOT NULL
);
```

## Como executar os microservicos

Use o Maven Wrapper de cada modulo.

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

No Linux ou macOS:

```bash
./mvnw spring-boot:run
```

Ordem recomendada de inicializacao:

1. `clients`
2. `product`
3. `orders`
4. `invoicing`
5. `logistics`

## Endpoints principais

### `clients` - `http://localhost:8082`

| Metodo | Rota | Descricao |
| --- | --- | --- |
| `POST` | `/api/v1/clients` | Cadastra um cliente |
| `GET` | `/api/v1/clients/{id}` | Consulta um cliente por ID |
| `GET` | `/api/v1/clients` | Lista todos os clientes |

### `product` - `http://localhost:8081`

| Metodo | Rota | Descricao |
| --- | --- | --- |
| `POST` | `/api/v1/products` | Cadastra um produto |
| `GET` | `/api/v1/products/{id}` | Consulta um produto por ID |
| `GET` | `/api/v1/products` | Lista todos os produtos |
| `GET` | `/api/v1/products/validate?ids=1,2,3` | Valida uma lista de produtos |

### `orders` - `http://localhost:8083`

| Metodo | Rota | Descricao |
| --- | --- | --- |
| `POST` | `/api/v1/orders` | Cria um pedido |
| `PATCH` | `/api/v1/orders/new-payment/{id}` | Reenvia dados de pagamento para um pedido com erro |
| `GET` | `/api/v1/orders/information/{id}` | Retorna o pedido consolidado |
| `POST` | `/api/v1/callback-payment` | Callback que aprova ou reprova o pagamento |

### `invoicing` - `http://localhost:8084`

| Metodo | Rota | Descricao |
| --- | --- | --- |
| `POST` | `/bucket` | Faz upload manual de arquivo no bucket |
| `GET` | `/bucket?filename=arquivo.pdf` | Retorna a URL assinada do arquivo |

## Exemplos de uso

### 1. Criar um produto

```bash
curl --request POST \
  --url http://localhost:8081/api/v1/products \
  --header 'Content-Type: application/json' \
  --data '{
    "name": "Notebook Gamer",
    "unitPrice": 7500.00
  }'
```

### 2. Criar um cliente

```bash
curl --request POST \
  --url http://localhost:8082/api/v1/clients \
  --header 'Content-Type: application/json' \
  --data '{
    "name": "Maria Silva",
    "cpf": "12345678901",
    "street": "Rua das Flores",
    "number": "100",
    "neighborhood": "Centro",
    "email": "maria@email.com",
    "phone": "11999999999"
  }'
```

### 3. Criar um pedido

```bash
curl --request POST \
  --url http://localhost:8083/api/v1/orders \
  --header 'Content-Type: application/json' \
  --data '{
    "clientId": 1,
    "paymentDetails": {
      "details": "Cartao final 1234",
      "paymentType": "CREDIT"
    },
    "items": [
      {
        "productId": 1,
        "quantity": 1,
        "unitPrice": 7500.00
      }
    ]
  }'
```

### 4. Confirmar o pagamento via callback

Use o `paymentKey` retornado pelo pedido criado.

Cabecalho obrigatorio:

- `apiKey: kZM1bhs7ZEuBNwv422r93ZaS390O7Ev2`

```bash
curl --request POST \
  --url http://localhost:8083/api/v1/callback-payment \
  --header 'Content-Type: application/json' \
  --header 'apiKey: kZM1bhs7ZEuBNwv422r93ZaS390O7Ev2' \
  --data '{
    "id": 1,
    "paymentKey": "SUBSTITUA_PELO_PAYMENT_KEY",
    "status": true,
    "observation": "Pagamento aprovado"
  }'
```

### 5. Consultar o pedido apos o processamento

```bash
curl --request GET \
  --url http://localhost:8083/api/v1/orders/information/1
```

Depois do fluxo completo, o pedido tende a conter:

- status atualizado
- `invoiceUrl` com a URL assinada do PDF no MinIO
- `trackingCode` com o codigo gerado pela logistica

## Estados do pedido

Os status previstos no dominio sao:

- `CREATED`
- `PAID`
- `INVOICED`
- `SHIPPED`
- `PAYMENT_ERROR`
- `PREPARING_SHIPMENT`

## Configuracoes relevantes

### `orders`

- Banco padrao: `jdbc:postgresql://127.0.0.1:5432/orders`
- Clientes Feign:
  - `http://localhost:8081/api/v1/products`
  - `http://localhost:8082/api/v1/clients`
- API key esperada no callback: `kZM1bhs7ZEuBNwv422r93ZaS390O7Ev2`

### `clients`

- Banco padrao: `jdbc:postgresql://127.0.0.1:5432/clients`

### `product`

- Banco padrao: `jdbc:postgresql://127.0.0.1:5432/products`

### `invoicing`

- MinIO padrao: `http://localhost:9000`
- Bucket padrao: `icompras.invoices`

### `logistics`

- Consome faturamento e publica envio usando o Kafka local na porta `9092`

## Observacoes

- O servico de pagamento esta simulado no modulo `orders` e atualmente apenas gera um `UUID` como `paymentKey`.
- `invoicing` e `logistics` nao persistem dados em banco; o estado consolidado fica no servico `orders`.
- A nota fiscal e gerada a partir do template Jasper em `invoicing/src/main/resources/reports/Invoice.jrxml`.
- O link da nota fiscal e uma URL assinada temporaria gerada pelo MinIO.

## Possiveis evolucoes

- Adicionar API Gateway e service discovery.
- Externalizar configuracoes com profiles e variaveis de ambiente.
- Incluir containers dos microservicos em um compose unico.
- Automatizar a inicializacao dos bancos e tabelas.
- Adicionar testes de integracao para o fluxo completo com Kafka e MinIO.
