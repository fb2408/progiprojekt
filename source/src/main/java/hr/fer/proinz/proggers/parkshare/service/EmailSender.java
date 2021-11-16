package hr.fer.proinz.proggers.parkshare.service;

public interface EmailSender {

    void send(String to, String text, String link);
}
