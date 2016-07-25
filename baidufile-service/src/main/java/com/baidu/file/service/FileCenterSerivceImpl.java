package com.baidu.file.service;

import com.baidu.file.api.FileCenter;
import com.baidu.file.api.entity.FilesSerivce;
import com.baidu.file.dao.FilesDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    public Integer delById(Integer id) {
        return filesDao.deleteById(id);
    }

    @Override
    public List<FileCenter> searchFile(String name) {
        return filesDao.searchFile(name);
    }

    @Override
    public List<FileCenter> getFiles() {
        return filesDao.getFiles();
    }
}
