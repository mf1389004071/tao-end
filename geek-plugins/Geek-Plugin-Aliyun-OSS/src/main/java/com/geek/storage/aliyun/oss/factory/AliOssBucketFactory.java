package com.geek.storage.aliyun.oss.factory;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.geek.common.core.storage.base.StorageFactory;
import com.geek.storage.aliyun.oss.domain.AliOssBucket;

/**
 * 配置类用于管理阿里云OSS客户端实例及其相关属性。
 */
@Configuration("oss")
public class AliOssBucketFactory extends StorageFactory<AliOssBucket> {
    private static final Logger logger = LoggerFactory.getLogger(AliOssBucketFactory.class);

    @Override
    protected AliOssBucket createBucket(String name, Properties props) {
        String endpoint = props.getProperty("endpoint");
        String accessKeyId = props.getProperty("accessKeyId");
        String accessKeySecret = props.getProperty("accessKeySecret");
        String bucketName = props.getProperty("bucketName");
        if (props == null || endpoint == null || accessKeyId == null ||
                accessKeySecret == null || bucketName == null) {
            throw new IllegalArgumentException("AliOssProperties or its required fields cannot be null");
        }
        OSS client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        AliOssBucket ossBucket = AliOssBucket.builder()
                .client(client)
                .bucketName(bucketName)
                .endpoint(endpoint)
                .build();
        logger.info("AliOSS 数据桶：{} - 创建成功", name);
        return ossBucket;
    }

    @Override
    protected void validateBucket(AliOssBucket aliOssBucket) {
        OSS ossClient = aliOssBucket.getClient();
        String bucketName = aliOssBucket.getBucketName();
        try {
            if (!ossClient.doesBucketExist(bucketName)) {
                throw new RuntimeException("Bucket " + bucketName + " does not exist");
            }
        } catch (OSSException oe) {
            logger.error("OSSException: " + oe.getMessage(), oe);
            throw new RuntimeException("OSS error: " + oe.getMessage());
        } catch (ClientException ce) {
            logger.error("ClientException: " + ce.getMessage(), ce);
            throw new RuntimeException("Client error: " + ce.getMessage());
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            throw new RuntimeException("Error validating OSS bucket: " + e.getMessage());
        }
    }
}
