package ayp.aug.contact.model;

import java.util.UUID;

/**
 * Created by Hattapong on 8/9/2016.
 */

public class Contact {
    UUID uuid;
    String name;
    String telephoneNo;
    String email;

    public Contact() {
        this.uuid = UUID.randomUUID();
    }

    public Contact(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephoneNo() {
        return telephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoFilename(){
        return "IMG_" + getUuid().toString() + ".jpg";
    }
}
