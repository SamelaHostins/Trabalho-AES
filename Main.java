// Autoras: Karoline, Maria Eduarda e Sâmela

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Criptografia e = new Criptografia();
        ValidarEntradas validarEntradas = new ValidarEntradas();
        Scanner scanner = new Scanner(System.in);

        // Exemplo: C:\Users\Acer\OneDrive\Documentos\teste.txt
        String arquivoDeEntrada = validarEntradas.obterCaminhoArquivoValido(scanner);
        System.out.println("Caminho de arquivo a criptografar: " + arquivoDeEntrada);
        System.out.println("");

        // Precisa validar do destino
        // String arquivoDeSaida = validarEntradas.obterNomeDoArquivoValido(scanner);
        // System.out.println("Nome do arquivo de destino: " + arquivoDeSaida);
        // System.out.println("");

        // // 20,1,94,33,199,0,48,9,31,94,112,40,59,30,100,248
        // String chave = validarEntradas.getChaveValida(scanner);
        // System.out.println("Chave: " + chave);
        // System.out.println("");
        // scanner.close();

        // Leitura do arquivo, é para ele não jogar exceção, pois já foi verificado se
        // está certo/existe
        byte[] arquivoEmBytes = Files.readAllBytes(Path.of(arquivoDeEntrada));

        byte[][] matriz = e.divideEmBlocosDe16Bytes(arquivoEmBytes);
        String resultado = e.lerArquivo(matriz);
        System.out.println(resultado);

    }

}