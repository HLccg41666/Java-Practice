/*
在学习完 Java 集合框架（List、Set、Map、泛型） 之后，需要通过一个小型项目来综合应用。
本项目实现一个 控制台版图书管理系统雏形，功能包括 图书的增删改查、
分类管理、借阅记录 等，旨在强化集合与泛型的使用。
*/

/*
    基础功能
添加图书（存入集合）
删除图书（通过 ID）
修改图书信息
查询图书（按 ID / 按书名）
显示所有图书
分类管理

使用 Map<String, List<Book>> 管理不同类别图书（如 “计算机类”、“文学类”）
借阅记录（简化版）

使用 Set<String> 存储借阅者姓名，避免重复
使用 Map<String, List<Book>> 存储借阅情况（键为用户名，值为借阅的书籍列表）
可扩展功能
图书排序（按价格 / 出版时间 / 名称）
数据导出到文件
 */

package Main;
import entity.Book;
import entity.Novel;
import entity.TextBook;
import service.BookService;
import service.BookServiceImpl;
import util.InputUtil;

import java.util.List;

public class Main {
    private static final BookService bookService = new BookServiceImpl();

    public static void main(String[] args) {

        while (true) {
            System.out.println("/************图书管理系统**********/");
            System.out.println("--------------------");
            System.out.println("一、图书的增删改查功能");
            System.out.println("二、分类管理功能");
            System.out.println("三、借阅记录功能");
            System.out.println("四、退出");
            System.out.println("--------------------");
            int choiceOne = InputUtil.inputInt("请输入您的选择：");
            switch (choiceOne) {

                case 1:
                    System.out.println("------图书的增删改查功能------");
                    System.out.println("1。添加图书");
                    System.out.println("2.删除图书");
                    System.out.println("3.修改图书");
                    System.out.println("4.查询图书(id)");
                    System.out.println("5.查询图书(name)");
                    System.out.println("6.显示所有图书");
                    int choiceTwo = InputUtil.inputInt("请输入您的选择：");
                    switch (choiceTwo) {
                        case 1 -> addBookFlow();
                        case 2 -> deleteBookFlow();
                        case 3 -> updateBookFlow();
                        case 4 -> getBookByIdFlow();
                        case 5 -> getBookByNameFlow();
                        case 6 -> listAllBooksFlow();
                    }
                    break;
                case 2:
                    System.out.println("------分类管理功能------");
                    System.out.println("1.查看某类图书");
                    System.out.println("2.强制刷新图书分类");
                    System.out.println("3.遍历所有图书（按照分类排列）");
                    int choiceThree = InputUtil.inputInt("请输入您的选择：");
                    switch (choiceThree) {
                        case 1 -> listCategoryFlow();
                        case 2 -> reBuildCategoryFlow();
                        case 3 -> listAllBooksByCategoryFlow();
                    }
                    break;
                case 3:
                    System.out.println("------借阅记录功能------");
                    System.out.println("1.借阅");
                    System.out.println("2.归还");
                    System.out.println("3.查看用户借阅记录");
                    System.out.println("4.查看所有已借阅图书");
                    System.out.println("5.查看所有借阅人");
                    System.out.println("6.遍历所有借阅记录（按用户排序）");
                    int choiceFour = InputUtil.inputInt("请输入您的选择：");
                    switch (choiceFour) {
                        case 1 -> borrowBookFlow();
                        case 2 -> returnBookFlow();
                        case 3 -> listBorrowRecordByNameFlow();
                        case 4 -> listAllBorrowBooksFlow();
                        case 5 -> listAllBorrowerFlow();
                        case 6 -> listAllBorrowRecordFlow();
                    }
                    break;
                case 4:
                    System.out.println("退出系统，感谢使用");
                    System.exit(0);
                    break;
                default:
                    System.out.println("输入错误，请重新输入");
            }
        }
    }

    //-----------图书的增删改查功能-----------
    //添加图书（存入List集合）
    private static void addBookFlow(){
        System.out.println("-----图书添加------");
        String id = InputUtil.inputString("请输入图书编号：");
        String name = InputUtil.inputString("请输入图书名：");
        String author = InputUtil.inputString("请输入图书作者：");
        double price = InputUtil.inputDouble("请输入图书价格：");
        String publishTime = InputUtil.inputString("请输入图书出版时间：");
        String category = InputUtil.inputString("请输入图书分类（实际类）：");
        String minCategory = InputUtil.inputString("请输入图书大分类（textbook/novel）：");
        Book book = null;
        if (minCategory.equalsIgnoreCase("textbook")){
            String number = InputUtil.inputString("请输入图书课程编号：");
            String publisher = InputUtil.inputString("请输入图书出版社：");
             book = new TextBook(id,name,author,price,publishTime,category,minCategory,number,publisher);
        } else if (minCategory.equalsIgnoreCase("novel")){
                String faction = InputUtil.inputString("请输入图书派系：");
                 book = new Novel(id,name,author,price,publishTime,category, minCategory, faction);
        }else{
            book = new Book(id,name,author,price,publishTime, minCategory, category);
        }
        boolean ok = bookService.addBook(book);
        if(ok) System.out.println("添加成功");

    }

    //删除图书
    private static void deleteBookFlow(){
        System.out.println("-----图书删除------");
        String id = InputUtil.inputString("请输入图书编号：");
        boolean ok =  bookService.deleteBook(id);
        if(ok) System.out.println("删除成功");
    }

