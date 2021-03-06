package br.com.gabrielacoelho;

public enum TipoProduto {
    ALIMENTOS("Alimentos",1.2),
    BEBIDA("Bebida", 2.3),
    HIGIENE("Higiente",1.5);

    TipoProduto(String tipo, double markup){
        this.markup = markup;
        this.tipo = tipo;
    }
    private double markup;
    private String tipo;

    public double getMarkup() {
        return this.markup;
    }

    public String getTipo() {
        return this.tipo;
    }

    public double calcularPrecoVenda(TipoProduto tipo, Double custo){
        return tipo.markup * custo;
    }
}

