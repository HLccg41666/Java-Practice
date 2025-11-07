package entity;

public class Book {
    public String id;
    private String name;//书名
    private String author;//作者
    private double price;//价格
    private String publishTime;//出版时间
    private String category;//分类
    private String minCategory;//小分类

    public Book(String id, String name, String author, double price, String publishTime, String category,String minCategory) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.price = price;
        this.publishTime = publishTime;
        this.category = category;
        this.minCategory = minCategory;

    }
    public Book(){}
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMinCategory() {
        return minCategory;
    }
    public void setMinCategory(String minCategory) {
        this.minCategory = minCategory;
    }

    @Override
    public String toString(){
        return String.format("编号:%s | 书名:%s | 作者:%s | 价格:%.2f | 出版时间:%s | 分类:%s", id,name,author,price,publishTime,category);
    }

}
