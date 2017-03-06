package com.swyftlabs.swyftbooks1.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Gerard on 2/16/2017.
 */

public class ResultItem implements Parcelable{


    private long id = -1;
    private String bookTitle;
    private String bookAuthor;
    private String bookISBN;
    private String bookImageLink;
    private String bookEdition;
    private String bookPublisher;
    private String bookListPrice;
    private String bookBinding;
    private String bookEAN;
    private String bookEISBN;

    public ArrayList<String> authors = new ArrayList<>();
    public ArrayList<String> images = new ArrayList<>();

    public ResultItem() {
    }

    public ResultItem(long id, String bookTitle, String bookAuthor, String bookISBN, String bookEAN, String bookEdition, String bookPublisher, String bookBinding, String bookListPrice, String bookImageLink) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookISBN = bookISBN;
        this.bookEAN = bookEAN;

        this.bookEdition = bookEdition;
        this.bookPublisher = bookPublisher;
        this.bookBinding = bookBinding;
        this.bookImageLink = bookImageLink;
        this.bookListPrice = bookListPrice;
    }

    public ResultItem(Parcel incoming){

    }

    public String getBookAuthor() {
        if(bookAuthor == null) {
            bookAuthor = "";
            for (int i = 0; i < authors.size(); i++) {
                if (i == authors.size() - 1) {
                    bookAuthor += authors.get(i);
                    break;
                }
                bookAuthor += authors.get(i) + ", ";
            }
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
        if(bookImageLink == null) {
            bookImageLink = images.get(0);
        }
        return bookImageLink;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBookImageLinkForParcel(){
        return bookImageLink;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getBookImageLink());
        dest.writeString(getBookTitle());
        dest.writeString(getBookAuthor());
        dest.writeString(getBookISBN());
        dest.writeString(getBookEAN());
        dest.writeString(getBookEdition());
        dest.writeString(getBookPublisher());
        dest.writeString(getBookBinding());
        dest.writeString(getBookListPrice());
    }

    public static final Parcelable.Creator<ResultItem> CREATOR = new Parcelable.Creator<ResultItem>(){

        @Override
        public ResultItem createFromParcel(Parcel incoming) {
            ResultItem item = new ResultItem();
            item.setBookImageLink(incoming.readString());
            item.setBookTitle(incoming.readString());
            item.setBookAuthor(incoming.readString());
            item.setBookISBN(incoming.readString());
            item.setBookEAN(incoming.readString());
            item.setBookEdition(incoming.readString());
            item.setBookPublisher(incoming.readString());
            item.setBookBinding(incoming.readString());
            item.setBookListPrice(incoming.readString());
            return item;
        }

        @Override
        public ResultItem[] newArray(int size) {
            return new ResultItem[size];
        }


    };

    public String getBookEISBN() {
        return bookEISBN;
    }

    public void setBookEISBN(String bookEISBN) {
        this.bookEISBN = bookEISBN;
    }
}
