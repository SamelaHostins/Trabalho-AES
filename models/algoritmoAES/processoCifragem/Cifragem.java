package models.algoritmoAES.processoCifragem;

// Autoras: Karoline, Maria Eduarda e Sâmela
public class Cifragem {

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
}
