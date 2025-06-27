import java.io.*;
import java.net.*;
import java.util.*;
import com.google.gson.*;

public class IBGENewsAPI {
    private static final String BASE_URL = "https://servicodados.ibge.gov.br/api/v3/noticias/";

    public static List<Noticia> buscarNoticias(String termo, String data) {
        List<Noticia> noticias = new ArrayList<>();
        try {
            String urlStr = BASE_URL;
            boolean temTermo = termo != null && !termo.trim().isEmpty();
            boolean temData = data != null && !data.trim().isEmpty();
            if (temTermo || temData) urlStr += "?";
            if (temTermo) urlStr += "q=" + URLEncoder.encode(termo, "UTF-8");
            if (temTermo && temData) urlStr += "&";
            if (temData) urlStr += "de=" + URLEncoder.encode(data, "UTF-8");

            String resposta = fazerRequisicao(urlStr);

            JsonObject respostaJson = JsonParser.parseString(resposta).getAsJsonObject();
            JsonArray itens = respostaJson.getAsJsonArray("items");
            int limite = Math.min(11, itens.size());
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

            if (temTermo) {
                String termoLower = termo.toLowerCase();
                noticias = noticias.stream()
                    .filter(n -> n.getTitulo().toLowerCase().contains(termoLower)
                              || n.getIntroducao().toLowerCase().contains(termoLower))
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