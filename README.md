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

# 2 entrega AEP EMPREGO JUSTO
# Tecnologias

- Java puro, usando JDBC para acessar o banco
- MySQL, administrado pelo phpMyAdmin
- Aplicação de console

Na 1ª entrega a gente tinha planejado Spring Boot com SQL Server. No 2º bimestre fizemos duas mudanças:

Java puro com JDBC no lugar do Spring Boot. Com o Spring as consultas são geradas automaticamente, e a gente queria escrever o SQL na mão para usar o GROUP BY e o HAVING que justificamos no documento do 1º bimestre.

MySQL no lugar do SQL Server. É o banco que a gente usa na matéria de Banco de Dados, já instalado e com o phpMyAdmin. Os motivos da 1ª entrega continuam valendo, porque os dois são bancos relacionais:

- integridade referencial pelas chaves estrangeiras
- média ponderada com GROUP BY e HAVING dentro do banco
- tabela associativa resolvendo o N:M entre publicações e critérios

O que mudou foram só os tipos de dados (AUTO_INCREMENT no lugar de IDENTITY, por exemplo).

A senha é salva com hash SHA-256 no lugar do BCrypt, que precisaria de uma biblioteca externa. Mesmo assim, a senha não fica salva em texto puro.

# Requisitos

| ID | Requisito | Situação |
|---|---|---|
| RF01 | Cadastro de trabalhador com nome, e-mail, senha, cidade/UF, setor e tempo de experiência | Feito |
| RF02 | Cadastro e consulta de empresas, bloqueando CNPJ repetido | Feito |
| RF03 | Avaliação com nota de 1 a 5 nos cinco critérios, comentário e opção de anonimato | Feito (CRUD completo) |
| RF04 | Denúncia por categoria e gravidade, indo para a fila de moderação | Feito |
| RF05 | Índice de Emprego Justo (média ponderada), só com 5 avaliações aprovadas | Feito |
| RF06 | Busca de empresas por nome, setor ou cidade | Feito |
| RF07 | Moderação: listar pendentes, aprovar e reprovar | Feito |
| RF08 | Listagem das publicações de uma empresa com o impacto de cada uma | Feito |

# Regras de negócio

- RN01 – Toda publicação nasce como PENDENTE e só conta depois de aprovada.
- RN02 – O índice só aparece com no mínimo 5 avaliações aprovadas.
- RN03 – Um usuário avalia a mesma empresa uma vez a cada 12 meses.
- RN04 – Na publicação anônima o nome não aparece, mas o usuario_id continua salvo no banco.

# Cronograma

| Sprint | Épico | Responsável |
| Sprint 1 – 05/09 a 19/09 | Cadastros e base | Guilherme |
| Sprint 2 – 20/09 a 04/10 | Motor de avaliação | Felipe |
| Sprint 3 – 05/10 a 19/10 | Índice e consulta | Felipe |
| Sprint 4 – 20/10 a 03/11 | Moderação e relatórios | Guilherme e Felipe |

# POO no projeto

- **Classe abstrata:** `Usuario`, em `src/modelo`
- **Interface:** `Publicacao`
- **Herança:** `Trabalhador` e `Moderador` herdam de `Usuario`
- **Polimorfismo (@Override):** `calcularPontuacao()` na `Avaliacao` faz a média ponderada das notas, e na `Denuncia` devolve um valor negativo pela gravidade
- **Composição:** a `Avaliacao` tem uma lista de `NotaCriterio`

# Pastas

/src/modelo    as classes (Usuario, Trabalhador, Moderador, Publicacao,
               Avaliacao, Denuncia, Empresa, NotaCriterio)
/src/dao       acesso ao banco
/src/app       Main.java, com o menu
/database      schema.sql
/docs          PDF da 1ª entrega e os diagramas


# Como rodar

1. O que precisa ter

- JDK 17 ou mais novo
- MySQL rodando (a gente usa o Vertrigo, que já vem com MySQL e phpMyAdmin)
- O driver `mysql-connector-j-8.4.0.jar`, que já está na pasta do projeto, junto com o README

2. Criar o banco

Abrir o phpMyAdmin, ir na aba Importar, escolher o arquivo `database/schema.sql` e executar. Ele cria:

- o banco `emprego_justo`
- as cinco tabelas
- os cinco critérios
- duas empresas de exemplo
- um moderador

3. Configurar a conexão

Copiar o `config.properties.exemplo` para um arquivo `config.properties` e colocar o usuário e a senha do seu MySQL (os mesmos que você usa para entrar no phpMyAdmin).

4. Compilar e rodar (Windows)

javac -d bin src/modelo/*.java src/dao/*.java src/app/*.java
java -cp "bin;mysql-connector-j-8.4.0.jar" app.Main

No Linux ou Mac, trocar o `;` por `:` no segundo comando.

Login do moderador para testar:** moderador@empregojusto.com / senha 123456

# Testando

1. Criar uma conta e entrar.
2. Cadastrar uma empresa (ou usar uma das que já vêm no banco).
3. Avaliar a empresa dando as cinco notas.
4. Ver "Minhas avaliações", editar e conferir que a nota mudou.
5. Fazer uma denúncia na mesma empresa.
6. Ver "Publicações de uma empresa": a avaliação aparece com nota positiva e a denúncia com valor negativo.
7. Entrar como moderador e aprovar as publicações.
