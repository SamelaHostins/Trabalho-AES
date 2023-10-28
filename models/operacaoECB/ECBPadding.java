package models.operacaoECB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import models.algoritmoAES.processoCifragem.Cifragem;
import models.entradaSaidas.ValidarEntradas;

// Autoras: Karoline, Maria Eduarda e Sâmela
public class ECBPadding extends Cifragem {

    // fazer em int
    public int[][] divideEmBlocosDe16Bytes(int[] arquivoEmInt) {
        // math.ceil para arredondar para cima
        int qtdDeBlocos = (int) Math.ceil((double) arquivoEmInt.length / 16);
        int[][] matrizDeBlocos = new int[qtdDeBlocos][16];

        // divide em blocos de 16 bytes
        for (int i = 0; i < qtdDeBlocos; i++) {
            int indiceInicial = i * 16;
            int indiceFinal = Math.min(indiceInicial + 16, arquivoEmInt.length);
            matrizDeBlocos[i] = Arrays.copyOfRange(arquivoEmInt, indiceInicial, indiceFinal);
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

    private int[][] adicionarBlocoAdicional(int[][] matrizDeBlocos, int tamanhoBloco) {
        int novoTamanho = matrizDeBlocos.length + 1;
        int[][] novaMatrizDeBlocos = new int[novoTamanho][tamanhoBloco];

        for (int i = 0; i < matrizDeBlocos.length; i++) {
            novaMatrizDeBlocos[i] = matrizDeBlocos[i];
        }
        // Adiciona o bloco adicional como uma nova linha
        novaMatrizDeBlocos[novoTamanho - 1] = new int[tamanhoBloco];
        for (int i = 0; i < tamanhoBloco; i++) {
            novaMatrizDeBlocos[novoTamanho - 1][i] = (int) tamanhoBloco;
        }
        return novaMatrizDeBlocos;
    }

    private int[] addPKCS7Padding(int[] bloco) {
        int padding = this.calcularPreenchimentoDoBloco(bloco.length);
        int[] blocoComPadding = Arrays.copyOf(bloco, bloco.length + padding);
        for (int i = bloco.length; i < blocoComPadding.length; i++) {
            blocoComPadding[i] = (int) padding; // Preenche o novo espaço com ints que indicam o valor do
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
    public String criptografaArquivo(String entrada, String saida, List<String[][]> listaDeRoundKey) throws Exception {
        String arqCriptografado = encryptFile(entrada, saida, listaDeRoundKey);
        System.out.println("Arquivo criptografado com sucesso.");
        return arqCriptografado;
    }

    public String encryptFile(String inputFile, String outputFile, List<String[][]> listaDeRoundKey) throws Exception {

        List<Integer> intList = new ArrayList<>();

        try (BufferedReader leitor = new BufferedReader(new FileReader(inputFile))) {
            int caractere;
            StringBuilder texto = new StringBuilder();

            while ((caractere = leitor.read()) != -1) {
                texto.append(caractere).append(" ");
            }

            String[] valores = texto.toString().trim().split(" ");

            for (String valor : valores) {
                intList.add(Integer.parseInt(valor));
            }
        }
        int[] arquivoEmInteiros = new int[intList.size()];
        for (int i = 0; i < intList.size(); i++) {
            arquivoEmInteiros[i] = intList.get(i);
        }

        int[][] matriz = this.divideEmBlocosDe16Bytes(arquivoEmInteiros);
        String[][] matrizEmHex = this.converteMatrizParaHex(matriz);
        List<String[][]> listaDeBlocos = organizarBlocos4x4(matrizEmHex);

        List<String[][]> cifragem = cifragemAES(listaDeRoundKey, listaDeBlocos);
        byte[] bytes;
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            StringBuilder builder = new StringBuilder();
            for (String[][] matrizToConvert : cifragem) {
                for (String[] linha : matrizToConvert) {
                    for (String elemento : linha) {
                        builder.append(elemento).append(" ");
                    }
                }
            }
            String texto = builder.toString().trim();
            bytes = texto.getBytes("UTF-8");
            fos.write(bytes);
        }

        System.out.println("Tamanho do arquivo criptografado em bytes: " + new File(outputFile).length());
        return bytes.toString();
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

    public static void main(String[] args) throws IOException {
        ECBPadding e = new ECBPadding();
        ValidarEntradas validarEntradas = new ValidarEntradas();
        Scanner scanner = new Scanner(System.in);

        String arquivoDeEntrada = validarEntradas.obterCaminhoArquivoValido(scanner);
        System.out.println("");

        List<Integer> intList = new ArrayList<>();

        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivoDeEntrada))) {
            int caractere;
            StringBuilder texto = new StringBuilder();

            while ((caractere = leitor.read()) != -1) {
                texto.append(caractere).append(" ");
            }

            String[] valores = texto.toString().trim().split(" ");

            for (String valor : valores) {
                intList.add(Integer.parseInt(valor));
            }
        }
        int[] arquivoEmInteiros = new int[intList.size()];
        for (int i = 0; i < intList.size(); i++) {
            arquivoEmInteiros[i] = intList.get(i);
        }

        int[][] matriz = e.divideEmBlocosDe16Bytes(arquivoEmInteiros);
        String[][] matrizEmHex = e.converteMatrizParaHex(matriz);
        List<String[][]> listaDeBlocos = e.organizarBlocos4x4(matrizEmHex);
        e.imprimirListaDeMatrizes(listaDeBlocos);

    }
}