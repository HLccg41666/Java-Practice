package view;

import entity.Book;
import entity.Novel;
import entity.TextBook;
import service.BookService;
import service.BookServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import Main.Main;

public class BookManagerGUI extends JFrame {


    private static BookService bookService = new BookServiceImpl();
    private JTable table;
    private DefaultTableModel tableModel;

    public BookManagerGUI() {
        setTitle("图书管理系统 GUI");
        setSize(900, 500); // 加宽窗口以显示更多列
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
        refreshTable();
        setVisible(true);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // ======= 表格区域（显示所有属性） =======
        // 表格列：类型、ID、书名、作者、价格、出版时间、分类、特有属性1、特有属性2
        tableModel = new DefaultTableModel(
                new Object[]{"类型", "ID", "书名", "作者", "价格", "出版时间", "分类", "特有属性1", "特有属性2"},
                0
        );
        table = new JTable(tableModel);
        // 调整列宽
        table.getColumnModel().getColumn(0).setPreferredWidth(60);  // 类型
        table.getColumnModel().getColumn(1).setPreferredWidth(60);  // ID
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // 书名
        table.getColumnModel().getColumn(5).setPreferredWidth(80);  // 出版时间
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ======= 操作区域 =======
        JPanel btnPanel = new JPanel();

        JButton addBtn = new JButton("添加图书");
        JButton delBtn = new JButton("删除选中图书");
        JButton refreshBtn = new JButton("刷新");

        btnPanel.add(addBtn);
        btnPanel.add(delBtn);
        btnPanel.add(refreshBtn);

        add(btnPanel, BorderLayout.SOUTH);

        // ======= 事件绑定 =======
        addBtn.addActionListener(e -> openAddDialog());
        delBtn.addActionListener(e -> deleteSelectedBook());
        refreshBtn.addActionListener(e -> refreshTable());
    }

    /**
     * 刷新表格：显示所有图书，区分类型并展示特有属性
     */
    private void refreshTable() {
        tableModel.setRowCount(0); // 清空表格
        List<Book> list = bookService.listAllBooks();
        for (Book book : list) {
            Object[] rowData;
            // 根据图书类型处理特有属性
            if (book instanceof TextBook) {
                TextBook textbook = (TextBook) book;
                rowData = new Object[]{
                        "教材",
                        textbook.getId(),
                        textbook.getName(),
                        textbook.getAuthor(),
                        textbook.getPrice(),
                        textbook.getPublishTime(),
                        textbook.getCategory(),
                        textbook.getMinCategory(),
                        "编号：" + textbook.getNumber(),  // 教材特有属性：number
                        "出版社：" + textbook.getPublisher()  // 教材特有属性：publisher
                };
            } else if (book instanceof Novel) {
                Novel novel = (Novel) book;
                rowData = new Object[]{
                        "小说",
                        novel.getId(),
                        novel.getName(),
                        novel.getAuthor(),
                        novel.getPrice(),
                        novel.getPublishTime(),
                        novel.getCategory(),
                        novel.getMinCategory(),
                        "流派：" + novel.getFaction(),  // 小说特有属性：faction
                        ""  // 无第二个特有属性，留空
                };
            } else {
                // 普通书籍
                rowData = new Object[]{
                        "普通",
                        book.getId(),
                        book.getName(),
                        book.getAuthor(),
                        book.getPrice(),
                        book.getPublishTime(),
                        book.getCategory(),
                        book.getMinCategory(),
                        "",  // 无特有属性
                        ""
                };
            }
            tableModel.addRow(rowData);
        }
    }

