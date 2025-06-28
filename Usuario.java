import java.util.*;

public class Usuario {
    public static final String FAVORITOS = "favoritos";
    public static final String LIDAS = "lidas";
    public static final String PARA_LER_DEPOIS = "paraLerDepois";

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
            case FAVORITOS: favoritos.put(noticia.getId(), noticia); break;
            case LIDAS: lidas.put(noticia.getId(), noticia); break;
            case PARA_LER_DEPOIS: paraLerDepois.put(noticia.getId(), noticia); break;
            default: 
                throw new IllegalArgumentException("Lista inválida: " + lista);
        }
    }

    public void removerNoticia(String lista, String idNoticia) {
        switch (lista) {
            case FAVORITOS: favoritos.remove(idNoticia); break;
            case LIDAS: lidas.remove(idNoticia); break;
            case PARA_LER_DEPOIS: paraLerDepois.remove(idNoticia); break;
            default:
                throw new IllegalArgumentException("Lista inválida: " + lista);
        }
    }

    public List<Noticia> getLista(String lista) {
        switch (lista) {
            case FAVORITOS: return new ArrayList<>(favoritos.values());
            case LIDAS: return new ArrayList<>(lidas.values());
            case PARA_LER_DEPOIS: return new ArrayList<>(paraLerDepois.values());
            default: return new ArrayList<>();
        }
    }
}