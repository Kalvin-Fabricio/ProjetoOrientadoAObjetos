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
                System.out.println("| 1 - Buscar notícias por termo                 |");
                System.out.println("| 2 - Buscar notícias por título                |");
                System.out.println("| 3 - Buscar notícias por data                  |");
                System.out.println("| 4 - Gerenciar listas                          |");
                System.out.println("| 5 - Exibir listas                             |");
                System.out.println("| 6 - Ordenar listas                            |");
                System.out.println("| 7 - Sair                                      |");
                System.out.println("|-----------------------------------------------|");
                System.out.print("| Opcao: ");
                String opcao = sc.nextLine().trim();
                System.out.println("|--                                           --|");

                switch (opcao) {
                    case "1": buscarNoticiasPorTermo(sc); break;
                    case "2": buscarNoticiasPorTitulo(sc); break;
                    case "3": buscarNoticiasPorData(sc); break;
                    case "4": gerenciarListas(sc); break;
                    case "5": exibirListas(sc); break;
                    case "6": ordenarListas(sc); break;
                    case "7":
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

    private String escolherLista(Scanner sc, String acao) {
        System.out.println("|       --Qual lista deseja " + acao + "?--        |");
        System.out.println("| 1 - Lista de favoritos\n| 2 - Lista de 'ja lidas'\n| 3 - Lista 'para ler depois'");
        System.out.print("|\n| Opcao: ");
        String op = sc.nextLine().trim();
        switch (op) {
            case "1": return Usuario.FAVORITOS;
            case "2": return Usuario.LIDAS;
            case "3": return Usuario.PARA_LER_DEPOIS;
            default:
                System.out.println("|--                                           --|");
                System.out.println("| Opcao invalida.");
                return null;
        }
    }

    private void exibirResultadosBusca(List<Noticia> noticias, Scanner sc) {
        if (noticias.isEmpty()) {
            System.out.println("|\n| Nenhuma noticia encontrada.");
            return;
        }
        for (int i = 0; i < noticias.size(); i++) {
            System.out.println("| " + i + " - " + noticias.get(i).getTitulo() + " (ID: " + noticias.get(i).getId() + ")");
        }
        System.out.print("| \n| Ver detalhes de qual noticia? ('Enter' para voltar): ");
        String idx = sc.nextLine().trim();
        if (idx.isEmpty()) {
            System.out.println("| Retornando ao menu.");
            return;
        }
        if (idx.matches("\\d+")) {
            int i = Integer.parseInt(idx);
            if (i >= 0 && i < noticias.size()) {
                System.out.println("|--                                           --|");
                System.out.println("| Resultado da busca:                           |");
                System.out.println(noticias.get(i));
            } else {
                System.out.println("| Indice fora do intervalo.");
            }
        }
    }

    private void buscarNoticiasPorTermo(Scanner sc) {
        System.out.print("| Buscar por termo: ");
        String termo = sc.nextLine().trim();
        if (termo.isEmpty()) {
            System.out.println("| Busca cancelada.");
            return;
        }
        System.out.println("|--                                           --|");
        List<Noticia> noticias = IBGENewsAPI.buscarPorTermo(termo);
        exibirResultadosBusca(noticias, sc);
    }

    private void buscarNoticiasPorTitulo(Scanner sc) {
        System.out.print("| Buscar por título: ");
        String titulo = sc.nextLine().trim();
        if (titulo.isEmpty()) {
            System.out.println("| Busca cancelada.");
            return;
        }
        System.out.println("|--                                           --|");
        List<Noticia> noticias = IBGENewsAPI.buscarPorTitulo(titulo);
        exibirResultadosBusca(noticias, sc);
    }

    private void buscarNoticiasPorData(Scanner sc) {
        System.out.print("| Buscar por data (AAAA-MM-DD): ");
        String data = sc.nextLine().trim();
        if (data.isEmpty()) {
            System.out.println("| Busca cancelada.");
            return;
        }
        System.out.println("|--                                           --|");
        List<Noticia> noticias = IBGENewsAPI.buscarPorData(data);
        exibirResultadosBusca(noticias, sc);
    }

    private void gerenciarListas(Scanner sc) {
        String lista = escolherLista(sc, "gerenciar");
        if (lista == null) return;

        // Permite escolher como buscar a notícia para gerenciar a lista
        System.out.println("| Como deseja buscar a notícia?");
        System.out.println("| 1 - Por termo");
        System.out.println("| 2 - Por título");
        System.out.println("| 3 - Por data");
        System.out.print("| Opcao: ");
        String tipoBusca = sc.nextLine().trim();

        List<Noticia> noticias;
        switch (tipoBusca) {
            case "1":
                System.out.print("| Digite um termo: ");
                String termo = sc.nextLine().trim();
                if (termo.isEmpty()) {
                    System.out.println("| Busca cancelada.");
                    return;
                }
                noticias = IBGENewsAPI.buscarPorTermo(termo);
                break;
            case "2":
                System.out.print("| Digite o título: ");
                String titulo = sc.nextLine().trim();
                if (titulo.isEmpty()) {
                    System.out.println("| Busca cancelada.");
                    return;
                }
                noticias = IBGENewsAPI.buscarPorTitulo(titulo);
                break;
            case "3":
                System.out.print("| Digite a data (AAAA-MM-DD): ");
                String data = sc.nextLine().trim();
                if (data.isEmpty()) {
                    System.out.println("| Busca cancelada.");
                    return;
                }
                noticias = IBGENewsAPI.buscarPorData(data);
                break;
            default:
                System.out.println("| Opção inválida.");
                return;
        }

        if (noticias.isEmpty()) {
            System.out.println("| Nenhuma noticia encontrada.");
            return;
        }
        for (int i = 0; i < noticias.size(); i++) {
            System.out.println("| " + i + " - " + noticias.get(i).getTitulo() + " (ID: " + noticias.get(i).getId() + ")");
        }
        System.out.print("|\n| Digite o indice da noticia para adicionar/remover ('Enter' para voltar): ");
        String idx = sc.nextLine().trim();
        if (idx.isEmpty()) {
            System.out.println("| Retornando ao menu.");
            return;
        }
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
        if (Usuario.FAVORITOS.equals(lista)) listaMap = usuario.getFavoritos();
        else if (Usuario.LIDAS.equals(lista)) listaMap = usuario.getLidas();
        else if (Usuario.PARA_LER_DEPOIS.equals(lista)) listaMap = usuario.getParaLerDepois();

        System.out.println("|--                                           --|");
        if (listaMap.containsKey(noticia.getId())) {
            usuario.removerNoticia(lista, noticia.getId());
            System.out.println("| Removido da lista.");
        } else {
            usuario.adicionarNoticia(lista, noticia);
            System.out.println("| Adicionado a lista.");
        }
        salvarUsuarioAtual();
    }

    private void exibirListas(Scanner sc) {
        String lista = escolherLista(sc, "exibir");
        if (lista == null) return;
        System.out.println("|--                                           --|");

        List<Noticia> listaNoticias = usuario.getLista(lista);

        if (listaNoticias.isEmpty()) {
            System.out.println("| Lista vazia.");
            return;
        }
        for (Noticia n : listaNoticias) {
            System.out.println(n);
        }
    }

    private void ordenarListas(Scanner sc) {
        String lista = escolherLista(sc, "ordenar");
        if (lista == null) return;
        System.out.println("|--                                           --|");
        System.out.println("|           --Como ordenar a lista?--           |");
        System.out.println("| 1 - Titulo\n| 2 - Data de publicação\n| 3 - Tipo ou categoria");
        System.out.print("|\n| Opcao: ");
        String ordOp = sc.nextLine().trim();
        System.out.println("|--                                           --|");

        List<Noticia> listaNoticias = usuario.getLista(lista);

        Comparator<Noticia> comparator;
        switch (ordOp) {
            case "1": comparator = Comparator.comparing(Noticia::getTitulo, String.CASE_INSENSITIVE_ORDER); break;
            case "2": comparator = Comparator.comparing(Noticia::getDataPublicacao); break;
            case "3": comparator = Comparator.comparing(Noticia::getTipo, String.CASE_INSENSITIVE_ORDER); break;
            default:
                System.out.println("Opção invalida.");
                return;
        }

        listaNoticias = listaNoticias.stream().sorted(comparator).collect(Collectors.toList());
        for (Noticia n : listaNoticias) {
            System.out.println(n);
        }
    }
}