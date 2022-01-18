package ivan.ermolaev;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipFile;

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
    void zipFileTest() throws Exception{

        ZipFile zipFile = new ZipFile(new File(cl.getResource("files/sample-zip-file.zip").toURI()));

        try  (InputStream CsvInputStream = zipFile.getInputStream(zipFile.getEntry("example.csv"))) {
            CSVReader csvReader = new CSVReader(new InputStreamReader(CsvInputStream));
            List<String[]> list = csvReader.readAll();
            assertThat(list)
                    .hasSize(3)
                    .contains(
                            new String[] {"Author", "Book"},
                            new String[] {"Block", "Apteka"},
                            new String[] {"Esenin", "Cherniy Chelovek"}
                    );
        }

        try (InputStream pdfInputStream = zipFile.getInputStream(zipFile.getEntry("junit-user-guide-5.8.2.pdf"))){
            PDF parsed = new PDF(pdfInputStream);
            assertThat(parsed.author).contains("Marc Philipp");
        }

        try (InputStream xlsInputStream = zipFile.getInputStream(zipFile.getEntry("sample-xlsx-file.xlsx"))) {
            XLS parsed = new XLS(xlsInputStream);
            assertThat(parsed.excel.getSheetAt(0).getRow(1).getCell(2).getStringCellValue())
                    .isEqualTo("Abril");
        }
    }
}