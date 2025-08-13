package pe.gob.casadelaliteratura.biblioteca.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(JavaMailSender javaMailSender,
                        SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String correoDestino, String subject,
                          String nombreUsuario, String codigoPrestamo,
                          String fechaPrestamo, String fechaVencimiento) throws MessagingException {

        MimeMessage mensaje = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

        helper.setTo(correoDestino);
        helper.setSubject(subject);

        // Procesar template HTML
        String htmlContent = processTemplate("body-email",
                nombreUsuario, codigoPrestamo, fechaPrestamo, fechaVencimiento
        );

        helper.setText(htmlContent, true);

        javaMailSender.send(mensaje);

    }

    private String processTemplate(String templateName, String nombreUsuario,
                                   String codigoPrestamo, String fechaPrestamo,
                                   String fechaVencimiento) {
        Context context = new Context();
        context.setVariable("nombreUsuario", nombreUsuario);
        context.setVariable("codigoPrestamo", codigoPrestamo);
        context.setVariable("fechaPrestamo", fechaPrestamo);
        context.setVariable("fechaVencimiento", fechaVencimiento);

        return templateEngine.process(templateName, context);
    }

}
