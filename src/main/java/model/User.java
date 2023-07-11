package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

public class User {
    private String username;
    private String password;
    private String profileImageAddress;
    private boolean mute;
    private DifficultyLevel difficultyLevel;
    private int score;
    private boolean bAndW;
    private boolean persian;
    private int numberOfBalls;
    private int ballSet;
    private Long finishTime;
    public User (String username, String password) {
        this.username = username;
        this.password = password;
        difficultyLevel = DifficultyLevel.MEDIUM;
        score = 0;
        mute = false;
        bAndW = false;
        persian = false;
        numberOfBalls = 8;
        ballSet = 1;
        finishTime = 0L;
        profileImageAddress = User.class.getResource("/Images/Default-male.jpg").toString();
    }
    
    public boolean isbAndW () {
        return bAndW;
    }
    
    public void setbAndW (boolean bAndW) {
        this.bAndW = bAndW;
    }
    
    public boolean isPersian () {
        return persian;
    }
    
    public void setPersian (boolean persian) {
        this.persian = persian;
    }
    
    public boolean isMute () {
        return mute;
    }
    
    public void setMute (boolean mute) {
        this.mute = mute;
    }
    
    public Long getFinishTime () {
        return finishTime;
    }
    
    public void setFinishTime (Long finishTime) {
        this.finishTime = finishTime;
    }
    
    public DifficultyLevel getDifficultyLevel () {
        return difficultyLevel;
    }
    
    public void setDifficultyLevel (DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
    
    public int getScore () {
        return score;
    }
    
    public void setScore (int score) {
        this.score = score;
    }
    
    public void setPassword (String password) {
        this.password = password;
    }
    
    public String getUsername () {
        return username;
    }
    
    public void setUsername (String username) {
        this.username = username;
    }
    
    public int getNumberOfBalls () {
        return numberOfBalls;
    }
    
    public void setNumberOfBalls (int numberOfBalls) {
        this.numberOfBalls = numberOfBalls;
    }
    
    public Image getProfileImage () {
        try {
            URL url = new URL(profileImageAddress);
            return new Image(url.toExternalForm());
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    public void setProfileImage (Image profileImage) {
        try {
            //file:/C:/University/AP/Training02/target/classes/Images/Default-female.jpg
//            String temp =  getClass().getClassLoader().getResource("/Images/" + username + "Profile.jpg").toString();
            File outputfile = new File("C:\\University\\AP\\Training02\\src\\main\\resources\\Images\\" + username + "Profile.jpg");
            outputfile.delete();
            outputfile.createNewFile();
            ImageIO.write(SwingFXUtils.fromFXImage(profileImage, null), "jpg", outputfile);
            profileImageAddress = User.class.getResource("/Images/" + username + "Profile.jpg").toString();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public int getBallSet () {
        return ballSet;
    }
    
    public void setBallSet (int ballSet) {
        this.ballSet = ballSet;
    }
    
    public String getProfileImageAddress () {
        return profileImageAddress;
    }
    
    public void setProfileImageAddress (String profileImageAddress) {
        this.profileImageAddress = profileImageAddress;
    }
    
    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) && password.equals(user.password) && profileImageAddress.equals(user.profileImageAddress);
    }
    
    @Override
    public int hashCode () {
        return Objects.hash(username, password, profileImageAddress);
    }
    
    public boolean checkPassword (String password) {
        return password.equals(this.password);
    }
}
