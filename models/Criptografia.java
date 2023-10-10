package models;// Autoras: Karoline, Maria Eduarda e Sâmela

import java.util.Arrays;

public class Criptografia {

    /*
     * 1° Verifica quantos blocos podem ser formados
     * 2° É criada uma matriz para armazenar os blocos (pois vamos usar no aes dps)
     * 3° Utiliza um for para copiar cada bloco para uma matriz,
     * sendo que cada linha representará um bloco
     */
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

    // para ver se deu certo
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

    // Isso porque o AES pede que os blocos sejam organizados em matrizes 4x4
    public byte[][][] organizarBlocos4x4(byte[][] blocos) {
        int numBlocos = blocos.length;
        byte[][][] matrizes4x4 = new byte[numBlocos][4][4];

        for (int i = 0; i < numBlocos; i++) {
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    matrizes4x4[i][row][col] = blocos[i][row * 4 + col];
                }
            }
        }

        return matrizes4x4;
    }

    // Para ver a lista de matrizes ex: matrizes4x4[0]
    public void imprimirMatrizes4x4(byte[][][] matrizes4x4) {
        for (int i = 0; i < matrizes4x4.length; i++) {
            System.out.println("");
            System.out.println("Matriz " + i + ":");
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    System.out.print(matrizes4x4[i][row][col] + " ");
                }
                System.out.println();
            }
        }
    }

    public String bytesParaHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }
}
