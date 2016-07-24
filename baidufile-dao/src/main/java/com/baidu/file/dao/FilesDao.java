package com.baidu.file.dao;

import com.baidu.tables.records.FileCenterRecord;
import com.baidu.file.api.FileCenter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

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
}
