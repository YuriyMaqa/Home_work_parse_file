package guru;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import guru.domain.Me;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FilesParsingTest {

    ClassLoader classLoader = getClass().getClassLoader();

    @BeforeAll
    static void setup() {
        Configuration.browserSize = "1980x1020";
    }

    @Test
    void parsePdfTest() throws Exception {
        Selenide.open("https://junit.org/junit5/docs/current/user-guide/");
        File pdfDownload = $(byText("PDF download")).download();
        PDF pdf = new PDF(pdfDownload);
        assertThat(pdf.author).contains("Sam Brannen");
    }

    @Test
    void parseXlsTest() throws Exception {
        Selenide.open("http://romashka2008.ru");
        File xlsDownload = $(".top-menu__items a[href*='prajs_ot']").download();
        XLS xls = new XLS(xlsDownload);
        xls.excel
                .getSheetAt(0)
                .getRow(11)
                .getCell(1)
                .getStringCellValue().contains("Сахалинская обл, Южно-Сахалинск");
    }

    @Test
    void parseCsvTest() throws Exception {

        try (InputStream is = classLoader.getResourceAsStream("files/username.csv");
             CSVReader reader = new CSVReader(new InputStreamReader(is))) {
            List<String[]> content = reader.readAll();
            assertThat(content.get(0)).contains("Username; Identifier;First name;Last name");
        }
    }

    @Test
    void jsonTest() throws Exception {
        Gson gson = new Gson();
        try (InputStream is = classLoader.getResourceAsStream("files/hw1.json")) {
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            assertThat(jsonObject.get("name").getAsString()).isEqualTo("Yuriy");
            assertThat(jsonObject.get("address").getAsJsonObject().get("street").getAsString()).isEqualTo("Mira");
        }
    }

    @Test
    void jsonTypeTest() throws Exception {
        Gson gson = new Gson();
        try (InputStream is = classLoader.getResourceAsStream("files/hw1.json")) {
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Me jsonObject = gson.fromJson(json, Me.class);
            assertThat(jsonObject.name).isEqualTo("Yuriy");
            assertThat(jsonObject.address.street).isEqualTo("Mira");
            assertThat(jsonObject.favoriteMusic.get(1)).isEqualTo("Nirvana");
        }
    }
}
