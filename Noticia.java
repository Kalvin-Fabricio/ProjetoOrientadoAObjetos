import java.util.Objects;


public class Noticia {
    private String id;
    private String titulo;
    private String introducao;
    private String dataPublicacao;
    private String link;
    private String tipo;
    private String fonte;

    public Noticia(String id, String titulo, String introducao, String dataPublicacao, String link, String tipo, String fonte) {
        this.id = id;
        this.titulo = titulo;
        this.introducao = introducao;
        this.dataPublicacao = dataPublicacao;
        this.link = link;
        this.tipo = tipo;
        this.fonte = fonte;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getIntroducao() { return introducao; }
    public String getDataPublicacao() { return dataPublicacao; }
    public String getLink() { return link; }
    public String getTipo() { return tipo; }
    public String getFonte() { return fonte; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Noticia)) return false;
        Noticia noticia = (Noticia) o;
        return id.equals(noticia.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "|\n| Titulo: " + titulo +
               "\n| Introducao: " + introducao +
               "\n| Data de publicacao: " + dataPublicacao +
               "\n| Link: " + link +
               "\n| Tipo: " + tipo +
               "\n| Fonte: " + fonte;
    }
}