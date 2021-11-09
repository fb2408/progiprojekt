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
@Table ( name ="client" , schema = "")
public class Client  implements Serializable {

	private static final long serialVersionUID =  7434614269030253777L;

	@Id
   	@Column(name = "userid" )
	private Long userid;

   	@Column(name = "walletbalance" )
	private String walletbalance;

	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getWalletbalance() {
		return this.walletbalance;
	}

	public void setWalletbalance(String walletbalance) {
		this.walletbalance = walletbalance;
	}

	@Override
	public String toString() {
		return "TpApiConfig{" +
				"userid='" + userid + '\'' +
				"walletbalance='" + walletbalance + '\'' +
				'}';
	}

}
