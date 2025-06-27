import java.util.*;

public class Usuario {
    private String nome;
    private Map<String, Noticia> favoritos;
    private Map<String, Noticia> lidas;
    private Map<String, Noticia> paraLerDepois;

    public Usuario(String nome) {
        this.nome = nome;
        this.favoritos = new HashMap<>();
        this.lidas = new HashMap<>();
        this.paraLerDepois = new HashMap<>();
    }

    public String getNome() { return nome; }
    public Map<String, Noticia> getFavoritos() { return favoritos; }
    public Map<String, Noticia> getLidas() { return lidas; }
    public Map<String, Noticia> getParaLerDepois() { return paraLerDepois; }

    public void adicionarNoticia(String lista, Noticia noticia) {
        switch (lista) {
            case "favoritos": favoritos.put(noticia.getId(), noticia); break;
            case "lidas": lidas.put(noticia.getId(), noticia); break;
            case "paraLerDepois": paraLerDepois.put(noticia.getId(), noticia); break;
        }
    }

    public void removerNoticia(String lista, String idNoticia) {
        switch (lista) {
            case "favoritos": favoritos.remove(idNoticia); break;
            case "lidas": lidas.remove(idNoticia); break;
            case "paraLerDepois": paraLerDepois.remove(idNoticia); break;
        }
    }

    public List<Noticia> getLista(String lista) {
        switch (lista) {
            case "favoritos": return new ArrayList<>(favoritos.values());
            case "lidas": return new ArrayList<>(lidas.values());
            case "paraLerDepois": return new ArrayList<>(paraLerDepois.values());
            default: return new ArrayList<>();
        }
    }
}