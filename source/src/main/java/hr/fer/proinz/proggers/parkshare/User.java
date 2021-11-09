package hr.fer.proinz.proggers.parkshare;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import java.io.Serializable;

/**
 * @Description  
 * @Author  Marin
 * @Date 2021-11-08 
 */

@Entity
@Table ( name ="User" , schema = "")
public class User  implements Serializable {

	private static final long serialVersionUID =  8609307306679654061L;

	@Id
   	@Column(name = "userid" )
	private Long userid;

   	@Column(name = "username" )
	private String username;

   	@Column(name = "userfirstname" )
	private String userfirstname;

   	@Column(name = "usersurname" )
	private String usersurname;

   	@Column(name = "useremail" )
	private String useremail;

   	@Column(name = "temppassword" )
	private String temppassword;

   	@Column(name = "usertype" )
	private String usertype;

   	@Column(name = "confirmed" )
	private Boolean confirmed;

	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserfirstname() {
		return this.userfirstname;
	}

	public void setUserfirstname(String userfirstname) {
		this.userfirstname = userfirstname;
	}

	public String getUsersurname() {
		return this.usersurname;
	}

	public void setUsersurname(String usersurname) {
		this.usersurname = usersurname;
	}

	public String getUseremail() {
		return this.useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public String getTemppassword() {
		return this.temppassword;
	}

	public void setTemppassword(String temppassword) {
		this.temppassword = temppassword;
	}

	public String getUsertype() {
		return this.usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public Boolean getConfirmed() {
		return this.confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	@Override
	public String toString() {
		return "TpApiConfig{" +
				"userid='" + userid + '\'' +
				"username='" + username + '\'' +
				"userfirstname='" + userfirstname + '\'' +
				"usersurname='" + usersurname + '\'' +
				"useremail='" + useremail + '\'' +
				"temppassword='" + temppassword + '\'' +
				"usertype='" + usertype + '\'' +
				"confirmed='" + confirmed + '\'' +
				'}';
	}

}
