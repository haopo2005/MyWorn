

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class Url implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ԭʼurl��ֵ����������������
	private String oriUrl;
	
	public Url(String temp)
	{
		this.oriUrl = temp;
	}
	
	public String getOriUrl() {
		return oriUrl;
	}
	public void setOriUrl(String oriUrl) {
		this.oriUrl = oriUrl;
	}
	
}
