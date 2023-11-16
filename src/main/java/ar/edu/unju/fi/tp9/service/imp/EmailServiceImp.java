package ar.edu.unju.fi.tp9.service.imp;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.tp9.exception.ManagerException;
import ar.edu.unju.fi.tp9.service.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class EmailServiceImp implements IEmailService {
	static Logger logger = Logger.getLogger(IEmailService.class);
    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImp(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Este metodo se encarga de setear y enviar el correo, seteando en true 
     * la etiqueta de contenido html, ademas de enviando la imagen para que 
     * el html pueda leerla en el correo. Envia una excepcion personalizada en caso
     * de no poder enviar el correo.
     * @param emisor
     * @param para
     * @param tema
     * @param cuerpo
     * @param imagen
     * @throws MessagingException
     * @catch MailException
     * @catch MessagingException
     * @catch IllegalArgumentException
     */
    @Override
    public void send(String emisor, String para, String tema, String cuerpo, InputStreamSource imagen) throws ManagerException {
        logger.info("Enviando correo a:" + para);
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(emisor);
            helper.setTo(para);
            helper.setSubject(tema);
            helper.setText(cuerpo, true);
            helper.addInline("logo", imagen, "image/png");
            mailSender.send(message);
        } catch (MailException e) {
            logger.error("Error al enviar el correo: " + e.getMessage());
            throw new ManagerException("Error al enviar el correo, por favor intente de nuevo más tarde.");
        } catch (MessagingException e) {
            logger.error("Error al configurar el mensaje: " + e.getMessage());
            throw new ManagerException("Error al configurar el mensaje, por favor verifique los datos ingresados.");
        } catch (IllegalArgumentException e) {
            logger.error("Argumento inválido: " + e.getMessage());
            throw new ManagerException("Uno o más de los argumentos proporcionados son inválidos. Por favor verifique los datos ingresados.");
        }
        logger.debug("Correo enviado a: " + para);
    }
}
