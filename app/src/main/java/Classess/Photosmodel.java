package Classess;

/**
 * Created by b on 25/4/19.
 */


public class Photosmodel {

    public String imgurl;
    public String description;
    public long timestanp;
    public String userid;
    public String username;
    public String profilepic;
    public String nameofbusiness;
    public String location;

    public Photosmodel(String imgurl) {
        this.imgurl = imgurl;
    }

    public String key;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimestanp() {
        return timestanp;
    }

    public void setTimestanp(long timestanp) {
        this.timestanp = timestanp;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getNameofbusiness() {
        return nameofbusiness;
    }

    public void setNameofbusiness(String nameofbusiness) {
        this.nameofbusiness = nameofbusiness;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Photosmodel() {

    }


}
