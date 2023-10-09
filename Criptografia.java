// Autoras: Karoline, Maria Eduarda e Sâmela

import java.util.Arrays;

public class Criptografia {

    /*
     * 1° Verifica quantos blocos podem ser formados
     * 2° É criada uma matriz para armazenar os blocos (pois vamos usar no aes dps)
     * 3° Utiliza um for para copiar cada bloco para uma matriz,
     * sendo que cada linha representará um bloco
     */
    public byte[][] divideEmBlocosDe16Bytes(byte[] arquivoEmBytes) {
        // math ceil para arredondar para cima para caber todos os bytes
        int qtdDeBlocos = (int) Math.ceil((double) arquivoEmBytes.length / 16);
        byte[][] matrizDeBlocos = new byte[qtdDeBlocos][16];

        if (qtdDeBlocos == 1) {
            matrizDeBlocos[0] = addPKCS7Padding(matriz);
        } else {
            for (int i = 0; i < qtdDeBlocos; i++) {
                int indiceInicial = i * 16;
                int indiceFinal = Math.min(indiceInicial + 16, arquivoEmBytes.length);
                matrizDeBlocos[i] = Arrays.copyOfRange(arquivoEmBytes, indiceInicial, indiceFinal);
            }

            // PKCS#7 apenas ao último bloco
            int ultimoBloco = qtdDeBlocos - 1;
            matrizDeBlocos[ultimoBloco] = addPKCS7Padding(matrizDeBlocos[ultimoBloco], 16);
        }

        return matrizDeBlocos;

    }

    // para ver se deu certo
    public String lerArquivo(byte[][] matriz) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < matriz.length; i++) {
            result.append("Bloco ").append(i).append(": ").append(Arrays.toString(matriz[i])).append("\n");
        }
        return result.toString();
    }
}
