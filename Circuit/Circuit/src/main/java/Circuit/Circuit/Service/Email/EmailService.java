package Circuit.Circuit.Service.Email;

import Circuit.Circuit.Model.Pedido;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    public void enviarEmailPedidoPersonalizado(Pedido pedido) {
        try {
            Context context = new Context();
            context.setVariable("pedido", pedido);
            context.setVariable("dataFormatada", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            String conteudoHtml = templateEngine.process("emails/pedido-email", context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(pedido.getFornecedor().getEmail());
            helper.setSubject("Circuit ERP - Atualização de Pedido: #" + pedido.getCodigo());
            helper.setText(conteudoHtml, true);
            helper.setFrom("circuiterp@gmail.com");
            helper.addInline("logoAdmin", new org.springframework.core.io.ClassPathResource("static/images/Circuit-logo-branca.png"));

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.err.println("Falha ao enviar e-mail do pedido " + pedido.getCodigo() + ": " + e.getMessage());
        }
    }
}