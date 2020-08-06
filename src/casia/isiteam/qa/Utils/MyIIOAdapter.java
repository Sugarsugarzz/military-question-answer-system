package casia.isiteam.qa.Utils;

import com.hankcs.hanlp.corpus.io.IIOAdapter;

import java.io.*;

public class MyIIOAdapter implements IIOAdapter {
    /**
     * 打开一个文件以供读取
     *
     * @param path 文件路径
     * @return 一个输入流
     * @throws IOException 任何可能的IO异常
     */
    @Override
    public InputStream open(String path) throws IOException {
        return new FileInputStream(this.getClass().getClassLoader().getResource(path).getFile());
    }

    /**
     * 创建一个新文件以供输出
     *
     * @param path 文件路径
     * @return 一个输出流
     * @throws IOException 任何可能的IO异常
     */
    @Override
    public OutputStream create(String path) throws IOException {
        return new FileOutputStream(this.getClass().getClassLoader().getResource(path).getFile());
    }
}
