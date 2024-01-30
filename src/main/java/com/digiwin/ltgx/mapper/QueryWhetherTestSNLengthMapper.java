package com.digiwin.ltgx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digiwin.ltgx.domain.QueryWhetherTestSNLengthDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 获取是否验证sn长度
 */
@Mapper
public interface QueryWhetherTestSNLengthMapper extends BaseMapper<QueryWhetherTestSNLengthDetail> {

    /**
     * 获取是否验证sn长度
     * @param opNo opNo
     * @return QueryWhetherTestSNLengthDetail
     */
    List<QueryWhetherTestSNLengthDetail> queryWhetherTestSNLength(@Param("opNo") String opNo);

}
