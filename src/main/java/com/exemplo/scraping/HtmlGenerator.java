package com.exemplo.scraping;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class HtmlGenerator {

    public static void gerarRelatorio(String termo,
            List<Vaga> glassdoor,
            List<Vaga> bne,
            List<Vaga> indeed) {

        String caminho = "resultado.html";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))) {

            StringBuilder html = new StringBuilder();

            html.append("<html>\n");
            html.append("<head>\n");
            html.append("<meta charset='UTF-8'>\n");
            html.append("<title>Resultados de Vagas - ").append(termo).append("</title>\n");

            // BNE

            html.append("<div class='fonte'>BNE (Banco Nacional de Empregos)</div>\n");
            html.append("<table>\n<tr><th>Título</th><th>Empresa</th><th>Local</th><th>Salário</th></tr>\n");

            for (Vaga v : bne) {
                html.append("<tr>")
                        .append("<td>").append(v.getTitulo()).append("</td>")
                        .append("<td>").append(v.getEmpresa()).append("</td>")

                        .append("<td>").append(v.getSalarioFormatado()).append("</td>")
                        .append("</tr>\n");
            }
            html.append("</table>\n");

        } catch (IOException e) {
            System.err.println("Erro ao gerar HTML: " + e.getMessage());
        }
    }
}
