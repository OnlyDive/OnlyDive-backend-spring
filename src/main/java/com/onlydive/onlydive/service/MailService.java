package com.onlydive.onlydive.service;

import com.onlydive.onlydive.exceptions.SpringOnlyDiveException;
import com.onlydive.onlydive.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    @Async
    public void sendMail(NotificationEmail notificationEmail, String mailType) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("noreply@OnlyDive.com");
            messageHelper.setTo(notificationEmail.recipient());
            messageHelper.setSubject(notificationEmail.subject());
            messageHelper.setText(mailContentBuilder.build(notificationEmail.body()));
        };
        try {
            mailSender.send(messagePreparator);
            log.info(mailType+" email sent!");
        } catch (MailException e) {
            throw new SpringOnlyDiveException("Exception occurred when sending mail to " + notificationEmail.recipient(), e);
        }
    }
}