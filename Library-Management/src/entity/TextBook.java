package entity;

import java.util.Date;

public class TextBook extends Book {
    private String number;//课程编号
    private String publisher;//出版社

    public TextBook(String id, String name, String author, double price, String publishTime, String category,String minCategory, String number, String publisher) {
        super(id, name, author, price, publishTime ,category , minCategory);
        this.number = number;
        this.publisher = publisher;
    }
    public TextBook(){}

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | 出版社:%s | 课程代码:%s", publisher,number );
    }
}
