package models.entradaSaidas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

// Autoras: Karoline, Maria Eduarda e Sâmela
public class FileManager {
	String data = null;

	public void criaArq(String fileName) {
		try {
			File myObj = new File(fileName);
			if (myObj.createNewFile()) {
				System.out.println("Arquvo criado: " + myObj.getName());
			} else {
				System.out.println("O arquivo já existe.");
			}
		} catch (IOException e) {
			System.out.println("Ocorreu um erro.");
			e.printStackTrace();
		}
	}

	public void leArq(String path, String text) {
		try {
			FileWriter fWriter = new FileWriter(path);
			fWriter.write(text);
			fWriter.close();
		} catch (IOException e) {
			System.out.print(e.getMessage());
		}
	}

	// metodo para arquivo texto
	public void leArqTexto(File myObj) throws IOException {
		try {
			Scanner myReader = new Scanner(myObj);

			while (myReader.hasNextLine()) {
				data = data + myReader.nextLine().trim();
			}

			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Ocorreu um erro.");
			e.printStackTrace();
		}
	}

	// metodo para arquivo binario
	public void leArqBinario(File file) {
		try {
			FileInputStream myReader = new FileInputStream(file);
			int byt;
			while ((byt = myReader.read()) != -1) {
				data = data + byt;
			}

			myReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// verifica se o arquivo é binario ou o texto do arquivo
	public boolean ehArqBinario(File file) throws FileNotFoundException, IOException {
		FileInputStream in = new FileInputStream(file);
		int size = in.available();

		if (size > 1024) {
			size = 1024;
		}

		byte[] fileData = new byte[size];

		in.read(fileData);
		in.close();

		int numberOfAsciiCharacters = 0;
		int numberOfOtherCharacters = 0;

		for (int i = 0; i < fileData.length; i++) {
			byte b = fileData[i];
			if (b < 0x09)
				return true;

			if (b == 0x09 || b == 0x0A || b == 0x0C || b == 0x0D)
				numberOfAsciiCharacters++;
			else if (b >= 0x20 && b <= 0x7E)
				numberOfAsciiCharacters++;
			else
				numberOfOtherCharacters++;
		}

		if (numberOfOtherCharacters == 0)
			return false;

		return 100 * numberOfOtherCharacters / (numberOfAsciiCharacters + numberOfOtherCharacters) > 95;
	}
}
