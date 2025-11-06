package service;

import entity.Book;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BookService {

    //--------1.图书的增删改查--------

    public boolean saveBooks();

    //添加图书（存入List集合）
    public boolean addBook(Book book);

    //删除图书（通过ID）
    public boolean deleteBook(String id);

    //修改图书
    public boolean updateBook(Book book);

    //查询图书（按照ID/按书名）
    public Book getBookById(String id);
    public Book getBookByName(String name);

    //显示所有图书
    public List<Book> listAllBooks();


    //--------2.分类管理(Map<String,List<Book>>)---------
    //查看某类图书
    public List<Book> listCategory(String category);
    //强制刷新图书分类
    public void reBuildCategory();
    //遍历所有图书（按照分类排列）
    public void listAllBooksByCategory();


    //--------3.借阅记录---------
    //借阅
    public boolean borrowBook(String name , String id);
    //归还
    public boolean returnBook(String name , String id);

    //查看借阅记录
    public List<Book> listBorrowRecordByName(String name);
    //查看所有已借阅图书
    public Set<String> listAllBorrowBooks();
    //查看所有借阅人
    public Set<String> listAllBorrower();
    //查看所有借阅记录
    public void listAllBorrowRecord();



    //--------4.图书排序（拓展）---------

}


