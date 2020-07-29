package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EAHistory {

    // 最近的一个属性
    private String lastAttr;
    // 最近的一个实体
    private String lastEntity;
    // 历史所有属性
    private ArrayList<String> histAttr;
    // 历史所有实体
    private ArrayList<String> histEntity;
    
    
	public EAHistory() {
		super();
	}


	public EAHistory(String lastAttr, String lastEntity, ArrayList<String> histAttr, ArrayList<String> histEntity) {
		super();
		this.lastAttr = lastAttr;
		this.lastEntity = lastEntity;
		this.histAttr = histAttr;
		this.histEntity = histEntity;
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


	public ArrayList<String> getHistAttr() {
		return histAttr;
	}


	public void setHistAttr(ArrayList<String> histAttr) {
		this.histAttr = histAttr;
	}


	public ArrayList<String> getHistEntity() {
		return histEntity;
	}


	public void setHistEntity(ArrayList<String> histEntity) {
		this.histEntity = histEntity;
	}


	@Override
	public String toString() {
		return "EAHistory [lastAttr=" + lastAttr + ", lastEntity=" + lastEntity + ", histAttr=" + histAttr
				+ ", histEntity=" + histEntity + "]";
	}
       
}
