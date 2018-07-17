package com.example.demo;

import java.nio.file.FileStore;

public class FileStorageException extends RuntimeException {

    public FileStorageException(String message){
        super(message);
    }

    public FileStorageException(String message,Throwable thr){
        super(message,thr);
    }
}
