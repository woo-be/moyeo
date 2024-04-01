package com.moyeo.service.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.moyeo.service.StorageService;
import java.io.InputStream;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NcpStorageService implements StorageService, InitializingBean {

  private static Log log = LogFactory.getLog(NcpStorageService.class);
  final String endPoint;
  final String regionName;
  final String accessKey;
  final String secretKey;
  final AmazonS3 s3;

  public NcpStorageService(
      @Value("${ncp.ss.endpoint}") String endPoint,
      @Value("${ncp.ss.regionname}") String regionName,
      @Value("${ncp.accesskey}") String accessKey,
      @Value("${ncp.secretkey}") String secretKey
  ) {

    this.endPoint = endPoint;
    this.regionName = regionName;
    this.accessKey = accessKey;
    this.secretKey = secretKey;

    this.s3 = AmazonS3ClientBuilder.standard()
        .withEndpointConfiguration(new EndpointConfiguration(endPoint, regionName))
        .withCredentials(
            new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
        .build();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    log.debug(String.format("endPoint:%s", this.endPoint));
    log.debug(String.format("regionName:%s", this.regionName));
    log.debug(String.format("accessKey:%s", this.accessKey));
    log.debug(String.format("secretKey:%s", this.secretKey));
  }

  @Override
  public String upload(String bucketName, String path, MultipartFile multipartFile)
      throws Exception {

    try (InputStream fileIn = multipartFile.getInputStream()) {

      String filename = UUID.randomUUID().toString();
      String objectName = path + filename;

      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentType(multipartFile.getContentType());

      PutObjectRequest putObjectRequest = new PutObjectRequest(
          bucketName,
          objectName,
          fileIn,
          objectMetadata
      ).withCannedAcl(CannedAccessControlList.PublicRead);

      s3.putObject(putObjectRequest);

      log.debug(String.format("Object %s has been created.\n", objectName));

      return filename;
    }
  }

  @Override
  public void delete(String bucketName, String path, String objectName) throws Exception {
    s3.deleteObject(bucketName, path + objectName);

    log.debug(String.format("Object %s has been deleted.\n", objectName));
  }
}
