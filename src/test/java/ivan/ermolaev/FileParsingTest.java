package ivan.ermolaev;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static com.codeborne.selenide.Selectors.byText;
import static org.assertj.core.api.Assertions.assertThat;

public class FileParsingTest {

    private ClassLoader cl = FileParsingTest.class.getClassLoader();

//    @Test
//    void zipTest() throws Exception {
//        //sample-zip-file.zip
//        try (InputStream stream = cl.getResourceAsStream("files/sample-zip-file.zip");
//             ZipInputStream zis = new ZipInputStream(stream)) {
//            ZipEntry zipEntry;
//            while ((zipEntry = zis.getNextEntry()) != null) {
//                assertThat(zipEntry.getName()).isEqualTo("sample.txt");
//            }
//        }
//        ZipFile zf = new ZipFile(new File(cl.getResource("files/sample-zip-file.zip").toURI()));
//    }


    @Test
    void parsePdfTest() throws Exception {
    try (InputStream stream = cl.getResourceAsStream("files/junit-user-guide-5.8.2.pdf")){
        PDF parsed = new PDF(stream);
        assertThat(parsed.author).contains("Marc Philipp");
        }
    }

    @Test
    void parseXlsTest() throws Exception {
        try (InputStream stream = cl.getResourceAsStream("files/sample-xlsx-file.xlsx")) {
            XLS parsed = new XLS(stream);
            assertThat(parsed.excel.getSheetAt(0).getRow(1).getCell(2).getStringCellValue())
                    .isEqualTo("Abril");
        }
    }

    @Test
    void parseCsvFile() throws Exception {
        try (InputStream stream = cl.getResourceAsStream("files/example.csv")) {
            CSVReader reader = new CSVReader(new InputStreamReader(stream));
            List<String[]> list = reader.readAll();
            assertThat(list)
                    .hasSize(3)
                    .contains(
                            new String[] {"Author", "Book"},
                            new String[] {"Block", "Apteka"},
                            new String[] {"Esenin", "Cherniy Chelovek"}
                    );
        }
    }
}