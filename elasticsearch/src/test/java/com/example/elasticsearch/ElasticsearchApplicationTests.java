package com.example.elasticsearch;

import com.example.elasticsearch.dao.ItemDao;
import com.example.elasticsearch.pojo.Item;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ElasticsearchApplicationTests {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ItemDao itemDao;

    /* 创建索引*/
    @Test
    void testCreateIndex() {
        elasticsearchTemplate.createIndex(Item.class);
    }

    /* 删除索引*/
    @Test
    void testDeleteIndex() {
        elasticsearchTemplate.deleteIndex(Item.class);
    }

    /* 新增方法*/
    @Test
    void insert() {
        Item item = new Item(1L, "小米手机7", " 手机", "小米", 3499.00, "http://image.baidu.com/13123.jpg");
        itemDao.save(item);
    }

    /* 批量新增方法*/
    @Test
    void inserList(){
        List<Item> items=new ArrayList<>();
        items.add(new Item(1L, "小米手机7", "手机", "小米", 3299.00, "http://image.baidu.com/13123.jpg"));
        items.add(new Item(2L, "坚果手机R1", "手机", "锤子", 3699.00, "http://image.baidu.com/13123.jpg"));
        items.add(new Item(3L, "华为META10", "手机", "华为", 4499.00, "http://image.baidu.com/13123.jpg"));
        items.add(new Item(4L, "小米Mix2S", "手机", "小米", 4299.00, "http://image.baidu.com/13123.jpg"));
        items.add(new Item(5L, "荣耀V10", "手机", "华为", 2799.00, "http://image.baidu.com/13123.jpg"));
        // 接收对象集合，实现批量新增
        itemDao.saveAll(items);
    }

    /** elasticsearch中本没有修改，它的修改原理是该是先删除在新增
     *  修改和新增是同一个接口，区分的依据就是id。
     */
    @Test
    void update(){
        Item item = new Item(1L, "苹果XSMax", " 手机",
                "小米", 3499.00, "http://image.baidu.com/13123.jpg");
        itemDao.save(item);
    }

    /* 基本查询 */
    @Test
    void queryAll(){
        // 查找所有
        Iterable<Item> items = itemDao.findAll();
        // 对某字段排序查找所有 Sort.by("price").descending() 降序
        // Sort.by("price").ascending():升序
//        Iterable<Item> items = itemDao.findAll(Sort.by("price").ascending());
//        Iterable<Item> items = itemDao.findAll(Sort.by("price").descending());
        for (Item item : items) {
            System.out.println(item);
        }
    }

    /* ！！！自定义方法 - 根据方法名称自动实现功能 */
    @Test
    void queryByPriceBetween(){
        List<Item> items = itemDao.findByPriceBetween(3000.00, 4000.00);
        for (Item item : items) {
            System.out.println(item);
        }
    }
    /* ！！！自定义方法 - 根据方法名称自动实现功能 */
    @Test
    void queryByTitleLike(){
        List<Item> items = itemDao.findByTitleLike("小");
        for (Item item : items) {
            System.out.println(item);
        }
    }

    /* ！！！自定义方法 - 根据方法名称自动实现功能 */
    @Test
    void queryByTitleContains(){
        List<Item> items = itemDao.findByTitleContains("7");
        for (Item item : items) {
            System.out.println(item);
        }
    }

    /* ！！！自定义方法 - 根据方法名称自动实现功能 */
    @Test
    void queryTest(){
        List<String> brands=new ArrayList<>();
        brands.add("华为");
        List<Item> items = itemDao.findByTitleLikeAndBrandInOrderByPrice("1",brands);
        for (Item item : items) {
            System.out.println(item);
        }
    }

    /* 自定义查询 matchQuery-带分析器词条匹配查询 */
    @Test
    void testMatchQuery(){
        //构建查询条件
        NativeSearchQueryBuilder queryBuilder=new NativeSearchQueryBuilder();
        //添加基本分词查询
        queryBuilder.withQuery(QueryBuilders.matchQuery("title","手机"));
        //搜索,获取结果
        Page<Item> items = itemDao.search(queryBuilder.build());
        //总条数
        long count = items.getTotalElements();
        System.out.println("总条数为："+count);
        for (Item item : items) {
            System.out.println(item);
        }
    }

    /* 自定义查询 termQuery-不带分析器词条匹配查询 */
    @Test
    void testTermQuery(){
        NativeSearchQueryBuilder queryBuilder=new NativeSearchQueryBuilder();
//        queryBuilder.withQuery(QueryBuilders.termQuery("price",2799));
        queryBuilder.withQuery(QueryBuilders.termQuery("title","手机"));
        Page<Item> items = itemDao.search(queryBuilder.build());
        for (Item item : items) {
            System.out.println(item);
        }
    }

    /* 自定义查询 模糊查询 */
    @Test
    void testFuzzyQuery(){
        NativeSearchQueryBuilder queryBuilder=new NativeSearchQueryBuilder();
//        queryBuilder.withQuery(QueryBuilders.fuzzyQuery("title","手机"));
        queryBuilder.withQuery(QueryBuilders.fuzzyQuery("brand","华为"));
        Page<Item> items = itemDao.search(queryBuilder.build());
        for (Item item : items) {
            System.out.println(item);
        }
    }

    /* 自定义查询 布尔查询 */
    @Test
    void testBooleanQuery(){
        NativeSearchQueryBuilder queryBuilder=new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("title","华为")).must(QueryBuilders.matchQuery("brand","华为")));
        Page<Item> items = itemDao.search(queryBuilder.build());
        for (Item item : items) {
            System.out.println(item);
        }
    }

    /* 自定义查询 分页查询 */
    @Test
    void testPageQuery(){
        NativeSearchQueryBuilder queryBuilder=new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.termQuery("category","手机"));
        //分页
        int page=0;
        int size=2;
        queryBuilder.withPageable(PageRequest.of(page,size));
        //Elasticsearch中的分页是从第0页开始。
        Page<Item> items = itemDao.search(queryBuilder.build());
        System.out.println("总条数："+items.getTotalElements());
        System.out.println("总页数："+items.getTotalPages());
        System.out.println("当前页："+items.getNumber());
        System.out.println("每页大小："+items.getSize());
        for (Item item : items) {
            System.out.println(item);
        }
    }

    /* 聚合，分组查询 */
    @Test
    void testAggregation(){
        NativeSearchQueryBuilder queryBuilder=new NativeSearchQueryBuilder();
        //不查询任何结果
//        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""},null));
        //1.添加一个新的聚合，聚合类型为terms，聚合名称为brands，聚合字段为brand
        queryBuilder.addAggregation(AggregationBuilders.terms("brands").field("brand"));
        //2.查询，需要将结果强转为 AggregatedPage 类型
        AggregatedPage<Item> aggPage=(AggregatedPage<Item>)itemDao.search(queryBuilder.build());
        //3.解析
        //3.1 从结果中取出名为brands的聚合，因为是利用String类型字段进行termsj聚合，所以结果要强转为StringTermsl类型
        StringTerms agg= (StringTerms)aggPage.getAggregation("brands");
        //3.2 获取桶
        List<StringTerms.Bucket> buckets = agg.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            //获取桶中的key，即品牌名
            String brand=bucket.getKeyAsString();
            //获取桶中的文档数
            long count=bucket.getDocCount();
            System.out.println(brand+"："+count);
        }
    }

    /* 嵌套聚合，分组求平均 */
    @Test
    void testSubAggregation(){
        NativeSearchQueryBuilder queryBuilder=new NativeSearchQueryBuilder();
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""},null));
        queryBuilder.addAggregation(
                AggregationBuilders.terms("brands").field("brand")
                //在品牌聚合桶内求平均值
                .subAggregation(AggregationBuilders.avg("avgPrice").field("price"))
        );
        AggregatedPage<Item> aggPage= (AggregatedPage<Item>) itemDao.search(queryBuilder.build());
        StringTerms agg= (StringTerms) aggPage.getAggregation("brands");
        List<StringTerms.Bucket> buckets = agg.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            String brand=bucket.getKeyAsString();
            long count=bucket.getDocCount();
            InternalAvg avg= (InternalAvg) bucket.getAggregations().asMap().get("avgPrice");
            double avgPrice=avg.getValue();
            System.out.println(brand+"："+count+"，平均价格为"+avgPrice);
        }

    }
}
