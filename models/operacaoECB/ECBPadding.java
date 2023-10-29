package models.operacaoECB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import models.algoritmoAES.processoCifragem.Cifragem;
import models.entradaSaidas.ValidarEntradas;

// Autoras: Karoline, Maria Eduarda e Sâmela
public class ECBPadding extends Cifragem {

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

    public byte[] lerArquivoSemSinal(String arquivoDeEntrada) {
        try {
            byte[] arquivoEmBytes = Files.readAllBytes(Path.of(arquivoDeEntrada));
            byte[] arquivoSemSinal = new byte[arquivoEmBytes.length];

            for (int i = 0; i < arquivoEmBytes.length; i++) {
                arquivoSemSinal[i] = (byte) (arquivoEmBytes[i] & 0xFF);
            }

            return arquivoSemSinal;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Criptografa arquivo
    public void criptografaArquivo(String entrada, String saida, List<String[][]> listaDeRoundKey) throws Exception {
        encryptFile(entrada, saida, listaDeRoundKey);
        System.out.println("Arquivo criptografado com sucesso.");
    }

    public void encryptFile(String arquivoDeEntrada, String arquivoDeSaida, List<String[][]> listaDeRoundKey)
            throws Exception {

        byte[] arquivoEmBytes = lerArquivoSemSinal(arquivoDeEntrada);
        byte[][] matriz = divideEmBlocosDe16Bytes(arquivoEmBytes);
        String[][] matrizEmHex = converteMatrizParaHex(matriz);
        List<String[][]> listaDeBlocos = organizarBlocos4x4(matrizEmHex);

        List<String[][]> cifragem = cifragemAES(listaDeRoundKey, listaDeBlocos);
        try (FileOutputStream fos = new FileOutputStream(arquivoDeSaida)) {
            // Converter a cifragem em uma representação de bytes
            for (String[][] matrizToConvert : cifragem) {
                for (String[] linha : matrizToConvert) {
                    for (String elemento : linha) {
                        // Converter cada valor hexadecimal para um byte
                        byte valorByte = (byte) Integer.parseInt(elemento, 16);
                        fos.write(valorByte);
                    }
                }
            }
        }
    }

    public void imprimirListaDeMatrizes(List<String[][]> listaDeMatrizes) {
        for (String[][] matriz : listaDeMatrizes) {
            for (String[] linha : matriz) {
                for (String elemento : linha) {
                    System.out.print(elemento + " ");
                }
                System.out.println(); // Pular para a próxima linha após cada linha da matriz
            }
            System.out.println(); // Pular uma linha entre matrizes
        }
    }

    public void encryptFileTeste(String arquivoDeEntrada, String arquivoDeSaida, String chave) {
        try {
            // Lê o conteúdo do arquivo de entrada
            byte[] bytesDoArquivo = Files.readAllBytes(Paths.get(arquivoDeEntrada));

            // Divide a string da chave e converte os valores em inteiros
            String[] chaveArray = chave.split(",");
            int[] chaveInteiros = new int[chaveArray.length];
            for (int i = 0; i < chaveArray.length; i++) {
                chaveInteiros[i] = Integer.parseInt(chaveArray[i]);
            }

            // Converte os inteiros em bytes
            byte[] chaveBytes = new byte[chaveInteiros.length];
            for (int i = 0; i < chaveInteiros.length; i++) {
                chaveBytes[i] = (byte) chaveInteiros[i];
            }

            // Cria a chave AES
            SecretKey chaveAES = new SecretKeySpec(chaveBytes, "AES");

            // Cria um objeto Cipher para criptografia AES no modo ECB
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, chaveAES);

            // Cifra o conteúdo do arquivo
            byte[] dadosCifrados = cipher.doFinal(bytesDoArquivo);

            // Grava os dados cifrados em um novo arquivo
            Files.write(Paths.get(arquivoDeSaida), dadosCifrados);
            System.out.println("Arquivo cifrado gerado com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decryptFile(String arquivoCifrado, String arquivoDescriptografado, String chave) {
        try {
            // Converte a chave em bytes
            String[] chaveArray = chave.split(",");
            int[] chaveInteiros = new int[chaveArray.length];
            for (int i = 0; i < chaveArray.length; i++) {
                chaveInteiros[i] = Integer.parseInt(chaveArray[i]);
            }

            byte[] chaveBytes = new byte[chaveInteiros.length];
            for (int i = 0; i < chaveInteiros.length; i++) {
                chaveBytes[i] = (byte) chaveInteiros[i];
            }

            SecretKey chaveAES = new SecretKeySpec(chaveBytes, "AES");

            // Cria um objeto Cipher para descriptografia AES no modo ECB
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, chaveAES);

            // Lê os dados cifrados do arquivo
            byte[] dadosCifrados = Files.readAllBytes(Paths.get(arquivoCifrado));

            // Realiza a descriptografia
            byte[] dadosDescriptografados = cipher.doFinal(dadosCifrados);

            // Grava os dados descriptografados em um novo arquivo
            Files.write(Paths.get(arquivoDescriptografado), dadosDescriptografados);
            System.out.println("Arquivo descriptografado gerado com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ECBPadding e = new ECBPadding();
        ValidarEntradas validarEntradas = new ValidarEntradas();
        Scanner scanner = new Scanner(System.in);

        // C:/Users/Acer/OneDrive/Documentos/teste.txt
        String arquivoDeEntrada = validarEntradas.obterCaminhoArquivoValido(scanner);
        System.out.println("");

        byte[] arquivoEmBytes = e.lerArquivoSemSinal(arquivoDeEntrada);
        byte[][] matriz = e.divideEmBlocosDe16Bytes(arquivoEmBytes);
        String[][] matrizEmHex = e.converteMatrizParaHex(matriz);
        List<String[][]> listaDeBlocos = e.organizarBlocos4x4(matrizEmHex);
        e.imprimirListaDeMatrizes(listaDeBlocos);

    }
}