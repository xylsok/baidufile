package com.baidu.file.dao;

import com.baidu.tables.records.FileCenterRecord;
import com.baidu.file.api.FileCenter;
import org.jooq.Result;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.baidu.tables.FileCenter.FILE_CENTER;

/**
 * Created by zhangzf on 16/7/24.
 */
@Component
public class FilesDao extends JooqDao<FileCenterRecord, FileCenter, String> {
    protected FilesDao() {
        super(FILE_CENTER, FileCenter.class);
    }

    @Override
    protected String getId(FileCenter fileCenter) {
        return null;
    }

    public FileCenter uploadFile(FileCenter fc) {
        FileCenterRecord uRecord = (FileCenterRecord) this.create().newRecord(FILE_CENTER);
        uRecord.from(uRecord);
        uRecord.setName(fc.getName());
        uRecord.setPath(fc.getPath());
        uRecord.setType(fc.getType());
        uRecord.setTag(fc.getTag());
        uRecord.setUploadTime(new Timestamp(new Date().getTime()));
        uRecord.insert();
        fc.setId(uRecord.getId());
        return fc;
    }

    public FileCenter getFileById(Integer id) {
        FileCenterRecord fileCenterRecord = create().selectFrom(FILE_CENTER).where(FILE_CENTER.ID.eq(id)).fetchOne();
        if (fileCenterRecord != null) {
            return fileCenterRecord.into(FileCenter.class);
        }
        return null;
    }

    public Integer deleteById(Integer id) {
        FileCenter fi = getFileById(id);
        Integer status = create().delete(FILE_CENTER)
                .where(FILE_CENTER.ID.eq(id))
                .execute();
        if (status == 1) {
            try {
                File file = new File(fi.getPath());
                // 路径为文件且不为空则进行删除
                if (file.isFile() && file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return status;
    }

    public List<FileCenter> searchFile(String name) {
        Result<FileCenterRecord> fetch = create().selectFrom(FILE_CENTER)
                .where(FILE_CENTER.NAME.like("%" + name + "%").or(FILE_CENTER.TAG.like("%" + name + "%")))
                .fetch();
        if (fetch.size() > 0) {
            return fetch.into(FileCenter.class);
        } else {
            return new ArrayList<FileCenter>();
        }
    }

    public List<FileCenter> getFiles() {
        Result<FileCenterRecord> fetch = create().selectFrom(FILE_CENTER).orderBy(FILE_CENTER.UPLOAD_TIME.desc()).fetch();
        if (fetch.size() > 0) {
            return fetch.into(FileCenter.class);
        } else {
            return new ArrayList<FileCenter>();
        }
    }
}
