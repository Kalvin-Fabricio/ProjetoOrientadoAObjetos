import java.util.*;
import java.util.stream.Collectors;

public class Menu {
    private Usuario usuario;
    private Map<String, Usuario> usuariosSalvos;

    public Menu(Usuario usuario, Map<String, Usuario> usuariosSalvos) {
        this.usuario = usuario;
        this.usuariosSalvos = usuariosSalvos;
    }

    public void exibir() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("\n|-----------------------------------------------|");
                System.out.println("| Usuario: " + usuario.getNome());
                System.out.println("|---------------Escolha uma opcao---------------|");
                System.out.println("| 1 - Buscar notícias por termo/data            |");
                System.out.println("| 2 - Gerenciar listas                          |");
                System.out.println("| 3 - Exibir listas                             |");
                System.out.println("| 4 - Ordenar listas                            |");
                System.out.println("| 5 - Sair                                      |");
                System.out.println("|-----------------------------------------------|");
                System.out.print("| Opcao: ");
                String opcao = sc.nextLine().trim();
                System.out.println("|--                                           --|");

                switch (opcao) {
                    case "1": buscarNoticias(sc); break;
                    case "2": gerenciarListas(sc); break;
                    case "3": exibirListas(sc); break;
                    case "4": ordenarListas(sc); break;
                    case "5":
                        salvarUsuarioAtual();
                        System.out.println("| Saindo. Ate logo!");
                        System.out.println("|-----------------------------------------------|");
                        return;
                    default: System.out.println("| Opção invalida.");
                }
                System.out.println("|-----------------------------------------------|");
                salvarUsuarioAtual();
            } catch (Exception e) {
                System.out.println("| Erro: " + e.getMessage());
                System.out.println("|-----------------------------------------------|");
            }
        }
    }

    private void salvarUsuarioAtual() {
        String chave = Armazenamento.padronizaChave(usuario.getNome());
        usuariosSalvos.put(chave, usuario);
        Armazenamento.salvar(usuariosSalvos);
    }

    private void buscarNoticias(Scanner sc) {
        System.out.print("| Buscar por termo: ");
        String termo = sc.nextLine().trim();
        System.out.print("| Filtrar por data (AAAA-MM-DD), ou 'Enter' para ignorar: ");
        String data = sc.nextLine().trim();
        System.out.println("|--                                           --|");
        List<Noticia> noticias = IBGENewsAPI.buscarNoticias(termo, data);
        if (noticias.isEmpty()) {
            System.out.println("|\n| Nenhuma noticia encontrada.");
            return;
        }

        for (int i = 1; i < noticias.size(); i++) {
            System.out.println("| " + i + " - " + noticias.get(i).getTitulo() + " (ID: " + noticias.get(i).getId() + ")");
        }
        System.out.print("| \n| Ver detalhes de qual noticia? ('Enter' para voltar): ");
        String idx = sc.nextLine().trim();
        if (idx.matches("\\d+")) {
            int i = Integer.parseInt(idx);
            if (i >= 0 && i < noticias.size()) {
                System.out.println("|--                                           --|");
                System.out.println("| Resultado da busca:                           |");
                System.out.println(noticias.get(i));
            }
        }
        
    }

    private void gerenciarListas(Scanner sc) {
        System.out.println("|       --Qual lista deseja gerenciar?--        |");
        System.out.println("| 1 - Lista de favoritos\n| 2 - Lista de 'ja lidas'\n| 3 - Lista 'para ler depois'");
        System.out.print("|\n| Opcao: ");
        String op = sc.nextLine().trim();
        String lista = "";
        if (op.equals("1")) lista = "favoritos";
        else if (op.equals("2")) lista = "lidas";
        else if (op.equals("3")) lista = "paraLerDepois";
        else {
            System.out.println("|--                                           --|");
            System.out.println("| Opcao invalida.");
            return;
        }
        System.out.println("|--                                           --|");
        System.out.print("| Digite um termo para buscar noticias: ");
        String termo = sc.nextLine().trim();
        System.out.println("|");
        List<Noticia> noticias = IBGENewsAPI.buscarNoticias(termo, "");
        if (noticias.isEmpty()) {
            System.out.println("| Nenhuma noticia encontrada.");
            return;
        }
        for (int i = 1; i < noticias.size(); i++) {
            System.out.println("| " + i + " - " + noticias.get(i).getTitulo() + " (ID: " + noticias.get(i).getId() + ")");
        }
        System.out.print("|\n| Digite o indice da noticia para adicionar/remover: ");
        String idx = sc.nextLine().trim();
        if (!idx.matches("\\d+")) {
            System.out.println("|--                                           --|");
            System.out.println("| Indice invalido.");
            return;
        }
        int i = Integer.parseInt(idx);
        if (i < 0 || i >= noticias.size()) {
            System.out.println("|--                                           --|");
            System.out.println("| Indice fora do intervalo.");
            return;
        }
        Noticia noticia = noticias.get(i);

        Map<String, Noticia> listaMap = null;
        if (lista.equals("favoritos")) listaMap = usuario.getFavoritos();
        else if (lista.equals("lidas")) listaMap = usuario.getLidas();
        else if (lista.equals("paraLerDepois")) listaMap = usuario.getParaLerDepois();

        System.out.println("|--                                           --|");
        if (listaMap.containsKey(noticia.getId())) {
            usuario.removerNoticia(lista, noticia.getId());
            System.out.println("| Removido da lista.");
        } else {
            usuario.adicionarNoticia(lista, noticia);
            System.out.println("| Adicionado a lista.");
        }
    }

    private void exibirListas(Scanner sc) {
        System.out.println("|         --Qual lista deseja exibir?--         |");
        System.out.println("| 1 - Lista de favoritos\n| 2 - Lista de 'ja lidas'\n| 3 - Lista 'para ler depois'");
        System.out.print("|\n| Opcao: ");
        String op = sc.nextLine().trim();
        System.out.println("|--                                           --|");
        List<Noticia> lista = new ArrayList<>();
        if (op.equals("1")) lista = usuario.getLista("favoritos");
        else if (op.equals("2")) lista = usuario.getLista("lidas");
        else if (op.equals("3")) lista = usuario.getLista("paraLerDepois");
        else {
            System.out.println("| Opção invalida.");
            return;
        }

        if (lista.isEmpty()) {
            System.out.println("| Lista vazia.");
            return;
        }
        for (Noticia n : lista) {
            System.out.println(n);
        }
    }

    private void ordenarListas(Scanner sc) {
        System.out.println("|         --Qual lista deseja ordenar?--        |");
        System.out.println("| 1 - Lista de favoritos\n| 2 - Lista de 'ja lidas'\n| 3 - Lista 'para ler depois'");
        System.out.print("|\n| Opcao: ");
        String listaOp = sc.nextLine().trim();
        System.out.println("|--                                           --|");
        System.out.println("|           --Como ordenar a lista?--           |");
        System.out.println("| 1 - Titulo\n| 2 - Data de publicação\n| 3 - Tipo ou categoria");
        System.out.print("|\n| Opcao: ");
        String ordOp = sc.nextLine().trim();
        System.out.println("|--                                           --|");

        List<Noticia> lista;
        if (listaOp.equals("1")) lista = usuario.getLista("favoritos");
        else if (listaOp.equals("2")) lista = usuario.getLista("lidas");
        else if (listaOp.equals("3")) lista = usuario.getLista("paraLerDepois");
        else {
            System.out.println("Opção invalida.");
            return;
        }

        Comparator<Noticia> comparator = (a, b) -> 0;
        if (ordOp.equals("1")) comparator = Comparator.comparing(Noticia::getTitulo, String.CASE_INSENSITIVE_ORDER);
        else if (ordOp.equals("2")) comparator = Comparator.comparing(Noticia::getDataPublicacao);
        else if (ordOp.equals("3")) comparator = Comparator.comparing(Noticia::getTipo, String.CASE_INSENSITIVE_ORDER);

        lista = lista.stream().sorted(comparator).collect(Collectors.toList());
        for (Noticia n : lista) {
            System.out.println(n);
        }
    }

}