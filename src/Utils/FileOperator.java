package Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;

public class FileOperator {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private String filepath = null;

    /**
     * 无参构造函数
     */
    public FileOperator() {
    }

    /**
     * 有参构造函数
     * @param filepath
     */
    public FileOperator(String filepath) {
        this.filepath = filepath;
        File file = new File(filepath);
        if (!file.exists()) {
            logger.error(filepath + " - 文件不存在！");
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("路径：" + filepath + "  创建失败！", e);
            }
        }
    }

    /**
     * Match文件转Map函数（Match File to Match Map）
     * @return
     */
    public Map<String, String> matchFileToMap() {

        Map<String, String> map = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String str;
            while ((str = br.readLine()) != null) {
                String[] str_list = str.split("：");
                String[] lst = str_list[1].split("\\|");
                for (String s: lst) {
                    map.put(s, str_list[0]);
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("路径：" + filepath + "  文件未找到！", e);
        } catch (IOException e) {
            logger.error("路径：" + filepath + "  文件内容读取发生错误！", e);
        }

        return map;
    }

    /**
     * Match文件转Segment文件（Match File to Segment File）
     * 将问句中所有可能涉及的实体词加入到分词器的词典中
     */
    public void matchFileToSegFile() {

        // 获取Match文件目录
        File file = new File("data/dict_for_match_query");
        Map<String, String> filepath_map = new HashMap<>();
        // 构建所有的文件路径
        String[] files = file.list();
        if (files == null) {
            System.out.println("Match File为空！请检查！");
            return;
        }
        for (String filename : files)
            filepath_map.put("data/dict_for_match_query/" + filename, "data/dict_for_segment/" + filename);
        // 读取文件内容，构建分词器词典
        for (String filepath : filepath_map.keySet()) {

            try {
                BufferedReader br = new BufferedReader(new FileReader(filepath));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath_map.get(filepath))));
                String str;
                while ((str = br.readLine()) != null) {
                    for (String s : str.split("：")[1].split("\\|")) {
                        bw.write(s + "\n");
                    }
                }
                bw.flush();
                bw.close();
            } catch (FileNotFoundException e) {
                logger.error(filepath + " - 写文件未找到！", e);
            } catch (IOException e) {
                logger.error(filepath + " - 写文件发生错误！", e);
            }
        }
    }

    /**
     * 将实体List写入文件
     * 以  实体：实体  的形式
     * @param entities
     * @param filepath
     */
    public void entitiesToFile(List<String> entities, String filepath) {

        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath)));
            for (String entity: entities) {
                bw.write(entity + "：" + entity + "\n");
            }
            bw.flush();
            bw.close();
        } catch (FileNotFoundException e) {
            logger.error(filepath + " - 写文件未找到！", e);
        } catch (IOException e) {
            logger.error(filepath + " - 写文件发生错误！", e);
        }
    }

}


