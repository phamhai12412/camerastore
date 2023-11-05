package ra.security.service.impl.usersevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage mimeMessage = new SimpleMailMessage();

        mimeMessage.setTo(to);
        mimeMessage.setSubject(subject);
        mimeMessage.setText(text);


        javaMailSender.send(mimeMessage);
    }
}
