package classes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {


    private static Pattern pattern;
    private static Matcher matcher;
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";


    public static boolean validate(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length()>0 ? true: false;
    }

    
}

