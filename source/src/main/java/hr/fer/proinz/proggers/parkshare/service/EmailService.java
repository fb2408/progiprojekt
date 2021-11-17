//package hr.fer.proinz.proggers.parkshare.service;
//
//import lombok.AllArgsConstructor;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//
//@Service
//@AllArgsConstructor
//public class EmailService implements EmailSender{
//
//    private final JavaMailSender mailSender;
//
//    @Override
//    public void send(String to, String text) {
//        try {
//            MimeMessage mimeMessage = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
//            helper.setTo(to);
//            helper.setSubject("Confirm your reservation");
//            helper.setText(text, true);
//            helper.setFrom("parhShare@gmail.com"); //temporarily
//            mailSender.send(mimeMessage);
//
//        } catch (MessagingException exc){
//            //exception wrapping
//            throw new IllegalStateException("failed to send email");
//        }
//    }
//}
