# 🚀 Victor API - Sistema de Gerenciamento de Projetos e Propostas

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen?style=flat-square&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=flat-square&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-20.10+-blue?style=flat-square&logo=docker)
![IntelliJ IDEA](https://img.shields.io/badge/IDE-IntelliJ%20IDEA-black?style=flat-square&logo=intellijidea)

## 📋 Sobre o Projeto

O **Victor API** é um sistema completo de gerenciamento empresarial que permite controlar **propostas comerciais** e **projetos** de forma integrada. O sistema foi desenvolvido seguindo as melhores práticas da indústria de software, com foco em qualidade, segurança e escalabilidade.

### 🎯 Principais Funcionalidades

- 📋 **Gestão de Propostas**: Controle completo do pipeline de vendas
- 🎯 **Gestão de Projetos**: Acompanhamento de projetos do início ao fim
- 🔄 **Conversão Automática**: Transforme propostas aprovadas em projetos
- 👥 **Gestão de Usuários**: Controle de acesso com diferentes níveis de permissão
- 🏢 **Gestão de Recursos**: Departamentos, fornecedores, escritórios
- 🌍 **Suporte Internacional**: Múltiplas moedas e países
- 📊 **Relatórios e Estatísticas**: Dashboards e métricas de negócio

---

## 🛠️ Tecnologias Utilizadas

### 🔧 **Stack Principal**
- **Linguagem**: Java 21 (OpenJDK)
- **Framework**: Spring Boot 3.5.4
- **Banco de Dados**: PostgreSQL 15
- **Gerenciador de Dependências**: Maven 3.9+
- **IDE**: IntelliJ IDEA Ultimate

### 📚 **Dependências Spring**
```xml
<!-- Principais dependências do projeto -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.4</version>
</parent>

<!-- Spring Boot Starters -->
- spring-boot-starter-web (3.5.4)
- spring-boot-starter-data-jpa (3.5.4)
- spring-boot-starter-security (3.5.4)
- spring-boot-starter-validation (3.5.4)

<!-- Banco de Dados -->
- postgresql (42.7.3)

<!-- Documentação -->
- springdoc-openapi-starter-webmvc-ui (2.6.0)

<!-- JWT -->
- java-jwt (4.4.0)
```

### 🐳 **Containerização**
- **Docker**: 20.10+
- **Docker Compose**: 2.0+

---

## 🚀 Como Executar o Projeto

### 📋 **Pré-requisitos**

Antes de executar o projeto, certifique-se de ter instalado:

1. **Docker Desktop** (Windows/Mac) ou **Docker Engine** (Linux)
   - Download: https://www.docker.com/products/docker-desktop
   - Versão mínima: 20.10+

2. **Docker Compose** (geralmente já vem com o Docker Desktop)
   - Versão mínima: 2.0+

3. **Git** (para clonar o repositório)
   - Download: https://git-scm.com/downloads

### 📥 **Passo 1: Obter o Código**

```bash
# Clone o repositório
git clone [URL_DO_REPOSITORIO]

# Entre na pasta do projeto
cd victorapi
```

### 🐳 **Passo 2: Executar com Docker**

```bash
# Execute o comando para subir toda a aplicação
docker-compose up -d
```

**O que acontece quando você executa este comando:**
- ⬇️ Download das imagens Docker necessárias
- 🐘 Criação do container PostgreSQL com banco de dados
- ☕ Compilação e execução da aplicação Java
- 🌐 Disponibilização da API na porta 8080

### ⏰ **Passo 3: Aguardar a Inicialização**

```bash
# Verifique se os containers estão rodando
docker-compose ps

# Acompanhe os logs da aplicação (opcional)
docker-compose logs -f api
```

**Tempo estimado**: 2-3 minutos para primeira execução

### ✅ **Passo 4: Verificar se está Funcionando**

Abra seu navegador e acesse:
```
http://localhost:8080/swagger-ui.html
```

Se a página do Swagger abrir, **parabéns!** 🎉 O projeto está rodando corretamente.

---

## 🔐 Informações de Acesso

### 🐘 **Banco de Dados PostgreSQL**

```yaml
Host: localhost
Porta: 5432
Banco: victorapi_db
Usuário: victorapi_user
Senha: victorapi_pass
```

**Para conectar via cliente SQL (DBeaver, pgAdmin, etc.):**
```
jdbc:postgresql://localhost:5432/victorapi_db
```

### 👤 **Usuários Padrão do Sistema**

O sistema vem com usuários pré-cadastrados para teste:

```json
// Administrador
{
  "email": "maria.silva@empresa.com",
  "senha": "password123",
  "role": "ADMIN"
}

// Gerente  
{
  "email": "joao.santos@empresa.com", 
  "senha": "password123",
  "role": "MANAGER"
}

// Usuário
{
  "email": "ana.oliveira@empresa.com",
  "senha": "password123", 
  "role": "USER"
}
```

---

## 📚 Como Usar a API

### 🔑 **Passo 1: Fazer Login**

1. Acesse o Swagger: http://localhost:8080/swagger-ui.html
2. Encontre o endpoint **POST /api/v1/auth/login**
3. Clique em **"Try it out"**
4. Use os dados de login:

```json
{
  "email": "maria.silva@empresa.com",
  "password": "password123"
}
```

5. Clique em **"Execute"**
6. **Copie o token** da resposta (campo "token")

### 🎫 **Passo 2: Autenticar nas Requisições**

1. No topo da página do Swagger, clique no botão **"Authorize"** 🔒
2. Digite: `Bearer [SEU_TOKEN_AQUI]`
   ```
   Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```
3. Clique em **"Authorize"**
4. Agora você pode usar todos os endpoints! ✅

### 🧪 **Passo 3: Testar Funcionalidades**

**Listar Propostas:**
- GET `/api/v1/proposals`

**Criar Nova Proposta:**
- POST `/api/v1/proposals`

**Converter Proposta em Projeto:**
- POST `/api/v1/proposals/{id}/convert-to-project`

**Listar Projetos:**
- GET `/api/v1/projects`

---

## 🌟 Funcionalidade Destacada: Conversão de Proposta para Projeto

### 💡 **O que faz?**
Converte automaticamente uma proposta comercial aprovada em um projeto executável, mantendo todos os dados relevantes.

### 🔧 **Como usar?**

1. **Liste as propostas** disponíveis:
   ```
   GET /api/v1/proposals
   ```

2. **Escolha uma proposta** (ex: ID 1)

3. **Converta para projeto**:
   ```
   POST /api/v1/proposals/1/convert-to-project
   ```
   
   Corpo da requisição:
   ```json
   {
     "projectName": "Projeto Sistema CRM - TechCorp",
     "projectTypeId": 1,
     "billable": true,
     "product": true,
     "classification": "ESTRATÉGICO"
   }
   ```

4. **Verifique o projeto criado**:
   ```
   GET /api/v1/projects
   ```

---

## 📊 Dados de Exemplo Pré-carregados

O sistema vem com dados realistas para demonstração:

### 📋 **Propostas**
- Sistema CRM (R$ 180.000)
- E-commerce Internacional (US$ 300.000) 
- App Mobile Saúde (US$ 95.000)
- Consultoria DevOps (€ 45.000)
- Website Institucional (R$ 25.000)

### 🎯 **Projetos**
- Sistema ERP Tech Solutions
- E-commerce Global Platform
- Health Mobile App
- Infraestrutura DevOps

### 👥 **Usuários**
- 3 usuários com diferentes níveis de acesso
- Departamentos: TI, Marketing, Vendas, RH, etc.

### 🌍 **Configurações**
- Países: Brasil, EUA, Alemanha, Argentina
- Moedas: Real, Dólar, Euro, Peso Argentino
- Áreas: Frontend, Backend, Mobile, DevOps

---

## 🐳 Comandos Docker Úteis

### 📊 **Monitoramento**
```bash
# Ver status dos containers
docker-compose ps

# Ver logs da aplicação
docker-compose logs api

# Ver logs do banco
docker-compose logs db

# Acompanhar logs em tempo real
docker-compose logs -f
```

### 🔄 **Controle da Aplicação**
```bash
# Parar a aplicação
docker-compose down

# Reiniciar a aplicação
docker-compose restart

# Rebuildar e subir
docker-compose up -d --build

# Limpar tudo e recomeçar
docker-compose down -v
docker-compose up -d
```

### 🧹 **Limpeza**
```bash
# Remover containers e volumes
docker-compose down -v

# Limpar imagens não utilizadas
docker system prune

# Ver uso de espaço
docker system df
```

---

## 🔧 Desenvolvimento Local (Opcional)

Se você quiser executar o projeto diretamente no IntelliJ IDEA:

### 📋 **Requisitos**
- **Java 21** (OpenJDK ou Oracle JDK)
- **Maven 3.9+**
- **IntelliJ IDEA** (Community ou Ultimate)
- **PostgreSQL 15** rodando localmente

### ⚙️ **Configuração**

1. **Clone o projeto** no IntelliJ IDEA
2. **Configure o Java 21** no projeto (File → Project Structure → Project SDK)
3. **Instale as dependências**: Maven reload
4. **Configure o banco** no `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/victorapi_db
   spring.datasource.username=victorapi_user
   spring.datasource.password=victorapi_pass
   ```
5. **Execute** a classe `VictorapiApplication.java`

---

## 📁 Estrutura do Projeto

```
victorapi/
├── 📁 src/main/java/br/edu/infnet/victorapi/
│   ├── 🔐 config/           # Configurações (Security, Jackson, OpenAPI)
│   ├── 📋 modules/          # Módulos de negócio
│   │   ├── proposals/       # Gestão de propostas
│   │   ├── projects/        # Gestão de projetos  
│   │   ├── users/          # Gestão de usuários
│   │   ├── auth/           # Autenticação
│   │   └── [outros 11 módulos]
│   ├── ⚠️ exceptions/       # Tratamento de exceções
│   └── 🔧 handlers/         # Manipuladores globais
├── 📁 src/main/resources/
│   └── application.properties
├── 📁 init-scripts/
│   └── 01-init.sql         # Script de inicialização do banco
├── 🐳 docker-compose.yml   # Configuração Docker
├── 📋 pom.xml              # Dependências Maven
├── 📚 README.md            # Este arquivo
└── 📖 Documentações/       # Guias e exemplos
```

---

## 🆘 Solução de Problemas

### ❌ **Erro: "Port 8080 already in use"**
```bash
# Encontre o processo usando a porta
netstat -ano | findstr :8080

# Pare o Docker Compose
docker-compose down

# Suba novamente
docker-compose up -d
```

### ❌ **Erro: "Database connection failed"**
```bash
# Verifique se o PostgreSQL está rodando
docker-compose ps

# Restart dos containers
docker-compose restart

# Se persistir, remova volumes e recrie
docker-compose down -v
docker-compose up -d
```

### ❌ **Erro: "Docker not found"**
- Instale o Docker Desktop
- Certifique-se que está rodando
- No Windows: verifique se o WSL2 está habilitado

### ❌ **Swagger não carrega**
- Aguarde 2-3 minutos após o `docker-compose up`
- Verifique os logs: `docker-compose logs api`
- Acesse: http://localhost:8080/swagger-ui/index.html

---

## 📞 Suporte

### 📧 **Contato**
Para dúvidas ou problemas:
- Crie uma issue no repositório
- Entre em contato com o desenvolvedor

### 📚 **Documentação Adicional**
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **Guia de Conversão**: `CONVERT_PROPOSAL_TO_PROJECT.md`
- **Pitch do Projeto**: `PITCH_VENDAS_VICTOR_API.md`

---

## 🎉 Conclusão

Parabéns! 🚀 Você agora tem o **Victor API** rodando localmente. 

### 🎯 **Próximos Passos:**
1. ✅ Explore a **documentação Swagger**
2. ✅ Teste os **endpoints de propostas**
3. ✅ Experimente a **conversão para projetos**
4. ✅ Analise a **estrutura do código**

**O sistema está pronto para demonstrações e apresentações!** 

---

*Desenvolvido com ❤️ usando Java 21, Spring Boot 3.5.4 e IntelliJ IDEA*
