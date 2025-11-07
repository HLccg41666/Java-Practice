package util;

import entity.Book;
import entity.Novel;
import entity.TextBook;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * FileUtil - 文件读写工具类
 * 支持从 books.txt 导入图书信息（使用逗号分隔）
 *
 * 文件格式（使用 , 分隔）：
 *
 * 普通书籍：
 *   Book,B001,Java入门,张三,59.9,2023-01,计算机
 * 教材：
 *   TextBook,T001,数据结构,李四,89.0,2022-05,计算机,CS101,高等出版社
 * 小说：
 *   Novel,N001,三体,刘慈欣,45.0,2020-03,文学,科幻
 */
public class FileUtil {

    public static List<Book> loadBooksFromFile(String path) {
        List<Book> books = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 跳过空行和注释行
                if (line.trim().isEmpty() || line.startsWith("//")) {
                    continue;
                }
                // 使用逗号分割行（修正为逗号分隔）
                String[] parts = line.split(",");
                // 验证分割后的字段数量是否合法
                if (parts.length < 2) {
                    System.out.println("❌ 无效行格式：" + line);
                    continue;
                }

                String type = parts[0];
                try {
                    switch (type.toLowerCase()) {
                        case "textbook":
                            // 教材格式：id,name,author,price,publishTime,category,minCategory,number,publisher
                            if (parts.length != 9) {
                                throw new IllegalArgumentException("字段数量错误（需9个字段）");
                            }
                            books.add(new TextBook(
                                    parts[1],    // id
                                    parts[2],    // name
                                    parts[3],    // author
                                    Double.parseDouble(parts[4]),  // price
                                    parts[5],    // publishTime
                                    parts[6],    // category
                                    parts[0],    // minCategory
                                    parts[7],    // number
                                    parts[8]     // publisher
                            ));
                            break;

                        case "novel":
                            // 小说格式：id,name,author,price,publishTime,category,minCategory,faction
                            if (parts.length != 8) {
                                throw new IllegalArgumentException("字段数量错误（需8个字段）");
                            }
                            books.add(new Novel(
                                    parts[1],    // id
                                    parts[2],    // name
                                    parts[3],    // author
                                    Double.parseDouble(parts[4]),  // price
                                    parts[5],    // publishTime
                                    parts[6],    // category
                                    parts[0],    // minCategory
                                    parts[7]     // faction
                            ));
                            break;

                        case "book":
                            // 普通书籍格式：id,name,author,price,publishTime,category,minCategory
                            if (parts.length != 7) {
                                throw new IllegalArgumentException("字段数量错误（需7个字段）");
                            }
                            books.add(new Book(
                                    parts[1],    // id
                                    parts[2],    // name
                                    parts[3],    // author
                                    Double.parseDouble(parts[4]),  // price
                                    parts[5],    // publishTime
                                    parts[6],    // category
                                    parts[0]     // minCategory
                            ));
                            break;

                        default:
                            System.out.println("⚠️ 未知类型：" + type);
                    }
                } catch (Exception e) {
                    System.out.println("❌ 无法解析行 [" + line + "]，错误：" + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ 读取文件失败：" + e.getMessage());
        }
        return books;
    }
}