package models.algoritmoAES;

// Autoras: Karoline, Maria Eduarda e Sâmela
public class ExpansaoDeChave {

    // Transforma a representação textual da chave em hexadecimal
    public String transformarChaveParaHexadecimal(String sequencia) {
        String[] numeros = sequencia.split(",");
        StringBuilder hexadecimal = new StringBuilder();

        for (String numero : numeros) {
            int valorEmDecimal = Integer.parseInt(numero);
            hexadecimal.append(String.format("%02X", valorEmDecimal));
            hexadecimal.append(",");
        }

        // Remova a vírgula extra no final, se houver
        if (hexadecimal.length() > 0 && hexadecimal.charAt(hexadecimal.length() - 1) == ',') {
            hexadecimal.setLength(hexadecimal.length() - 1);
        }

        return hexadecimal.toString();
    }

    public String[][] organizarChaveEmMatriz4x4(String chave) {
        String[] hexStrings = chave.split(",");
        String[][] matriz4x4 = new String[4][4];
        int hexIndex = 0;
        for (int col = 0; col < 4; col++) {
            for (int linha = 0; linha < 4; linha++) {
                matriz4x4[linha][col] = hexStrings[hexIndex++];
            }
        }
        return matriz4x4;
    }

    public void imprimirMatrizDaChave(String[][] matriz4x4) {
        for (int linha = 0; linha < 4; linha++) {
            for (int col = 0; col < 4; col++) {
                System.out.print(matriz4x4[linha][col] + " ");
            }
            System.out.println();
        }
    }

    // Tabela RCon para as constantes de rodada
    private int[] RoundConstant = {
            0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1B, 0x36
    };

    private int[][] sBox = {
            { 63, 124, 119, 123, 242, 107, 111, 197, 48, 1, 103, 43, 254, 215, 171, 118 },
            { 202, 130, 201, 125, 250, 89, 71, 240, 173, 212, 162, 175, 156, 164, 114, 192 },
            { 183, 253, 147, 38, 54, 63, 247, 204, 52, 229, 229, 163, 241, 133, 181, 51 },
            { 145, 235, 62, 60, 195, 72, 234, 174, 51, 5, 154, 15, 252, 217, 123, 10 },
            { 9, 35, 26, 30, 234, 90, 82, 45, 148, 57, 48, 107, 65, 18, 72, 38 },
            { 83, 209, 0, 237, 32, 92, 85, 52, 54, 163, 17, 100, 182, 150, 254, 57 },
            { 150, 39, 46, 36, 241, 3, 83, 3, 70, 45, 249, 2, 127, 80, 60, 91 },
            { 89, 163, 64, 13, 68, 5, 154, 61, 35, 57, 185, 199, 86, 69, 58, 30 },
            { 65, 103, 19, 71, 161, 77, 146, 13, 196, 247, 17, 21, 207, 188, 220, 124 },
            { 23, 242, 193, 47, 238, 164, 23, 145, 230, 43, 4, 115, 191, 175, 18, 103 },
            { 60, 129, 17, 207, 239, 29, 119, 249, 2, 4, 42, 164, 109, 1, 21, 120 },
            { 139, 188, 141, 224, 7, 254, 50, 82, 47, 74, 25, 2, 124, 92, 119, 181 },
            { 156, 85, 122, 20, 197, 199, 174, 64, 116, 151, 236, 175, 58, 45, 153, 179 },
            { 19, 68, 112, 201, 85, 195, 74, 23, 196, 125, 41, 30, 200, 156, 141, 25 },
            { 152, 100, 105, 96, 79, 220, 8, 31, 175, 187, 154, 6, 100, 183, 118, 222 },
            { 66, 48, 3, 46, 243, 0, 17, 206, 168, 77, 146, 85, 3, 70, 140, 163 }
    };
}
