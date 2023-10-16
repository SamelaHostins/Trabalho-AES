package models.entradaSaidas;

import java.io.File;
import java.util.Scanner;

// Autoras: Karoline, Maria Eduarda e Sâmela
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

        // ver dps que é p validar em inteiro não em byte
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

}
