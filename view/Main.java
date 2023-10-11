package view;// Autoras: Karoline, Maria Eduarda e Sâmela

import java.io.IOException;
import java.util.Scanner;

import models.Criptografia;
import models.ValidarEntradas;

public class Main {

    public static void main(String[] args) throws IOException {
        Criptografia e = new Criptografia();
        ValidarEntradas validarEntradas = new ValidarEntradas();
        Scanner scanner = new Scanner(System.in);

        // Exemplo: C:\Users\Acer\OneDrive\Documentos\teste.txt
        // String arquivoDeEntrada = validarEntradas.obterCaminhoArquivoValido(scanner);
        // System.out.println("Caminho de arquivo a criptografar: " + arquivoDeEntrada);
        // System.out.println("");

        // Precisa validar do destino
        // String arquivoDeSaida = validarEntradas.obterNomeDoArquivoValido(scanner);
        // System.out.println("Nome do arquivo de destino: " + arquivoDeSaida);
        // System.out.println("");

        // 20,1,94,33,199,0,48,9,31,94,112,40,59,30,100,248
        String chave = validarEntradas.getChaveValida(scanner);
        System.out.println("Chave: " + chave);
        System.out.println("");

        String chaveHexadecimal = validarEntradas.transformarChaveParaHexadecimal(chave);

        String[][] matrizDaChave = validarEntradas.organizarChaveEmMatriz4x4(chaveHexadecimal);
        System.out.println("Matriz da chave:");
        
        validarEntradas.imprimirMatrizDaChave(matrizDaChave);
        System.out.println("");
        scanner.close();

        // FileManager fileManager = new FileManager();
        // File file = new File(arquivoDeEntrada);
        // if (fileManager.ehArqBinario(file)) {
        // fileManager.leArqBinario(file);
        // } else {
        // fileManager.leArq(arquivoDeSaida, chave);
        // }

        // fileManager.criaArq(arquivoDeSaida);

        // Leitura do arquivo, é para ele não jogar exceção, pois já foi verificado se
        // está certo/existe
        // byte[] arquivoEmBytes = Files.readAllBytes(Path.of(arquivoDeEntrada));

        // byte[][] matriz = e.divideEmBlocosDe16Bytes(arquivoEmBytes);
        // System.out.println("Resultado do file para bytes:");
        // e.printMatrizDeBlocos(matriz);

        // String[][] matrizEmHex = e.bytesMatrixParaHex(matriz);

        // System.out.println("Resultado do file para hexadecimal em matrizes 4x4:");
        // String[][][] matrizes4x4 = e.organizarBlocos4x4(matrizEmHex);
        // e.imprimirMatrizes4x4(matrizes4x4);

    }

}