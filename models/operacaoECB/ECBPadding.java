package models.operacaoECB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

// Autoras: Karoline, Maria Eduarda e Sâmela
public class ECBPadding {
    
    public byte[][] divideEmBlocosDe16Bytes(byte[] arquivoEmBytes) {
        // math.ceil para arredondar para cima
        int qtdDeBlocos = (int) Math.ceil((double) arquivoEmBytes.length / 16);
        byte[][] matrizDeBlocos = new byte[qtdDeBlocos][16];

        // divide em blocos de 16 bytes
        for (int i = 0; i < qtdDeBlocos; i++) {
            int indiceInicial = i * 16;
            int indiceFinal = Math.min(indiceInicial + 16, arquivoEmBytes.length);
            matrizDeBlocos[i] = Arrays.copyOfRange(arquivoEmBytes, indiceInicial, indiceFinal);
        }

        int ultimoBloco = qtdDeBlocos - 1;
        int padding = this.calcularPreenchimentoDoBloco(matrizDeBlocos[ultimoBloco].length);
        // Quando o último bloco não precisa de preenchimento, ainda assim é
        // gerado um bloco adicional.
        if (padding == 16) {
            matrizDeBlocos = this.adicionarBlocoAdicional(matrizDeBlocos, 16);
        } else {
            matrizDeBlocos[ultimoBloco] = addPKCS7Padding(matrizDeBlocos[ultimoBloco]);
        }
        return matrizDeBlocos;
    }

    private int calcularPreenchimentoDoBloco(int tamanhoBloco) {
        int padding = 16 - (tamanhoBloco % 16);
        return padding;
    }

    private byte[][] adicionarBlocoAdicional(byte[][] matrizDeBlocos, int tamanhoBloco) {
        int novoTamanho = matrizDeBlocos.length + 1;
        byte[][] novaMatrizDeBlocos = new byte[novoTamanho][tamanhoBloco];

        for (int i = 0; i < matrizDeBlocos.length; i++) {
            novaMatrizDeBlocos[i] = matrizDeBlocos[i];
        }
        // Adiciona o bloco adicional como uma nova linha
        novaMatrizDeBlocos[novoTamanho - 1] = new byte[tamanhoBloco];
        for (int i = 0; i < tamanhoBloco; i++) {
            novaMatrizDeBlocos[novoTamanho - 1][i] = (byte) tamanhoBloco;
        }
        return novaMatrizDeBlocos;
    }

    private byte[] addPKCS7Padding(byte[] bloco) {
        int padding = this.calcularPreenchimentoDoBloco(bloco.length);
        byte[] blocoComPadding = Arrays.copyOf(bloco, bloco.length + padding);
        for (int i = bloco.length; i < blocoComPadding.length; i++) {
            blocoComPadding[i] = (byte) padding; // Preenche o novo espaço com bytes que indicam o valor do
                                                 // preenchimento
        }
        return blocoComPadding;
    }

    // para ver se deu certo a divisão em blocos de 16 bytes
    public void printMatrizDeBlocos(byte[][] matrizDeBlocos) {
        int larguraColuna = 4; // Largura fixa para cada número (3 dígitos + 1 espaço)

        for (int i = 0; i < matrizDeBlocos.length; i++) {
            for (int j = 0; j < matrizDeBlocos[i].length; j++) {
                System.out.printf("%" + larguraColuna + "d", matrizDeBlocos[i][j]);
                if ((j + 1) % 16 == 0) {
                    System.out.println();
                }
            }
            System.out.println();
        }
    }
    
    // Criptografa arquivo
    public String criptografaArquivo(String entrada, String saida, String chave) throws Exception {
    	//encryptFile("C:/Users/karoline.custodio/OneDrive - Anheuser-Busch InBev/My Documents/SegInformacao/L07 - Criptografia Blowfish.pdf", "saida.bin", chave);
    	System.out.println(entrada);	
    	System.out.println(saida);	
    	System.out.println(chave);	
    	String arqCriptografado = encryptFile(entrada, saida, chave);
        System.out.println("Arquivo criptografado com sucesso.");	
        return arqCriptografado;
    }

    // Decriptografa o arquivo criptografado
    public static void decriptografaArquivo(String entrada, String saida, String chave) throws Exception {
    	decryptFile(saida, "decriptografado.pdf", chave);
        System.out.println("Arquivo decriptografado com sucesso.");
    }
    
    public static String encryptFile(String inputFile, String outputFile, String chaveScanner) throws Exception {
    	byte[] arquivoBytes = Files.readAllBytes(Paths.get(inputFile));
        byte[] textoCriptografado = encryptECBInByte(arquivoBytes, chaveScanner);

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(textoCriptografado);
        }

        System.out.println("Tamanho do arquivo criptografado em bytes: " + new File(outputFile).length());
        return textoCriptografado.toString();
    }

    public static void decryptFile(String inputFile, String outputFile, String chave) throws Exception {
        byte[] arquivoBytes = Files.readAllBytes(Paths.get(inputFile));
        byte[] textoDecriptografado = decryptECB(arquivoBytes, chave);

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(textoDecriptografado);
        }
    }
       
    public static byte[] encryptECBInByte(byte[] plaintext, String chave) throws Exception {
    	String[] numeros = chave.split(",");
        byte[] bytesChave = new byte[numeros.length];

        for (int i = 0; i < numeros.length; i++) {
            int numero = Integer.parseInt(numeros[i]);
            bytesChave[i] = (byte) numero;
        }

        SecretKey secretKey = new SecretKeySpec(bytesChave, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plaintext);

        return encryptedBytes;
    }
    
    public static byte[] decryptECB(byte[] ciphertext, String chave) throws Exception {
    	byte[] chaveBytes = chave.getBytes();
    	
        SecretKey secretKey = new SecretKeySpec(chaveBytes, "AES");
        
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(ciphertext);
        return decryptedBytes;
    }
}