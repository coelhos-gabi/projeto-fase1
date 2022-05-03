package br.com.gabrielacoelho;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ManipulacaoData {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Entre com a data da compra (dd/mm/yyyy): ");
        String stringDate = scan.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data = LocalDate.parse(stringDate, formatter);
        System.out.printf("Saída com LocalDate: %s\n", data);

    }
}

