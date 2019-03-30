package Classess;

/**
 * Created by b on 26/3/19.
 */

public class ListDetailsModel {

    public String icon;
    public String description;
    public  String services;
    public String text;
    public String etc;



    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }

    public ListDetailsModel(String icon, String description, String services, String text, String etc) {

        this.icon = icon;
        this.description = description;
        this.services = services;
        this.text = text;
        this.etc = etc;
    }

    public ListDetailsModel() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


}
