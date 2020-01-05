package com.eastcom.harup.aidl;
import com.eastcom.harup.aidl.Book;
import com.eastcom.harup.aidl.IBookCallback;
// IBookManager.aidl

// Declare any non-default types here with import statements

interface IBookManager {

    void addBook(in Book book);

    List<Book> getBookList();

    void registerListener(IBookCallback callback);

    void unRegisterListener(IBookCallback callback);
}
