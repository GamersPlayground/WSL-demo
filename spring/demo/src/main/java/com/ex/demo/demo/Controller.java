package com.ex.demo.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller {
    
    @GetMapping("/get")
    public String doGet() {
        return "<!DOCTYPE html>" +
        "<html>" +
        "<head>" +
        "    <title>Hello</title>" +
        "</head>" +
        "<body>" +
        "    <H1>Hello, API!!</H1>" +
        "    <p>" +
        "    This is a paragraph." +
        "    </p>" +
        "</body>" +
        "</html>";
    }
    
    @Autowired
    private FileService fileWriter;
    private FileService fileCreator; 

    @PostMapping("/post")
    public String postMethodName(@RequestBody String entity) {
        fileCreator = new FileService(); 
        fileWriter = new FileService();
        String cleanedUpEntity = cleanUpString(entity);
        //System.out.print(entity);

        try{
            fileCreator.createFile();
            fileWriter.writeToFile("D:\\dev\\Java\\spring\\testFile.gcc", cleanedUpEntity);
            Process process = Runtime.getRuntime().exec("wsl <gcc testFile.gcc>");            
            return /*"This is Posted" + entity + "\n\n"*/entity + "\n\n" + execute() + process;
        } catch (IOException e)
        {
            System.out.print(e);
            return "Error writing to file";
        }
        
    }
    

    private String execute() {
        String ret = "";
        try{
            //Run macro on target
            Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","ls -al"});
            
            //Read output
            StringBuilder out = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null, previous = null;
            while ((line = br.readLine()) != null)
                if (!line.equals(previous)) {
                    previous = line;
                    out.append(line).append('\n');
                    System.out.println(line);
                    ret += line;
                }

            //Check result
            if (p.waitFor() == 0) {
                return ret;
            } else {
                return out.toString();
            }


        }catch(Exception e) {
            ret = e.toString();
            System.err.println(ret);
        }
        return ret;        
    }

    public String cleanUpString(String inputString) {
        // Example cleanup operations
        String cleanedString = inputString.trim(); // Remove leading/trailing whitespace
        cleanedString = cleanedString.replaceAll("[^a-zA-Z0-9]", ""); // Remove special characters
        cleanedString = cleanedString.toLowerCase(); // Convert to lowercase
    
        // Add more cleanup operations as needed
        // ...
    
        return cleanedString;
    }

}
