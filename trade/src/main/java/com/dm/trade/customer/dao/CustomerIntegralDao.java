package com.dm.trade.customer.dao;

import com.dm.trade.customer.domain.CustomerIntegralDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 用户积分记录表
 * @author zhongchao
 * @email zhong_ch@foxmail.com
 * @date 2018-03-25 11:48:05
 */
@Mapper
public interface CustomerIntegralDao {

	CustomerIntegralDO get(Integer id);
	
	List<CustomerIntegralDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(CustomerIntegralDO customerIntegral);
	
	int update(CustomerIntegralDO customerIntegral);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
