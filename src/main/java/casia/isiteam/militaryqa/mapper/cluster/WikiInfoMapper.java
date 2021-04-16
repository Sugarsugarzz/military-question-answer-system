package casia.isiteam.militaryqa.mapper.cluster;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface WikiInfoMapper {

    /**
     * 根据游标查询数据
     * @param cursor 游标
     * @param limit  每批查询数量
     */
    List<Map<String, Object>> list(@Param("cursor") Long cursor,
                                   @Param("limit") Long limit);
}
