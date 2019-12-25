package parser.main;

import java.math.BigDecimal;

public class Item {

	private String student_name;
	private Integer student_group;
	private Integer student_number;
	private String date;
	private String data_source;
	private String tonality;
	private String filename;

	public String getStudent_name() {
		return student_name;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public Integer getStudent_group() {
		return student_group;
	}

	public void setStudent_group(Integer student_group) {
		this.student_group = student_group;
	}

	public Integer getStudent_number() {
		return student_number;
	}

	public void setStudent_number(Integer student_number) {
		this.student_number = student_number;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getData_source() {
		return data_source;
	}

	public void setData_source(String data_source) {
		this.data_source = data_source;
	}

	public String getTonality() {
		return tonality;
	}

	public void setTonality(String tonality) {
		this.tonality = tonality;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
