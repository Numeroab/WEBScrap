// package com.exemplo.scraping;

// import org.jsoup.Jsoup;
// import org.jsoup.nodes.Document;
// import org.jsoup.nodes.Element;
// import org.jsoup.select.Elements;
// import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class BneScraper {

    public static List<Vaga> buscar(WebDriver driver, String termo) {
        List<Vaga> resultados = new ArrayList<>();
        try {
            String url = "https://www.bne.com.br/vagas-de-emprego?q=" + termo;
            driver.get(url);
            Thread.sleep(4000);

            Document doc = Jsoup.parse(driver.getPageSource());
            Elements vagas = doc.select(".vaga");

            for (Element vaga : vagas) {
                String titulo = vaga.select(".titulo").text();
                String empresa = vaga.select(".empresa").text();
                String local = vaga.select(".local").text();
                Float salario = vaga.select(".salario").isEmpty() ? null : Float.parseFloat(vaga.select(".salario").text().replace("R$", "").replace(".", "").replace(",", ".").trim());

                if (!titulo.isEmpty())
                    resultados.add(new Vaga(titulo, empresa, local, salario));
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar no BNE: " + e.getMessage());
        }
        return resultados;
    }
}
