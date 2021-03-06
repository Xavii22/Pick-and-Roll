package com.pickandroll.erp.utils;

import java.util.Locale;
import java.util.Random;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Utils {

    // Retorna una contraseña encriptada
    public String encrypPasswd(String passwd) {
        BCryptPasswordEncoder e = new BCryptPasswordEncoder();
        return e.encode(passwd);
    }

    // Comprueba si es un DNI válido
    public boolean checkDni(String dni) {
        String dniNum = dni.substring(0, dni.length() - 1);
        if (dni.length() < 9 || !isNumeric(dniNum)) {
            return false;
        }
        char dniLetra = Character.toUpperCase(dni.charAt(dni.length() - 1));
        String lletraDni = "TRWAGMYFPDXBNJZSQVHLCKE";

        return dniLetra == (lletraDni.charAt(Integer.parseInt(dniNum) % 23));
    }

    // Le pasa un string y comprueba si es completamente numérico
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Devuelve un String del message.properties
    public String alert(String msgCode) {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/messages"); // src/main/resources
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource.getMessage(msgCode, null, Locale.ENGLISH);
    }

    // Genera un token de 30 caracteres
    public String genToken() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase();
        String nums = "1234567890";

        char[] alphanum = (upper + lower + nums).toCharArray();

        String token = "";
        Random random = new Random();
        
        for (int i = 0; i < 30; i++) {
            token += alphanum[random.nextInt(alphanum.length)];
        }
        
        return token;
    }
}
