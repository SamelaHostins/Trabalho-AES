// Autoras: Karoline, Maria Eduarda e Sâmela

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Criptografia e = new Criptografia();
        ValidarEntradas validarEntradas = new ValidarEntradas();
        Scanner scanner = new Scanner(System.in);

        // Exemplo: C:/Users/Acer/OneDrive/Documentos/AtividadeFundamentos.pdf
        String arquivo = validarEntradas.obterCaminhoArquivoValido(scanner);
        System.out.println("Caminho de arquivo a criptografar: " + arquivo);
        System.out.println("");

        // Precisa validar do destino
        String arquivoDeSaida = validarEntradas.obterNomeDoArquivoValido(scanner);
        System.out.println("Nome do arquivo de destino: " + arquivoDeSaida);
        System.out.println("");

        String chave = validarEntradas.getChaveValida(scanner);
        System.out.println("Chave: " + chave);
        System.out.println("");
        scanner.close();

    }

}