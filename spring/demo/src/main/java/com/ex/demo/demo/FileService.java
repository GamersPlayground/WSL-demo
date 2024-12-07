package com.ex.demo.demo;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

@Service
public class FileService {
    public void writeToFile(String filePath, String content) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    public void deleteFile(String filePath){

        try{
            Path path = Paths.get(filePath);
            if(Files.exists(path)){
                Files.delete(path);
            }
        } catch (IOException e) {
            System.err.println("error creating file: " + e.getMessage());
        }
    }
    public void createFile(String filePath){
        //String opsys = System.getProperty("os.name");    
        String dir = System.getProperty("user.dir");

        System.out.println(dir);


        try{
            Path path = Paths.get(filePath);
            System.out.println(path); 

            if(!Files.exists(path)){
                Files.createFile(path); 
                System.out.println("File created Successfully: " + path);

            } else {
                System.out.println("File already exists: " + path);
            }
        } catch (IOException e) {
            System.err.println("error creating file: " + e.getMessage());
        }
    }
    
}

