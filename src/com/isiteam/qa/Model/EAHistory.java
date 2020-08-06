package com.isiteam.qa.Model;

import java.util.ArrayList;

public class EAHistory {

    // 最后一个属性
    private String lastAttr;
    // 最后一个实体
    private String lastEntity;
    // 历史所有属性
    private ArrayList<String> histAttrs;
    // 历史所有实体
    private ArrayList<String> histEntities;

	public EAHistory(String lastAttr, String lastEntity, ArrayList<String> histAttrs, ArrayList<String> histEntities) {
		this.lastAttr = lastAttr;
		this.lastEntity = lastEntity;
		this.histAttrs = histAttrs;
		this.histEntities = histEntities;
	}

	public String getLastAttr() {
		return lastAttr;
	}

	public void setLastAttr(String lastAttr) {
		this.lastAttr = lastAttr;
	}

	public String getLastEntity() {
		return lastEntity;
	}

	public void setLastEntity(String lastEntity) {
		this.lastEntity = lastEntity;
	}

	public ArrayList<String> getHistAttrs() {
		return histAttrs;
	}

	public void setHistAttrs(ArrayList<String> histAttrs) {
		this.histAttrs = histAttrs;
	}

	public ArrayList<String> getHistEntities() {
		return histEntities;
	}

	public void setHistEntities(ArrayList<String> histEntities) {
		this.histEntities = histEntities;
	}

	@Override
	public String toString() {
		return "EAHistory{" +
				"lastAttr='" + lastAttr + '\'' +
				", lastEntity='" + lastEntity + '\'' +
				", histAttrs=" + histAttrs +
				", histEntities=" + histEntities +
				'}';
	}
}
