package casia.isiteam.militaryqa.common;

import casia.isiteam.militaryqa.model.Qa;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * 存储多轮问答状态
 */
public class QaStatusCache {

    /**
     * 存储多轮信息
     * 同一用户五分钟未提问，则清除该用户状态
     */
    public static ExpiringMap<String, ArrayList<Qa>> Qas = ExpiringMap.builder()
                                                            .maxSize(100)
                                                            .expiration(5, TimeUnit.MINUTES)
                                                            .variableExpiration().expirationPolicy(ExpirationPolicy.ACCESSED).build();

    /**
     * 是否使用人称代词，[0]为前状态，[1]为当前状态
     * 同一用户五分钟未提问，则清除该用户状态
     */
    public static ExpiringMap<String, boolean[]> isUsingPronounMap = ExpiringMap.builder()
                                                            .maxSize(100)
                                                            .expiration(5, TimeUnit.MINUTES)
                                                            .variableExpiration().expirationPolicy(ExpirationPolicy.ACCESSED).build();

}
