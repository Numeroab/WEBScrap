package com.exemplo.scraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class GlassdoorScraper {

    public static List<Vaga> buscar(WebDriver driver, String termo) {
        List<Vaga> resultados = new ArrayList<>();
        try {
            String url = "https://www.glassdoor.com.br/Vaga/" + termo + "-vagas-SRCH_KO0,4.htm";
            driver.get(url);
            Thread.sleep(5000); // espera carregar

            Document doc = Jsoup.parse(driver.getPageSource());
            Elements vagas = doc.select(".react-job-listing");

            for (Element vaga : vagas) {
                String titulo = vaga.select("a.jobLink").text();
                String empresa = vaga.select(".employerName").text();
                String local = vaga.select(".location").text();
                Float salario = vaga.select(".salaryText").isEmpty() ? null : Float.parseFloat(vaga.select(".salaryText").text().replaceAll("[^0-9]", ""));

                if (!titulo.isEmpty())
                    resultados.add(new Vaga(titulo, empresa, local, salario));
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar no Glassdoor: " + e.getMessage());
        }
        return resultados;
    }
}
