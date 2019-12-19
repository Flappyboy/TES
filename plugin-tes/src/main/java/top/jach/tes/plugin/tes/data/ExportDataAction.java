package top.jach.tes.plugin.tes.data;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.DefaultOutputInfos;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.domain.meta.Meta;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.impl.domain.info.InfoOfInfo;
import top.jach.tes.core.impl.domain.info.value.FileInfo;
import top.jach.tes.core.impl.domain.info.value.ValueInfo;
import top.jach.tes.core.impl.domain.meta.InfoField;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ExportDataAction implements Action {
    public static final String ExportDir = "ExportDir";
    public static final String InfoPrefix = "Info_";// Info_1, Info_2 ...

    private Preprocessor preprocessor = null;

    public ExportDataAction() {
    }
    public ExportDataAction(Preprocessor preprocessor) {
        this.preprocessor = preprocessor;
    }

    public interface Preprocessor{
        Info process(Info info);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public Meta getInputMeta() {
        return () -> Arrays.asList(
                InfoField.createField(ExportDir).setInfoClass(FileInfo.class)
        );
    }

    @Override
    public OutputInfos execute(InputInfos inputInfos, Context context) throws ActionExecuteFailedException {
        File exportDir = ValueInfo.getValueFromInputInfos(inputInfos, ExportDir, FileInfo.class);
        File dataDir = getDataDir(exportDir);

        DefaultOutputInfos outputInfos = new DefaultOutputInfos();
        for (int i = 1; i < 9999; i++) {

            Info info = inputInfos.getInfo(InfoPrefix + i, Info.class);
            try {
                if (info == null) {
                    break;
                }
                context.Logger().info("export {} start", i);
                if(preprocessor != null) {
                    info = preprocessor.process(info);
                }
                File jsonFile = new File(String.format("%s/%s_%d.json",
                        StringUtils.stripEnd(dataDir.getAbsolutePath(),"\\/"),
                        info.getName(),
                        info.getId()));
                FileUtils.write(jsonFile, JSONObject.toJSONString(info, SerializerFeature.PrettyFormat), "utf8");
                FileInfo fi = FileInfo.createInfo(jsonFile);
                outputInfos.addInfoReadyToSave(fi);
                context.Logger().info("export {} end", i);
            }catch (Exception e){
                context.Logger().error("export error i:{} ,infoId: {}, infoName: {}", i, info.getId(), info.getName());
                e.printStackTrace();
            }

        }
        return outputInfos;
    }

    private File getDataDir(File exportDir) throws ActionExecuteFailedException {
        try {
            if (exportDir.exists()) {
                if (!exportDir.isDirectory()) {
                    FileUtils.forceDelete(exportDir);
                    exportDir.mkdirs();
                }
            } else {
                exportDir.mkdirs();
            }
        } catch (IOException e) {
            throw new ActionExecuteFailedException("ExportData exportDir error", e);
        }


        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date currentDay = cal.getTime();

        Integer maxNum = 0;
        for (File dir :
                exportDir.listFiles()) {
            if(!dir.isDirectory()){
                continue;
            }
            try {
                String name = dir.getName();
                String datestr = name.substring(6, 14);
                Integer num = Integer.valueOf(name.substring(14, 17));
                Date date = format.parse(datestr);
                if(date.after(currentDay) || date.equals(currentDay)){
                    if(num > maxNum){
                        maxNum = num;
                    }
                }
            } catch (ParseException e) {
                continue;
            }
        }

        File dataDir = new File(String.format("%s/data_v%s%03d",
                StringUtils.stripEnd(exportDir.getAbsolutePath(),"\\/"),
                format.format(new Date()),
                maxNum+1
                ));
        if(dataDir.isFile()){
            try {
                FileUtils.forceDelete(dataDir);
            } catch (IOException e) {
                throw new ActionExecuteFailedException(e);
            }
        }
        if (!dataDir.exists()){
            dataDir.mkdirs();
        }
        return dataDir;
    }
}
