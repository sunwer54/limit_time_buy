package com.buy.mapper;

import com.buy.pojo.TbItemLimitTimeBuy;
import com.buy.pojo.TbItemLimitTimeBuyExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbItemLimitTimeBuyMapper {
    long countByExample(TbItemLimitTimeBuyExample example);

    int deleteByExample(TbItemLimitTimeBuyExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbItemLimitTimeBuy record);

    int insertSelective(TbItemLimitTimeBuy record);

    List<TbItemLimitTimeBuy> selectByExample(TbItemLimitTimeBuyExample example);

    TbItemLimitTimeBuy selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbItemLimitTimeBuy record, @Param("example") TbItemLimitTimeBuyExample example);

    int updateByExample(@Param("record") TbItemLimitTimeBuy record, @Param("example") TbItemLimitTimeBuyExample example);

    int updateByPrimaryKeySelective(TbItemLimitTimeBuy record);

    int updateByPrimaryKey(TbItemLimitTimeBuy record);
}