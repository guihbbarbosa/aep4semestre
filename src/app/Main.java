package app;

import java.util.List;
import java.util.Scanner;

import dao.CriterioDAO;
import dao.EmpresaDAO;
import dao.PublicacaoDAO;
import dao.UsuarioDAO;
import modelo.Avaliacao;
import modelo.Denuncia;
import modelo.Empresa;
import modelo.Moderador;
import modelo.NotaCriterio;
import modelo.Publicacao;
import modelo.Trabalhador;
import modelo.Usuario;

// Projeto Emprego Justo - AEP 4o Semestre
// Programa de console com o menu do sistema
public class Main {

    private static Scanner teclado = new Scanner(System.in);

    private static UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static EmpresaDAO empresaDAO = new EmpresaDAO();
    private static PublicacaoDAO publicacaoDAO = new PublicacaoDAO();
    private static CriterioDAO criterioDAO = new CriterioDAO();

    private static Usuario usuarioLogado = null;

    public static void main(String[] args) {
        System.out.println("=== EMPREGO JUSTO ===");

        boolean rodando = true;

        while (rodando) {
            if (usuarioLogado == null) {
                rodando = menuInicial();
            } else {
                rodando = menuPrincipal();
            }
        }

        System.out.println("Programa encerrado.");
    }

    // ----- MENUS -----

    private static boolean menuInicial() {
        System.out.println();
        System.out.println("1 - Criar conta");
        System.out.println("2 - Entrar");
        System.out.println("0 - Sair");
        System.out.print("Opcao: ");
        String opcao = teclado.nextLine();

        if (opcao.equals("1")) {
            cadastrarTrabalhador();
        } else if (opcao.equals("2")) {
            fazerLogin();
        } else if (opcao.equals("0")) {
            return false;
        } else {
            System.out.println("Opcao invalida.");
        }

        return true;
    }

    private static boolean menuPrincipal() {
        System.out.println();
        System.out.println(usuarioLogado.descreverPerfil());
        System.out.println("1 - Cadastrar empresa");
        System.out.println("2 - Buscar empresas");
        System.out.println("3 - Avaliar empresa");
        System.out.println("4 - Minhas avaliacoes");
        System.out.println("5 - Editar avaliacao");
        System.out.println("6 - Excluir avaliacao");
        System.out.println("7 - Fazer denuncia");
        System.out.println("8 - Ranking das empresas");
        System.out.println("9 - Publicacoes de uma empresa");

        if (usuarioLogado instanceof Moderador) {
            System.out.println("10 - Moderar publicacoes");
        }

        System.out.println("0 - Sair da conta");
        System.out.print("Opcao: ");
        String opcao = teclado.nextLine();

        try {
            if (opcao.equals("1")) {
                cadastrarEmpresa();
            } else if (opcao.equals("2")) {
                buscarEmpresas();
            } else if (opcao.equals("3")) {
                criarAvaliacao();
            } else if (opcao.equals("4")) {
                listarMinhasAvaliacoes();
            } else if (opcao.equals("5")) {
                editarAvaliacao();
            } else if (opcao.equals("6")) {
                excluirAvaliacao();
            } else if (opcao.equals("7")) {
                registrarDenuncia();
            } else if (opcao.equals("8")) {
                mostrarRanking();
            } else if (opcao.equals("9")) {
                mostrarPublicacoesDaEmpresa();
            } else if (opcao.equals("10") && usuarioLogado instanceof Moderador) {
                moderarPublicacoes();
            } else if (opcao.equals("0")) {
                usuarioLogado = null;
                System.out.println("Voce saiu da conta.");
            } else {
                System.out.println("Opcao invalida.");
            }
        } catch (Exception erro) {
            System.out.println("Deu erro: " + erro.getMessage());
        }

        return true;
    }

    // ----- USUARIO -----

