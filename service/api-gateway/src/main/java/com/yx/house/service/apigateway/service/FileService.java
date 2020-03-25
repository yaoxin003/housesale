package com.yx.house.service.apigateway.service;

import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/24/15:37
 */
@Service
public class FileService {

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
