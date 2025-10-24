package util;
import java.util.Scanner;

    /**
     * InputUtil - 控制台输入工具
     * - 封装了字符串、整数、双精度数的安全读取
     * - 当输入非法时会提示并循环等待用户重新输入
     */
    public class InputUtil {
        private static final Scanner scanner = new Scanner(System.in);

        /**
         * 读取非空字符串
         * @param prompt 提示信息
         * @return 用户输入的字符串（已 trim）
         */
        public static String inputString(String prompt) {
            while (true) {
                System.out.print(prompt);
                String line = scanner.nextLine();
                if (line == null) continue;
                line = line.trim();
                if (!line.isEmpty()) return line;
                System.out.println("输入不能为空，请重新输入。");
            }
        }

        /**
         * 读取整数（并处理异常）
         * @param prompt 提示信息
         * @return 用户输入的整数
         */
        public static int inputInt(String prompt) {
            while (true) {
                System.out.print(prompt);
                String line = scanner.nextLine();
                try {
                    return Integer.parseInt(line.trim());
                } catch (Exception e) {
                    System.out.println("输入错误：请输入整数。");
                }
            }
        }

        /**
         * 读取 double（价格等）
         * @param prompt 提示信息
         * @return 用户输入的 double
         */
        public static double inputDouble(String prompt) {
            while (true) {
                System.out.print(prompt);
                String line = scanner.nextLine();
                try {
                    return Double.parseDouble(line.trim());
                } catch (Exception e) {
                    System.out.println("输入错误：请输入数字（可带小数）。");
                }
            }
        }
    }

