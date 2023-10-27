package view;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import models.algoritmoAES.expansaoDeChave.ExpansaoDeChave;
import models.entradaSaidas.FileManager;
import models.entradaSaidas.ValidarEntradas;
import models.operacaoECB.ECBPadding;

// Autoras: Karoline, Maria Eduarda e Sâmela
public class Main {

    public static void main(String[] args) throws IOException {
        ECBPadding e = new ECBPadding();
        ValidarEntradas validarEntradas = new ValidarEntradas();
        Scanner scanner = new Scanner(System.in);
        ExpansaoDeChave ex = new ExpansaoDeChave();

        // Exemplo: C:\Users\Acer\OneDrive\Documentos\teste.txt      
        System.out.println("Caminho de arquivo a criptografar: " );
        String arquivoDeEntrada = scanner.nextLine();
        System.out.println("");
        
        // 20,1,94,33,199,0,48,9,31,94,112,40,59,30,100,248
        String chave = validarEntradas.getChaveValida(scanner);
      //  System.out.println("Chave: " + chave);
        System.out.println("");
        

        // Nome do arquivo que sera encriptografado
        System.out.println("Nome do arquivo de saida: " );
        String arquivoDeSaida = scanner.nextLine();
        System.out.println("");
      
        
        // String chaveHexadecimal = ex.transformarChaveParaHexadecimal(chave);

        // String[][] matrizDaChave = ex.organizarChaveEmMatriz4x4(chaveHexadecimal);
        // System.out.println("Matriz da chave:");

        // ex.imprimirMatrizDaChave(matrizDaChave);
        // System.out.println("");
        // scanner.close();

         FileManager fileManager = new FileManager();
         File file = new File(arquivoDeEntrada);
         if (fileManager.ehArqBinario(file)) {
        	 fileManager.leArqBinario(file);
         } else {
        	 fileManager.leArq(arquivoDeSaida, chave);
         }

        // fileManager.criaArq(arquivoDeSaida);
        try {
        	 e.criptografaArquivo(arquivoDeEntrada,arquivoDeSaida, chave);
         } catch (Exception e1) {
        	    // Lide com a exceção aqui
        	    e1.printStackTrace(); // ou qualquer tratamento de erro específico que você desejar
        }
        
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