package com.example.elasticsearch.dao;

import com.example.elasticsearch.pojo.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @Param:
 * 	Item:为实体类
 * 	Long:为Item实体类中主键的数据类型
 */
public interface ItemDao extends ElasticsearchRepository<Item,Long> {

    List<Item> findByPriceBetween(double price1, double price2);

    List<Item> findByTitleLike(String key);

    List<Item> findByTitleContains(String key);

    List<Item> findByTitleLikeAndBrandInOrderByPrice(String key,List<String> brands);

}
