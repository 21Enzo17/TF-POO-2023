package ar.edu.unju.fi.tp9.util;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class EmailService {
    static Logger logger = LogManager.getLogger(EmailService.class);
    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String emisor, String para, String tema, String cuerpo, InputStreamSource imagen) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(emisor);
        helper.setTo(para);
        helper.setSubject(tema);
        helper.setText(cuerpo, true);
        helper.addInline("logo", imagen, "image/png");
        mailSender.send(message);
        logger.debug("Correo enviado a: " + para);
    }


}