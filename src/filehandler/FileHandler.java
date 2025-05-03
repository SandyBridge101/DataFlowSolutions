package filehandler;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import com.opencsv.exceptions.CsvException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import com.opencsv.*;

import utils.*;

public class FileHandler {
    private static final Logger logger = LoggerUtil.getLogger(FileHandler.class);


    public static String readFile(String filePath) throws IOException, CsvException {
        logger.info("Reading file at " + filePath);
        String text = "";
        if (filePath.endsWith(".txt")) {
            text=readTextFile(filePath);
        } else if (filePath.endsWith(".docx")) {
            text=readDocxFile(filePath);
        } else if (filePath.endsWith(".pdf")) {
            text=readPdfFile(filePath);
        } else if (filePath.endsWith(".csv")) {
            text=readCsvFile(filePath);
        } else {
            logger.info("Unsupported file type.");
        }
        return text;
    }

    private static String readTextFile(String path) throws IOException {
        logger.info("Reading TXT file...");
        StringBuilder textOutput = new StringBuilder();
        List<String> lines = Files.readAllLines(Paths.get(path));
        for (String line : lines) {
            textOutput.append(line).append("\n");
        }
        lines.forEach(System.out::println);
        return textOutput.toString();

    }

    private static String readDocxFile(String path) throws IOException {
        logger.info("Reading DOCX file...");
        StringBuilder textOutput = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(path);) {
            XWPFDocument doc = new XWPFDocument(fis);
            for (XWPFParagraph para : doc.getParagraphs()) {
                //System.out.println(para.getText());
                textOutput.append(para.getText()).append("\n");
            }
            return textOutput.toString();
        }
    }

    private static String readPdfFile(String path) throws IOException {
        logger.info("Reading PDF file...");
        StringBuilder textOutput = new StringBuilder();
        try (PDDocument document = Loader.loadPDF(new File(path))) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            //System.out.println(text);
            textOutput.append(text).append("\n");
            return textOutput.toString();
        }
    }

    private static String readCsvFile(String path) throws IOException, CsvException {
        logger.info("Reading CSV file...");
        StringBuilder textOutput = new StringBuilder();
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            List<String[]> lines = reader.readAll();
            for (String[] line : lines) {
                //System.out.println(String.join(", ", line));
                textOutput.append(String.join(", ", line)).append("\n");
            }
            return textOutput.toString();
        }
    }
    public static void writeFile(String filePath, List<String> content) throws Exception {
        logger.info("Writing file...");
        if (filePath.endsWith(".txt")) {
            writeTextFile(filePath, content);
        } else if (filePath.endsWith(".docx")) {
            writeDocxFile(filePath, content);
        } else if (filePath.endsWith(".pdf")) {
            writePdfFile(filePath, content);
        } else if (filePath.endsWith(".csv")) {
            writeCsvFile(filePath, content);
        } else {
            logger.info("Unsupported file type.");
        }
    }

    private static void writeTextFile(String path, List<String> content) throws IOException {
        Files.write(Paths.get(path), content);
        logger.info("TXT file written.");
    }

    private static void writeDocxFile(String path, List<String> content) throws IOException {
        XWPFDocument doc = new XWPFDocument();
        for (String line : content) {
            XWPFParagraph para = doc.createParagraph();
            para.createRun().setText(line);
        }
        try (FileOutputStream out = new FileOutputStream(path)) {
            doc.write(out);
        }

        logger.info("DOCX file written.");
    }

    private static void writePdfFile(String path, List<String> content) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.LETTER);
        document.addPage(page);

        try (PDPageContentStream stream = new PDPageContentStream(document, page)) {

            stream.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 12);
            stream.beginText();
            stream.newLineAtOffset(50, 700);
            for (String line : content) {
                stream.showText(line);
                stream.newLineAtOffset(0, -15);
            }
            stream.endText();
        }

        document.save(path);
        document.close();
        logger.info("PDF file written.");
    }

    private static void writeCsvFile(String path, List<String> content) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(path))) {
            for (String line : content) {
                String[] row = line.split(",");
                writer.writeNext(row);
            }
        }
        logger.info("CSV file written.");
    }

    /*
    public static void main(String[] args) {
        List<String> content = Arrays.asList(
                "Hello, this is line 1",
                "This is line 2",
                "Line 3 has, a comma"
        );

        try {
            String filePath = "output/example.pdf"; // Change to desired file
            writeFile(filePath, content);
        } catch (Exception e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }

     */
}
