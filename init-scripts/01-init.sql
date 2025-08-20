-- Arquivo de inicialização do banco PostgreSQL
-- Sistema de Gerenciamento de Projetos

-- ========================================
-- TABELAS BÁSICAS
-- ========================================

DO $$ BEGIN
    CREATE TYPE user_role AS ENUM ('ROLE_ADMIN', 'ROLE_USER');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

CREATE TABLE IF NOT EXISTS countries (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code_2 VARCHAR(2) UNIQUE,
    code_3 VARCHAR(3) UNIQUE,
    currency_code VARCHAR(3),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS coin_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(3) UNIQUE,
    symbol VARCHAR(10),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS departments (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50) UNIQUE,
    description TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS project_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50) UNIQUE,
    description TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sectors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50) UNIQUE,
    description TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS areas (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50) UNIQUE,
    description TEXT,
    area_type VARCHAR(20) NOT NULL DEFAULT 'BASIC',
    specialization_type VARCHAR(100),
    priority_level INTEGER,
    requires_certification BOOLEAN DEFAULT false,
    budget_limit DECIMAL(15,2),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS project_statuses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50) UNIQUE,
    description TEXT,
    color VARCHAR(7),
    sort_order INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    is_initial BOOLEAN DEFAULT false,
    is_final BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS proposal_statuses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50) UNIQUE,
    description TEXT,
    color VARCHAR(7),
    sort_order INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    is_initial BOOLEAN DEFAULT false,
    is_final BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS offices (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50) UNIQUE,
    address TEXT,
    city VARCHAR(255),
    state VARCHAR(255),
    postal_code VARCHAR(20),
    country_id INTEGER REFERENCES countries(id),
    phone VARCHAR(50),
    email VARCHAR(255),
    is_main_office BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    department_id INTEGER REFERENCES departments(id),
    position VARCHAR(255),
    is_active BOOLEAN DEFAULT true,
    last_login TIMESTAMP WITH TIME ZONE,
    role user_role DEFAULT 'ROLE_USER' NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS clients_suppliers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    document VARCHAR(50),
    document_type VARCHAR(20),
    email VARCHAR(255),
    phone VARCHAR(50),
    address TEXT,
    city VARCHAR(255),
    state VARCHAR(255),
    postal_code VARCHAR(20),
    country_id INTEGER REFERENCES countries(id),
    type VARCHAR(20) CHECK (type IN ('client', 'supplier', 'both')),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS contracts (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    contract_number VARCHAR(100),
    start_date DATE,
    end_date DATE,
    value DECIMAL(15,2),
    coin_type_id INTEGER REFERENCES coin_types(id),
    client_supplier_id INTEGER REFERENCES clients_suppliers(id),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS proposals (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    proposal_number VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    department_id INTEGER REFERENCES departments(id),
    sector_id INTEGER REFERENCES sectors(id),
    area_id INTEGER REFERENCES areas(id),
    client_supplier_id INTEGER REFERENCES clients_suppliers(id),
    contract_id INTEGER REFERENCES contracts(id),
    office_id INTEGER REFERENCES offices(id),
    coin_type_id INTEGER REFERENCES coin_types(id),
    country_id INTEGER REFERENCES countries(id),
    status_id INTEGER REFERENCES proposal_statuses(id),
    responsible_id INTEGER REFERENCES users(id),
    origin_proposal_id INTEGER REFERENCES proposals(id),
    site VARCHAR(500),
    value DECIMAL(15,2),
    schedule TEXT,
    ibt DECIMAL(10,4),
    payment_days INTEGER,
    estimated_start DATE,
    probability DECIMAL(5,2) CHECK (probability >= 0 AND probability <= 100),
    proposal_sub_number VARCHAR(100),
    exchange_rate DECIMAL(10,4) DEFAULT 1.0000,
    company_name VARCHAR(255),
    priority INTEGER DEFAULT 0,
    due_days INTEGER
);

CREATE TABLE IF NOT EXISTS projects (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    departments_id INTEGER REFERENCES departments(id),
    project_types_id INTEGER REFERENCES project_types(id),
    sectors_id INTEGER REFERENCES sectors(id),
    areas_id INTEGER REFERENCES areas(id),
    project_statuses_id INTEGER REFERENCES project_statuses(id),
    origin_projects_id INTEGER REFERENCES projects(id),
    countries_id INTEGER REFERENCES countries(id),
    clients_suppliers_id INTEGER REFERENCES clients_suppliers(id),
    last_project_statuses_id INTEGER REFERENCES project_statuses(id),
    coin_type_id INTEGER REFERENCES coin_types(id),
    origin_proposal_id INTEGER REFERENCES proposals(id),
    billable_fl BOOLEAN DEFAULT false,
    international_fl BOOLEAN DEFAULT false,
    project_dir VARCHAR(500),
    site VARCHAR(500),
    is_default BOOLEAN DEFAULT false,
    exchange_rate DECIMAL(10,4) DEFAULT 1.0000,
    opening_email VARCHAR(255),
    classification VARCHAR(100),
    investiment_fl BOOLEAN DEFAULT false,
    product_fl BOOLEAN DEFAULT false
);

-- ========================================
-- INSERIR DADOS BÁSICOS (NA ORDEM CORRETA)
-- ========================================

INSERT INTO countries (name, code_2, code_3, currency_code) VALUES
('Brasil', 'BR', 'BRA', 'BRL'),
('Estados Unidos', 'US', 'USA', 'USD'),
('Alemanha', 'DE', 'DEU', 'EUR'),
('Reino Unido', 'GB', 'GBR', 'GBP'),
('França', 'FR', 'FRA', 'EUR'),
('Japão', 'JP', 'JPN', 'JPY'),
('Canadá', 'CA', 'CAN', 'CAD'),
('Austrália', 'AU', 'AUS', 'AUD')
ON CONFLICT (code_2) DO NOTHING;

INSERT INTO coin_types (name, code, symbol) VALUES
('Real Brasileiro', 'BRL', 'R$'),
('Dólar Americano', 'USD', '$'),
('Euro', 'EUR', '€'),
('Libra Esterlina', 'GBP', '£'),
('Iene Japonês', 'JPY', '¥'),
('Dólar Canadense', 'CAD', 'C$'),
('Dólar Australiano', 'AUD', 'A$')
ON CONFLICT (code) DO NOTHING;

INSERT INTO departments (name, code) VALUES
('Desenvolvimento', 'DEV'),
('Marketing', 'MKT'),
('Vendas', 'SALES'),
('Recursos Humanos', 'HR'),
('Financeiro', 'FIN'),
('Operações', 'OPS'),
('Tecnologia da Informação', 'IT')
ON CONFLICT (code) DO NOTHING;

INSERT INTO project_types (name, code) VALUES
('Desenvolvimento de Software', 'SOFTWARE'),
('Website', 'WEBSITE'),
('Aplicativo Mobile', 'MOBILE'),
('Consultoria', 'CONSULTING'),
('Treinamento', 'TRAINING'),
('Pesquisa e Desenvolvimento', 'RND'),
('Infraestrutura', 'INFRASTRUCTURE')
ON CONFLICT (code) DO NOTHING;

INSERT INTO sectors (name, code) VALUES
('Tecnologia', 'TECH'),
('Saúde', 'HEALTH'),
('Educação', 'EDUCATION'),
('Financeiro', 'FINANCE'),
('Varejo', 'RETAIL'),
('Manufactura', 'MANUFACTURING'),
('Serviços', 'SERVICES')
ON CONFLICT (code) DO NOTHING;

INSERT INTO areas (name, code, description, area_type, specialization_type, priority_level, requires_certification, budget_limit) VALUES
('Frontend', 'FRONTEND', 'Desenvolvimento de interfaces de usuário', 'BASIC', NULL, NULL, NULL, NULL),
('Backend', 'BACKEND', 'Desenvolvimento de serviços e APIs', 'BASIC', NULL, NULL, NULL, NULL),
('Mobile', 'MOBILE', 'Desenvolvimento de aplicações móveis', 'BASIC', NULL, NULL, NULL, NULL),
('DevOps', 'DEVOPS', 'Infraestrutura e automação', 'BASIC', NULL, NULL, NULL, NULL),
('UI/UX', 'UIUX', 'Design de experiência do usuário', 'BASIC', NULL, NULL, NULL, NULL),
('Banco de Dados', 'DATABASE', 'Administração e modelagem de dados', 'BASIC', NULL, NULL, NULL, NULL),
('Segurança', 'SECURITY', 'Segurança da informação', 'BASIC', NULL, NULL, NULL, NULL),
('Segurança Crítica', 'SEC_CRITICAL', 'Segurança para sistemas críticos', 'SPECIALIZED', 'CYBER_SECURITY', 5, true, 500000.00),
('AI/Machine Learning', 'AI_ML', 'Inteligência artificial e aprendizado de máquina', 'SPECIALIZED', 'ARTIFICIAL_INTELLIGENCE', 4, true, 300000.00),
('Blockchain', 'BLOCKCHAIN', 'Desenvolvimento em tecnologia blockchain', 'SPECIALIZED', 'DISTRIBUTED_SYSTEMS', 4, true, 250000.00),
('Cloud Architecture', 'CLOUD_ARCH', 'Arquitetura de soluções em nuvem', 'SPECIALIZED', 'CLOUD_COMPUTING', 3, false, 200000.00)
ON CONFLICT (code) DO NOTHING;

INSERT INTO project_statuses (name, code, color, sort_order, is_initial, is_final) VALUES
('Novo', 'NEW', '#3B82F6', 1, true, false),
('Em Análise', 'ANALYZING', '#F59E0B', 2, false, false),
('Aprovado', 'APPROVED', '#10B981', 3, false, false),
('Em Desenvolvimento', 'DEVELOPMENT', '#8B5CF6', 4, false, false),
('Em Testes', 'TESTING', '#F97316', 5, false, false),
('Finalizado', 'COMPLETED', '#059669', 6, false, true),
('Cancelado', 'CANCELLED', '#DC2626', 7, false, true),
('Suspenso', 'SUSPENDED', '#6B7280', 8, false, false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO proposal_statuses (name, code, color, sort_order, is_initial, is_final) VALUES
('Rascunho', 'DRAFT', '#9CA3AF', 1, true, false),
('Em Análise', 'ANALYZING', '#F59E0B', 2, false, false),
('Aguardando Cliente', 'WAITING_CLIENT', '#3B82F6', 3, false, false),
('Aprovada', 'APPROVED', '#10B981', 4, false, true),
('Rejeitada', 'REJECTED', '#DC2626', 5, false, true),
('Cancelada', 'CANCELLED', '#6B7280', 6, false, true),
('Expirada', 'EXPIRED', '#EF4444', 7, false, true)
ON CONFLICT (code) DO NOTHING;

INSERT INTO offices (name, code, city, country_id, is_main_office) VALUES
('Matriz', 'HQ', 'São Paulo', 1, true),
('Filial Rio de Janeiro', 'RJ', 'Rio de Janeiro', 1, false),
('Filial Brasília', 'BSB', 'Brasília', 1, false),
('Filial Internacional - EUA', 'USA', 'New York', 2, false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO clients_suppliers (name, document, document_type, email, phone, city, state, country_id, type) VALUES
('Tech Solutions Ltda', '12.345.678/0001-90', 'CNPJ', 'contato@techsolutions.com.br', '(11) 3333-1111', 'São Paulo', 'SP', 1, 'client'),
('Global Systems Inc', '987654321', 'EIN', 'info@globalsystems.com', '+1-555-123-4567', 'New York', 'NY', 2, 'client'),
('Software Providers S.A.', '98.765.432/0001-10', 'CNPJ', 'vendas@softproviders.com.br', '(21) 4444-2222', 'Rio de Janeiro', 'RJ', 1, 'supplier'),
('Innovation Corp', '456789123', 'EIN', 'procurement@innovation.com', '+1-555-987-6543', 'San Francisco', 'CA', 2, 'both'),
('Consultoria Alpha', '11.222.333/0001-44', 'CNPJ', 'alpha@consultoria.com.br', '(11) 5555-3333', 'São Paulo', 'SP', 1, 'supplier'),
('MegaCorp International', '123456789', 'TAX ID', 'contact@megacorp.de', '+49-30-12345678', 'Berlin', 'Berlin', 3, 'client');

INSERT INTO users (name, email, password_hash, phone, department_id, position, role) VALUES
('Admin Sistema', 'admin@victorapi.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '(11) 99999-0000', 1, 'Administrador do Sistema', 'ROLE_ADMIN'),
('João Silva', 'joao.silva@empresa.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '(11) 99999-1111', 1, 'Desenvolvedor Senior', 'ROLE_USER'),
('Maria Santos', 'maria.santos@empresa.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '(11) 99999-2222', 2, 'Gerente de Marketing', 'ROLE_USER'),
('Pedro Oliveira', 'pedro.oliveira@empresa.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '(11) 99999-3333', 3, 'Vendedor', 'ROLE_USER'),
('Ana Costa', 'ana.costa@empresa.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '(11) 99999-4444', 7, 'Analista de Sistemas', 'ROLE_USER'),
('Carlos Pereira', 'carlos.pereira@empresa.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '(11) 99999-5555', 5, 'Gerente Financeiro', 'ROLE_USER'),
('Juliana Lima', 'juliana.lima@empresa.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '(11) 99999-6666', 4, 'Analista de RH', 'ROLE_USER')
ON CONFLICT (email) DO NOTHING;

INSERT INTO contracts (name, description, contract_number, start_date, end_date, value, coin_type_id, client_supplier_id) VALUES
('Desenvolvimento Sistema ERP', 'Contrato para desenvolvimento de sistema ERP completo', 'CONT-2024-001', '2024-01-15', '2024-12-15', 250000.00, 1, 1),
('Licenciamento Software', 'Contrato de licenciamento de software por 2 anos', 'CONT-2024-002', '2024-03-01', '2026-02-28', 50000.00, 2, 2),
('Consultoria em TI', 'Serviços de consultoria em tecnologia da informação', 'CONT-2024-003', '2024-06-01', '2024-11-30', 75000.00, 1, 5),
('Desenvolvimento Mobile App', 'Desenvolvimento de aplicativo mobile para Android e iOS', 'CONT-2024-004', '2024-02-01', '2024-08-31', 120000.00, 2, 4),
('Suporte Técnico', 'Contrato de suporte técnico 24/7 por 1 ano', 'CONT-2024-005', '2024-04-01', '2025-03-31', 30000.00, 3, 6);

INSERT INTO proposals (name, description, proposal_number, department_id, sector_id, area_id, client_supplier_id, office_id, coin_type_id, country_id, status_id, responsible_id, site, value, payment_days, estimated_start, probability, company_name, priority) VALUES
('Proposta Sistema CRM', 'Desenvolvimento de sistema de gestão de relacionamento com cliente', 'PROP-2024-001', 1, 1, 2, 1, 1, 1, 1, 2, 2, 'www.crmsystem.com.br', 180000.00, 30, '2024-09-01', 75.00, 'Tech Solutions Ltda', 1),
('Proposta E-commerce Internacional', 'Plataforma de e-commerce para mercado internacional', 'PROP-2024-002', 1, 5, 1, 2, 4, 2, 2, 4, 3, 'www.globalecommerce.com', 300000.00, 45, '2024-10-15', 90.00, 'Global Systems Inc', 1),
('Proposta App Mobile Saúde', 'Aplicativo mobile para gestão de saúde pessoal', 'PROP-2024-003', 1, 2, 3, 4, 1, 2, 2, 3, 5, 'www.healthapp.com', 95000.00, 30, '2024-08-20', 65.00, 'Innovation Corp', 2),
('Proposta Consultoria DevOps', 'Serviços de consultoria em DevOps e infraestrutura', 'PROP-2024-004', 7, 1, 4, 6, 1, 3, 3, 1, 4, NULL, 45000.00, 15, '2024-09-10', 40.00, 'MegaCorp International', 3),
('Proposta Website Institucional', 'Desenvolvimento de website institucional responsivo', 'PROP-2024-005', 1, 7, 1, 3, 2, 1, 1, 2, 2, 'www.siteinstitucional.com.br', 25000.00, 30, '2024-11-01', 80.00, 'Software Providers S.A.', 2),
('Proposta Segurança Crítica', 'Implementação de sistema de segurança crítica para dados sensíveis', 'PROP-2024-006', 7, 1, 8, 6, 1, 1, 1, 1, 1, 'www.securitysystem.com.br', 750000.00, 60, '2024-12-01', 85.00, 'MegaCorp International', 1),
('Proposta AI/ML Analytics', 'Sistema de analytics com inteligência artificial e machine learning', 'PROP-2024-007', 1, 1, 9, 2, 4, 2, 2, 2, 5, 'www.aianalytics.com', 450000.00, 45, '2025-01-15', 70.00, 'Global Systems Inc', 1),
('Proposta Blockchain DeFi', 'Plataforma DeFi baseada em blockchain para finanças descentralizadas', 'PROP-2024-008', 1, 4, 10, 4, 1, 1, 1, 3, 6, 'www.defiplatform.com', 380000.00, 30, '2025-02-01', 60.00, 'Innovation Corp', 2);

INSERT INTO projects (name, description, departments_id, project_types_id, sectors_id, areas_id, project_statuses_id, countries_id, clients_suppliers_id, coin_type_id, origin_proposal_id, billable_fl, international_fl, site, exchange_rate, classification, investiment_fl, product_fl) VALUES
('Sistema ERP Tech Solutions', 'Desenvolvimento completo do sistema ERP para gestão empresarial', 1, 1, 1, 2, 4, 1, 1, 1, 1, true, false, 'www.erpsystem.com.br', 1.0000, 'Projeto Estratégico', false, true),
('E-commerce Global Platform', 'Plataforma de e-commerce internacional com múltiplas moedas', 1, 2, 5, 1, 3, 2, 2, 2, 2, true, true, 'www.globalecommerce.com', 5.2500, 'Projeto Internacional', true, true),
('Health Mobile App', 'Aplicativo mobile para monitoramento de saúde pessoal', 1, 3, 2, 3, 5, 2, 4, 2, 3, true, true, 'www.healthapp.com', 5.2500, 'Projeto de Saúde', false, true),
('Infraestrutura DevOps', 'Implementação de infraestrutura DevOps e CI/CD', 7, 7, 1, 4, 1, 3, 6, 3, 4, true, true, NULL, 6.1200, 'Projeto de Infraestrutura', true, false),
('Sistema Interno de RH', 'Sistema interno para gestão de recursos humanos', 4, 1, 7, 2, 6, 1, NULL, 1, NULL, false, false, 'hr.empresa.com', 1.0000, 'Projeto Interno', false, false),
('Website Institucional', 'Website institucional responsivo e moderno', 1, 2, 7, 1, 4, 1, 3, 1, 5, true, false, 'www.siteinstitucional.com.br', 1.0000, 'Projeto Marketing', false, false),
('App de Vendas Mobile', 'Aplicativo mobile para equipe de vendas', 1, 3, 1, 3, 2, 1, NULL, 1, NULL, false, false, 'vendas.empresa.com', 1.0000, 'Projeto Vendas', false, true),
('Sistema Segurança Crítica', 'Implementação de sistema de segurança crítica empresarial', 7, 1, 1, 8, 1, 1, 6, 1, 6, true, false, 'security.megacorp.com', 1.0000, 'Projeto Segurança Crítica', true, true),
('Plataforma AI Analytics', 'Sistema de analytics com inteligência artificial', 1, 1, 1, 9, 2, 2, 2, 2, 7, true, true, 'ai.globalsystems.com', 5.2500, 'Projeto AI/ML', true, true),
('DeFi Blockchain Platform', 'Plataforma de finanças descentralizadas em blockchain', 1, 1, 4, 10, 3, 2, 4, 2, 8, true, true, 'defi.innovation.com', 5.2500, 'Projeto Blockchain', true, true),
('Cloud Migration Project', 'Migração completa da infraestrutura para arquitetura em nuvem', 7, 7, 1, 11, 4, 1, 1, 1, NULL, true, false, 'cloud.empresa.com', 1.0000, 'Projeto Cloud Architecture', true, false);