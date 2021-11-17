package hr.fer.proinz.proggers.parkshare.service;

import org.springframework.scheduling.annotation.EnableAsync;

public interface EmailSender {
    void send(String to, String name, String link);
}
