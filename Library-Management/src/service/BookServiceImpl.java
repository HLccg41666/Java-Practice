package service;

import entity.Book;
import util.FileUtil;

import java.util.*;

public class BookServiceImpl implements BookService {
    //通过List<Book>集合来存储图书
    private List<Book> booksList = new ArrayList<>();

    //通过Map<String,List<Book>>来存储图书分类(图书分类，图书列表)
    private Map<String,List<Book>> categoryMap = new HashMap<>();

    //通过set集合记录借阅出去的书
    private Set<String> borrowerSetById = new HashSet<>();
    
    //Map<String List<Book>>来存储借阅情况（借阅人，书籍列表）
    private Map<String,List<Book>> borrowRecordMap = new HashMap<>();



    private  String filePath = "";

    public BookServiceImpl(){
        this("Library-Management/books.txt");
    }
    public BookServiceImpl(String filePath){
        this.filePath = filePath;
        List<Book> loaded = FileUtil.BookRead(filePath);
        if(!loaded.isEmpty()){
            booksList.addAll(loaded);
            System.out.println("成功加载" + loaded.size() + "本图书");
        }else {
            System.out.println("错误：未加载任何图书");
        }
    }

    public boolean saveBooks(){
        boolean b = FileUtil.BookWrite(filePath, booksList);
        return b;
    }


    //--------1.图书的增删改查--------

    //添加图书（存入List集合）
    public boolean addBook(Book book){
        if(book == null || book.getId() == null || book.getId().isEmpty()){
            System.out.println("图书信息输入不完整");
            return false;
        }
        if(getBookById(book.getId()) != null){
            System.out.println("图书id已存在");
            return false;
        }
        boolean ok = booksList.add(book);
        if(ok) saveBooks();
        //添加分类
        categoryMap.computeIfAbsent(book.getCategory(), k -> new ArrayList<>()).add(book);
        return true;
    }

    //删除图书（通过ID）
    public boolean deleteBook(String id) {
        Book book = getBookById(id);

        if (book == null) {
            System.out.println("删除失败：未找到 ID = " + id + " 的图书。");
            return false;
        }

        if(borrowerSetById.contains(id)){
            System.out.println("删除失败：该图书已被借出。");
            return false;
        }

        boolean ok = booksList.remove(book);
        if(ok) saveBooks();


        List<Book> catBook = categoryMap.get(book.getCategory());
        if(catBook != null){
            catBook.remove(book);
            if(catBook.isEmpty()){
                categoryMap.remove(book.getCategory());
            }
        }
        return true;
    }

    //修改图书
    public boolean updateBook(Book book){
        if(book == null || book.getId() == null){
            System.out.println("图书信息输入不完整");
            return false;
        }
        for (int i = 0; i < booksList.size(); i++) {
             if(booksList.get(i).getId().equals(book.getId())){

                 String oldCategory = booksList.get(i).getCategory();
                 String newCategory = book.getCategory();

                 booksList.set(i,book);

                 //分类不同，需要删除旧分类，添加新分类。
                 if(!oldCategory.equals(newCategory)){
                     List<Book> oldBooks = categoryMap.get(oldCategory);
                     if(oldBooks != null){
                         oldBooks.remove(book);
                         if(oldBooks.isEmpty()){
                             categoryMap.remove(oldCategory);
                         }
                     }
                     categoryMap.computeIfAbsent(newCategory,k->new ArrayList<>()).add(book);
                 }else {
                     List<Book> list = categoryMap.get(oldCategory);
                     if (!list.isEmpty()){
                         int index = list.indexOf(book);
                         if(index >= 0){
                             list.set(index,book);
                         }
                     }
                 }

                 saveBooks();
                 return true;
             }
        }
        System.out.println("修改失败：未找到 ID = " + book.getId() + " 的图书。");
        return false;
    }

