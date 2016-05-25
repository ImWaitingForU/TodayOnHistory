package com.chan.today.bean;

/**
 * Created by Chan on 2016/5/23.
 *
 * 历史上的今天bean
 */
public class TodayBean {

	private String day; // 日
	private String des; // 描述
	private String id; // 事件Id
	private String lunar; // 事件农历
	private String month; // 事件月份
	private String pic; // 图片路径
	private String title;// 标题
	private String year;// 年份

	@Override
	public String toString() {
		return "day=" + day + " des=" + des + " id=" + id + " lunar=" + lunar
				+ " month=" + month + " pic=" + pic + " title=" + title + " "
				+ "year=" + year + "\n";
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLunar() {
		return lunar;
	}

	public void setLunar(String lunar) {
		this.lunar = lunar;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
}
