package com.clothes.noc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String serverEmail;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Async
    public void sendMail(String toEmail, String subject, Map<String, Object> variables, String template) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom(serverEmail);
            messageHelper.setSubject(subject);
            messageHelper.setTo(toEmail);

            messageHelper.setText(generateBody(variables, template), true);
        };

        javaMailSender.send(preparator);
        log.info("Send a email");
    }

    public String generateBody(Map<String, Object> variables, String template){
        Context context = new Context();
        for (Map.Entry<String, Object> entry : variables.entrySet()){
            context.setVariable(entry.getKey(), entry.getValue());
        }
        return templateEngine.process(template, context);

    }
}
