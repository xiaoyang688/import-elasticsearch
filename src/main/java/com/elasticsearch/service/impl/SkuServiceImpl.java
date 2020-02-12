package com.elasticsearch.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.elasticsearch.dao.SkuMapper;
import com.elasticsearch.pojo.Sku;
import com.elasticsearch.service.SkuService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuMapper skuMapper;

    @Override
    public void importElasticsearch() {
        HttpHost httpHost = new HttpHost("192.168.123.192", 9200, "http");
        RestClientBuilder restClientBuilder = RestClient.builder(httpHost);
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);

        List<Sku> skus = skuMapper.selectAll();
        BulkRequest bulkRequest = new BulkRequest();
        for (Sku sku : skus) {
            IndexRequest indexRequest = new IndexRequest("sku", "doc", sku.getId());
            Map skuMap = new HashMap();
            skuMap.put("name",sku.getName());
            skuMap.put("brandName",sku.getBrandName());
            skuMap.put("categoryName",sku.getCategoryName());
            skuMap.put("price",sku.getPrice());
            skuMap.put("createTime",sku.getCreateTime());
            skuMap.put("saleNum",sku.getSaleNum());
            skuMap.put("commentNum",sku.getCommentNum());
            skuMap.put("spec", JSONObject.parseObject(sku.getSpec()));
            indexRequest.source(skuMap);;
            bulkRequest.add(indexRequest);
        }
        try {
            BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            System.out.println(bulk.buildFailureMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
