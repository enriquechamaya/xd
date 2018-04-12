package trismegistoplanilla.sqlserverdao;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import trismegistoplanilla.beans.Correo;
import trismegistoplanilla.dao.CorreoDAO;

public class CorreoSqlserverDAO implements CorreoDAO {

    @Override
    public boolean enviarCorreo(Correo c) {
        boolean envio = false;

        try {
            Properties p = new Properties();
            p.put("mail.smtp.host", "smtp.gmail.com");
            p.setProperty("mail.smtp.starttls.enable", "true");
            p.setProperty("mail.smtp.port", "587");
            p.setProperty("mail.smtp.user", "tplanilla@gmail.com");
            p.setProperty("mail.smtp.auth", "true");

            Session session = Session.getInstance(p, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("tplanilla@gmail.com", "aylffncgufqnockh");
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("tplanilla@gmail.com", "TRISMEGISTO - PLANILLA"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(c.getDestino()));
            message.setSubject(c.getAsunto());
            message.setContent(c.getMensaje(), "text/html; chartset=utf-8");

            Transport.send(message);

            envio = true;
        } catch (UnsupportedEncodingException | MessagingException e) {
            System.out.println("NO SE PUDO ENVIAR EL MENSAJE " + e.getMessage());
            envio = false;
        }
        return envio;
    }

//    public static void main(String[] args) throws UnsupportedEncodingException, MessagingException {
//        CorreoSqlserverDAO metodo = new CorreoSqlserverDAO();
//        Correo c = new Correo();
//        c.setDestino("echamaya.ti@sacooliveros.edu.pe");
//        c.setAsunto("BIENVENIDO PAPU!");
//        c.setMensaje("<div style='background:red;'><h1>TU CODIGO DE VERIFICACION :  ACM1PT</h1></div>");
//        System.out.println(metodo.enviarCorreo(c));
//    }
}
