package com.moyeo.service;

import java.rmi.server.ExportException;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

  String upload(String buckname, String path, MultipartFile multipartFile) throws Exception;

  void delete(String buckname, String path, String objectName) throws Exception;

}
