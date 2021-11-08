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
@Table ( name ="clientreservation" , schema = "")
public class Clientreservation  implements Serializable {

	private static final long serialVersionUID =  7967231899192326399L;

	@Id
   	@Column(name = "clientuserid" )
	private Long clientuserid;

   	@Column(name = "owneruserid" )
	private Long owneruserid;

   	@Column(name = "parkingspotnumber" )
	private Long parkingspotnumber;

   	@Column(name = "timeofstart" )
	private Date timeofstart;

   	@Column(name = "duration" )
	private Long duration;

   	@Column(name = "recurring" )
	private Boolean recurring;

	public Long getClientuserid() {
		return this.clientuserid;
	}

	public void setClientuserid(Long clientuserid) {
		this.clientuserid = clientuserid;
	}

	public Long getOwneruserid() {
		return this.owneruserid;
	}

	public void setOwneruserid(Long owneruserid) {
		this.owneruserid = owneruserid;
	}

	public Long getParkingspotnumber() {
		return this.parkingspotnumber;
	}

	public void setParkingspotnumber(Long parkingspotnumber) {
		this.parkingspotnumber = parkingspotnumber;
	}

	public Date getTimeofstart() {
		return this.timeofstart;
	}

	public void setTimeofstart(Date timeofstart) {
		this.timeofstart = timeofstart;
	}

	public Long getDuration() {
		return this.duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Boolean getRecurring() {
		return this.recurring;
	}

	public void setRecurring(Boolean recurring) {
		this.recurring = recurring;
	}

	@Override
	public String toString() {
		return "TpApiConfig{" +
				"clientuserid='" + clientuserid + '\'' +
				"owneruserid='" + owneruserid + '\'' +
				"parkingspotnumber='" + parkingspotnumber + '\'' +
				"timeofstart='" + timeofstart + '\'' +
				"duration='" + duration + '\'' +
				"recurring='" + recurring + '\'' +
				'}';
	}

}
