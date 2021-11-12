package hr.fer.proinz.proggers.parkshare.dto;

import hr.fer.proinz.proggers.parkshare.model.UserModel;

public class UserDTO {

    private Integer userid;
    private String username;
    private String userfirstname;
    private String usersurname;
    private String usermail;
    private String temppassword;
    private String usertype;
    private boolean confirmed;

    public UserDTO(String username, String userfirstname, String usersurname, String usermail, String temppassword, String usertype, boolean confirmed) {
        this.username = username;
        this.userfirstname = userfirstname;
        this.usersurname = usersurname;
        this.usermail = usermail;
        this.temppassword = temppassword;
        this.usertype = usertype;
        this.confirmed = confirmed;
    }
    public UserDTO(UserModel userModel){
        this(userModel.getName(), userModel.getFirstName(), userModel.getSurname(), userModel.getEmail(), "", userModel.getType(), userModel.getConfirmed());
        setUserid(userModel.getId());
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserfirstname() {
        return userfirstname;
    }

    public void setUserfirstname(String userfirstname) {
        this.userfirstname = userfirstname;
    }

    public String getUsersurname() {
        return usersurname;
    }

    public void setUsersurname(String usersurname) {
        this.usersurname = usersurname;
    }

    public String getUsermail() {
        return usermail;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }

    public String getTemppassword() {
        return temppassword;
    }

    public void setTemppassword(String temppassword) {
        this.temppassword = temppassword;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
