package br.com.gabrielacoelho;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Supermercado {
    private static Object[][] produtos = new Object[1][9];
    // PRODUTOS:
    // TIPO - MARCA - IDENTIFICADOR - NOME - PRECOCUSTO - QUANTIDADE - DATA COMPRA - PRECO VENDA - ESTOQUE
    private static Object[][] vendas = new Object[1][7];
    // VENDAS:
    // CPF - CODIGO - NOME - QUANTIDADE - PRECO VENDA - VALOR A PAGAR - TIPO CLIENTE
    private static int quantidadeLinhasProdutos = 0;
    private static int quantidadeLinhasVendas = 0;
    private static int quantidadeLinhasCliente = 0;
    public static void main(String[] args) {

        Scanner ler = new Scanner(System.in);
        String option;
        System.out.println("###### SUPERMERCADO ######");
        System.out.println("Bem-vindo!");

        do {
            System.out.println("O que deseja fazer?");
            System.out.println("1 - Cadastrar/Comprar produtos");
            System.out.println("2 - Imprimir estoque");
            System.out.println("3 - Listar os produto pelo Tipo");
            System.out.println("4 - Pesquisar um produto pelo identificador");
            System.out.println("5 - Pesquisar um produto pelo nome");
            System.out.println("6 - Realizar uma venda");
            System.out.println("7 - Imprimir relatorio de vendas analitico, todas as vendas");
            System.out.println("8 - Imprimir relatorio de vendas sintetico, consolidado por CPF");
            System.out.println("0 - Sair");
            option= ler.nextLine();

            switch (option) {
                case"0":
                    System.out.println("Saindo...");
                    System.exit(1);
                case "1":
                    if (quantidadeLinhasProdutos == produtos.length) {
                        produtos = redimensionarProdutos(produtos);
                    }
                    programa(produtos, ler);
                    break;
                case "2":
                    imprimirEstoque(produtos);
                    break;
                case "3":
                    listarProdutosTipo(produtos,ler);
                    break;
                case "4":
                    pesquisarIdentificadorProduto(produtos, ler);
                    break;
                case "5":
                    pesquisarNomeProduto(produtos, ler);
                    break;
                case "6":
                    realizarVenda(produtos, vendas, ler);
                    break;
                case"7":
                    //RELATORIO DE VENDAS ANALITICO, TODAS AS VENDAS
                    //CPF   | TIPO CLIENTE | QUANTIDADE PRODUTOS  | VALOR PAGO
                    imprimirRelatorioVendas(vendas);
                    break;
                case"8":
                    //RELATORIO DE VENDAS SINTETICO, CONSOLIDADO POR CPF
                    //break;
                default:
                    System.out.println("Opção inválida!");
            }
        }while(!(option.equals("0")));
    }
    public static void programa(Object[][] produtos, Scanner ler){
        //imprimirCadastro(produtos);
        System.out.print("Insira a marca do produto: ");
        String marca = ler.nextLine();
        System.out.print("Insira o identificador do produto: ");
        String identificador = ler.nextLine();

        int linha = estaCadastrado(produtos,identificador);

        if(linha < 0){
            String metodo = "produtos";
            int linhaLivre = encontrarPosicaoLivre(produtos, metodo);

            produtos[linhaLivre][1] = marca;
            produtos[linhaLivre][2] = identificador;

            cadastrarTipoProduto(produtos, ler, linhaLivre);
            cadastrarNomeProduto(produtos, ler, linhaLivre);
            cadastrarPrecoCusto(produtos, ler, linhaLivre);
            cadastrarQuantidadeProduto(produtos, ler, linhaLivre);
            cadastrarDataCompra(produtos, linhaLivre);
            cadastrarPrecoVenda(produtos, linhaLivre);

            calcularEstoque(produtos, linhaLivre);
            quantidadeLinhasProdutos++;
        } else{
            System.out.println("ATUALIZANDO ESTOQUE DO PRODUTO");
            cadastrarPrecoCusto(produtos, ler, linha);
            cadastrarQuantidadeProduto(produtos, ler, linha);
            cadastrarDataCompra(produtos, linha);
            cadastrarPrecoVenda(produtos, linha);
            calcularEstoque(produtos, linha);
        }

        System.out.println();
    }

    public static int estaCadastrado(Object[][] produtos, String identificador){
        for (int i = 0; i < produtos.length; i++) {
            Object object = produtos[i][2];
            if (object instanceof String) {
                String identificadorRegistrado = (String) object;
                if (identificadorRegistrado != null) {
                    if (identificadorRegistrado.equals(identificador)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public static int encontrarPosicaoLivre(Object[][] matriz, String metodo){
        if(metodo.equals("produtos")) {
            for (int i = 0; i < matriz.length; i++) {
                String identificador = (String) matriz[i][2];
                if (identificador == null) {
                    return i;
                }
            }
        }
        if(metodo.equals("vendas")){
            for (int i = 0; i < matriz.length; i++) {
                String codigo = (String) matriz[i][2];
                if (codigo == null) {
                    return i;
                }
            }
        }
        if(metodo.equals("vendasCliente")){
            for (int i = 0; i < matriz.length; i++) {
                String codigo = (String) matriz[i][0];
                if (codigo == null) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static void cadastrarTipoProduto(Object[][]produtos, Scanner ler, int linha){
        System.out.println("Insira a opção do tipo:");
        for (TipoProduto value : TipoProduto.values()) {
            System.out.println(value.ordinal() + " - " + value.getTipo());
        }
        String option = ler.nextLine();
        TipoProduto tipoProduto;
        switch (option) {
            case "0":
                tipoProduto = TipoProduto.ALIMENTOS;
                produtos[linha][0] = tipoProduto;
                break;
            case "1":
                tipoProduto = TipoProduto.BEBIDA;
                produtos[linha][0] = tipoProduto;
                break;
            case "2":
                tipoProduto = TipoProduto.HIGIENE;
                produtos[linha][0] = tipoProduto;
                break;
            case "9":
                break;
            default:
                System.out.println("Opção inválida!");
                cadastrarTipoProduto(produtos, ler, linha);
                break;
        }
    }

    public static void cadastrarNomeProduto(Object[][] produtos, Scanner ler, int linha){
        System.out.print("Insira o nome: ");
        String nome = ler.nextLine().toUpperCase();
        produtos[linha][3] = nome;
    }

    public static void cadastrarPrecoCusto(Object[][]produtos, Scanner ler, int linha){

        try {
            double precoCusto;
            do {
                System.out.print("Insira o preço de custo: ");
                String custo = ler.nextLine();
                precoCusto = Double.parseDouble(custo.replace(',', '.'));
                if (precoCusto <= 0) {
                    System.out.println("O preço de custo deve ser maior que 0.");
                }
            } while (precoCusto <= 0);

            produtos[linha][4] = precoCusto;
        }catch(Exception exception) {
            if(exception instanceof InputMismatchException) { //nextDouble
                System.out.println("ERRO DE PREÇO");
                ler.nextLine();
                cadastrarPrecoCusto(produtos, ler, linha);
            }
            if(exception instanceof NullPointerException){
                System.out.println("Insira o preço novamente");
                ler.nextLine();
                cadastrarPrecoCusto(produtos,ler,linha);
            }
            if (exception instanceof NumberFormatException) {
                System.out.println("Não deve conter letras! Tente novamente" + "");
                cadastrarPrecoCusto(produtos, ler, linha);
            }
        }
    }

    public static void cadastrarQuantidadeProduto(Object[][]produtos, Scanner ler, int linha){

        try{
            int quantidade;
            do {
                System.out.print("Insira a quantidade: ");
                quantidade  = ler.nextInt();
                if (quantidade < 0) {
                    System.out.println("A quantidade não pode ser negativa.");
                }
            } while (quantidade < 0);
            produtos[linha][5] = quantidade;
            ler.nextLine();
        }catch(Exception exception){
            if(exception instanceof InputMismatchException){
                System.out.println("Erro de inserção! Tente novamente, utilize somente números");
                ler.nextLine();
                cadastrarQuantidadeProduto(produtos,ler, linha);
            }
            if(exception instanceof NullPointerException){
                System.out.println("Algo deu errado!");
                System.out.println("Insira a quantidade novamente:");
                ler.nextLine();
                cadastrarQuantidadeProduto(produtos, ler, linha);
            }
        }
    }

    public static void cadastrarDataCompra(Object[][]produtos, int linha){
        /*ler.nextLine();
        System.out.print("Insira a data da compra dd/MM/yyyy: ");
        String stringDataCompra = ler.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataCompra = LocalDate.parse(stringDataCompra, formatter);*/
        LocalDateTime dataCompra = LocalDateTime.now();
        produtos[linha][6] = dataCompra;
    }

    public static void cadastrarPrecoVenda(Object[][]produtos, int linha) {

        //ADICIONAR VERIFICAÇÕES DE ERRO

        TipoProduto tipo = (TipoProduto) produtos[linha][0];
        double precoVenda = tipo.calcularPrecoVenda((Double) produtos[linha][4]);
        produtos[linha][7] = precoVenda;
    }

    public static void calcularEstoque(Object[][]produtos, int linha){

        //ADICIONAR VERIFICAÇÕES DE ERRO

        int quantidade = (Integer) produtos[linha][5];
        if(produtos[linha][8] == null){
            produtos[linha][8] = quantidade;
        } else {
            int estoque = (Integer) produtos[linha][8];
            estoque += quantidade;
            produtos[linha][8] = estoque;
        }
    }

    /*public static void imprimirCadastro(Object[][]produtos){
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
    }*/

    public static void imprimirEstoque(Object[][]produtos){
        String action = "estoque";
        imprimirCabecalho(action);
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
        System.out.println();
    }

    public static void listarProdutosTipo(Object[][]produtos, Scanner ler){
        System.out.println("Insira a opção do tipo a ser listado:");
        for (TipoProduto value : TipoProduto.values()) {
            System.out.println(value.ordinal() + " - " + value.getTipo());
        }
        System.out.println("9 - VOLTAR AO MENU INICIAL");
        String option = ler.nextLine();
        TipoProduto tipoProduto;
        switch (option) {
            case "0":
                tipoProduto = TipoProduto.ALIMENTOS;
                imprimirProdutosTipo(produtos, tipoProduto);
                break;
            case "1":
                tipoProduto = TipoProduto.BEBIDA;
                imprimirProdutosTipo(produtos, tipoProduto);
                break;
            case "2":
                tipoProduto = TipoProduto.HIGIENE;
                imprimirProdutosTipo(produtos, tipoProduto);
                break;
            case "9":
                break;
            default:
                System.out.println("Opção inválida!");
        }
        System.out.println();
    }

    public static void imprimirProdutosTipo(Object[][] produtos, TipoProduto tipoProduto){
        String action = "produtos";
        imprimirCabecalho(action);
        int contador = 0;

        for (int i = 0; i < produtos.length; i++) {
            TipoProduto tipoCadastrado = (TipoProduto) produtos[i][0];
            if (tipoCadastrado == tipoProduto) {
                imprimirDado(produtos, i);
            } else{
                contador++;
            }
        }
        if(contador == 0 && produtos.length > 1) {
            System.out.println("Não há produtos cadastrados para esse tipo");
        }
    }
    public static void imprimirCabecalho(String action){
        if(action.equals("produtos")) {
            System.out.println("Tipo - Marca - Identificador - Nome - Preço de Custo" +
                    " - Quantidade - Data Compra - Preço de Venda - Estoque");
        }
        if(action.equals("estoque")){
            System.out.println("---------------------------------------");
            System.out.println("               ESTOQUE                 ");
            System.out.println("---------------------------------------");
            System.out.println("Marca - Identificador - Nome - Estoque");
        }
        if(action.equals("notafiscal")){
            System.out.println("----------------------------------------------------------------");
            System.out.println("                          NOTA FISCAL                           ");
            System.out.println("----------------------------------------------------------------");
            System.out.println("Identificador - Nome - Quantidade - Preco Unitário - Valor a Pagar");
        }
        if(action.equals("vendas")){
            System.out.println("-----------------------------------------------------------------");
            System.out.println("                    RELATÓRIO DE VENDAS                          ");
            System.out.println("-----------------------------------------------------------------");
            System.out.println("CPF - Tipo Cliente - Quantidade - Valor Pago");
        }

    }
    public static Object[][] redimensionarProdutos(Object[][]produtos){
        Object[][] novaTabela = new Object[produtos.length * 2][9];
        for (int i = 0; i < produtos.length; i++) {
            for (int j = 0; j < produtos[i].length; j++) {
                novaTabela[i][j] = produtos[i][j];
            }
        }
        return novaTabela;
    }

    public static void pesquisarIdentificadorProduto(Object[][]produtos, Scanner ler){
        System.out.println("Insira o identificador do produto:");
        String identificador = ler.nextLine();
        String action = "produtos";
        int i = estaCadastrado(produtos, identificador);
        if (i < 0){
            System.out.println("Código identificador não encontrado");
            System.out.println();
        }else{
            imprimirCabecalho(action);
            imprimirDado(produtos, i);
            }
    }

    public static void imprimirDado(Object[][]produtos, int i){
        TipoProduto tipoCadastrado = (TipoProduto) produtos[i][0];
        String marca = (String) produtos[i][1];
        String identificador = (String) produtos[i][2];
        String nome = (String) produtos[i][3];
        double precoCusto = (Double) produtos[i][4];
        int quantidade = (Integer) produtos[i][5];
        LocalDateTime dataCompra = (LocalDateTime) produtos[i][6];
        String data = dataCompra.format(DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm"));
        double precoVenda = (Double) produtos[i][7];
        int estoque = (Integer) produtos[i][8];
        System.out.printf("%s - %s - %s - %s - %.2f - %d - %s - %.2f - %d %n",tipoCadastrado.getTipo(), marca,
                identificador, nome, precoCusto, quantidade, data, precoVenda, estoque);
    }
    private static void pesquisarNomeProduto(Object[][] produtos, Scanner ler) {
        String action = "produtos";
        int contador = 0;
        System.out.println("Insira o nome do produto que deseja encontrar:");
        String nome = ler.nextLine();
        imprimirCabecalho(action);
        for (int i = 0; i < produtos.length; i++) {
            String nomeCadastrado = (String) produtos[i][3];
            if(nomeCadastrado != null) {
                if (nomeCadastrado.toUpperCase().contains(nome.toUpperCase())) {
                    imprimirDado(produtos, i);
                    contador++;
                }
            }
        }
        if(contador == 0){
            System.out.println("NOME NÃO ENCONTRADO");
        }
        System.out.println();
    }
    public static void realizarVenda(Object[][] produtos, Object[][] vendas, Scanner ler){
        Object [][] vendasCliente = new Object[1][5];

        TipoClientes tipoClientes = null;

        int linhaLivre = encontrarPosicaoLivre(vendas,"vendas");

        String cpf = cadastrarCpf(vendas, ler, linhaLivre);

        if(!(cpf.equals("00000000191"))){
            tipoClientes = registrarTipoCliente(vendas, ler, linhaLivre);
        }

        String identificador = "";
        do {
            if (quantidadeLinhasVendas == vendas.length) {
                vendas = redimensionarVendas(vendas);
            }
            if(quantidadeLinhasCliente == vendasCliente.length){
                vendasCliente = redimensionarVendasCliente(vendasCliente);
            }

            int i = 0;
            do{
                System.out.println("--- Para concluir a venda digite FIM ---");
                System.out.print("Insira o identificador do produto: ");
                identificador = ler.nextLine();

                if (identificador.equalsIgnoreCase("fim")) {
                    break;
                } else {
                    i = estaCadastrado(produtos, identificador);
                    if (i < 0) {
                        System.out.println("Produto não encontrado, insira novamente.");
                    }else {
                        registrarVenda(produtos, vendas, ler, identificador, vendasCliente, cpf);
                        ler.nextLine();
                    }
                }

            } while ((i < 0));

        } while (!identificador.equalsIgnoreCase("fim"));

        double valor = imprimirNotaFiscal(vendasCliente);
        System.out.println();
        double totalCompra;
        if(tipoClientes != null) {
            totalCompra = tipoClientes.getDesconto() * valor;
        }else{
            totalCompra = valor;
        }
        System.out.println("--------------------------------------------------------------");
        System.out.printf("VALOR TOTAL DA COMPRA:   R$%.2f %n", totalCompra);
        System.out.println();
    }

    public static String cadastrarCpf(Object[][]vendas, Scanner ler, int linha){
        String cpf;
        System.out.println("Quer adicionar CPF/CNPJ?");
        System.out.println("0 - Não | 1 - Sim");
        String resposta = ler.nextLine();
        if (resposta.equals("0")){
            cpf = "00000000191";
            TipoClientes tipoCliente = TipoClientes.FISICA;
            vendas[linha][6] = tipoCliente;
        }else {
            System.out.println("Insira o documento:");
            cpf = ler.nextLine();
        }
        vendas[linha][0] = cpf;
        return cpf;
    }

    public static TipoClientes registrarTipoCliente(Object[][]vendas, Scanner ler, int linha){
        System.out.println("Insira o tipo do cliente");
        for (TipoClientes value : TipoClientes.values()) {
            System.out.println(value.ordinal() + "-" +value.getTipo());
        }
        TipoClientes tipoCliente;
        String cliente = ler.nextLine();
        switch(cliente){
            case"0":
                tipoCliente = TipoClientes.FISICA;
                vendas[linha][6] = tipoCliente;
                break;
            case"1":
                tipoCliente = TipoClientes.JURIDICA;
                vendas[linha][6] = tipoCliente;
                break;
            case"2":
                tipoCliente = TipoClientes.VIP;
                vendas[linha][6] = tipoCliente;
                break;
            default:
                System.out.println("Opção inválida");
        }
        return (TipoClientes) vendas[linha][6];
    }
    public static void registrarVenda(Object[][]produtos,Object[][]vendas, Scanner ler, String identificador,
                                      Object[][]vendasCliente, String cpf){

            System.out.print("Insira a quantidade: ");
            int quantidade = ler.nextInt();
            int linhaClientes = encontrarPosicaoLivre(vendasCliente, "vendasCliente");
            int linhaLivreVendas = encontrarPosicaoLivre(vendas, "vendas");
            for (int i = 0; i < produtos.length; i++) {
                String identificadorCadastrado = (String) produtos[i][2];
                if (identificadorCadastrado.equalsIgnoreCase(identificador)) {
                    if (quantidade > (Integer) produtos[i][8]) {
                        System.out.println("Estoque insuficiente: " + produtos[i][8]);
                    } else {
                        vendas[linhaLivreVendas][0] = cpf;
                        vendas[linhaLivreVendas][1] = identificador;
                        String nome = (String) produtos[i][3];
                        vendas[linhaLivreVendas][2] = nome;
                        vendas[linhaLivreVendas][3] = quantidade;
                        double precoVenda = (Double) produtos[i][7];
                        vendas[linhaLivreVendas][4] = precoVenda;
                        vendas[linhaLivreVendas][5] = (Double) vendas[linhaLivreVendas][4] * quantidade;
                        int estoque = (Integer) produtos[i][8];
                        estoque -= quantidade;
                        produtos[i][8] = estoque;
                        vendasCliente[linhaClientes][0] = identificador;
                        vendasCliente[linhaClientes][1] = nome;
                        vendasCliente[linhaClientes][2] = quantidade;
                        vendasCliente[linhaClientes][3] = precoVenda;
                        vendasCliente[linhaClientes][4] = (Integer) vendasCliente[linhaClientes][2] * (Double) vendasCliente[linhaClientes][3];
                        linhaClientes++;
                        quantidadeLinhasVendas++;
                        quantidadeLinhasCliente++;
                        return;
                    }
                }
            }
    }
    public static Object[][] redimensionarVendas(Object[][]vendas){
        Object[][] novaTabela = new Object[vendas.length * 2][7];
        for (int i = 0; i < vendas.length; i++) {
            for (int j = 0; j < vendas[i].length; j++) {
                novaTabela[i][j] = vendas[i][j];
            }
        }
        return novaTabela;
    }
    public static Object[][] redimensionarVendasCliente(Object[][]vendasCliente){
        Object[][] novaTabela = new Object[vendasCliente.length + 1][7];
        for (int i = 0; i < vendasCliente.length; i++) {
            for (int j = 0; j < vendasCliente[i].length; j++) {
                novaTabela[i][j] = vendasCliente[i][j];
            }
        }
        return novaTabela;
    }

    public static double imprimirNotaFiscal(Object[][]vendasCliente){
        String action = "notafiscal";
        double valorPagar = 0;
        imprimirCabecalho(action);
        String identificador;
        for (int i = 0; i < vendasCliente.length; i++) {
            identificador = (String) vendasCliente[i][0];
            if(identificador != null) {
                String nome = (String) vendasCliente[i][1];
                int quantidade = (Integer) vendasCliente[i][2];
                double precoVenda = (Double) vendasCliente[i][3];
                double valorProduto = (Double) vendasCliente[i][4];
                valorPagar += valorProduto;
                System.out.printf("%s - %s - %d - %.2f - %.2f %n", identificador, nome, quantidade, precoVenda, valorProduto);
            }else{
                return valorPagar;
            }
        }
        return valorPagar;
    }

    public static void imprimirRelatorioVendas(Object[][]vendas){
        String action = "vendas";
        imprimirCabecalho(action);
        for (int i = 0; i < vendas.length; i++) {
            String cpf = (String) vendas[i][0];
            TipoClientes tipoCliente = (TipoClientes) vendas[i][6];
            int quantidade = (Integer) vendas[i][3];
            double valorPago = (Double) vendas[i][5] * quantidade;
            System.out.printf("%s - %s - %d - %.2f %n", cpf, tipoCliente.getTipo(), quantidade, valorPago);
        }
        System.out.println();
        System.out.println();
    }
}





















