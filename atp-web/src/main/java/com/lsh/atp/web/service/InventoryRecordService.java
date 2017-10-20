package com.lsh.atp.web.service;

import com.lsh.atp.core.dao.es.ElasticSearchScanDao;
import com.lsh.atp.core.service.goodsapi.GoodsService;
import com.lsh.atp.core.task.model.InventoryRecord;
import com.lsh.atp.web.domain.InventoryRecodeQuery;
import com.lsh.base.common.json.JsonUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhangqiang on 17/5/15.
 */
@Service
public class InventoryRecordService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryRecordService.class);

    private final String indexName = "atp";

    private final String type = "inventoryRecord";

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ElasticSearchScanDao esScanDao;

    public List<InventoryRecord> search(InventoryRecodeQuery query){
        Long itemId = query.getItemId();
        String dc = query.getDc();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if( query.getWumartCode() != null && (itemId == null || itemId == 0L)){
            //转码
            String market = "1";
            if(dc.equals("DC55") || dc.equals("DC59")){
                market = "3";
            }
            itemId = goodsService.code2ItemId(query.getWumartCode(),market);
        }
        if(itemId != null){
            boolQueryBuilder.must(new TermQueryBuilder("skuId",query.getItemId()));
        }else{
            return Collections.emptyList();
        }

        if(dc != "" && !"All".equals(dc)){
            boolQueryBuilder.must(new TermQueryBuilder("dc",query.getDc()));
        }
        if(query.getQueryTime() != ""){
            SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
            Date from = null;
            Date to = null;
            try{
                from = sdf.parse(query.getQueryTime());
                to = DateUtils.addDays(from,1);
                boolQueryBuilder.must(new RangeQueryBuilder("updatedAt").from(from.getTime()).to(to.getTime()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(query.getRecordType()!= "" && !"All".equals(query.getRecordType())){
            boolQueryBuilder.must(new TermQueryBuilder("recordType",query.getRecordType()));
        }

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices(indexName)
                .withTypes(type)
                .withFilter(boolQueryBuilder)
                .withSort(new FieldSortBuilder("updatedAt").order(SortOrder.ASC))
                .build();
        List<InventoryRecord> result = esScanDao.searchAll(searchQuery,InventoryRecord.class);

        Collections.sort(result, new Comparator<InventoryRecord>() {
            @Override
            public int compare(InventoryRecord o1, InventoryRecord o2) {
                return o1.getUpdatedAt().compareTo(o2.getUpdatedAt());
            }
        });
        return result;
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
        Date date =sdf.parse("2016-12-12");

        System.out.println(date);
        System.out.println(DateUtils.addDays(date,1));
    }
}
