package com.eventostec.api.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

    @Value("${aws.region}")
    private String awsRegion;

    @Bean
    public AmazonS3 createS3Instance() {
        return AmazonS3ClientBuilder
                //usará as configurações standard configuradas
                //no computador
                .standard()
                //se o upload der algum problema use o código abaixo
                //.withCredentials(new DefaultAWSCredentialsProviderChain())

                //você pode também instalar plugin do AWS, pois o intelij
                //identifica o profile default configurado
                .withRegion(awsRegion).build();
    }
}
