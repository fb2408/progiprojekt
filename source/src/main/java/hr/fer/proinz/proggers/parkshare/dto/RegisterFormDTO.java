package hr.fer.proinz.proggers.parkshare.dto;

public class RegisterFormDTO {
    private Integer userId;
    private String username;
    private String userFirstName;
    private String userSurname;
    private String userMail;
    private String password;
    private boolean isOwner;
    private boolean confirmed;
    private String iban;
//    private MultipartFile idPicture;
//    private MultipartFile profilePicture;

    public RegisterFormDTO(String username, String userfirstname, String usersurname, String usermail, String temppassword, boolean isOwner, boolean confirmed, String iban) {
        this.username = username;
        this.userFirstName = userfirstname;
        this.userSurname = usersurname;
        this.userMail = usermail;
        this.password = temppassword;
        this.isOwner = isOwner;
        this.confirmed = confirmed;
        this.iban = iban;
    }

    public RegisterFormDTO() {

    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userid) {
        this.userId = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userfirstname) {
        this.userFirstName = userfirstname;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

}
