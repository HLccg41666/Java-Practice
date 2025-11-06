package util;

import entity.Book;
import entity.Novel;
import entity.TextBook;

import java.io.*;
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

    /*
    * 作用：从文件中读取图书信息
    * @param path 文件路径
    * @return 返回一个 Book 对象的 List
    * */
    public static List<Book> BookRead(String path) {
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

    /*
    * 作用：将图书信息写入文件
    * @param path 文件路径
    * @param books 图书信息
    * @return 返回一个布尔值，表示是否成功写入文件
    * */
    public static boolean BookWrite(String path, List<Book> books) {
        File file = new File(path);

        if (file.exists()) {
            file.mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Book book : books) {
                bw.write(formatBookLine(book));
                bw.newLine();
            }

            bw.flush();
            return true;

        } catch (IOException e) {
            System.out.println("写入文件失败：" + e.getMessage());
            return false;
        }

    }

    /*
    * 作用：格式化book类型数据存入文件
    * @param b Book类型，图书信息
    * @return 返回一个字符串类型的图书信息
    * */
    private static String formatBookLine(Book b) {
        //检查变量 b 是否是 TextBook 类型（或其子类型）的实例。
        //如果检查通过，自动将 b 转换为 TextBook 类型，并赋值给新变量 tb（无需显式强制转换）。
        if (b instanceof TextBook tb) {
            // TextBook,id,title,author,price,publishTime,category,minCategory,number,publisher

            //String.join() 是 Java 8 及以上版本提供的一个静态方法，用于将多个字符串（或可转换为字符串的对象）
            //通过指定的分隔符连接成一个新的字符串。它简化了字符串拼接的操作，避免了手动循环拼接分隔符的繁琐。
            return String.join(",",
                    "Textbook",
                    safe(b.getId()),
                    safe(b.getName()),
                    safe(b.getAuthor()),
                    String.valueOf(b.getPrice()),
                    safe(b.getPublishTime()),
                    safe(b.getCategory()),
                    safe(tb.getNumber()),
                    safe(tb.getPublisher())
            );

        } else if (b instanceof Novel nv) {
            // Novel,id,title,author,price,publishTime,category,minCategory,faction
            return String.join(",",
                    "Novel",
                    safe(b.getId()),
                    safe(b.getName()),
                    safe(b.getAuthor()),
                    String.valueOf(b.getPrice()),
                    safe(b.getPublishTime()),
                    safe(b.getCategory()),
                    safe(nv.getFaction())
            );


        } else {
            // Book,id,title,author,price,publishTime,category,minCategory
            return String.join(",",
                    "Book",
                    safe(b.getId()),
                    safe(b.getName()),
                    safe(b.getAuthor()),
                    String.valueOf(b.getPrice()),
                    safe(b.getPublishTime()),
                    safe(b.getCategory())
            );

        }
    }

    private static String safe(String s) {
        return s == null ? "" : s.replace("\n", " ").replace("\r", " ");
    }
}