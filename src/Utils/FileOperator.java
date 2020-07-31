package Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;

public class FileOperator {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    /**
     * Match文件转Map函数（Match File to Match Map）
     * @return Map函数
     */
    public static Map<String, String> matchFileToMap(String filepath) {

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
     * Map函数转Match文件（Match Map to Match File）
     * @param map Map函数
     * @param filepath 生成文件路径
     */
    public static void mapToMatchFile(Map<String, String> map, String filepath) {

        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath)));
            for (String key: map.keySet()) {
                bw.write(key + "：" + map.get(key) + "\n");
            }
            bw.flush();
            bw.close();
        } catch (FileNotFoundException e) {
            logger.error(filepath + " - 写文件未找到！", e);
        } catch (IOException e) {
            logger.error(filepath + " - 写文件发生错误！", e);
        }

    }

    /**
     * Match文件转Segment文件（Match File to Segment File）
     * 将问句中所有可能涉及的实体词加入到分词器的词典中
     */
    public static void matchFileToSegFile() {

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
     * Match文件转Segment文件（Match File to Segment File）
     * 将问句中所有可能涉及的实体词加入到分词器的词典中
     */
    public static void matchFileToSegFile(String filepath, String target_filepath) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target_filepath)));
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

    /**
     * 将实体List写入文件
     * 以  实体：实体  的形式，一般在getXXX列表后使用此方法。
     * @param entities 实体列表
     * @param filepath 文件路径
     */
    public static void entitiesToFile(List<String> entities, String filepath) {

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


