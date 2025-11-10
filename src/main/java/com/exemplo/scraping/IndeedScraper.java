import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public static List<Vaga> buscar(WebDriver driver, String termo) {
    List<Vaga> resultados = new ArrayList<>();
    try {
        String url = "https://br.indeed.com/jobs?q=" + termo + "&l=cascavel%2C+pr";
        driver.get(url);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".job_seen_beacon")));

        List<WebElement> vagas = driver.findElements(By.cssSelector(".job_seen_beacon"));

        for (WebElement vaga : vagas) {
            String titulo = vaga.findElement(By.cssSelector("h2 span")).getText();
            String empresa = vaga.findElement(By.cssSelector(".companyName")).getText();
            String local = vaga.findElement(By.cssSelector(".companyLocation")).getText();
            String salarioText = "";

            try {
                salarioText = vaga.findElement(By.cssSelector("[data-testid='attribute_snippet_testid salary-snippet-container']")).getText();
            } catch (Exception ignored) {}

            Float salario = null;
            if (!salarioText.isEmpty()) {
                salarioText = salarioText.replace("R$", "").replaceAll("[^0-9,\\-]", "");
                String[] valores = salarioText.split("-");
                if (valores.length > 0) {
                    try {
                        salario = Float.parseFloat(valores[0].replace(",", ".").trim());
                    } catch (NumberFormatException e) {
                        salario = null;
                    }
                }
            }

            resultados.add(new Vaga(titulo, empresa, salario));
        }

    } catch (Exception e) {
        System.err.println("Erro ao buscar no Indeed: " + e.getMessage());
    }
    return resultados;
}