    private static void cadastrarTrabalhador() {
        try {
            System.out.print("Nome: ");
            String nome = teclado.nextLine();

            System.out.print("E-mail: ");
            String email = teclado.nextLine();

            if (usuarioDAO.emailJaCadastrado(email)) {
                System.out.println("Esse e-mail ja esta cadastrado.");
                return;
            }

            System.out.print("Senha: ");
            String senha = teclado.nextLine();

            System.out.print("Cidade/UF: ");
            String cidadeUf = teclado.nextLine();

            System.out.print("Setor em que trabalha: ");
            String setor = teclado.nextLine();

            int anos = lerNumero("Anos de experiencia: ");

            String hash = UsuarioDAO.gerarHash(senha);
            Trabalhador trabalhador = new Trabalhador(0, nome, email, hash, cidadeUf, setor, anos);
            usuarioDAO.inserirTrabalhador(trabalhador);

            System.out.println("Conta criada. Seu codigo e " + trabalhador.getId() + ".");
        } catch (Exception erro) {
            System.out.println("Deu erro: " + erro.getMessage());
        }
    }

    private static void fazerLogin() {
        try {
            System.out.print("E-mail: ");
            String email = teclado.nextLine();

            System.out.print("Senha: ");
            String senha = teclado.nextLine();

            Usuario usuario = usuarioDAO.autenticar(email, senha);

            if (usuario == null) {
                System.out.println("E-mail ou senha errados.");
                return;
            }

            usuarioLogado = usuario;
            System.out.println("Bem-vindo!");
        } catch (Exception erro) {
            System.out.println("Deu erro: " + erro.getMessage());
        }
    }

    // ----- EMPRESAS -----

    private static void cadastrarEmpresa() throws Exception {
        System.out.print("Razao social: ");
        String razaoSocial = teclado.nextLine();

        System.out.print("CNPJ: ");
        String cnpj = teclado.nextLine();

        if (empresaDAO.cnpjJaCadastrado(cnpj)) {
            System.out.println("Esse CNPJ ja esta cadastrado.");
            return;
        }

        System.out.print("Setor: ");
        String setor = teclado.nextLine();

        System.out.print("Porte (PEQUENO, MEDIO ou GRANDE): ");
        String porte = teclado.nextLine();

        System.out.print("Cidade/UF: ");
        String cidadeUf = teclado.nextLine();

        Empresa empresa = new Empresa(0, razaoSocial, cnpj, setor, porte, cidadeUf);
        empresaDAO.inserir(empresa);

        System.out.println("Empresa cadastrada com o codigo " + empresa.getId() + ".");
    }

    private static void buscarEmpresas() throws Exception {
        System.out.print("Digite o nome, setor ou cidade (ou ENTER pra ver todas): ");
        String termo = teclado.nextLine();

        List<Empresa> empresas;

        if (termo.trim().equals("")) {
            empresas = empresaDAO.listarTodas();
        } else {
            empresas = empresaDAO.buscar(termo);
        }

        if (empresas.isEmpty()) {
            System.out.println("Nenhuma empresa encontrada.");
            return;
        }

        System.out.println();
        for (Empresa empresa : empresas) {
            System.out.println(empresa.getId() + " - " + empresa.getRazaoSocial()
                    + " | " + empresa.getSetor() + " | " + empresa.getCidadeUf());
        }
    }

    // ----- AVALIACOES (CRUD) -----

    private static void criarAvaliacao() throws Exception {
        buscarEmpresas();
        int empresaId = lerNumero("Codigo da empresa que voce quer avaliar: ");

        Empresa empresa = empresaDAO.buscarPorId(empresaId);

        if (empresa == null) {
            System.out.println("Empresa nao encontrada.");
            return;
        }

        System.out.println("Avaliando: " + empresa.getRazaoSocial());

        System.out.print("Voce trabalha la agora? (s/n): ");
        String resposta = teclado.nextLine();
        String vinculo = "EX_FUNCIONARIO";
        if (resposta.equalsIgnoreCase("s")) {
            vinculo = "ATUAL";
        }

        System.out.print("Comentario: ");
        String comentario = teclado.nextLine();

        System.out.print("Publicar anonimo? (s/n): ");
        boolean anonima = teclado.nextLine().equalsIgnoreCase("s");

        Avaliacao avaliacao = new Avaliacao(0, usuarioLogado.getId(), empresaId,
                comentario, anonima, vinculo, "PENDENTE");

        // pega os criterios do banco e pede uma nota pra cada um
        List<NotaCriterio> criterios = criterioDAO.listarParaAvaliacao();

        System.out.println();
        System.out.println("De uma nota de 1 a 5 pra cada item:");

        for (NotaCriterio criterio : criterios) {
            int nota = lerNota(criterio.getCriterioNome());
            criterio.setNota(nota);
            avaliacao.adicionarNota(criterio);
        }

        publicacaoDAO.inserirAvaliacao(avaliacao);

        System.out.println("Avaliacao salva com o codigo " + avaliacao.getId() + ".");
        System.out.println("Nota: " + String.format("%.1f", avaliacao.calcularPontuacao()));
        System.out.println("Ela fica PENDENTE ate o moderador aprovar.");
    }

