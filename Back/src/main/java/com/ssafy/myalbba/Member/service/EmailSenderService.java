package com.ssafy.myalbba.Member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender mailSender;
    private final VerificationCodeService verificationCodeService;
    
    // 사장님 인증 버튼 누르면 사장님께 인증 코드 발송

    // 인증 코드
    public static String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    // 메시지 전송
    public void sendEmail(String ownerEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("yoojung0709@gmail.com");
        message.setTo(ownerEmail);
        message.setSubject("인증번호입니다.");

        String code = generateCode(); // 인증코드 발급
        verificationCodeService.saveCode(ownerEmail, code);
        message.setText(code);

        mailSender.send(message);
    }

    public void sendEmailPaydayAlarm(String ownerEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("yoojung0709@gmail.com");
        message.setTo(ownerEmail);
        message.setSubject("월급 정산 알람입니다.");
        message.setText("월급 정산 날짜입니다. 월급 정산을 다 마치셨습니까?");

        mailSender.send(message);
    }

    public void sendEmailPayCheck(String workerEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("yoojung0709@gmail.com");
        message.setTo(workerEmail);
        message.setSubject("월급 정산 완료 알람입니다.");
        message.setText("사장님이 월급 정산을 완료하였습니다");

        mailSender.send(message);
    }

}
