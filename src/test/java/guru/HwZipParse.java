package guru;


import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.opencsv.CSVReader;
import guru.domain.Me;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class HwZipParse {
    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    void homeWorkXlsTest() throws Exception {
        ZipFile zipFile = new ZipFile(new File(classLoader.getResource("files/hw.zip").toURI()));
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {

            ZipEntry zipEntry = entries.nextElement();

            if (zipEntry.getName().equals("prajs_ot_2104.xls")) {
                try (InputStream is = zipFile.getInputStream(zipEntry)) {
                    XLS xls = new XLS(is);
                    xls.excel
                            .getSheetAt(0)
                            .getRow(11)
                            .getCell(1)
                            .getStringCellValue().contains("Сахалинская обл, Южно-Сахалинск");
                }
            }
        }
    }

    @Test
    void homeWorkCsvTest() throws Exception {
        ZipFile zipFile = new ZipFile(new File(classLoader.getResource("files/hw.zip").toURI()));
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {

            ZipEntry zipEntry = entries.nextElement();

            if (zipEntry.getName().equals("username.csv")) {
                try (InputStream is = zipFile.getInputStream(zipEntry)) {
                    CSVReader reader = new CSVReader(new InputStreamReader(is));
                    List<String[]> content = reader.readAll();
                    AssertionsForClassTypes.assertThat(content.get(0)).contains("Username; Identifier;First name;Last name");
                }
            }
        }
    }

    @Test
    void homeWorkPdfTest() throws Exception {
        ZipFile zipFile = new ZipFile(new File(classLoader.getResource("files/hw.zip").toURI()));
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {

            ZipEntry zipEntry = entries.nextElement();

            if (zipEntry.getName().equals("junit-user-guide-5.8.2.pdf")) {
                try (InputStream is = zipFile.getInputStream(zipEntry)) {
                    PDF pdf = new PDF(is);
                    AssertionsForClassTypes.assertThat(pdf.author).contains("Sam Brannen");
                }
            }
        }
    }

    @Test
    void homeWorkJsonTest() throws Exception {
        ZipFile zipFile = new ZipFile(new File(classLoader.getResource("files/hw.zip").toURI()));
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {

            ZipEntry zipEntry = entries.nextElement();

            if (zipEntry.getName().equals("hw1.json")) {
                try (InputStream is = zipFile.getInputStream(zipEntry)) {
                    Gson gson = new Gson();
                    String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    Me jsonObject = gson.fromJson(json, Me.class);
                    AssertionsForClassTypes.assertThat(jsonObject.name).isEqualTo("Yuriy");
                    AssertionsForClassTypes.assertThat(jsonObject.address.street).isEqualTo("Mira");
                }
            }
        }
    }
}



