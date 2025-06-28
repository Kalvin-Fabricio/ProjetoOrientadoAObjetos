import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Map<String, Usuario> usuarios = Armazenamento.carregar();
        Scanner sc = new Scanner(System.in);

        System.out.println("\n|---------------------------------------------|");
        System.out.println("|--- Bem-vindo ao Blog de Notícias do IBGE ---|");
        System.out.println("|---------------------------------------------|\n");

        System.out.print("Digite seu nome ou apelido:\n");
        String nome = sc.nextLine().trim();
        String chave = Armazenamento.padronizaChave(nome);

        Usuario usuario;
        if (usuarios.containsKey(chave)) {
            usuario = usuarios.get(chave);
            System.out.println("\nBem-vindo de volta, " + usuario.getNome() + "!");
        } else {
            usuario = new Usuario(nome);
            usuarios.put(chave, usuario);
            Armazenamento.salvar(usuarios);
            System.out.println("\nNovo usuário criado! Bem-vindo, " + usuario.getNome() + "!");
        }
        Menu menu = new Menu(usuario, usuarios);
        menu.exibir();
    }
}