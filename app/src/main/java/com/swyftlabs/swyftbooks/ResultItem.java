package com.swyftlabs.swyftbooks;

import java.util.ArrayList;

/**
 * Created by Gerard on 2/16/2017.
 */

public class ResultItem {

    private String bookTitle;
    private String bookAuthor;
    private String bookISBN;
    private String bookImageLink;
    private String bookEdition;
    private String bookPublisher;
    private String bookListPrice;
    private String bookBinding;
    private String bookEAN;



    ArrayList<String> authors = new ArrayList<>();
    ArrayList<String> images = new ArrayList<>();



    public ResultItem() {

    }

    public String getBookAuthor() {
        bookAuthor = "";
        for(int i = 0; i < authors.size(); i++){
            if(i == authors.size()-1){
                bookAuthor += authors.get(i);
                break;
            }
            bookAuthor += authors.get(i) + ", ";
        }
        return bookAuthor;
    }

    public String getBookEAN() {
        return bookEAN;
    }

    public void setBookEAN(String bookEAN) {
        this.bookEAN = bookEAN;
    }

    public String getBookBinding() {
        return bookBinding;
    }

    public void setBookBinding(String bookBinding) {
        this.bookBinding = bookBinding;
    }

    public String getBookPublisher() {
        return bookPublisher;
    }

    public void setBookPublisher(String bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

    public String getBookListPrice() {
        return bookListPrice;
    }

    public void setBookListPrice(String bookListPrice) {
        this.bookListPrice = bookListPrice;
    }

    public String getBookEdition() {
        return bookEdition;
    }

    public void setBookEdition(String bookEdition) {
        this.bookEdition = bookEdition;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookImageLink() {
        return images.get(0);
    }

    public void setBookImageLink(String bookImageLink) {
        this.bookImageLink = bookImageLink;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
}
