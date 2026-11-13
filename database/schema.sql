-- Projeto Emprego Justo - AEP 4o Semestre
-- Script de criacao do banco - MySQL

CREATE DATABASE IF NOT EXISTS emprego_justo
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE emprego_justo;

-- apaga as tabelas se ja existirem, pra poder rodar o script de novo
DROP TABLE IF EXISTS notas_criterio;
DROP TABLE IF EXISTS publicacoes;
DROP TABLE IF EXISTS criterios;
DROP TABLE IF EXISTS empresas;
DROP TABLE IF EXISTS usuarios;

-- Guarda trabalhadores e moderadores na mesma tabela.
-- A coluna tipo_usuario diz qual e qual.
CREATE TABLE usuarios (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    nome              VARCHAR(100) NOT NULL,
    email             VARCHAR(120) NOT NULL UNIQUE,
    senha_hash        VARCHAR(255) NOT NULL,
    tipo_usuario      VARCHAR(20)  NOT NULL,   -- TRABALHADOR ou MODERADOR
    cidade_uf         VARCHAR(60),
    setor_atuacao     VARCHAR(80),
    tempo_experiencia INT,                     -- em anos
    registro_interno  VARCHAR(30),             -- usado so pelo moderador
    criado_em         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- CNPJ e unico pra nao cadastrar a mesma empresa duas vezes (RF02)
CREATE TABLE empresas (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    razao_social VARCHAR(150) NOT NULL,
    cnpj         VARCHAR(18)  NOT NULL UNIQUE,
    setor        VARCHAR(80),
    porte        VARCHAR(20),
    cidade_uf    VARCHAR(60)
) ENGINE=InnoDB;

-- Os 5 criterios de avaliacao e o peso de cada um
CREATE TABLE criterios (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(60)  NOT NULL,
    descricao VARCHAR(200),
    peso      DECIMAL(3,2) NOT NULL
) ENGINE=InnoDB;

-- Guarda avaliacoes e denuncias (tipo_publicacao separa as duas).
-- Categoria e gravidade so sao preenchidas na denuncia.
CREATE TABLE publicacoes (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id       INT NOT NULL,
    empresa_id       INT NOT NULL,
    tipo_publicacao  VARCHAR(20) NOT NULL,   -- AVALIACAO ou DENUNCIA
    comentario       TEXT,
    anonima          BOOLEAN NOT NULL DEFAULT TRUE,
    vinculo          VARCHAR(20),            -- ATUAL ou EX_FUNCIONARIO
    categoria        VARCHAR(50),            -- so na denuncia
    gravidade        INT,                    -- so na denuncia (1 a 5)
    status_moderacao VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    criado_em        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_publicacoes_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_publicacoes_empresa
        FOREIGN KEY (empresa_id) REFERENCES empresas(id)
) ENGINE=InnoDB;

-- Tabela associativa, resolve o N:M entre publicacoes e criterios.
-- Com o ON DELETE CASCADE, apagando a avaliacao as notas somem junto.
CREATE TABLE notas_criterio (
    publicacao_id INT NOT NULL,
    criterio_id   INT NOT NULL,
    nota          INT NOT NULL,

    PRIMARY KEY (publicacao_id, criterio_id),
    CONSTRAINT fk_notas_publicacao
        FOREIGN KEY (publicacao_id) REFERENCES publicacoes(id) ON DELETE CASCADE,
    CONSTRAINT fk_notas_criterio
        FOREIGN KEY (criterio_id) REFERENCES criterios(id),
    -- o MySQL 5.7 aceita esse CHECK mas nao aplica. a validacao de 1 a 5
    -- tambem e feita no Java, no metodo lerNota() do Main
    CONSTRAINT ck_notas_faixa CHECK (nota BETWEEN 1 AND 5)
) ENGINE=InnoDB;

-- Dados iniciais

-- os 5 criterios com os pesos que definimos na 1a entrega
INSERT INTO criterios (nome, descricao, peso) VALUES
    ('Jornada de trabalho',       'Horarios, horas extras, folgas e ferias',      0.25),
    ('Remuneracao e beneficios',  'Salario em dia, beneficios e equiparacao',     0.20),
    ('Seguranca e saude',         'EPI, treinamentos e condicoes do ambiente',    0.25),
    ('Respeito e assedio',        'Tratamento dos gestores e canais de denuncia', 0.20),
    ('Ambiente e gestao',         'Metas claras, crescimento e comunicacao',      0.10);

-- um moderador pra testar. senha 123456 (salva como hash SHA-256)
INSERT INTO usuarios (nome, email, senha_hash, tipo_usuario, registro_interno) VALUES
    ('Moderador do Sistema',
     'moderador@empregojusto.com',
     '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',
     'MODERADOR',
     'MOD-001');

-- duas empresas de exemplo
INSERT INTO empresas (razao_social, cnpj, setor, porte, cidade_uf) VALUES
    ('Transportadora Rota Norte LTDA', '11.111.111/0001-11', 'Logistica', 'MEDIO',   'Maringa/PR'),
    ('Mercado Bom Preco LTDA',         '22.222.222/0001-22', 'Varejo',    'PEQUENO', 'Maringa/PR');
