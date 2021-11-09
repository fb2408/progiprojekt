package hr.fer.proinz.proggers.parkshare;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description  
 * @Author  Marin
 * @Date 2021-11-08 
 */

@Entity
@Table ( name ="parkingspotoccupancy" , schema = "")
public class Parkingspotoccupancy  implements Serializable {

	private static final long serialVersionUID =  2996714736344928502L;

	@Id
   	@Column(name = "userid" )
	private Long userid;

   	@Column(name = "parkingspotnumber" )
	private Long parkingspotnumber;

   	@Column(name = "datefrom" )
	private Date datefrom;

   	@Column(name = "dateto" )
	private Date dateto;

   	@Column(name = "occupancy" )
	private Boolean occupancy;

	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getParkingspotnumber() {
		return this.parkingspotnumber;
	}

	public void setParkingspotnumber(Long parkingspotnumber) {
		this.parkingspotnumber = parkingspotnumber;
	}

	public Date getDatefrom() {
		return this.datefrom;
	}

	public void setDatefrom(Date datefrom) {
		this.datefrom = datefrom;
	}

	public Date getDateto() {
		return this.dateto;
	}

	public void setDateto(Date dateto) {
		this.dateto = dateto;
	}

	public Boolean getOccupancy() {
		return this.occupancy;
	}

	public void setOccupancy(Boolean occupancy) {
		this.occupancy = occupancy;
	}

	@Override
	public String toString() {
		return "TpApiConfig{" +
				"userid='" + userid + '\'' +
				"parkingspotnumber='" + parkingspotnumber + '\'' +
				"datefrom='" + datefrom + '\'' +
				"dateto='" + dateto + '\'' +
				"occupancy='" + occupancy + '\'' +
				'}';
	}

}
