package ar.edu.unju.fi.tp9.service;


import org.apache.log4j.Logger;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class IEmailService {
    static Logger logger = Logger.getLogger(IEmailService.class);
    private JavaMailSender mailSender;

    public IEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    /**
     * Este metodo se encarga de setear y enviar el correo, seteando en true 
     * la etiqueta de contenido html, ademas de enviando la imagen para que 
     * el html pueda leerla en el correo.
     * @param emisor
     * @param para
     * @param tema
     * @param cuerpo
     * @param imagen
     * @throws MessagingException
     */
    public void send(String emisor, String para, String tema, String cuerpo, InputStreamSource imagen) throws MessagingException {
        logger.info("Enviando correo a:" + para);
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
