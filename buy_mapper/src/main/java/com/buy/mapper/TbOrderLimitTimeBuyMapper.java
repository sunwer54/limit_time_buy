package com.buy.mapper;

import com.buy.pojo.TbOrderLimitTimeBuy;
import com.buy.pojo.TbOrderLimitTimeBuyExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbOrderLimitTimeBuyMapper {
    long countByExample(TbOrderLimitTimeBuyExample example);

    int deleteByExample(TbOrderLimitTimeBuyExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbOrderLimitTimeBuy record);

    int insertSelective(TbOrderLimitTimeBuy record);

    List<TbOrderLimitTimeBuy> selectByExample(TbOrderLimitTimeBuyExample example);

    TbOrderLimitTimeBuy selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbOrderLimitTimeBuy record, @Param("example") TbOrderLimitTimeBuyExample example);

    int updateByExample(@Param("record") TbOrderLimitTimeBuy record, @Param("example") TbOrderLimitTimeBuyExample example);

    int updateByPrimaryKeySelective(TbOrderLimitTimeBuy record);

    int updateByPrimaryKey(TbOrderLimitTimeBuy record);
}