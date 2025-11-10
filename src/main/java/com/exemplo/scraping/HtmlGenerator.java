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

            // ✅ CSS seguro sem aspas quebrando o código
            html.append("<style>\n")
                .append("body { font-family: Arial, sans-serif; background: #f5f5f5; padding: 20px; }\n")
                .append("h1 { text-align: center; color: #333; }\n")
                .append(".fonte { margin-top: 30px; font-size: 22px; color: #0084ff; }\n")
                .append("table { width: 100%; border-collapse: collapse; background: white; margin-top: 10px; }\n")
                .append("th, td { border: 1px solid #ddd; padding: 10px; }\n")
                .append("th { background: #0077ff; color: white; }\n")
                .append("tr:nth-child(even) { background: #f2f2f2; }\n")
                .append("</style>\n");

            html.append("</head>\n<body>\n");

            html.append("<h1>Resultados da Busca: ").append(termo).append("</h1>\n");

            // =========================
            // ✅ GLASSDOOR
            // =========================
            html.append("<div class='fonte'>Glassdoor</div>\n");
            html.append("<table>\n<tr><th>Título</th><th>Empresa</th><th>Local</th><th>Salário</th></tr>\n");

            for (Vaga v : glassdoor) {
                html.append("<tr>")
                    .append("<td>").append(v.getTitulo()).append("</td>")
                    .append("<td>").append(v.getEmpresa()).append("</td>")
                   
                    .append("<td>").append(v.getSalarioFormatado()).append("</td>")
                    .append("</tr>\n");
            }
            html.append("</table>\n");

            // =========================
            // ✅ BNE
            // =========================
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

            // =========================
            // ✅ INDEED
            // =========================
            html.append("<div class='fonte'>Indeed</div>\n");
            html.append("<table>\n<tr><th>Título</th><th>Empresa</th><th>Local</th><th>Salário</th></tr>\n");

            for (Vaga v : indeed) {
                html.append("<tr>")
                    .append("<td>").append(v.getTitulo()).append("</td>")
                    .append("<td>").append(v.getEmpresa()).append("</td>")
                    
                    .append("<td>").append(v.getSalarioFormatado()).append("</td>")
                    .append("</tr>\n");
            }
            html.append("</table>\n");

            html.append("</body></html>");

            writer.write(html.toString());
            System.out.println("\n✅ Arquivo HTML gerado: resultado.html");

        } catch (IOException e) {
            System.err.println("Erro ao gerar HTML: " + e.getMessage());
        }
    }
}
