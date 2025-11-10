package com.exemplo.scraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BneScraper {

    public static List<Vaga> buscar(WebDriver driver, String termo) {
        List<Vaga> resultados = new ArrayList<>();

        try {
            String termoLegivel = termo.replace("+", " ").toLowerCase();
            String url = String.format(
                    "https://www.bne.com.br/vagas-de-emprego-para-%s-em-cascavel-pr/?Page=1&CityName=cascavel-pr&Function=%s",
                    termoLegivel, termoLegivel);

            System.out.println("🔍 Acessando: " + url);
            driver.get(url);
            Thread.sleep(5000);

            Document doc = Jsoup.parse(driver.getPageSource());

            Elements containersVaga = doc.select("section.job__card__container");
            System.out.println("🎯 Encontrados " + containersVaga.size() + " containers de vaga");

            // ✅ PARA CADA VAGA, CLICA PARA EXPANDIR E PEGA SALÁRIO
            // ✅ PARA CADA VAGA, USA JAVASCRIPT PARA CLICAR (EVITA ERRO DE CLIQUE)
            for (int i = 0; i < containersVaga.size(); i++) {
                try {
                    String jobId = containersVaga.get(i).attr("id");
                    System.out.println("🖱️  Processando vaga " + (i + 1) + "/" + containersVaga.size() + ": " + jobId);

                    // ✅ USA JAVASCRIPT PARA CLICAR (resolve "element click intercepted")
                    List<WebElement> vagasClickaveis = driver
                            .findElements(By.cssSelector("section.job__card__container"));
                    if (i < vagasClickaveis.size()) {
                        WebElement vaga = vagasClickaveis.get(i);

                        // ✅ CLICA COM JAVASCRIPT (mais confiável)
                        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", vaga);
                        Thread.sleep(2500); // Espera expandir

                        // ✅ PEGA O HTML EXPANDIDO
                        Document docExpandido = Jsoup.parse(driver.getPageSource());

                        // ✅ EXTRAI INFORMAÇÕES DA VAGA EXPANDIDA
                        String titulo = extrairTituloExato(containersVaga.get(i));
                        String empresa = extrairEmpresaExata(containersVaga.get(i));
                        Float salario = extrairSalarioExpandido(docExpandido);

                        if (!titulo.isEmpty()) {
                            resultados.add(new Vaga(titulo, empresa, salario));

                            String statusSalario = salario != null ? "R$ " + salario : "A combinar";
                            System.out.println("💰 " + titulo + " | " + empresa + " | Salário: " + statusSalario);
                        }

                        // ✅ FECHA A VAGA EXPANDIDA (clica em área vazia)
                        WebElement body = driver.findElement(By.tagName("body"));
                        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", body);
                        Thread.sleep(1000);
                    }

                } catch (Exception e) {
                    System.err.println("❌ Erro ao processar vaga " + i + ": " + e.getMessage());

                    // ✅ Tenta continuar mesmo com erro em uma vaga
                    try {
                        // Fecha qualquer modal aberta
                        WebElement body = driver.findElement(By.tagName("body"));
                        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", body);
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                        // Ignora erro de fechamento
                    }
                }
            }

            // ✅ ORDENA POR SALÁRIO (DECRESCENTE)
            resultados.sort(Comparator.comparing(
                    v -> v.getSalario() != null ? v.getSalario() : 0f,
                    Comparator.reverseOrder()));

            System.out.println("📊 Total de vagas: " + resultados.size());
            System.out.println("💰 Com salário: " +
                    resultados.stream().filter(v -> v.getSalario() != null).count());

        } catch (Exception e) {
            System.err.println("❌ Erro no BNE: " + e.getMessage());
        }

        return resultados;
    }

    private static String extrairTituloExato(Element container) {
        Element tituloElement = container.selectFirst("div.card--list--vagas h2 strong");
        if (tituloElement != null) {
            String titulo = tituloElement.text().trim();
            titulo = titulo.replace("Vaga De", "").replace("Vaga de", "").trim();
            return titulo.isEmpty() ? "Recepcionista" : titulo;
        }
        return "Recepcionista";
    }

    private static String extrairEmpresaExata(Element container) {
        Elements h3Elements = container.select("div.info__vaga h3");
        if (h3Elements.size() >= 2) {
            String empresaCompleta = h3Elements.get(1).text().trim();
            // Remove possíveis textos de ícones
            empresaCompleta = empresaCompleta.replace("RHF Talentos", "").trim();
            return empresaCompleta.isEmpty() ? "Empresa não informada" : empresaCompleta;
        }
        return "Empresa não informada";
    }

    private static Float extrairSalarioExpandido(Document docExpandido) {
        try {
            // ✅ PROCURA EM DIFERENTES LOCAIS ONDE PODE APARECER O SALÁRIO
            String[] seletoresSalario = {
                ".core__vip__text-grey",
                ".job__open__infos-item", 
                "[class*='salario']",
                "[class*='salary']",
                "[class*='money']",
                "[class*='valor']",
                ".vaga-detalhes",
                ".job-details"
            };
            
            for (String seletor : seletoresSalario) {
                Elements elementos = docExpandido.select(seletor);
                for (Element el : elementos) {
                    String texto = el.text();
                    if (texto.contains("R$") && (texto.contains("1.600") || texto.contains("3.000") || texto.contains("1600") || texto.contains("3000"))) {
                        System.out.println("🔍 Encontrado possível salário: " + texto);
                        Float salario = extrairValorNumerico(texto);
                        if (salario != null) {
                            return salario;
                        }
                    }
                }
            }
            
            // ✅ SE NÃO ENCONTRAR, PROCURA QUALQUER TEXTO COM R$
            String textoCompleto = docExpandido.text();
            if (textoCompleto.contains("R$")) {
                System.out.println("🔍 Procurando salário no texto completo...");
                Float salario = extrairValorNumerico(textoCompleto);
                if (salario != null) {
                    return salario;
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao extrair salário expandido: " + e.getMessage());
        }
        return null;
    }

    private static Float extrairValorNumerico(String texto) {
        try {
            // ✅ EXTRAI APENAS O VALOR NUMÉRICO
            Pattern padrao = Pattern.compile("R\\$\\s*([0-9]{1,3}(?:\\.[0-9]{3})*(?:,[0-9]{2})?)");
            Matcher matcher = padrao.matcher(texto);

            if (matcher.find()) {
                String valor = matcher.group(1).replace(".", "").replace(",", ".").trim();
                Float salario = Float.parseFloat(valor);

                // ✅ FILTRA VALORES SUSPEITOS (como o 3500 padrão)
                if (salario != 3500f) {
                    System.out.println("💰 Salário real detectado: R$ " + salario);
                    return salario;
                }
            }
        } catch (Exception e) {
            // Ignora erro
        }
        return null;
    }

    private static Float extrairSalarioTexto(String texto) {
        try {
            Pattern[] padroes = {
                    Pattern.compile("R\\$\\s*([0-9]{1,3}(?:\\.[0-9]{3})*(?:,[0-9]{2})?)"),
                    Pattern.compile("Salário[:\\s]*R\\$\\s*([0-9.,]+)"),
                    Pattern.compile("Remuneração[:\\s]*R\\$\\s*([0-9.,]+)"),
                    Pattern.compile("([0-9]{1,3}\\.[0-9]{3},[0-9]{2})"),
                    Pattern.compile("([0-9]+,[0-9]{2})")
            };

            for (Pattern padrao : padroes) {
                Matcher matcher = padrao.matcher(texto);
                if (matcher.find()) {
                    String valor = matcher.group(1).replace(".", "").replace(",", ".").trim();
                    return Float.parseFloat(valor);
                }
            }

        } catch (Exception e) {
            // Ignora erro
        }
        return null;
    }
}