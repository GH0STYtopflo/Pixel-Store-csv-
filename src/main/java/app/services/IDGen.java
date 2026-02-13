package app.services;

import java.security.SecureRandom;
import java.time.LocalDate;

public class IDGen {

    public static int gen(){
        LocalDate today = LocalDate.now();
        int ID = (today.getYear() * 10000 + today.getMonthValue() * 100 + today.getDayOfMonth()) * 100;
        ID += random();
        return ID;
    }

    private static int random(){
        SecureRandom securerandom = new SecureRandom();
        int random = securerandom.nextInt(100);
        return random;
    }
}
