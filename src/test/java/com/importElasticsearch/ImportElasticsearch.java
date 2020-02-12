package com.importElasticsearch;

import com.elasticsearch.service.SkuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/mapper.xml")
public class ImportElasticsearch {

    @Autowired
    private SkuService skuService;

    @Test
    public void importElasticsearch(){
        skuService.importElasticsearch();
    }
}
