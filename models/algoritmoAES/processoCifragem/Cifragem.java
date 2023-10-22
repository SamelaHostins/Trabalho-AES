package models.algoritmoAES.processoCifragem;

import java.util.ArrayList;
import java.util.List;

// Autoras: Karoline, Maria Eduarda e Sâmela
public class Cifragem {

    public String[][] ChaveMatrixParaHex(int[][] matriz) {
        int numRows = matriz.length;
        int numCols = matriz[0].length;
        String[][] hexMatrix = new String[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                hexMatrix[i][j] = IntParaHex(new int[] { matriz[i][j] });
            }
        }

        return hexMatrix;
    }

    public String IntParaHex(int[] elementosChave) {
        StringBuilder result = new StringBuilder();
        for (int c : elementosChave) {
            result.append(String.format("%02X", c));
        }
        return result.toString();
    }

    // Isso porque o AES pede que os blocos sejam organizados em matrizes 4x4
    public List<String[][]> organizarBlocos4x4(List<String[]> blocos) {
        List<String[][]> ListaBlocos = new ArrayList<>();

        for (String[] bloco : blocos) {
            String[][] matriz = new String[4][4];
            for (int col = 0; col < 4; col++) {
                for (int linha = 0; linha < 4; linha++) {
                    matriz[linha][col] = bloco[col * 4 + linha];
                }
            }
            ListaBlocos.add(matriz);
        }

        return ListaBlocos;
    }

    // Para ver a lista de matrizes ex: ListaBlocos[0]
    public void imprimirMatrizes4x4(List<String[][]> listaMatrizes) {
        for (int i = 0; i < listaMatrizes.size(); i++) {
            System.out.println("");
            System.out.println("Matriz " + i + ":");
            String[][] matriz = listaMatrizes.get(i);
            for (int linha = 0; linha < 4; linha++) {
                for (int col = 0; col < 4; col++) {
                    System.out.print(matriz[linha][col] + " ");
                }
                System.out.println();
            }
        }
    }

    public List<String[][]> cifragemAES(List<String[][]> roundKeys, List<String[][]> blocos) {
        return null;
    }

    // 1) XOR com a roundKey
    public String[][] XorComRoundKey(String[][] roundKey, String[][] bloco) {
        String[][] resultado = new String[4][4];

        for (int linha = 0; linha < 4; linha++) {
            for (int col = 0; col < 4; col++) {
                int valorRoundKey = Integer.parseInt(roundKey[linha][col], 16);
                int valorBloco = Integer.parseInt(bloco[linha][col], 16);
                int resultadoXOR = valorRoundKey ^ valorBloco;

                // Converte o resultado para hexadecimal
                String resultadoHex = Integer.toHexString(resultadoXOR);

                // Garante que o resultado tenha dois caracteres (0-padded)
                if (resultadoHex.length() == 1) {
                    resultadoHex = "0" + resultadoHex;
                }

                resultado[linha][col] = resultadoHex;
            }
        }

        return resultado;
    }

    // 2) SubBytes
    private void substituirElementos(String[][] matriz) {
        String[][] sBox = {
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

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                String elemento = matriz[i][j];
                int linha = 0;
                int coluna;

                char primeiroCaractere = Character.toLowerCase(elemento.charAt(0));

                if (Character.isDigit(primeiroCaractere)) {
                    linha = Integer.parseInt(elemento.substring(0, 1));
                } else {
                    if (primeiroCaractere >= 'a' && primeiroCaractere <= 'z') {
                        linha = primeiroCaractere - 'a' + 10;
                    }
                }

                char segundoCaractere = Character.toLowerCase(elemento.charAt(1));
                if (Character.isDigit(segundoCaractere)) {
                    coluna = Integer.parseInt(elemento.substring(1, 2));
                } else {
                    coluna = segundoCaractere - 'a' + 10;
                }

                matriz[i][j] = sBox[linha][coluna];
            }
        }
    }

    // 3) Embaralhando matriz para o proximo passo
    public void shiftRows(String[][] matriz) {
        String[][] newMatriz = new String[matriz.length][matriz.length];
        for (int li = 0; li < matriz.length; li++) {
            for (int col = 0; col < matriz.length; col++) {
                // caso seja maior que zero a linha, deve-se embaralhar os bytes
                if (li > 0) {
                    // se eu estiver na primeira posição e não estiver na última coluna
                    if (li == 1 && col != matriz.length - 1) {
                        // li 1
                        // col 0
                        if (col == 0) {
                            // o resultado da linha 1 coluna 0 deve estar na última coluna da matriz
                            newMatriz[li][matriz.length - 1] = matriz[li][col];
                        }
                        // 1;0
                        // 1:1
                        newMatriz[li][col] = matriz[li][col + 1];
                    }
                    if (li == 2) {
                        if (col == 0) {
                            // o resultado da linha 2 coluna 0 deve estar na penúltima coluna da matriz
                            newMatriz[li][matriz.length - 2] = matriz[li][col];
                            newMatriz[li][col] = matriz[li][col + 2];
                        }
                        if (col == 1) {
                            newMatriz[li][matriz.length - 1] = matriz[li][col];
                        }
                        if (col == 3) {
                            // o que tiver na posição 2,3 ficará na posição 2,1
                            newMatriz[li][matriz.length - 3] = matriz[li][col];
                        }
                    }
                    if (li == 3) {
                        if (col == 0) {
                            // o resultado da linha 3 coluna 0 deve estar na antepenúltima coluna da matriz
                            newMatriz[li][matriz.length - 3] = matriz[li][col];
                            newMatriz[li][col] = matriz[li][col + 3];
                        }
                        if (col == 1) {
                            newMatriz[li][matriz.length - 2] = matriz[li][col];
                        }
                        if (col == 2) {
                            newMatriz[li][matriz.length - 1] = matriz[li][col];
                        }
                    }
                } else {
                    // na linha 0 mantêm-se os valores como são
                    newMatriz[li][col] = matriz[li][col];
                }
            }
        }

        // Atualiza a matriz original com os valores embaralhados
        for (int li = 0; li < matriz.length; li++) {
            for (int col = 0; col < matriz.length; col++) {
                matriz[li][col] = newMatriz[li][col];
            }
        }
    }

    // 4) Gerando RoundConstant - MixColumns
    private byte[][] mixColumns(byte[][] state) {
        byte[][] newState = new byte[state.length][state[0].length];
        for (int c = 0; c < 4; c++) {
            // primeira linha da matriz de multiplicação
            // 2,3,1,1
            newState[0][c] = xor(
                    galoi(state[0][c], 0x02),
                    galoi(state[1][c], 0x03), state[2][c], state[3][c]);
            // segunda linha da matriz de multiplicação
            // 1,2,3,1
            newState[1][c] = xor(state[0][c], galoi(state[1][c], 0x02),
                    galoi(state[2][c], 0x03), state[3][c]);
            // terceira linha da matriz de multiplicação
            // 1,1,2,3
            newState[2][c] = xor(state[0][c], state[1][c], galoi(state[2][c], 0x02),
                    galoi(state[3][c], 0x03));
            // quarta linha da matriz de multiplicação
            // 3,1,1,2
            newState[3][c] = xor(galoi(state[0][c], 0x03), state[1][c], state[2][c],
                    galoi(state[3][c], 0x02));
        }
        return newState;
    }

    // metodo xor

    private byte xor(byte b1, byte b2, byte b3, byte b4) {
        byte bResult = 0;
        bResult ^= b1;
        bResult ^= b2;
        bResult ^= b3;
        bResult ^= b4;
        return bResult;
    }

    // metodo de multiplicação galoi

    private static byte galoi(int v1, int v2) {
        byte bytes[] = new byte[8];
        byte result = 0;
        bytes[0] = (byte) v1;
        // fazer implementação
        return result;
    }

    private void printMatrix(String[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Cifragem chave = new Cifragem();

        String[][] bloco = {
                { "44", "4e", "56", "4e" },
                { "45", "56", "49", "54" },
                { "53", "4f", "4d", "4f" },
                { "45", "4c", "45", "21" }
        };

        String[][] roundKey = {
                { "41", "45", "49", "4d" },
                { "42", "46", "4a", "4e" },
                { "43", "47", "4b", "4f" },
                { "44", "48", "4c", "50" }
        };

        String[][] matrizResultado = chave.XorComRoundKey(roundKey, bloco);
        chave.substituirElementos(matrizResultado);
        chave.shiftRows(matrizResultado);
        chave.printMatrix(matrizResultado);
    }
}