package com.opencart.automation.utility.pojoClasses;

import java.util.Arrays;

public class Property {

	private String x;
	private String y;
	private String activepod;
	private String lnk;
	private String id;
	private String name;
	private String addr;
	private String boro;
	private String zip;
	private String status;
	private String opening;
	private String wait;
	private String updated;
	private String labelpos;
	
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getActivepod() {
		return activepod;
	}
	public void setActivepod(String activepod) {
		this.activepod = activepod;
	}
	public String getLnk() {
		return lnk;
	}
	public void setLnk(String lnk) {
		this.lnk = lnk;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getBoro() {
		return boro;
	}
	public void setBoro(String boro) {
		this.boro = boro;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOpening() {
		return opening;
	}
	public void setOpening(String opening) {
		this.opening = opening;
	}
	public String getWait() {
		return wait;
	}
	public void setWait(String wait) {
		this.wait = wait;
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	public String getLabelpos() {
		return labelpos;
	}
	public void setLabelpos(String labelpos) {
		this.labelpos = labelpos;
	}
	
	@Override
	public String toString() {
		return "Properties [x=" + x + ", y=" + y + ", activepod=" + activepod + ", lnk=" + lnk + ", id=" + id
				+ ", name=" + name + ", addr=" + addr + ", boro=" + boro + ", zip=" + zip + ", status=" + status
				+ ", opening=" + opening + ", wait=" + wait + ", updated=" + updated + ", labelpos=" + labelpos + "]";
	}
	
}
