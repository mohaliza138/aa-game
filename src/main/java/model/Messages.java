package model;

public enum Messages {
    SIGN_IN("Sign in menu"),
    SIGN_UP("Sing up menu"),
    START("Start menu"),
    MAIN_MENU("Main menu"),
    SETTINGS_MENU("Settings menu"),
    LEADERBOARD("Leaderboard menu"),
    PROFILE_MENU("Profile menu"),
    USED_USERNAME("Username already in use"),
    NO_USER_WITH_THIS_ID("There's no user with the id given"),
    WRONG_PASSWORD("Wrong password"),
    INVALID_IMAGE_SCALE("The selected image is not square. Please select a square image"),
    INVALID_PASSWORD("Invalid password format"),
    EMPTY_FIELD("You should fill all the text fields"),
    SUCCESS("Success");
    
    public final String text;
    private Messages (String string) {
        this.text = string;
    }
}
