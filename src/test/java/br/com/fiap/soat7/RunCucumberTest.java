package br.com.fiap.soat7;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/resources/features", // Caminho para os arquivos .feature
        glue = "br/com/fiap/soat7/steps",      // Caminho para as classes de Step Definitions
        plugin = {"pretty", "html:target/cucumber-report.html"} // Formatos de relatório
)
public class RunCucumberTest {
}