package edu.eci.arsw.exams.moneylaunderingapi.service;

public class MoneyLaunderingException extends Exception{

    private static final long serialVersionUID = 1L;

    public MoneyLaunderingException(String message) {
        super(message);
    }
}