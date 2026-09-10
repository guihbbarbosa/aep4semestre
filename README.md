# Emprego Justo

Plataforma de avaliação e transparência sobre condições de trabalho e direitos trabalhistas nas empresas.

**AEP – 4º Semestre (2026.2) – Unicesumar (Maringá – PR)**
Curso: Engenharia de Software 

## Equipe (Squad)

| Integrante | GitHub | Papel principal |
| Guilherme Henrique Beitum Barbosa| @guihbbarbosa (https://github.com/guihbbarbosa) | Banco de dados e persistência e Interface |
| Felipe Falaschi Cadedo | @felipefalaschi (https://github.com/felipefalaschi) | Modelo de domínio e regras de negócio e Relatórios |


## ODS atendida

**ODS 8 – Trabalho Decente e Crescimento Econômico** (meta 8.8: proteção dos direitos trabalhistas e promoção de ambientes de trabalho seguros).
Complementarmente, **ODS 10 – Redução das Desigualdades**.

O sistema reduz a assimetria de informação entre empresa e trabalhador: quem procura emprego passa a consultar,
de forma estruturada e anônima, como é a rotina real de uma empresa antes de aceitar a vaga.

## Lista de Requisitos (Escopo)

| ID | Requisito |
|---|---|
| RF01 | O sistema deve permitir o cadastro de trabalhadores contendo nome completo, e-mail, senha, cidade/UF, setor de atuação e tempo de experiência, com autenticação por e-mail e senha criptografada. |
| RF02 | O sistema deve permitir o cadastro e a consulta de empresas contendo razão social, CNPJ, setor econômico, porte e cidade/UF, bloqueando o cadastro de CNPJ já existente. |
| RF03 | O sistema deve permitir o registro de avaliações de empresa, com nota de 1 a 5 em cada um dos cinco critérios (jornada de trabalho, remuneração e benefícios, segurança e saúde, respeito e assédio, ambiente e gestão), comentário descritivo, indicação de vínculo (atual ou ex-funcionário) e opção de publicação anônima. |
| RF04 | O sistema deve permitir o registro de denúncias de irregularidade trabalhista, classificadas por categoria (hora extra não paga, ausência de EPI, assédio moral, atraso salarial ou outra) e por grau de gravidade, encaminhando-as automaticamente para a fila de moderação. |
| RF05 | O sistema deve calcular e exibir o Índice de Emprego Justo de cada empresa, obtido pela média ponderada das notas aprovadas, exibindo o resultado apenas quando houver no mínimo 3 avaliações aprovadas. |
| RF06 | O sistema deve permitir a busca e a filtragem de empresas por razão social, setor, cidade/UF e nota mínima, apresentando o resultado ordenado pelo Índice de Emprego Justo. |
| RF07 | O sistema deve permitir que o moderador liste as publicações pendentes e realize as operações de aprovação, edição, reprovação e exclusão sobre elas, registrando a data da decisão. |
| RF08 | O sistema deve permitir a geração de relatórios consolidados por empresa e por setor, apresentando a média de cada critério e a quantidade de denúncias por categoria, com exportação em arquivo. |

### Regras de negócio

- **RN01** – Toda publicação é gravada com status "pendente" e só se torna visível após aprovação de um moderador.
- **RN02** – O Índice de Emprego Justo só é exibido publicamente com no mínimo 3 avaliações aprovadas.
- **RN03** – Um mesmo usuário pode avaliar a mesma empresa apenas uma vez a cada 12 meses.
- **RN04** – Em publicações anônimas, a identidade não aparece em nenhuma tela pública, mas fica registrada no banco para auditoria.

## Cronograma / Backlog (2º Bimestre)

| Sprint / Data | Épico | User Story | Responsável |
|---|---|---|---|
| Sprint 1 – 05/09 a 19/09 | Cadastros e Base | COMO UM trabalhador EU QUERO criar minha conta e localizar a empresa em que trabalho PARA QUE eu consiga avaliá-la sem duplicar cadastros. | Guilherme |
| Sprint 2 – 20/09 a 04/10 | Motor de Avaliação | COMO UM trabalhador EU QUERO avaliar a empresa por critérios de forma anônima PARA QUE eu relate a realidade sem medo de retaliação. | Felipe |
| Sprint 3 – 05/10 a 19/10 | Índice e Consulta | COMO UM candidato EU QUERO consultar o Índice de Emprego Justo e filtrar empresas por setor e cidade PARA QUE eu decida onde me candidatar com informação real. | Felipe |
| Sprint 4 – 20/10 a 03/11 | Moderação e Relatórios | COMO UM moderador EU QUERO aprovar, editar ou remover publicações denunciadas PARA QUE a plataforma mantenha credibilidade. | Guilherme e Felipe |

## Tecnologias

- **Linguagem:** Java com Spring Boot
- **Banco de dados:** SQL Server 
- **Arquitetura:** MVC em camadas (model, repository, service, controller)
- **Controle de versão:** Git / GitHub

## Estrutura do repositório

```
/src        código-fonte da aplicação, organizado por camada
/docs       documento da 1ª entrega (PDF), diagramas e matriz CSD
/database   script SQL de criação das tabelas e carga inicial dos critérios
README.md   este arquivo
```

## Como executar o projeto (a ser detalhado na 2ª entrega)

1. Instalar o [JDK versão] e o SQL Server [versão] com o SQL Server Management Studio (SSMS).
2. Criar o banco no SSMS: `CREATE DATABASE emprego_justo;`
3. Rodar o script de criação das tabelas: abrir `database/schema.sql` no SSMS e executar, ou pelo terminal:
   `sqlcmd -S localhost -d emprego_justo -U sa -P [senha] -i database/schema.sql`
4. Configurar a string de conexão em `src/main/resources/application.properties`:
   `spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=emprego_justo;encrypt=false`
5. Executar a aplicação: `./mvnw spring-boot:run`

## Diagramas

- Diagrama de Classes (UML): `docs/diagrama-classes.png`
- Diagrama Entidade-Relacionamento (DER): `docs/der.png`
