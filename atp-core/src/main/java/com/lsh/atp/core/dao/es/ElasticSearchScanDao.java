package com.lsh.atp.core.dao.es;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqiang on 17/5/11.
 */
@Repository
public class ElasticSearchScanDao {

    @Autowired
    private ElasticsearchOperations elasticsearchTemplate;

     public List searchAll(SearchQuery searchQuery, Class<?> clazz){
        List result = new ArrayList<>();
        String scrollId = elasticsearchTemplate.scan(searchQuery,1000,false);
        boolean hasRecords = true;
        while(hasRecords){
            Page<?> page = elasticsearchTemplate.scroll(scrollId, 5000L, clazz);
            if(page != null){
                result.addAll(page.getContent());
                hasRecords = page.hasNext();
            }else{
                hasRecords = false;
            }
        }
        return result;
    }
}
