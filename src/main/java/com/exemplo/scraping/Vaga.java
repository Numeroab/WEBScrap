package com.exemplo.scraping;

public class Vaga {

    private String titulo;
    private String empresa;
    private Float salario; // trocar float → Float (pode ser null)

    public Vaga(String titulo, String empresa, Float salario) {
        this.titulo = titulo;
        this.empresa = empresa;
        this.salario = salario;
    }

    public String getTitulo() { return titulo; }
    public String getEmpresa() { return empresa; }
    public Float getSalario() { return salario; }

    public String getSalarioFormatado() {
        if (salario == null) {
            return "Não informado";
        } else {
            return "R$ " + salario;
        }
        
    }

    @Override
    public String toString() {
        return "🔹 " + titulo + " | " + empresa + " | " + getSalarioFormatado();
    }
}
