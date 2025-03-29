package com.jp.productcatalogservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer
{
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "product-events";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate ;


    public void publishProductData(Long productId,String productName, String type, String description) throws JsonProcessingException
    {

        ProductData productMessage = new ProductData();
        productMessage.setProductId(productId);
        productMessage.setProductName(productName);
        productMessage.setUpdateType(type);
        productMessage.setDescription(description);

        logger.info("created the product message object: "+productMessage);
        // convert to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String datum =  objectMapper.writeValueAsString(productMessage);
        logger.info("converted the product message object to JSON: "+datum);

        logger.info(String.format("#### -> Producing message -> %s", datum));
        this.kafkaTemplate.send(TOPIC, datum);
    }

}
