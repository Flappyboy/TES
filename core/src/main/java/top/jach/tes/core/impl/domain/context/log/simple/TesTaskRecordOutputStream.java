package top.jach.tes.core.impl.domain.context.log.simple;

import java.io.*;

public class TesTaskRecordOutputStream extends OutputStream {
    private String taskId;
    private OutputStream outputStream;

    public TesTaskRecordOutputStream(String taskId) {
        this.taskId = taskId;
        try {
            outputStream = new FileOutputStream(taskId+".log");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        outputStream.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        outputStream.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}
