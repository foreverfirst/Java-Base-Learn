package com.lc;

import com.alibaba.fastjson.JSON;
import com.lc.entity.Student;
import com.lc.util.BeanUtil;
import lombok.val;
import org.apache.ibatis.session.SqlSessionFactory;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchPhase;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@SpringBootTest
class ApplicationTests {

    @Autowired
    DataSource dataSource;
    @Autowired
    SqlSessionFactory sessionFactory;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RestHighLevelClient restClient;
    @Test
    public void demo() {

//        System.out.println(dataSource);
//        System.out.println(sessionFactory);
        ValueOperations<String, String> operations  = redisTemplate.opsForValue();
        Set<String> keys = redisTemplate.keys("*");
        System.out.println(keys);


    }

    @Test
    void contextLoads() {

        ApplicationContext applicationContext = new BeanUtil().applicationContext;
        System.out.println(applicationContext.getBean(DataSource.class));
//        String[] allBeanNames = applicationContext.getBean("db1");
//        for (String beanName : allBeanNames) {
//            System.out.println(beanName);
//        }

    }


    // ElasticSearch
    // 测试创建索引
    @Test
    void creatIndex() throws IOException {
        // 1、创建索引请求
        CreateIndexRequest request = new CreateIndexRequest("liuchen" );
        // 2.客户端执行请求IndicesClient,请求后获得响应
        CreateIndexResponse createIndexResponse =
                restClient.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);
    }

    //获取索引
    @Test
    void existIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("liuchen");
        boolean exists = restClient.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }
    // 测试删除索引
    @Test
    void testDeleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("kuang_index");
        AcknowledgedResponse delete = restClient.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }

    // 测试添加文档
    @Test
    void testAddDocument() throws IOException {
        // 创建对象
        Student student = new Student(1, "张三", "男",new Date(19990102),3);
        // 创建请求
        IndexRequest request = new IndexRequest("liuchen");
        // 规则 put /kuang_index/_doc/1
        request.id("1");
        request.timeout(TimeValue.timeValueSeconds(1));
        
        // 将我们的数据放入请求 json
        request.source(JSON.toJSONString(student), XContentType.JSON);
        // 客户端发送请求 , 获取响应的结果
        IndexResponse indexResponse = restClient.index(request,
                RequestOptions.DEFAULT);
        System.out.println(indexResponse.toString()); //
        System.out.println(indexResponse.status()); // 对应我们命令返回的状态
    }

    //获取文档
    @Test
    void getDocument() throws IOException {
        GetRequest liuchen = new GetRequest("liuchen","1");
        GetResponse getResponse  = restClient.get(liuchen, RequestOptions.DEFAULT);
        System.out.println(getResponse); // 返回的全部内容和命令式
    }

    //搜索查询
    @Test
    void search() throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // QueryBuilders.termQuery 精确
        // QueryBuilders.matchQuery() 匹配
        // QueryBuilders.matchAllQuery() 匹配所有
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "aa");
         MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", "张三");
        sourceBuilder.query(matchQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        SearchRequest liuchen = new SearchRequest("liuchen");
        liuchen.source(sourceBuilder);
        SearchResponse searchResponse = restClient.search(liuchen,RequestOptions.DEFAULT);
        System.out.println(searchResponse);

    }

}
