package com.fbo.financaspessoais;

//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class FinancasPessoaisApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancasPessoaisApplication.class, args);
	}


//	public static void main(String[] args) throws IOException {
////		proccessPDF();
////		readPDFC6();
//		readPDFNubank();
//	}
//	public static void proccessInSrtSubtitle(){
//		Path entradaPath = Paths.get("G:\\Downfilmes\\Monster\\Monster.2023.WEB-DL.1080p.MUBI.RHRN.Dream.srt");
//		Path saidaPath = Paths.get("G:\\Downfilmes\\Monster\\Monster.2023.WEB-DL.1080p.MUBI.RHRN.Dream-tratado.srt");
//
//		try (BufferedWriter writer = Files.newBufferedWriter(saidaPath, StandardCharsets.UTF_8);
//			 Stream<String> lines = Files.lines(entradaPath, StandardCharsets.UTF_8)) {
//
//			lines.forEach(line -> {
//				// Processar cada linha conforme necessário
//
//
//				String lineWithoutAccent = Normalizer.normalize(line, Normalizer.Form.NFD)
//						.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
//
//				try {
//					writer.write(lineWithoutAccent + System.lineSeparator());
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				System.out.println(line + "-> Sem acento -> " + lineWithoutAccent);
//			});
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}


	public static void proccessPDF() throws IOException {

		String entradaPath = "C:\\Users\\Fábio\\Downloads\\fatura-janeiro.pdf";
		Path saidaPath = Paths.get("G:\\pdf-to-txt-c6.txt");

		try (PDDocument document = PDDocument.load(new File(entradaPath));
			 BufferedWriter writer = Files.newBufferedWriter(saidaPath, StandardCharsets.UTF_8)) {

			 PDFTextStripper pdfTextStripper = new PDFTextStripper();
			 String pdfText = pdfTextStripper.getText(document);

			try {
				writer.write(pdfText + System.lineSeparator());
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Conteúdo -> " + pdfText);
		};
	}

	public static void readPDFC6(){
		try {
//			InputStream inputStream = file.getInputStream();
			//Todo utilizar no PdfReader o inputStream;

			StringBuffer sb = new StringBuffer();

//			Path saidaPath = Paths.get("G:\\pdf-to-txt-c6-itextpdf.txt");
//            BufferedWriter writer = Files.newBufferedWriter(saidaPath, StandardCharsets.UTF_8);
			PdfReader reader = new PdfReader("C:\\Users\\Fábio\\Downloads\\fatura-janeiro.pdf");
			int numPages = reader.getNumberOfPages();

			for (int pageNum = 1; pageNum <= numPages; pageNum++) {
				String pageText = PdfTextExtractor.getTextFromPage(reader, pageNum);
//                writer.write(pageText + System.lineSeparator());
				if (pageText.contains("FABIO F SILVA"))
					sb.append("Texto da página " + pageNum + ":\n" + pageText + System.lineSeparator());

//				System.out.println("Texto da página " + pageNum + ":\n" + pageText);
			}

			System.out.println("Todo conteúdo = " + sb.toString());

//			String[] valoresEmReais = sb.toString().split("Valores em reais");

			List<String> valores = extrairLinhasComValores(sb.toString());


			valores.forEach(v -> {
				System.out.println("Conteúdo -> "+v);
			});

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void readPDFNubank(){
		try {
//			InputStream inputStream = file.getInputStream();
			//Todo utilizar no PdfReader o inputStream;

			StringBuffer sb = new StringBuffer();

//			Path saidaPath = Paths.get("G:\\pdf-to-txt-c6-itextpdf.txt");
//            BufferedWriter writer = Files.newBufferedWriter(saidaPath, StandardCharsets.UTF_8);
			PdfReader reader = new PdfReader("C:\\Users\\Fábio\\Downloads\\Nubank_2024-01-02.pdf");
			int numPages = reader.getNumberOfPages();

			for (int pageNum = 1; pageNum <= numPages; pageNum++) {
				String pageText = PdfTextExtractor.getTextFromPage(reader, pageNum);
//                writer.write(pageText + System.lineSeparator());
				if(pageText.contains("TRANSAÇÕES"))
					sb.append("Texto da página " + pageNum + ":\n" + pageText + System.lineSeparator());
//				System.out.println("Texto da página " + pageNum + ":\n" + pageText);
			}

			System.out.println("Todo conteúdo = " + sb.toString());

//			String[] valoresEmReais = sb.toString().split("Valores em reais");

			List<String> valores = extrairLinhasComValores(sb.toString());


			valores.forEach(v -> {
				System.out.println("Conteúdo -> "+v);
			});

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<String> extrairLinhasComValores(String conteudo) {
		List<String> linhasComValores = new ArrayList<>();

		// Padrão para identificar linhas com valores monetários
		Pattern padraoValores = Pattern.compile("\\d{2} (?i)(jan|fev|mar|abr|mai|jun|jul|ago|set|out|nov|dez).*");


		// Criar um Matcher para encontrar correspondências no conteúdo
		Matcher matcher = padraoValores.matcher(conteudo);

		// Iterar sobre as correspondências e adicionar as linhas correspondentes à lista
		while (matcher.find()) {
			linhasComValores.add(matcher.group());
		}

		return linhasComValores;
	}


}
