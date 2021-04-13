package casia.isiteam.militaryqa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EaHistory {

	/**
	 * 多轮提问中最后一个属性
	 */
    private String lastAttr;
	/**
	 * 多轮提问中最后一个实体
	 */
    private String lastEntity;
	/**
	 * 多轮提问中所有属性
	 */
    private Set<String> histAttrs;
	/**
	 * 多轮提问中所有实体
	 */
    private Set<String> histEntities;
}
