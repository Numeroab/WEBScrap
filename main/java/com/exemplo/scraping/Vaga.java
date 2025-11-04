package main.java.com.exemplo.scraping;

public class Vaga {
    private String titulo;
    private String empresa;
    private String local;
    private float salario;

    public Vaga(String titulo, String empresa, String local, float salario) {
        this.titulo = titulo;
        this.empresa = empresa;
        this.local = local;
        this.salario = salario;
    }

    @Override
    public String toString() {
        return "🔹 " + titulo + " | " + empresa + " | " + local + " | " + salario;
    }
}
