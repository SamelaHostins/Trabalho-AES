package view;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import models.algoritmoAES.expansaoDeChave.Chave;
import models.algoritmoAES.expansaoDeChave.ExpansaoDeChave;
import models.algoritmoAES.processoCifragem.CriptografarArquivo;
import models.entradaSaidas.ValidarEntradas;

public class Main {

    public static void main(String[] args) throws IOException {
        ValidarEntradas validarEntradas = new ValidarEntradas();
        ExpansaoDeChave ex = new ExpansaoDeChave();
        CriptografarArquivo criptografarArquivo = new CriptografarArquivo();
        Chave chaveExpansao = new Chave();
        Scanner scanner = new Scanner(System.in);

        // 20,1,94,33,199,0,48,9,31,94,112,40,59,30,100,248
        String chave = validarEntradas.getChaveValida(scanner);
        // processo da expansão da chave
        // System.out.println("Chave: " + chave);
        System.out.println("");
        String chaveHexadecimal = chaveExpansao.transformarChaveParaHexadecimal(chave);
        String[][] matrizDaChave = chaveExpansao.organizarChaveEmMatriz4x4(chaveHexadecimal);
        List<String[][]> listaDeRoundKey = ex.gerarMatrizes(11, 4, 4, matrizDaChave);

        // C:/Users/Acer/OneDrive/Documentos/teste.txt
        String arquivoDeEntrada = validarEntradas.obterCaminhoArquivoValido(scanner);
        System.out.println("");

        String arquivoDeSaida = validarEntradas.obterNomeDoArquivoValido(scanner);
        System.out.println("");

        try {
            criptografarArquivo.criptografaArquivo(arquivoDeEntrada, arquivoDeSaida, listaDeRoundKey);
        } catch (Exception e1) {
            // Lide com a exceção aqui
            e1.printStackTrace(); // ou qualquer tratamento de erro específico que você desejar
        }

        String arquivoDeSaida2 = "arquivo_cifrado";
        criptografarArquivo.encryptFileTeste(arquivoDeEntrada, arquivoDeSaida2, chave);

        String arquivoCifrado = "saida";
        String arquivoDescriptografado = "arquivo_descriptografado.txt";
        criptografarArquivo.decryptFile(arquivoCifrado, arquivoDescriptografado, chave);
    }

}
