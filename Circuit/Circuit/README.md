# Circuit - Sistema ERP para Assistências Técnicas

Sistema completo de gestão empresarial desenvolvido com Spring Boot, focado especificamente em assistências técnicas. O Circuit automatiza e gerencia todo o fluxo de trabalho de uma assistência técnica, desde o recebimento do equipamento até a entrega final.

## 📋 Índice

- [Descrição do Projeto](#descrição-do-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Pré-requisitos](#pré-requisitos)
- [Configuração](#configuração)
- [Execução](#execução)
- [Endpoints Principais](#endpoints-principais)
- [Segurança](#segurança)

## 🎯 Descrição do Projeto

O Circuit é um sistema ERP (Enterprise Resource Planning) completo desenvolvido em Java com Spring Boot, projetado especificamente para gerenciar assistências técnicas. O sistema automatiza todo o fluxo de trabalho, desde o recebimento do equipamento até a entrega final, proporcionando controle total sobre:

- **Recebimento de Equipamentos** - Registro completo de aparelhos recebidos para manutenção
- **Ordens de Serviço** - Gestão completa do ciclo de vida da manutenção
- **Controle de Estoque** - Gestão de produtos, peças e materiais de reposição
- **Financeiro** - Controle de contas a pagar e receber
- **Gestão de Clientes e Fornecedores** - Cadastro completo de ambas as partes
- **Pedidos de Peças** - Sistema de pedidos a fornecedores
- **Vendas de Produtos** - Venda de produtos e peças
- **Dashboard** - Métricas e relatórios em tempo real
- **Notificações** - Alertas automáticos por email
- **Segurança** - Autenticação e controle de acesso por roles

## ✨ Funcionalidades

### Gestão de Estoque
- Cadastro e controle de produtos e peças
- Controle de marcas e categorias
- Gestão de aparelhos e modelos
- Estoque mínimo e alertas

### Vendas e Pedidos
- Criação e gerenciamento de pedidos
- Integração com estoque
- Controle de status do pedido
- Histórico de vendas

### Serviços Técnicos
- Ordem de serviço completa
- Acompanhamento de status
- Gestão de equipamentos em manutenção

### Financeiro
- Contas a pagar (fornecedores)
- Contas a receber (clientes)
- Controle de forma e condição de pagamento
- Status financeiro

### Gestão de Clientes e Fornecedores
- Cadastro completo de clientes
- Gestão de fornecedores
- Integração com API de CEP

### Sistema de Notificações
- Alertas automáticos
- Notificações por email
- Sistema de notificações push

### Dashboard e Relatórios
- Métricas em tempo real
- Relatórios financeiros
- Gráficos e visualizações

## 🛠️ Tecnologias Utilizadas

### Backend
- **Spring Boot 4.0.1** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Segurança e autenticação
- **Spring MVC** - Web e REST APIs
- **Spring Thymeleaf** - Templates HTML
- **PostgreSQL** - Banco de dados
- **Lombok** - Redução de código boilerplate
- **Jakarta Mail** - Envio de emails

### Frontend
- **HTML5** - Estrutura das páginas
- **CSS3** - Estilização
- **JavaScript (Vanilla)** - Lógica frontend
- **Thymeleaf** - Integração com backend

### Outros
- **Maven** - Gerenciamento de dependências
- **Java 17** - Linguagem de desenvolvimento

## 📁 Estrutura do Projeto

```
Circuit/
├── src/
│   ├── main/
│   │   ├── java/Circuit/Circuit/
│   │   │   ├── Controller/         
│   │   │   │   ├── AparelhoController.java
│   │   │   │   ├── ClienteController.java
│   │   │   │   ├── ContasPagarController.java
│   │   │   │   ├── ContasReceberController.java
│   │   │   │   ├── DashboardController.java
│   │   │   │   ├── EstoqueController.java
│   │   │   │   ├── FornecedorController.java
│   │   │   │   ├── PedidoController.java
│   │   │   │   ├── VendaController.java
│   │   │   │   ├── OrdemServicoController.java
│   │   │   │   ├── ProdutoController.java
│   │   │   │   ├── PecaController.java
│   │   │   │   ├── ServicoController.java
│   │   │   │   ├── UsuarioController.java
│   │   │   │   ├── LoginController.java
│   │   │   │   └── RelatorioController.java
│   │   │   ├── Service/             
│   │   │   │   ├── DashboardService.java
│   │   │   │   ├── EstoqueService.java
│   │   │   │   ├── VendaService.java
│   │   │   │   ├── ContasPagarService.java
│   │   │   │   ├── ContasReceberService.java
│   │   │   │   ├── FornecedorService.java
│   │   │   │   ├── PedidoService.java
│   │   │   │   ├── EmailService.java
│   │   │   │   └── Api/CepService.java
│   │   │   ├── Model/              
│   │   │   │   ├── Produto.java
│   │   │   │   ├── Peca.java
│   │   │   │   ├── Cliente.java
│   │   │   │   ├── Fornecedor.java
│   │   │   │   ├── Venda.java
│   │   │   │   ├── Pedido.java
│   │   │   │   ├── OrdemServico.java
│   │   │   │   ├── ContasPagar.java
│   │   │   │   ├── ContasReceber.java
│   │   │   │   ├── ItemPedido.java
│   │   │   │   ├── ItemVenda.java
│   │   │   │   └── Notificacao.java
│   │   │   ├── Repository/         
│   │   │   ├── Security/            
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── LogoutHandler.java
│   │   │   ├── Dto/                
│   │   │   └── CircuitApplication.java  
│   │   ├── resources/
│   │   │   ├── templates/          
│   │   │   │   ├── home.html
│   │   │   │   ├── login.html
│   │   │   │   ├── clientes.html
│   │   │   │   ├── fornecedores.html
│   │   │   │   ├── estoque.html
│   │   │   │   ├── pedidos.html
│   │   │   │   ├── vendas.html
│   │   │   │   ├── contas-pagar.html
│   │   │   │   ├── contas-receber.html
│   │   │   │   ├── ordens-servico.html
│   │   │   │   ├── servicos.html
│   │   │   │   ├── financeiro.html
│   │   │   │   ├── relatorios.html
│   │   │   │   └── Emails/
│   │   │   ├── static/              
│   │   │   │   ├── css/
│   │   │   │   ├── js/
│   │   │   │   └── images/
│   │   │   ├── application.properties
│   │   │   └── logback-spring.xml     
│   │   └── test/
│   └── test/java/Circuit/Circuit/
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

## 📦 Pré-requisitos

- **Java 17** ou superior
- **Maven 3.6+**
- **PostgreSQL 12+**
- **IDE** (IntelliJ IDEA, Eclipse, VS Code)
- **Banco de dados PostgreSQL** configurado

## ⚙️ Configuração

### 1. Clone o repositório

```bash
git clone <url-do-repositorio>
cd Circuit
```

### 2. Configure o banco de dados

Edite o arquivo [`application.properties`](src/main/resources/application.properties) com suas credenciais:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/circuit_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 3. Configure o email (opcional)

Para ativar o envio de emails, configure as seguintes propriedades:

```properties
spring.mail.host=smtp.seu-provedor.com
spring.mail.port=587
spring.mail.username=seu-email@exemplo.com
spring.mail.password=sua-senha
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### 4. Build do projeto

```bash
mvn clean install
```

## 🚀 Execução

### Executar com Maven

```bash
mvn spring-boot:run
```

### Executar com IDE

1. Abra o projeto na IDE
2. Execute a classe [`CircuitApplication`](src/main/java/Circuit/Circuit/CircuitApplication.java)
3. Acesse `http://localhost:8080`

### Acessar o sistema

- **URL:** http://localhost:8080
- **Login padrão:** admin
- **Senha padrão:** admin

> ⚠️ **Importante:** Altere as credenciais padrão após o primeiro acesso!

## 🔌 Endpoints Principais

### Autenticação
- `POST /api/login` - Autenticar usuário
- `GET /api/logout` - Realizar logout

### Dashboard
- `GET /api/dashboard` - Obter métricas do dashboard

### Estoque
- `GET /api/produtos` - Listar todos os produtos
- `POST /api/produtos` - Criar novo produto
- `GET /api/produtos/{id}` - Obter produto por ID
- `PUT /api/produtos/{id}` - Atualizar produto
- `DELETE /api/produtos/{id}` - Deletar produto

- `GET /api/pecas` - Listar todas as peças
- `POST /api/pecas` - Criar nova peça
- `GET /api/pecas/{id}` - Obter peça por ID
- `PUT /api/pecas/{id}` - Atualizar peça

### Vendas
- `GET /api/vendas` - Listar todas as vendas
- `POST /api/vendas` - Criar nova venda
- `GET /api/vendas/{id}` - Obter venda por ID
- `GET /api/vendas/relatorio` - Gerar relatório de vendas

### Pedidos
- `GET /api/pedidos` - Listar todos os pedidos
- `POST /api/pedidos` - Criar novo pedido
- `GET /api/pedidos/{id}` - Obter pedido por ID
- `PUT /api/pedidos/{id}/status` - Atualizar status do pedido

### Serviços
- `GET /api/servicos` - Listar todos os serviços
- `POST /api/servicos` - Criar novo serviço
- `GET /api/servicos/{id}` - Obter serviço por ID

### Ordem de Serviço
- `GET /api/ordens-servico` - Listar todas as ordens
- `POST /api/ordens-servico` - Criar nova ordem
- `GET /api/ordens-servico/{id}` - Obter ordem por ID
- `PUT /api/ordens-servico/{id}/status` - Atualizar status da ordem

### Financeiro
- `GET /api/contas-pagar` - Listar contas a pagar
- `POST /api/contas-pagar` - Criar conta a pagar
- `GET /api/contas-receber` - Listar contas a receber
- `POST /api/contas-receber` - Criar conta a receber

### Clientes
- `GET /api/clientes` - Listar todos os clientes
- `POST /api/clientes` - Criar novo cliente
- `GET /api/clientes/{id}` - Obter cliente por ID

### Fornecedores
- `GET /api/fornecedores` - Listar todos os fornecedores
- `POST /api/fornecedores` - Criar novo fornecedor
- `GET /api/fornecedores/{id}` - Obter fornecedor por ID

### Usuários
- `GET /api/usuarios` - Listar todos os usuários
- `POST /api/usuarios` - Criar novo usuário
- `GET /api/usuarios/{id}` - Obter usuário por ID

### Relatórios
- `GET /api/relatorios/financeiro` - Relatório financeiro
- `GET /api/relatorios/vendas` - Relatório de vendas
- `GET /api/relatorios/estoque` - Relatório de estoque

## 🔒 Segurança

O sistema utiliza **Spring Security** para controle de acesso:

- **JWT (JSON Web Token)** para autenticação
- **RBAC (Role-Based Access Control)** para permissões
- **Password hashing** com BCrypt
- **CSRF Protection** ativado
- **Logout handler** personalizado

### Papéis (Roles)

- `ADMIN` - Acesso total ao sistema
- `GERENTE` - Acesso limitado a gerenciamento
- `FUNCIONARIO` - Acesso básico às operações

## 📊 Modelos de Dados Principais

### Produto
- ID, nome, descrição, preço, quantidade, categoria, marca

### Peca
- ID, nome, descrição, preço, quantidade, categoria, aparelho, modelo

### Venda
- ID, cliente, data, total, status, itens

### Pedido
- ID, cliente, data, status, itens, valor total

### OrdemServico
- ID, cliente, aparelho, serviço, status, data início, data fim, valor

### ContasPagar
- ID, fornecedor, valor, data vencimento, status, forma pagamento

### ContasReceber
- ID, cliente, valor, data vencimento, status, forma pagamento

## 📝 Licença

Este projeto é proprietário da Circuit.

## 👥 Desenvolvedor

Gustavo erthal - erthal-guu.

## 📧 Contato

Para suporte ou dúvidas, entre em contato através do email: suporte@circuit.com

---

**Versão:** 0.0.1-SNAPSHOT  
**Última atualização:** 2026-03-09
