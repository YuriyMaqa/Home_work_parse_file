package guru;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SelenideFilesTest {

    @Test
    void selenideDownloadTest() throws Exception {
        Selenide.open("https://github.com/junit-team/junit5/blob/main/README.md");
        File downloadedFile = $("#raw-url").download();
        try (InputStream is = new FileInputStream(downloadedFile)) {
            assertThat(new String(is.readAllBytes(), UTF_8)).contains("This repository is the home of the next generation of JUnit");
        }
        String readString = Files.readString(downloadedFile.toPath(), UTF_8);
    }

    @Test
    void uploadSelenideTest() {
        open("https://the-internet.herokuapp.com/upload");
        $("input[type='file']").uploadFromClasspath("files/1.txt");
        $("#file-submit").click();
        $("div.example").shouldHave(text("File Uploaded!"));
        $("#uploaded-files").shouldHave(text("1.txt"));

        //  bad practice         .uploadFile(new File("C:\\Users\\mewo4\\IdeaProjects\\lesson_work_with_file\\src\\test\\resources\\files\\1.txt"));
    }

}
