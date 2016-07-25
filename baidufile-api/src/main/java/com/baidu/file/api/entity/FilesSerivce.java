package com.baidu.file.api.entity;

import com.baidu.file.api.FileCenter;

import java.util.List;

/**
 * Created by zhangzf on 16/7/24.
 */
public interface FilesSerivce {
    FileCenter uploadFile(FileCenter fileCenter);

    FileCenter getFileById(Integer id);

    Integer delById(Integer id);

    List<FileCenter> searchFile(String name);

    List<FileCenter> getFiles();
}
