import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.google.gson.*;


public class Armazenamento {
    private static final String ARQUIVO = "usuarios_ibge.json";

    public static String padronizaChave(String nome) {
        return nome.trim().toLowerCase();
    }

    public static void salvar(Map<String, Usuario> usuarios) {
        if (usuarios == null) usuarios = new HashMap<>();
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(ARQUIVO), StandardCharsets.UTF_8)) {
            new Gson().toJson(usuarios, writer);
        } catch (Exception e) {
            System.out.println("|--                                           --|");
            System.out.println("| Erro ao salvar: " + e.getMessage());
        }
    }

    public static Map<String, Usuario> carregar() {
        try (Reader reader = new InputStreamReader(new FileInputStream(ARQUIVO), StandardCharsets.UTF_8)) {
            Map<String, Usuario> usuarios = new Gson().fromJson(
                reader, new com.google.gson.reflect.TypeToken<Map<String, Usuario>>() {}.getType()
            );
            if (usuarios == null) return new HashMap<>();
            return usuarios;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public static void salvarOuAtualizarUsuario(Usuario usuario) {
        Map<String, Usuario> usuarios = carregar();
        usuarios.put(padronizaChave(usuario.getNome()), usuario);
        salvar(usuarios);
    }
}