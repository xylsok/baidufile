package com.baidu.file.service;

import com.baidu.file.api.FileCenter;
import com.baidu.file.api.entity.FilesSerivce;
import com.baidu.file.dao.FilesDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zhangzf on 16/7/24.
 */
@Service("FilesSerivce")
public class FileCenterSerivceImpl implements FilesSerivce{
    @Resource
    FilesDao filesDao;
    @Override
    public FileCenter uploadFile(FileCenter filcCenter) {
        return filesDao.uploadFile(filcCenter);
    }

    @Override
    public FileCenter getFileById(Integer id) {
        return filesDao.getFileById(id);
    }

    @Override
    public Integer delById(Integer id,String path) {
        return filesDao.deleteById(id,path);
    }
}
