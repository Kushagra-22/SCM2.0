package com.scm.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ImageService {
    public String uploadImage(MultipartFile image,String fileName);
    public String getUrlFromPublicId(String id);
}
