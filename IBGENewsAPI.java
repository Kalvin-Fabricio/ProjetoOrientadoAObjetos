import java.io.*;
import java.net.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import com.google.gson.*;

public class IBGENewsAPI {
    private static final String BASE_URL = "https://servicodados.ibge.gov.br/api/v3/noticias/";

    public static List<Noticia> buscarPorTermo(String termo) {
        return buscarNoticias(termo, null, false, null);
    }

    public static List<Noticia> buscarPorTitulo(String titulo) {
        return buscarNoticias(null, null, true, titulo);
    }

    public static List<Noticia> buscarPorData(String data) {
        List<Noticia> noticias = buscarNoticias(null, data, false, null);
        if (data != null && !data.trim().isEmpty()) {
            noticias = noticias.stream()
                .filter(n -> {
                    try {
                        String dataPub = n.getDataPublicacao().split(" ")[0];                   
                        LocalDate dataNoticia = LocalDate.parse(dataPub, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        LocalDate dataUser = LocalDate.parse(data);
                        return dataNoticia.equals(dataUser);
                    } catch (DateTimeParseException e) {
                        return false;
                    }
                })
                .toList();
        }
        return noticias;
    }

    private static List<Noticia> buscarNoticias(String termo, String data, boolean filtrarPorTitulo, String valorTitulo) {
        List<Noticia> noticias = new ArrayList<>();
        try {
            StringBuilder urlBuilder = new StringBuilder(BASE_URL);
            boolean hasParam = false;
            if ((termo != null && !termo.trim().isEmpty()) ||
                (data != null && !data.trim().isEmpty())) {
                urlBuilder.append("?");
            }
            if (termo != null && !termo.trim().isEmpty()) {
                urlBuilder.append("q=").append(URLEncoder.encode(termo, "UTF-8"));
                hasParam = true;
            }
            if (data != null && !data.trim().isEmpty()) {
                if (hasParam) urlBuilder.append("&");
                urlBuilder.append("de=").append(URLEncoder.encode(data, "UTF-8"));
            }
            String urlStr = urlBuilder.toString();

            String resposta = fazerRequisicao(urlStr);

            JsonObject respostaJson = JsonParser.parseString(resposta).getAsJsonObject();
            JsonArray itens = respostaJson.getAsJsonArray("items");
            int limite = itens.size();
            for (int i = 0; i < limite; i++) {
                JsonObject obj = itens.get(i).getAsJsonObject();
                Noticia noticia = new Noticia(
                    obj.get("id").getAsString(),
                    obj.get("titulo").getAsString(),
                    obj.get("introducao").getAsString(),
                    obj.get("data_publicacao").getAsString(),
                    obj.get("link").getAsString(),
                    obj.has("tipo") ? obj.get("tipo").getAsString() : "Desconhecido",
                    "IBGE"
                );
                noticias.add(noticia);
            }

            if (filtrarPorTitulo && valorTitulo != null && !valorTitulo.trim().isEmpty()) {
                String tituloLower = valorTitulo.toLowerCase();
                noticias = noticias.stream()
                        .filter(n -> n.getTitulo().toLowerCase().contains(tituloLower))
                        .toList();
            }

        } catch (Exception e) {
            System.out.println("|--                                           --|");
            System.out.println("| Erro ao buscar notícias: " + e.getMessage());
        }
        return noticias;
    }

    private static String fazerRequisicao(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        StringBuilder resposta = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            resposta.append(inputLine);
        }
        in.close();
        return resposta.toString();
    }
}