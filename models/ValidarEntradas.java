package models;// Autoras: Karoline, Maria Eduarda e Sâmela

import java.io.File;
import java.util.Scanner;

public class ValidarEntradas {

    // Verifica se o file passado existe
    public boolean isFileValido(String nomeArquivo) {
        File file = new File(nomeArquivo);
        return file.exists() && file.isFile();
    }

    public String obterCaminhoArquivoValido(Scanner scanner) {
        String arquivo;

        while (true) {
            System.out.println("Informe o caminho do arquivo: ");
            arquivo = scanner.nextLine();

            if (isFileValido(arquivo)) {
                return arquivo;
            } else {
                System.out.println("Caminho de arquivo inválido. Tente novamente.");
            }
        }
    }

    public String obterNomeDoArquivoValido(Scanner scanner) {
        String arquivo;

        while (true) {
            System.out.println("Informe o nome do arquivo de destino a ser gerado: ");
            arquivo = scanner.nextLine();

            if (arquivo != null && !arquivo.trim().isEmpty()) {
                return arquivo;
            } else {
                System.out.println("Nome do arquivo de destino inválido. Tente novamente.");
            }
        }
    }

    // Valida se a chave foi escrita corretamente pelo usuário
    public String validarChave(String chave) {
        if (!chave.matches("^\\d{1,3}(,\\d{1,3}){15}$")) {
            return "A chave fornecida não está no formato correto.";
        }

        String[] bytesStr = chave.split(",");
        for (String byteStr : bytesStr) {
            int byteValue = Integer.parseInt(byteStr);
            if (byteValue < 0 || byteValue > 255) {
                return "Cada byte da chave deve ser um número inteiro entre 0 e 255.";
            }
        }

        return null; // A chave é válida
    }

    // getChaveValida valida a chave a partir do método validarChave
    public String getChaveValida(Scanner scanner) {
        String chave = "";
        boolean chaveValida = false;

        while (!chaveValida) {
            System.out.println("Informe a chave de criptografia (16 bytes separados por vírgula):");
            chave = scanner.nextLine();

            String erro = validarChave(chave);
            if (erro == null) {
                chaveValida = true;
            } else {
                System.out.println(erro);
            }
        }

        return chave;
    }

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

}