    //查询图书（按照ID/按书名）
    public Book getBookById(String id){
        if(id == null || id.isEmpty()){
            System.out.println("图书编号不能为空");
            return null;
        }
        for(Book book : booksList){
            if(book.getId().equals(id)){
                return book;
            }
        }
        return null;
    }
    public Book getBookByName(String name){
        if(name ==null || name.isEmpty()){
            System.out.println("图书名不能为空");
            return null;
        }
        for(Book book : booksList){
            if (book.getName().equals(name)){
                return book;
            }
        }
        return null;
    }

    //显示所有图书
    public List<Book> listAllBooks(){
        //返回副本，避免修改内部数据
        return new ArrayList<>(booksList);
    }



    //--------2.分类管理(Map<String,List<Book>>)---------

    //查看某类图书
    public List<Book> listCategory(String category){
        //返回一个空列表，避免后续null造成空指针异常的可能性
        if (category == null) return Collections.emptyList();
        List<Book> list = categoryMap.get(category);
        return list == null ? Collections.emptyList() : new ArrayList<>(list);
    }

    //强制刷新图书分类
    public void reBuildCategory(){
        categoryMap.clear();
        for (Book book : booksList) {
            categoryMap.computeIfAbsent(book.getCategory(), k -> new ArrayList<>()).add(book);
        }
    }

    //遍历所有图书（按照分类排列）
    public void listAllBooksByCategory(){
        if (!booksList.isEmpty()) {
            for (String key : categoryMap.keySet()) {
                System.out.println('[' + key + "类]：");
                categoryMap.get(key).forEach(System.out::println);
            }
        }else {
            System.out.println("暂无任何图书信息");
        }
    }



    //--------3.借阅记录---------

    //借阅
    public boolean borrowBook(String name , String id){
        if(name == null || id == null){
            System.out.println("借阅人不能为空和图书编号不能为空");
            return false;
        }
        if(!booksList.contains(getBookById(id))){
            System.out.println("图书不存在");
            return false;
        }
        if(borrowerSetById.contains(id)){
            System.out.println("该图书已被借出");
            return false;
        }

        borrowerSetById.add(id);
        borrowRecordMap.computeIfAbsent(name,k->new ArrayList<>()).add(getBookById(id));
        return true;

    }

    //归还
    public boolean returnBook(String name , String id){
        if(name ==null || id == null){
            System.out.println("归还失败：用户名或图书ID为空");
            return false;
        }
        if(!booksList.contains(getBookById(id))){
            System.out.println("归还失败，图书库不存在该书");
            return false;
        }
        if(borrowRecordMap.get(name) == null){
            System.out.println("归还失败：用户当前无借阅记录。");
            return false;
        }
        List<Book> list = borrowRecordMap.get(name);
        if(!list.contains(getBookById(id))){
            System.out.println("归还失败：该用户未借阅 ID =" + id);
            return false;
        }
        list.remove(getBookById(id));
        borrowerSetById.remove(id);
        if(list.isEmpty()){
            borrowRecordMap.remove(name);
        }
        return true;
    }

    //查看用户借阅记录
    public List<Book> listBorrowRecordByName(String name){
        List<Book> list = borrowRecordMap.get(name);
        if(list == null){
            System.out.println("该用户当前无借阅记录。");
            return Collections.emptyList();
        }
        return new ArrayList<>(list);
    }

    //查看所有已借阅图书
    public Set<String> listAllBorrowBooks() {
        return new HashSet<>(borrowerSetById);
    }

    //查看所有借阅人
    public Set<String> listAllBorrower(){
        return new HashSet<>(borrowRecordMap.keySet());
    }

    //遍历所有借阅记录（按用户排序）
    public void listAllBorrowRecord() {
        if (!borrowRecordMap.isEmpty()) {
            for (String name : borrowRecordMap.keySet()) {
                System.out.println("用户" + name + "的借阅记录如下：");
                borrowRecordMap.get(name).forEach(System.out::println);
            }
        }else {
            System.out.println("暂无任何借阅记录");
        }
    }
}
