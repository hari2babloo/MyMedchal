package Classess;

/**
 * Created by b on 26/3/19.
 */

public class Signup {

    public String username;
    public String email;
    public  String age;
    public String userid;
    public String imgurl;
    public String gender;
    public String xxx;

    public Signup(String username, String email, String age, String userid, String imgurl, String gender) {
        this.username = username;
        this.email = email;
        this.age = age;
        this.userid = userid;
        this.imgurl = imgurl;
        this.gender = gender;
    }

    public Signup(String userid, String imgurl) {
        this.username = username;
        this.email = email;
        this.age = age;
        this.userid = userid;
        this.imgurl = imgurl;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getXxx() {
        return xxx;
    }

    public void setXxx(String xxx) {
        this.xxx = xxx;
    }

    public Signup(String xxx) {
        this.xxx = xxx;

    }

    public Signup() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


}
