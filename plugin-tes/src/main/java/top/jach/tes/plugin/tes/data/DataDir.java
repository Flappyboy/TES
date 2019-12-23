package top.jach.tes.plugin.tes.data;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DataDir extends File{
    private Date date;
    private Integer num;

    private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    static {
        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    private DataDir(File file) throws Exception {
        super(file.getAbsolutePath());
        if(!file.isDirectory() || !file.getName().matches("data_v[0-9]{11}")){
            throw new Exception("输入的目录名不符合要求，或者不是目录");
        }
        String name = file.getName();
        String datestr = name.substring(6, 14);
        this.date = format.parse(datestr);
        this.num = Integer.valueOf(name.substring(14, 17));
    }

    public static DataDir createNewDataFile(File workDir) throws IOException {
        checkWorkDir(workDir);
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date currentDay = cal.getTime();
        DataDir lastDataDir = lastDataDir(workDir);

        Integer num = 1;
        if (lastDataDir.getDate().equals(currentDay)){
            num = lastDataDir.getNum()+1;
        }

        File dataDir = new File(String.format("%s/data_v%s%03d",
                StringUtils.stripEnd(workDir.getAbsolutePath(),"\\/"),
                format.format(currentDay),
                num
        ));
        try {
            return new DataDir(dataDir);
        } catch (Exception e) {
            throw new RuntimeException("代码错误  "+dataDir);
        }
    }

    public static DataDir lastDataDir(File workDir){
        Date lastDate = new Date(0l);
        Integer maxNum = 0;
        for (File dir :
                workDir.listFiles()) {
            try {
                DataDir dataDir = new DataDir(dir);
                Date date = dataDir.getDate();
                Integer num = dataDir.getNum();
                if(date.after(lastDate) || date.equals(lastDate)){
                    if(date.after(lastDate)) {
                        maxNum = 0;
                    }
                    lastDate = date;
                    if(num > maxNum){
                        maxNum = num;
                    }
                }
            } catch (ParseException e) {
                continue;
            } catch (Exception e) {
                continue;
            }
        }

        File dataDir = new File(String.format("%s/data_v%s%03d",
                StringUtils.stripEnd(workDir.getAbsolutePath(),"\\/"),
                format.format(lastDate),
                maxNum
        ));
        try {
            return new DataDir(dataDir);
        } catch (Exception e) {
            throw new RuntimeException("代码错误  "+dataDir);
        }
    }

    public static void checkWorkDir(File workDir) throws IOException {
        if (workDir.exists()) {
            if (!workDir.isDirectory()) {
                FileUtils.forceDelete(workDir);
                workDir.mkdirs();
            }
        } else {
            workDir.mkdirs();
        }
    }

    public Date getDate() {
        return date;
    }

    public Integer getNum() {
        return num;
    }
}
