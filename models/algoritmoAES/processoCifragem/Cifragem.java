package models.algoritmoAES;

// Autoras: Karoline, Maria Eduarda e Sâmela
public class Cifragem {

    public String[][] bytesMatrixParaHex(byte[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;
        String[][] hexMatrix = new String[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                hexMatrix[i][j] = bytesParaHex(new byte[]{matrix[i][j]});
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

    //3) Embaralhando matriz para o proximo passo
    public byte[][] shiftRows(byte[][] resultMatriz) {
        byte[][] newMatriz = new byte[resultMatriz.length][resultMatriz.length];
        for (int li = 0; li < resultMatriz.length; li++) {
            for (int col = 0; col < resultMatriz.length; col++) {
                // caso seja maior que zero a linha,  deve-se embaralhar os bytes
                if (li > 0) {
                    // se eu estiver na primeira posicao e n estiver na ultima coluna
                    if (li == 1 && col != resultMatriz.length - 1) {
                        //li  1
                        //col 0
                        if (col == 0) {
                            //o resultado da linha 1 coluna 0 tem q estar na ultima coluna da matriz
                            newMatriz[li][resultMatriz.length - 1] = resultMatriz[li][col];
                        }
                        //1;0
                        //1:1
                        newMatriz[li][col] = resultMatriz[li][col + 1];
                    }
                    if (li == 2) {
                        if (col == 0) {
                            //o resultado da linha 2 coluna 0 tem q estar na penultima coluna da matriz
                            newMatriz[li][resultMatriz.length - 2] = resultMatriz[li][col];
                            newMatriz[li][col] = resultMatriz[li][col + 2];
                        }
                        if (col == 1) {
                            newMatriz[li][resultMatriz.length - 1] = resultMatriz[li][col];
                        }
                        if (col == 3) {
                            // o que tiver na posicao 2,3 ficara na posicao 2,1
                            newMatriz[li][resultMatriz.length - 3] = resultMatriz[li][col];
                        }
                    }
                    if (li == 3) {
                        if (col == 0) {
                            //o resultado da linha 3 coluna 0 tem q estar na antepenultima coluna da matriz
                            newMatriz[li][resultMatriz.length - 3] = resultMatriz[li][col];
                            newMatriz[li][col] = resultMatriz[li][col + 3];
                        }
                        if (col == 1) {
                            newMatriz[li][resultMatriz.length - 2] = resultMatriz[li][col];
                        }
                        if (col == 2) {
                            newMatriz[li][resultMatriz.length - 1] = resultMatriz[li][col];
                        }
                    }
                } else {
                    //na linha 0 mantem-se os valores como sao
                    newMatriz[li][col] = resultMatriz[li][col];
                }
            }
        }
        return newMatriz;
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
}