    //修改图书
    private static void updateBookFlow(){
        System.out.println("-----图书修改------");
        String id = InputUtil.inputString("请输入图书编号：");
        String name = InputUtil.inputString("请输入新图书名：");
        String author = InputUtil.inputString("请输入新图书作者：");
        double price = InputUtil.inputDouble("请输入新图书价格：");
        String publishTime = InputUtil.inputString("请输入新图书出版时间：");
        String newCategory = InputUtil.inputString("请输入新图书分类：");
        String minCategory = InputUtil.inputString("请输入新图书大分类（textbook/novel）：");
        Book newBook = null;
       if(minCategory.equalsIgnoreCase("textbook")){
           String number = InputUtil.inputString("请输入新图书课程编号：");
           String publisher = InputUtil.inputString("请输入新图书出版社：");
           newBook = new TextBook(id,name,author,price,publishTime,newCategory, minCategory, number,publisher);
       } else if (minCategory.equalsIgnoreCase("novel")) {
           String faction = InputUtil.inputString("请输入新图书派系：");
           newBook = new Novel(id,name,author,price,publishTime,newCategory, minCategory, faction);
       }else {
           newBook = new Book(id,name,author,price,publishTime,newCategory, minCategory);
       }
        boolean ok = bookService.updateBook(newBook);
        if(ok) System.out.println("修改成功");
    }

    //查询图书（按照ID/按书名）
    private static void getBookByIdFlow(){
        System.out.println("-----图书修改(按id)------");
        String id = InputUtil.inputString("请输入你要查询图书的id：");
        System.out.println("图书信息如下：");
        System.out.println(bookService.getBookById(id));
        System.out.println();
    }

    private static void getBookByNameFlow(){
        System.out.println("-----图书修改(按name)------");
        String name = InputUtil.inputString("请输入你要查询图书的书名：");
        System.out.println("图书信息如下：");
        System.out.println(bookService.getBookByName(name));
        System.out.println();
    }

    //显示所有图书
    private static void listAllBooksFlow(){
        System.out.println("图书列表如下：");
        bookService.listAllBooks().forEach(System.out::println);
        System.out.println();
    }


    //-----------分类管理功能-----------
    //查看某类图书
    private static void listCategoryFlow(){
        System.out.println("-----查看某类图书------：");
        String minCategory = InputUtil.inputString("请输入你要查看的图书种类：");
        if(!bookService.listCategory(minCategory).isEmpty()){
            System.out.println("图书信息如下：");
            bookService.listCategory(minCategory).forEach(System.out::println);
        }else {
            System.out.println("没有该类图书");
        }
    }

    //强制刷新图书分类
    private static void reBuildCategoryFlow(){
        System.out.println("-----强制刷新图书分类------：");
        bookService.reBuildCategory();
        System.out.println("刷新成功");
    }

    //遍历所有图书（按照分类排列）
    private static void listAllBooksByCategoryFlow(){
        System.out.println("-----遍历所有图书（按照分类排列）------：");
        System.out.println("图书信息如下：");
        bookService.listAllBooksByCategory();
    }


    //-----------借阅记录功能-----------
    //借阅
    private static void borrowBookFlow(){
        System.out.println("-----借阅------：");
        String userName = InputUtil.inputString("请输入借阅人姓名：");
        String bookId = InputUtil.inputString("请输入借阅图书的编号：");
        boolean ok = bookService.borrowBook(userName,bookId);
        if (ok) System.out.println("借阅成功");
    }

    //归还
    private static void returnBookFlow(){
        System.out.println("-----归还------：");
        String userName = InputUtil.inputString("请输入归还者姓名：");
        String bookId = InputUtil.inputString("请输入归还图书的编号：");
        boolean ok = bookService.returnBook(userName,bookId);
        if (ok) System.out.println("归还成功");
    }

    //查看借阅记录
    private static void listBorrowRecordByNameFlow(){
        System.out.println("-----查看用户借阅记录------：");
        String userName = InputUtil.inputString("请输入用户名：");
        if(!bookService.listBorrowRecordByName(userName).isEmpty()){
            System.out.println(userName+"用户的借阅记录如下：");
            bookService.listBorrowRecordByName(userName).forEach(System.out::println);
        }else {
            System.out.println("该用户为借阅记录");
        }
    }

    //查看所有已借阅图书
    private static void listAllBorrowBooksFlow() {
        System.out.println("-----查看所有已借阅图书-----");
        if (!bookService.listAllBorrowBooks().isEmpty()) {
            bookService.listAllBorrowBooks().forEach(System.out::println);
        }else {
            System.out.println("暂无图书被借阅");
        }
    }

    //查看所有借阅人
    private static void listAllBorrowerFlow() {
        System.out.println("-----查看所有借阅人-----");
        if (!bookService.listAllBorrower().isEmpty()) {
            System.out.println(bookService.listAllBorrower());
        }else {
            System.out.println("暂无任何用户借阅图书");
        }
    }

    //遍历所有借阅记录（按用户排序）
    private static void listAllBorrowRecordFlow(){
        System.out.println("-----遍历所有借阅记录（按用户排序）-----");
        bookService.listAllBorrowRecord();
    }

}


