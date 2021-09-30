package br.com.feltex.whatsapbot.controller;

import br.com.feltex.whatsapbot.modelo.Mensagem;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/zap-zap")
@Slf4j
public class EnviaMensagem {

    private final WebDriver webDriver;

    public EnviaMensagem(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @PostMapping
    public ResponseEntity receberMensagem(@RequestBody Mensagem mensagem) {
        mensagem.getContatos().forEach(contato -> enviarMensagem(contato, mensagem.getConteudo()));
        return new ResponseEntity<>("Envio de mensagens concluído!", HttpStatus.OK);
    }

    private void enviarMensagem(String contato, String conteudo) {
        try {
            var elmentoContato = findContato(contato);
            elmentoContato.click();

            var caixaMensagem = findCaixaTexto();
            caixaMensagem.sendKeys(conteudo);
            caixaMensagem.sendKeys(Keys.RETURN);

        } catch (Exception e) {
            log.error("Não foi possível enviar a mensagem para o contato {}", contato, e);
        }
    }

    private WebElement findContato(String nomeContato) {
        var xPathContato = "//*[@id=\"pane-side\"]/*//span[@title='" + nomeContato + "']";
        return webDriver.findElement(By.xpath(xPathContato));
    }

    private WebElement findCaixaTexto() {
        var xPathCaixaTexto = "//*[@id=\"main\"]/footer//*[@role='textbox']";
        return webDriver.findElement(By.xpath(xPathCaixaTexto));
    }
}