    private static void listarMinhasAvaliacoes() throws Exception {
        List<Avaliacao> avaliacoes = publicacaoDAO.listarAvaliacoesDoUsuario(usuarioLogado.getId());

        if (avaliacoes.isEmpty()) {
            System.out.println("Voce ainda nao avaliou nenhuma empresa.");
            return;
        }

        System.out.println();
        for (Avaliacao avaliacao : avaliacoes) {
            Empresa empresa = empresaDAO.buscarPorId(avaliacao.getEmpresaId());

            System.out.println("Codigo " + avaliacao.getId() + " - " + empresa.getRazaoSocial());
            System.out.println("   Nota: " + String.format("%.1f", avaliacao.calcularPontuacao())
                    + " | Status: " + avaliacao.getStatusModeracao());
            System.out.println("   Comentario: " + avaliacao.getComentario());

            for (NotaCriterio nota : avaliacao.getNotas()) {
                System.out.println("      " + nota.getCriterioNome() + ": " + nota.getNota());
            }

            System.out.println();
        }
    }

    private static void editarAvaliacao() throws Exception {
        listarMinhasAvaliacoes();
        int id = lerNumero("Codigo da avaliacao que voce quer editar: ");

        Avaliacao avaliacao = publicacaoDAO.buscarAvaliacaoPorId(id);

        if (avaliacao == null) {
            System.out.println("Avaliacao nao encontrada.");
            return;
        }

        if (avaliacao.getUsuarioId() != usuarioLogado.getId()) {
            System.out.println("Essa avaliacao nao e sua.");
            return;
        }

        System.out.println("Comentario atual: " + avaliacao.getComentario());
        System.out.print("Novo comentario (ENTER pra manter): ");
        String comentario = teclado.nextLine();

        if (!comentario.trim().equals("")) {
            avaliacao.setComentario(comentario);
        }

        System.out.print("Quer mudar as notas? (s/n): ");
        if (teclado.nextLine().equalsIgnoreCase("s")) {
            for (NotaCriterio nota : avaliacao.getNotas()) {
                System.out.println("Nota atual de " + nota.getCriterioNome() + ": " + nota.getNota());
                nota.setNota(lerNota(nota.getCriterioNome()));
            }
        }

        publicacaoDAO.atualizarAvaliacao(avaliacao);

        System.out.println("Avaliacao atualizada. Nota nova: "
                + String.format("%.1f", avaliacao.calcularPontuacao()));
        System.out.println("Ela voltou pra PENDENTE.");
    }

    private static void excluirAvaliacao() throws Exception {
        listarMinhasAvaliacoes();
        int id = lerNumero("Codigo da avaliacao que voce quer excluir: ");

        Avaliacao avaliacao = publicacaoDAO.buscarAvaliacaoPorId(id);

        if (avaliacao == null) {
            System.out.println("Avaliacao nao encontrada.");
            return;
        }

        if (avaliacao.getUsuarioId() != usuarioLogado.getId()) {
            System.out.println("Essa avaliacao nao e sua.");
            return;
        }

        System.out.print("Tem certeza? (s/n): ");
        if (!teclado.nextLine().equalsIgnoreCase("s")) {
            System.out.println("Cancelado.");
            return;
        }

        publicacaoDAO.excluir(id);
        System.out.println("Avaliacao excluida.");
    }

    // ----- DENUNCIAS -----

