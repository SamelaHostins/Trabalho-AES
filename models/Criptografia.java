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
    public String[][][] organizarBlocos4x4(String[][] blocosHex) {
        int numBlocos = blocosHex.length;
        String[][][] matrizes4x4 = new String[numBlocos][4][4];

        for (int i = 0; i < numBlocos; i++) {
            for (int col = 0; col < 4; col++) {
                for (int linha = 0; linha < 4; linha++) {
                    matrizes4x4[i][linha][col] = blocosHex[i][col * 4 + linha];
                }
            }
        }

        return matrizes4x4;
    }

    // Para ver a lista de matrizes ex: matrizes4x4[0]
    public void imprimirMatrizes4x4(String[][][] matrizes4x4) {
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

    public String[][] bytesMatrixParaHex(byte[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;
        String[][] hexMatrix = new String[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                hexMatrix[i][j] = bytesParaHex(new byte[] { matrix[i][j] });
            }
        }

        return hexMatrix;
    }

    public String bytesParaHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }

    private byte[] addPKCS7Padding_2(byte[] blocoPreencher, Integer quantidadeBytes) {
        // precisa preencher de acordo com a quantidade de bytes faltantes:
        // por exemplo recebi um bloco de byte que tem preenchido 2 bytes e eu tenho um
        // total de 16 bytes
        // logo faltam 14
        // mesmo que o ultimo bloco nao tenha bytes para ser preenchidos
        // deverá ser criado um novo bloco de 16 bytes adicional
        Integer quantidadeBytesPreenchidos = 0;
        for (byte b : blocoPreencher) {
            if (b != 0) {
                quantidadeBytesPreenchidos++;
            }
        }
        if (quantidadeBytesPreenchidos < quantidadeBytes) {
            byte[] byteArray = new byte[quantidadeBytes - quantidadeBytesPreenchidos];
            Arrays.fill(byteArray, (byte) 0xFF);
            return byteArray;
        }
        byte[] byteArrayAdicional = new byte[16];
        Arrays.fill(byteArrayAdicional, (byte) 0xFF);
        return byteArrayAdicional;
    }
}
