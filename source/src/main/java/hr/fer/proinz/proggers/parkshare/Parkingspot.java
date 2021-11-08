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
@Table ( name ="parkingspot" , schema = "")
public class Parkingspot  implements Serializable {

	private static final long serialVersionUID =  9178266223829810878L;

	@Id
   	@Column(name = "userid" )
	private Long userid;

   	@Column(name = "parkingspotnumber" )
	private Long parkingspotnumber;

   	@Column(name = "parkingspottype" )
	private String parkingspottype;

   	@Column(name = "canbereserved" )
	private Boolean canbereserved;

   	@Column(name = "description" )
	private String description;

   	@Column(name = "point1" )
	private Long point1;

   	@Column(name = "point2" )
	private Long point2;

   	@Column(name = "point3" )
	private Long point3;

   	@Column(name = "point4" )
	private Long point4;

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

	public String getParkingspottype() {
		return this.parkingspottype;
	}

	public void setParkingspottype(String parkingspottype) {
		this.parkingspottype = parkingspottype;
	}

	public Boolean getCanbereserved() {
		return this.canbereserved;
	}

	public void setCanbereserved(Boolean canbereserved) {
		this.canbereserved = canbereserved;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getPoint1() {
		return this.point1;
	}

	public void setPoint1(Long point1) {
		this.point1 = point1;
	}

	public Long getPoint2() {
		return this.point2;
	}

	public void setPoint2(Long point2) {
		this.point2 = point2;
	}

	public Long getPoint3() {
		return this.point3;
	}

	public void setPoint3(Long point3) {
		this.point3 = point3;
	}

	public Long getPoint4() {
		return this.point4;
	}

	public void setPoint4(Long point4) {
		this.point4 = point4;
	}

	@Override
	public String toString() {
		return "TpApiConfig{" +
				"userid='" + userid + '\'' +
				"parkingspotnumber='" + parkingspotnumber + '\'' +
				"parkingspottype='" + parkingspottype + '\'' +
				"canbereserved='" + canbereserved + '\'' +
				"description='" + description + '\'' +
				"point1='" + point1 + '\'' +
				"point2='" + point2 + '\'' +
				"point3='" + point3 + '\'' +
				"point4='" + point4 + '\'' +
				'}';
	}

}
