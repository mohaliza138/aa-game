package model;

import java.util.regex.Pattern;

public enum Validations {
    PASSWORD("[^\\s]+");
    private final String regex;
    private Validations (String regex) {
        this.regex = regex;
    }
    
    public boolean check (String string){
        return Pattern.matches(regex, string);
    }
}
