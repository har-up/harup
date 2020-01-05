// IBookCallback.aidl
package com.eastcom.harup.aidl;
import com.eastcom.harup.aidl.Book;
// Declare any non-default types here with import statements

interface IBookCallback {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onAdd(in Book book);
}