    private static void registrarDenuncia() throws Exception {
        buscarEmpresas();
        int empresaId = lerNumero("Codigo da empresa: ");

        Empresa empresa = empresaDAO.buscarPorId(empresaId);

        if (empresa == null) {
            System.out.println("Empresa nao encontrada.");
            return;
        }

        System.out.println("Categoria:");
        System.out.println("1 - Hora extra nao paga");
        System.out.println("2 - Falta de EPI");
        System.out.println("3 - Assedio moral");
        System.out.println("4 - Atraso de salario");
        System.out.println("5 - Outra");
        int opcao = lerNumero("Opcao: ");

        String categoria = "OUTRA";
        if (opcao == 1) {
            categoria = "HORA_EXTRA";
        } else if (opcao == 2) {
            categoria = "SEM_EPI";
        } else if (opcao == 3) {
            categoria = "ASSEDIO";
        } else if (opcao == 4) {
            categoria = "ATRASO_SALARIAL";
        }

        System.out.print("Descreva o que aconteceu: ");
        String comentario = teclado.nextLine();

        int gravidade = lerNota("Gravidade (1 = leve, 5 = grave)");

        Denuncia denuncia = new Denuncia(0, usuarioLogado.getId(), empresaId,
                comentario, categoria, gravidade, "PENDENTE");

        publicacaoDAO.inserirDenuncia(denuncia);

        System.out.println("Denuncia registrada com o codigo " + denuncia.getId() + ".");
    }

    // ----- RELATORIOS -----

    private static void mostrarRanking() throws Exception {
        List<String> linhas = empresaDAO.listarRanking();

        System.out.println();
        System.out.println("--- RANKING ---");

        if (linhas.isEmpty()) {
            System.out.println("Nenhuma empresa tem as 5 avaliacoes aprovadas que a regra RN02 pede.");
            return;
        }

        for (String linha : linhas) {
            System.out.println(linha);
        }
    }

    // mostra avaliacoes e denuncias juntas.
    // a lista e de Publicacao, mas cada objeto responde do seu jeito
    private static void mostrarPublicacoesDaEmpresa() throws Exception {
        buscarEmpresas();
        int empresaId = lerNumero("Codigo da empresa: ");

        List<Publicacao> publicacoes = publicacaoDAO.listarPorEmpresa(empresaId);

        if (publicacoes.isEmpty()) {
            System.out.println("Essa empresa ainda nao tem publicacoes.");
            return;
        }

        System.out.println();
        double soma = 0;

        for (Publicacao publicacao : publicacoes) {
            System.out.println(publicacao.resumo());
            soma = soma + publicacao.calcularPontuacao();
        }

        System.out.println("Soma do impacto: " + String.format("%.1f", soma));
    }

    // ----- MODERACAO -----

    private static void moderarPublicacoes() throws Exception {
        List<Publicacao> pendentes = publicacaoDAO.listarPendentes();

        if (pendentes.isEmpty()) {
            System.out.println("Nao tem nada pendente.");
            return;
        }

        System.out.println();
        System.out.println("--- PENDENTES ---");

        for (Publicacao publicacao : pendentes) {
            System.out.println(publicacao.resumo());
        }

        int id = lerNumero("Codigo da publicacao: ");

        System.out.print("Aprovar (a) ou reprovar (r)? ");
        String decisao = teclado.nextLine();

        if (decisao.equalsIgnoreCase("a")) {
            publicacaoDAO.atualizarStatus(id, "APROVADA");
            System.out.println("Aprovada.");
        } else if (decisao.equalsIgnoreCase("r")) {
            publicacaoDAO.atualizarStatus(id, "REPROVADA");
            System.out.println("Reprovada.");
        } else {
            System.out.println("Opcao invalida.");
        }
    }

    // ----- METODOS PRA LER DO TECLADO -----

    private static int lerNumero(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = teclado.nextLine();

            try {
                return Integer.parseInt(entrada.trim());
            } catch (NumberFormatException erro) {
                System.out.println("Digite so numeros.");
            }
        }
    }

    private static int lerNota(String nomeDoItem) {
        while (true) {
            int nota = lerNumero(nomeDoItem + " (1 a 5): ");

            if (nota >= 1 && nota <= 5) {
                return nota;
            }

            System.out.println("A nota tem que ser de 1 a 5.");
        }
    }
}
