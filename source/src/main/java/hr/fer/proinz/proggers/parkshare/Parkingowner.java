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
@Table ( name ="parkingowner" , schema = "")
public class Parkingowner  implements Serializable {

	private static final long serialVersionUID =  6862798747100965630L;

	@Id
   	@Column(name = "userid" )
	private Long userid;

   	@Column(name = "iban" )
	private String iban;

   	@Column(name = "idpicture" )
	private String idpicture;

	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getIdpicture() {
		return this.idpicture;
	}

	public void setIdpicture(String idpicture) {
		this.idpicture = idpicture;
	}

	@Override
	public String toString() {
		return "TpApiConfig{" +
				"userid='" + userid + '\'' +
				"iban='" + iban + '\'' +
				"idpicture='" + idpicture + '\'' +
				'}';
	}

}
