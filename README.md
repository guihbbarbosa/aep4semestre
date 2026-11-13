# Emprego Justo

Plataforma de avaliação e transparência sobre condições de trabalho nas empresas.

AEP – 4º Semestre (2026.2)
Curso: Engenharia de Software

Integrantes: Guilherme Henrique Beitum Barbosa e Felipe Falaschi Cadedo

ODS 8 – Trabalho Decente e Crescimento Econômico.** O trabalhador vê como é a rotina real de uma empresa antes de aceitar a vaga e pode avaliar de forma anônima, sem medo de retaliação.

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
