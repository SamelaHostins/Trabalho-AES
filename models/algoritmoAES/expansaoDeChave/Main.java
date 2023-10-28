package models.algoritmoAES.expansaoDeChave;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import models.algoritmoAES.processoCifragem.Cifragem;
import models.entradaSaidas.FileManager;
import models.entradaSaidas.ValidarEntradas;
import models.operacaoECB.ECBPadding;

public class Main {

    public static void main(String[] args) throws IOException {
        ECBPadding e = new ECBPadding();
        Cifragem c = new Cifragem();
        ValidarEntradas validarEntradas = new ValidarEntradas();
        Scanner scanner = new Scanner(System.in);
        ExpansaoDeChave ex = new ExpansaoDeChave();
        Chave chaveExpansao = new Chave();

        // 20,1,94,33,199,0,48,9,31,94,112,40,59,30,100,248
        String chave = validarEntradas.getChaveValida(scanner);
        // processo da expansão da chave
        // System.out.println("Chave: " + chave);
        System.out.println("");
        String chaveHexadecimal = chaveExpansao.transformarChaveParaHexadecimal(chave);
        String[][] matrizDaChave = chaveExpansao.organizarChaveEmMatriz4x4(chaveHexadecimal);
        List<String[][]> listaDeRoundKey = ex.gerarMatrizes(11, 4, 4, matrizDaChave);

        //C:/Users/Acer/OneDrive/Documentos/teste.txt
        System.out.println("Caminho de arquivo a criptografar: ");
        String arquivoDeEntrada = scanner.nextLine();
        System.out.println("");

        System.out.println("Nome do arquivo de saida: ");
        String arquivoDeSaida = scanner.nextLine();
        System.out.println("");

        FileManager fileManager = new FileManager();
        File file = new File(arquivoDeEntrada);
        if (fileManager.ehArqBinario(file)) {
            fileManager.leArqBinario(file);
        } else {
            fileManager.leArq(arquivoDeSaida, chave);
        }

    }
}
