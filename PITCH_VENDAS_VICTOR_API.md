# 🚀 Victor API - Sistema de Gerenciamento de Projetos e Propostas

## Apresentação Executiva

**Professor(a),**

Tenho o prazer de apresentar o **Victor API**, uma solução empresarial completa desenvolvida para demonstrar domínio avançado em tecnologias modernas de desenvolvimento backend. Este projeto representa a implementação de um sistema robusto de gerenciamento de projetos e propostas comerciais, seguindo as melhores práticas da indústria.

---

## 📋 Visão Geral do Projeto

### 🎯 Objetivo
Desenvolver uma API REST enterprise-grade que demonstre competências em:
- **Arquitetura de Software Moderna**
- **Desenvolvimento Backend Avançado**
- **Integração de Tecnologias**
- **Padrões de Qualidade de Código**

### 💼 Domínio de Negócio
Sistema de gestão para empresas que precisam controlar:
- **Propostas Comerciais** (Pipeline de vendas)
- **Projetos** (Execução e acompanhamento)
- **Recursos** (Usuários, departamentos, fornecedores)
- **Configurações** (Países, moedas, status, tipos)

---

## 🏗️ Arquitetura Técnica

### Stack Tecnológico
```
├── Backend Framework: Spring Boot 3.5.4
├── Linguagem: Java 17+ 
├── Banco de Dados: PostgreSQL 15
├── Segurança: Spring Security + JWT
├── Documentação: OpenAPI 3 (Swagger)
├── Containerização: Docker + Docker Compose
├── Build Tool: Maven
└── Padrões: REST, Clean Architecture, DDD
```

### Arquitetura Modular
```
victorapi/
├── 🔐 Security (JWT + Role-based)
├── 📋 Proposals (15 módulos de negócio)
├── 🎯 Projects
├── 👥 Users & Departments  
├── 🌍 Countries & Offices
├── 💰 Financial (Currencies, Contracts)
├── ⚙️  Configuration (Status, Types, Areas)
└── 📚 Documentation (Swagger completo)
```

---

## 🔧 Funcionalidades Implementadas

### 🔒 **Sistema de Autenticação & Autorização**
- **JWT Token-based Authentication**
- **Role-based Access Control** (ADMIN, MANAGER, USER)
- **Security Configuration** com Spring Security
- **Proteção de Endpoints** por nível de acesso

### 📊 **Módulo de Propostas Comerciais**
- ✅ CRUD completo com validações
- ✅ Sistema de filtros avançados
- ✅ Busca por múltiplos critérios
- ✅ Controle de numeração única
- ✅ Cálculos de valor total e estatísticas
- ✅ **INOVAÇÃO**: Conversão automática para projetos

### 🎯 **Módulo de Projetos**
- ✅ Gestão completa de projetos
- ✅ Hierarquia e relacionamentos
- ✅ Controle de status e tipos
- ✅ Integração com propostas originais
- ✅ Classificação e categorização

### 👥 **Gestão de Recursos**
- ✅ **Usuários**: Controle completo com departamentos
- ✅ **Departamentos**: Organização hierárquica
- ✅ **Fornecedores/Clientes**: Base comercial
- ✅ **Escritórios**: Gestão multi-localização

### 🌐 **Configurações Globais**
- ✅ **Países**: Base internacional com moedas
- ✅ **Tipos de Moeda**: Sistema multi-currency
- ✅ **Áreas**: Classificação técnica
- ✅ **Status**: Workflow configurável
- ✅ **Setores**: Organização por mercado

---

## 💡 Diferenciais Técnicos

### 🎨 **Padrões de Desenvolvimento**
```java
// Exemplo: Arquitetura Clean com DTOs
@Service
@PreAuthorize("hasRole('ADMIN')")
public class ProposalService {
    
    @Transactional
    public ProjectResponseDTO convertProposalToProject(
        Integer proposalId, 
        ConvertProposalToProjectDTO convertDTO) {
        // Lógica de negócio complexa
        // Mapeamento automático de entidades
        // Validações de integridade
    }
}
```

### 📚 **Documentação Swagger Profissional**
- **66 DTOs documentados** com exemplos realistas
- **Schema annotations** completas
- **Exemplos baseados em dados reais** do banco
- **Interface de teste funcional**

```java
@Schema(description = "Nome da proposta", example = "Sistema ERP TechCorp")
String name,

@Schema(description = "Valor da proposta", example = "250000.00")
BigDecimal value
```

### 🔄 **Funcionalidade Inovadora: Converter Proposta → Projeto**
```java
// Endpoint exclusivo desenvolvido
@PostMapping("/{id}/convert-to-project")
public ResponseEntity<?> convertProposalToProject(
    @PathVariable Integer id,
    @Valid @RequestBody ConvertProposalToProjectDTO convertDTO) {
    
    ProjectResponseDTO project = proposalService
        .convertProposalToProject(id, convertDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(project);
}
```

**Mapeamento Inteligente:**
- ✅ Transferência automática de 12+ campos
- ✅ Preservação de relacionamentos
- ✅ Configurações personalizáveis
- ✅ Validações de integridade referencial

---

## 📊 Métricas do Projeto

