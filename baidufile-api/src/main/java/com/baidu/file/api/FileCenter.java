package com.baidu.file.api;

import lombok.Data;
import java.util.Date;

/**
 * Created by zhangzf on 16/7/24.
 */
@Data
public class FileCenter {
    private Integer id;
    private String name;
    private String type;
    private Date uploadTime;
    private String tag;
    private String path;
}
