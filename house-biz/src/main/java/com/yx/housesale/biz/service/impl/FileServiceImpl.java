package com.yx.housesale.biz.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.io.Files;
import com.yx.housesale.biz.mapper.UserMapper;
import com.yx.housesale.biz.service.FileService;
import com.yx.housesale.common.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/09/17:34
 */
@Service
public class FileServiceImpl implements FileService{

    @Value("${file.path}")
    private String filePath;

    public List<String> getImgPath(List<MultipartFile> files){
        List<String> paths = new ArrayList<>();
        files.forEach(file -> {
            try {
                File localFile = this.saveToLocal(file,filePath);
               String path= StringUtils.substringAfterLast(localFile.getAbsolutePath(),filePath);
               paths.add(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return paths;
    }


    private File saveToLocal(MultipartFile file, String filePath) throws IOException {
        File newFile = new File(filePath + "/" + Instant.now().getEpochSecond() + "/"
                + file.getOriginalFilename());
        if(!newFile.exists()){
            newFile.getParentFile().mkdirs();
            newFile.createNewFile();
        }
        Files.write(file.getBytes(),newFile);
        return newFile;
    }
}
