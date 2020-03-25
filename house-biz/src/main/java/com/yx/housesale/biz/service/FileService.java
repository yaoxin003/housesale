package com.yx.housesale.biz.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/09/17:33
 */
public interface FileService {

    public List<String> getImgPath(List<MultipartFile> files);

}
