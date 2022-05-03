package br.com.gabrielacoelho;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Supermercado {
    private static Object[][] produtos = new Object[2][9];

    public static void main(String[] args) {

        Scanner ler = new Scanner(System.in);
        String option;
        System.out.println("###### SUPERMERCADO ######");
        System.out.println("Bem-vindo!");
        int quantidadeLinhas = 0;

        do {
            System.out.println("O que deseja fazer?");
            System.out.println("1 - Cadastrar/Comprar produtos");
            System.out.println("2 - Imprimir estoque");
            System.out.println("3 - Listar os produto pelo Tipo");
            System.out.println("0 - Sair");
            option= ler.nextLine();

            switch (option) {
                case"0":
                    System.out.println("Saindo...");
                    System.exit(1);
                case "1":
                    if (quantidadeLinhas == produtos.length) {
                        redimensionar();
                    }
                    programa(produtos, ler);
                    quantidadeLinhas++;
                    break;
                case "2":
                    imprimirEstoque(produtos);
                    break;
                case "3":
                    //listarProdutosTipo();
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }while(!(option.equals("0")));
    }

    public static void programa(Object[][] produtos, Scanner ler){
        imprimirCadastro(produtos);
        System.out.print("Insira a marca do produto: ");
        String marca = ler.nextLine();
        System.out.print("Insira o identificador do produto: ");
        String identificador = ler.nextLine();

        int linha = estaCadastrado(produtos, marca, identificador);

        if(linha < 0){
            int linhaLivre = encontrarPosicaoLivre(produtos);

            produtos[linhaLivre][1] = marca;
            produtos[linhaLivre][2] = identificador;

            cadastrarTipoProduto(produtos, ler, linhaLivre);
            cadastrarNomeProduto(produtos, ler, linhaLivre);
            cadastrarPrecoCusto(produtos, ler, linhaLivre);
            cadastrarQuantidadeProduto(produtos, ler, linhaLivre);
            cadastrarDataCompra(produtos, ler, linhaLivre);
            cadastrarPrecoVenda(produtos, linhaLivre);

            calcularEstoque(produtos, linhaLivre);

        } else{
            cadastrarPrecoCusto(produtos, ler, linha);
            cadastrarQuantidadeProduto(produtos, ler, linha);
            cadastrarDataCompra(produtos, ler, linha);
            cadastrarPrecoVenda(produtos, linha);
            calcularEstoque(produtos, linha);
        }

        System.out.println();
    }

    public static int estaCadastrado(Object[][] produtos, String marca, String identificador){
        for (int i = 0; i < produtos.length; i++) {
            Object object = produtos[i][1];
            Object object2 = produtos[i][2];
            if (object instanceof String && object2 instanceof String) {
                String marcaRegistrada = (String) object;
                String identificadorRegistrado = (String) object2;
                if (marcaRegistrada != null && identificadorRegistrado != null) {
                    if (marcaRegistrada.equals(marca) && identificadorRegistrado.equals(identificador)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public static int encontrarPosicaoLivre(Object[][] produtos){
        for (int i = 0; i < produtos.length; i++) {
           String marca = (String) produtos[i][1];
           String identificador = (String) produtos[i][2];
           if(marca == null && identificador == null){
               return i;
           }
        }
        return -1;
    }

    public static void cadastrarTipoProduto(Object[][]produtos, Scanner ler, int linha){
        System.out.println("Insira o tipo do produto:");
        System.out.println("ALIMENTOS - BEBIDA - HIGIENE");
        String tipoProduto = ler.nextLine().toUpperCase();
        TipoProduto tipo = TipoProduto.valueOf(tipoProduto);
        produtos[linha][0] = tipo;
    }

    public static void cadastrarNomeProduto(Object[][] produtos, Scanner ler, int linha){
        System.out.print("Insira o nome: ");
        String nome = ler.nextLine();
        produtos[linha][3] = nome;
    }

    public static void cadastrarPrecoCusto(Object[][]produtos, Scanner ler, int linha){
        System.out.print("Insira o preço de custo: ");
        Double custo = ler.nextDouble();
        produtos[linha][4] = custo;
    }

    public static void cadastrarQuantidadeProduto(Object[][]produtos, Scanner ler, int linha){
        System.out.print("Insira a quantidade: ");
        int quantidade = ler.nextInt();
        produtos[linha][5] = quantidade;
    }

    public static void cadastrarDataCompra(Object[][]produtos, Scanner ler, int linha){
        ler.nextLine();
        System.out.print("Insira a data da compra dd/MM/yyyy: ");
        String stringDataCompra = ler.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataCompra = LocalDate.parse(stringDataCompra, formatter);
        produtos[linha][6] = dataCompra;
    }

    public static void cadastrarPrecoVenda(Object[][]produtos, int linha) {
        TipoProduto tipo = (TipoProduto) produtos[linha][0];
        double precoVenda = tipo.calcularPrecoVenda(tipo, (double) produtos[linha][4]);
        produtos[linha][7] = precoVenda;
    }

    public static void calcularEstoque(Object[][]produtos, int linha){
        int quantidade = (Integer) produtos[linha][5];
        if(produtos[linha][8] == null){
            produtos[linha][8] = quantidade;
        } else {
            int estoque = (Integer) produtos[linha][8];
            estoque += quantidade;
            produtos[linha][8] = estoque;
        }
    }

    public static void imprimirCadastro(Object[][]produtos){
        System.out.println();
        System.out.println("#################################################");
        System.out.println("------------ PRODUTOS CADASTRADOS ------------");
        System.out.println("#################################################");
        System.out.println();
        System.out.println("Tipo - Marca - Identificador - Nome - Preço de Custo" +
                " - Quantidade - Data Compra - Preço de Venda - Estoque");
        for (Object[]array : produtos) {
            for(Object elemento : array) {
                System.out.print(elemento + " - ");
            }
            System.out.println();
        }
        System.out.println("---------------------------------------");
    }

    public static void imprimirEstoque(Object[][]produtos){
        System.out.println("---------------------------------------");
        System.out.println("               ESTOQUE                 ");
        System.out.println("---------------------------------------");
        System.out.println("Marca - Identificador - Nome - Estoque");
        for (int i = 0; i < produtos.length; i++) {
            String marca = (String) produtos[i][1];
            if(marca == null){
                break;
            }
            String identificador = (String) produtos[i][2];
            String nome = (String) produtos[i][3];
            int estoque = (Integer) produtos[i][8];
            System.out.println(marca + " - " + identificador + " - " + nome + " - " + estoque);
        }
        System.out.println("---------------------------------------");

    }

    public static void redimensionar(){
        Object[][] novaTabela = new Object [produtos.length * 2][9];
        for (int i = 0; i < produtos.length; i++) {
            for (int j = 0; j < produtos[i].length; j++) {
                novaTabela[i][j] = produtos[i][j];
            }
        }
        produtos =  novaTabela;
    }
}





















