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
@Table ( name ="parking" , schema = "")
public class Parking  implements Serializable {

	private static final long serialVersionUID =  1015615279523203616L;

	@Id
   	@Column(name = "userid" )
	private Long userid;

   	@Column(name = "parkingname" )
	private String parkingname;

   	@Column(name = "parkingphoto" )
	private String parkingphoto;

   	@Column(name = "hourlyprice" )
	private String hourlyprice;

   	@Column(name = "description" )
	private String description;

	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getParkingname() {
		return this.parkingname;
	}

	public void setParkingname(String parkingname) {
		this.parkingname = parkingname;
	}

	public String getParkingphoto() {
		return this.parkingphoto;
	}

	public void setParkingphoto(String parkingphoto) {
		this.parkingphoto = parkingphoto;
	}

	public String getHourlyprice() {
		return this.hourlyprice;
	}

	public void setHourlyprice(String hourlyprice) {
		this.hourlyprice = hourlyprice;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "TpApiConfig{" +
				"userid='" + userid + '\'' +
				"parkingname='" + parkingname + '\'' +
				"parkingphoto='" + parkingphoto + '\'' +
				"hourlyprice='" + hourlyprice + '\'' +
				"description='" + description + '\'' +
				'}';
	}

}