### 📈 **Escala de Desenvolvimento**
```
📁 Estrutura:
├── 15 Módulos de Negócio
├── 66 DTOs Documentados  
├── 45+ Endpoints REST
├── 15+ Tabelas Relacionais
├── 300+ Linhas de SQL inicial
└── 3000+ Linhas de código Java

🔧 Funcionalidades:
├── Autenticação JWT completa
├── Sistema de roles e permissões
├── CRUD para todas entidades
├── Filtros e buscas avançadas
├── Validações de negócio
├── Tratamento de exceções
├── Documentação Swagger
└── Funcionalidade de conversão inovadora
```

### 🏆 **Qualidade de Código**
- ✅ **Padrão DTO** em todas as operações
- ✅ **Validações Jakarta** com mensagens customizadas  
- ✅ **Exception Handling** centralizado
- ✅ **Security** em todos os endpoints
- ✅ **Transações** para operações críticas
- ✅ **Clean Code** com nomes expressivos

---

## 🎓 Demonstração de Competências

### 🔹 **Backend Development**
- Spring Boot avançado com configurações personalizadas
- JPA/Hibernate com relacionamentos complexos
- Implementação de padrões arquiteturais (Repository, Service, DTO)

### 🔹 **Database Design**
- Modelagem relacional com 15+ tabelas
- Relacionamentos 1:N e N:N
- Constraints e validações de integridade
- Scripts de inicialização com dados realistas

### 🔹 **Security & Authentication**
- JWT implementation from scratch
- Role-based authorization
- Security configuration personalizada
- Proteção de endpoints por hierarquia

### 🔹 **API Design & Documentation**
- RESTful APIs seguindo padrões da indústria
- OpenAPI 3.0 com documentação completa
- Swagger UI funcional para testes
- Versionamento de API (/api/v1/)

### 🔹 **DevOps & Deployment**
- Docker containerization
- Docker Compose para ambiente completo
- Configuração de banco PostgreSQL
- Environment variables e configurações

---

## 🚀 Funcionalidade Destacada: Conversão Inteligente

### 💡 **Problema Resolvido**
Em empresas reais, propostas aprovadas precisam ser convertidas em projetos executáveis. Esta funcionalidade automatiza esse processo crítico.

### ⚙️ **Solução Implementada**
```bash
# Exemplo de uso da API
POST /api/v1/proposals/1/convert-to-project
{
  "projectName": "Projeto Sistema CRM - TechCorp",
  "projectTypeId": 1,
  "billable": true,
  "classification": "ESTRATÉGICO"
}

# Resultado: Projeto criado automaticamente com:
✅ Dados mapeados da proposta original
✅ Relacionamentos preservados  
✅ Configurações personalizadas aplicadas
✅ Referência à proposta mantida
```

### 📋 **Campos Mapeados Automaticamente**
```
Proposta → Projeto:
├── name → name
├── description → description  
├── departmentId → departmentsId
├── clientSupplierId → clientsSuppliersId
├── sectorId → sectorsId
├── areaId → areasId
├── countryId → countriesId
├── coinTypeId → coinTypeId
├── site → site
├── exchangeRate → exchangeRate
└── proposalId → originProposalId
```

---

## 📁 Estrutura de Entrega

### 📂 **Código Fonte**
```
/victorapi
├── 📋 Documentação completa (README.md)
├── 🐳 Docker setup (docker-compose.yml)
├── 📊 Scripts SQL de inicialização
├── 🔧 Código fonte modularizado
├── 📚 Documentação de API (Swagger)
├── 🎯 Exemplos de uso (CONVERT_PROPOSAL_TO_PROJECT.md)
└── ⚙️  Configurações de ambiente
```

### 🎯 **Como Executar**
```bash
# 1. Clone o repositório
git clone [repo-url]

# 2. Suba o ambiente
docker-compose up -d

# 3. Acesse a documentação
http://localhost:8080/swagger-ui.html

# 4. Teste os endpoints
# Login → Obter token → Usar nos testes
```

---

## 🎖️ Conclusão

O **Victor API** representa mais do que um projeto acadêmico - é uma demonstração prática de competências enterprise-level em desenvolvimento backend. Cada linha de código foi pensada para:

### ✅ **Demonstrar Excelência Técnica**
- Implementação de padrões modernos
- Código limpo e manutenível  
- Arquitetura escalável

### ✅ **Resolver Problemas Reais**
- Gestão de propostas comerciais
- Controle de projetos empresariais
- Automação de processos críticos

### ✅ **Aplicar Melhores Práticas**
- Security-first approach
- API-first design
- Documentation-driven development

**Este projeto demonstra capacidade de desenvolver soluções enterprise-grade que poderiam ser implantadas em ambiente produtivo real.**

---

*Desenvolvido com dedicação e atenção aos detalhes técnicos*  
**Victor API** - *Transformando propostas em projetos de sucesso*

---

## 📞 Demonstração Ao Vivo

Estou preparado para apresentar:
- ✅ **Walkthrough completo** da aplicação
- ✅ **Demo da funcionalidade de conversão** 
- ✅ **Explicação da arquitetura** implementada
- ✅ **Discussão das decisões técnicas** tomadas

**Agendemos uma sessão para ver a API em funcionamento!** 🚀