    /**
     * 打开添加图书对话框（支持选择类型，动态显示对应属性输入框）
     */
    private void openAddDialog() {
        // 1. 先选择图书类型
        String[] types = {"普通书籍", "教材", "小说"};
        String selectedType = (String) JOptionPane.showInputDialog(
                this,
                "选择图书类型：",
                "添加图书 - 选择类型",
                JOptionPane.QUESTION_MESSAGE,
                null,
                types,
                types[0]
        );
        if (selectedType == null) {
            return; // 用户取消选择
        }

        // 2. 根据类型创建对应输入框
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField publishTimeField = new JTextField(); // 新增：出版时间
        JTextField categoryField = new JTextField();
        JTextField mincategoryField = new JTextField();


        // 特有属性输入框
        JTextField field1 = new JTextField(); // 教材：number；小说：faction
        JTextField field2 = new JTextField(); // 教材：publisher；小说：无

        // 动态组装输入表单
        java.util.List<Object> formItems = new java.util.ArrayList<>();
        formItems.add("图书ID:");
        formItems.add(idField);
        formItems.add("书名:");
        formItems.add(nameField);
        formItems.add("作者:");
        formItems.add(authorField);
        formItems.add("价格:");
        formItems.add(priceField);
        formItems.add("出版时间:"); // 新增
        formItems.add(publishTimeField);
        formItems.add("分类:");
        formItems.add(categoryField);
        formItems.add("大分类:");
        formItems.add(mincategoryField);

        // 根据类型添加特有属性输入项
        if (selectedType.equals("教材")) {
            formItems.add("教材编号:");
            formItems.add(field1);
            formItems.add("出版社:");
            formItems.add(field2);
        } else if (selectedType.equals("小说")) {
            formItems.add("小说流派:");
            formItems.add(field1);
        }

        // 转换为数组（JOptionPane要求）
        Object[] form = formItems.toArray();

        // 显示对话框
        int option = JOptionPane.showConfirmDialog(
                this,
                form,
                "添加图书 - " + selectedType,
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            try {
                // 基础属性
                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                String author = authorField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                String publishTime = publishTimeField.getText().trim(); // 出版时间
                String category = categoryField.getText().trim();
                String minCategory = mincategoryField.getText().trim();


                // 校验必填项
                if (id.isEmpty() || name.isEmpty() || author.isEmpty() || publishTime.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "ID、书名、作者、出版时间不能为空！");
                    return;
                }

                Book book = null;
                // 根据类型创建对应实例
                switch (selectedType) {
                    case "普通书籍":
                        book = new Book(id, name, author, price, publishTime, category , minCategory);
                        break;
                    case "教材":
                        String number = field1.getText().trim();
                        String publisher = field2.getText().trim();
                        if (number.isEmpty() || publisher.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "教材编号和出版社不能为空！");
                            return;
                        }
                        book = new TextBook(id, name, author, price, publishTime, category, number, publisher, minCategory);
                        break;
                    case "小说":
                        String faction = field1.getText().trim();
                        if (faction.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "小说流派不能为空！");
                            return;
                        }
                        book = new Novel(id, name, author, price, publishTime, category, minCategory , faction);
                        break;
                }

                // 添加到服务层
                bookService.addBook(book);
                refreshTable();
                JOptionPane.showMessageDialog(this, "添加成功！");

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "价格格式错误（请输入数字）！");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "添加失败：" + e.getMessage());
            }
        }
    }

    /**
     * 删除选中的图书
     */
    private void deleteSelectedBook() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "请先选中一行！");
            return;
        }

        String id = (String) table.getValueAt(row, 1); // ID在第2列（索引1）
        boolean result = bookService.deleteBook(id);

        if (result) {
            JOptionPane.showMessageDialog(this, "删除成功！");
        } else {
            JOptionPane.showMessageDialog(this, "删除失败，未找到图书！");
        }

        refreshTable();
    }

    private static void preloadSampleData() {
        // 推荐使用相对路径，避免环境依赖
        String filePath = "D:/Java Project/Practices_Java_25/Practices_Week_ 3/Library-Management/books.txt"; // 假设文件在项目根目录
        List<Book> list = util.FileUtil.loadBooksFromFile(filePath);

        if (list.isEmpty()) {
            System.out.println("⚠️ 未读取到文件数据，加载默认样例数据...");
            // 可选：此处可添加默认数据，防止空库
            // 例如：bookService.addBook(new Book("B000", "默认书籍", "未知", 0.0, "2024", "其他"));
        } else {
            for (Book b : list) {
                bookService.addBook(b);
            }
            System.out.println("✅ 成功从文件导入 " + list.size() + " 本图书。");
        }
    }

    public static void main(String[] args) {
        // 确保在EDT中运行GUI
        preloadSampleData();

        SwingUtilities.invokeLater(() -> new BookManagerGUI());
    }
}