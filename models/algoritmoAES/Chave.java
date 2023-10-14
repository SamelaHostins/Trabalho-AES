package models.algoritmoAES;

import java.util.ArrayList;
import java.util.List;

public class Chave {
    private List<String[][]> ListaDeMatrizes = new ArrayList<>();

    public List<String[][]> gerarMatrizes(int numMatrizes, int numLinhas, int numColunas, String[][] primeiraMatriz) {
        ListaDeMatrizes.clear(); // Limpa a lista de matrizes

        // Adicione a matriz fornecida como o primeiro elemento
        ListaDeMatrizes.add(primeiraMatriz);

        for (int i = 1; i < numMatrizes; i++) {
            ListaDeMatrizes.add(gerarMatriz(numLinhas, numColunas, ListaDeMatrizes.get(i - 1)));
        }

        return ListaDeMatrizes;
    }

    private String[][] gerarMatriz(int numLinhas, int numColunas, String[][] matrizAnterior) {
        String[][] matrix = new String[numLinhas][numColunas];

        for (int i = 0; i < numLinhas; i++) {
            for (int j = 0; j < numColunas; j++) {
                if (j == 0) {
                    String[] ultimaColunaMatrizAnterior = obterUltimaColuna(matrizAnterior);
                    rotacionarColunaParaEsquerda(ultimaColunaMatrizAnterior);
                    substituirElementos(ultimaColunaMatrizAnterior);
                    matrix[i][j] = ultimaColunaMatrizAnterior[i];
                } else {
                    // Calcule o valor com XOR da coluna anterior e a coluna equivalente da matriz
                    // anterior
                     int valorColunaAnterior = Integer.parseInt(matrizAnterior[i][j - 1], 16);
                     int valorColunaMatrizAnterior = Integer.parseInt(matrix[i][j - 1], 16);
                     int valorXOR = valorColunaAnterior ^ valorColunaMatrizAnterior;
                     matrix[i][j] = String.format("%02X", valorXOR);
                    // matrix[i][j] = "63";
                }

            }
        }
        return matrix;
    }

    public int getQuantidadeDeMatrizes() {
        return ListaDeMatrizes.size();
    }

    private String[] obterUltimaColuna(String[][] matriz) {
        int numLinhas = matriz.length;
        String[] ultimaColuna = new String[numLinhas];

        for (int i = 0; i < numLinhas; i++) {
            ultimaColuna[i] = matriz[i][matriz[0].length - 1];
        }

        return ultimaColuna;
    }

    private void rotacionarColunaParaEsquerda(String[] coluna) {
        String primeiroElemento = coluna[0];
        int numLinhas = coluna.length;

        for (int i = 0; i < numLinhas - 1; i++) {
            coluna[i] = coluna[i + 1];
        }
        coluna[numLinhas - 1] = primeiroElemento;
    }

    private void substituirElementos(String[] colunaRotacionada) {
        String[][] matriz = {
                { "63", "7c", "77", "7b", "f2", "6b", "6f", "c5", "30", "01", "67", "2b",
                        "fe", "d7", "ab", "76" },
                { "ca", "82", "c9", "7d", "fa", "59", "47", "f0", "ad", "d4", "a2", "af",
                        "9c", "a4", "72", "c0" },
                { "b7", "fd", "93", "26", "36", "3f", "f7", "cc", "34", "a5", "e5", "f1",
                        "71", "d8", "31", "15" },
                { "04", "c7", "23", "c3", "18", "96", "05", "9a", "07", "12", "80", "e2",
                        "eb", "27", "b2", "75" },
                { "09", "83", "2c", "1a", "1b", "6e", "5a", "a0", "52", "3b", "d6", "b3",
                        "29", "e3", "2f", "84" },
                { "53", "d1", "00", "ed", "20", "fc", "b1", "5b", "6a", "cb", "be", "39",
                        "4a", "4c", "58", "cf" },
                { "d0", "ef", "aa", "fb", "43", "4d", "33", "85", "45", "f9", "02", "7f",
                        "50", "3c", "9f", "a8" },
                { "51", "a3", "40", "8f", "92", "9d", "38", "f5", "bc", "b6", "da", "21",
                        "10", "ff", "f3", "d2" },
                { "cd", "0c", "13", "ec", "5f", "97", "44", "17", "c4", "a7", "7e", "3d",
                        "64", "5d", "19", "73" },
                { "60", "81", "4f", "dc", "22", "2a", "90", "88", "46", "ee", "b8", "14",
                        "de", "5e", "0b", "db" },
                { "e0", "32", "3a", "0a", "49", "06", "24", "5c", "c2", "d3", "ac", "62",
                        "91", "95", "e4", "79" },
                { "e7", "c8", "37", "6d", "8d", "d5", "4e", "a9", "6c", "56", "f4", "ea",
                        "65", "7a", "ae", "08" },
                { "ba", "78", "25", "2e", "1c", "a6", "b4", "c6", "e8", "dd", "74", "1f",
                        "4b", "bd", "8b", "8a" },
                { "70", "3e", "b5", "66", "48", "03", "f6", "0e", "61", "35", "57", "b9",
                        "86", "c1", "1d", "9e" },
                { "e1", "f8", "98", "11", "69", "d9", "8e", "94", "9b", "1e", "87", "e9",
                        "ce", "55", "28", "df" },
                { "8c", "a1", "89", "0d", "bf", "e6", "42", "68", "41", "99", "2d", "0f",
                        "b0", "54", "bb", "16" }
        };

        for (int i = 0; i < colunaRotacionada.length; i++) {
            String elemento = colunaRotacionada[i];
            int linha;
            int coluna;

            char primeiroCaractere = Character.toLowerCase(elemento.charAt(0));
            if (Character.isDigit(primeiroCaractere)) {
                linha = Integer.parseInt(elemento.substring(0, 1));
            } else {
                linha = elemento.charAt(0) - 'a' + 10;
            }

            char segundoCaractere = Character.toLowerCase(elemento.charAt(1));
            if (Character.isDigit(segundoCaractere)) {
                coluna = Integer.parseInt(elemento.substring(1, 2));
            } else {
                coluna = segundoCaractere - 'a' + 10;
            }

            colunaRotacionada[i] = matriz[linha][coluna];
        }
    }

    public static void main(String[] args) {
        Chave chave = new Chave(); // Crie uma instância da classe Chave
        int numMatrizes = 3;
        int numLinhas = 4;
        int numColunas = 4;

        // Fornecer a matriz que será adicionada como o primeiro elemento
        String[][] primeiraMatriz = {
                { "41", "45", "49", "4d" },
                { "42", "46", "4a", "4e" },
                { "43", "47", "4b", "4f" },
                { "44", "48", "4c", "50" }
        };

        List<String[][]> ListaDeMatrizes = chave.gerarMatrizes(numMatrizes, numLinhas, numColunas, primeiraMatriz);

        // Agora você tem uma lista de matrizes, com a matriz fornecida como o primeiro
        // elemento.

        // Para imprimir as matrizes, você pode usar um loop e um método de impressão
        // personalizado.
        for (String[][] matrix : ListaDeMatrizes) {
            printMatrix(matrix);
        }

        // Obter a quantidade de matrizes geradas e imprimir
        int quantidadeDeMatrizes = chave.getQuantidadeDeMatrizes();
        System.out.println("Quantidade de matrizes geradas: " + quantidadeDeMatrizes);
    }

    private static void printMatrix(String[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}