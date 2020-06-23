package com.reading_app.bookboogie;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.Serializable;

public class Book implements Serializable {

//    public int img;     // 책 이미지 리소스 id를 담는 변수.
//    public String title;
//    public double rating;
//    public int readYear;
//    public int readMonth;
//    public String category;
//    public String memo;

    // 검색해서 받아올 수 있는 정보
    public String title; // 검색결과 문서의 제목
    public String image = "";
    public String author;
    public String  price;
    public String publisher;
    public String isbn;
    public String description;

    // 사용자가 입력해야 하는 정보
    public double rating;
    public int readYear;
    public int readMonth;
    public String category;
    public String memo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // 책 이미지를 가져올때 바로 uri로 가져오도록 함.
    public Uri getImage() {

        Uri image_uri = Uri.parse(image);

        return image_uri;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReadYear() {
        return readYear;
    }

    public void setReadYear(int readYear) {
        this.readYear = readYear;
    }

    public int getReadMonth() {
        return readMonth;
    }

    public void setReadMonth(int readMonth) {
        this.readMonth = readMonth;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Book(){
        this.title = "title";
        this.image = "image";
        this.author = "author";
        this.price = "price";
        this.publisher = "publisher";
        this.isbn = "isbn";
        this.description = "description";
        this.rating = 0;
        this.readYear = 0;
        this.readMonth = 0;
        this.category = "category";
        this.memo = "memo";
    }

    public Book(String title, String image, String author, String price, String publisher,
                String isbn, String description, double rating, int readYear, int readMonth, String category, String memo){
        this.title = title;
        this.image = image;
        this.author = author;
        this.price = price;
        this.publisher = publisher;
        this.isbn = isbn;
        this.description = description;
        this.rating = rating;
        this.readYear = readYear;
        this.readMonth = readMonth;
        this.category = category;
        this.memo = memo;
    }







}
