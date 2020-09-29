package casia.isiteam.militaryqa.model;

import java.util.Set;

public class EAHistory {

    // 最后一个属性
    private String lastAttr;
    // 最后一个实体
    private String lastEntity;
    // 历史所有属性
    private Set<String> histAttrs;
    // 历史所有实体
    private Set<String> histEntities;

	public EAHistory() {
	}

	public EAHistory(String lastAttr, String lastEntity, Set<String> histAttrs, Set<String> histEntities) {
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

	public Set<String> getHistAttrs() {
		return histAttrs;
	}

	public void setHistAttrs(Set<String> histAttrs) {
		this.histAttrs = histAttrs;
	}

	public Set<String> getHistEntities() {
		return histEntities;
	}

	public void setHistEntities(Set<String> histEntities) {
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
