package com.ex.demo.demo;

import java.io.IOException;

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

    @PostMapping("/compile")
    public String CompileCode(@RequestBody User user) {

        try{
            
            Utils.fileService = new FileService(); 
            Utils.fileService.createFile(Utils.filePath+Utils.filename);
            String code = user.getCode();
            Utils.fileService.writeToFile(Utils.filePath+Utils.filename, code);
            CommandExecutor1 commandExecutor1 = new CommandExecutor1();
            // For CPP: check which c compiler is present 
            // 1. gcc
            // 2. Windows 
            // 3. mac clang
            // etc and then execute that command here only giving command for linux: 
            //String ret = commandExecutor1.executeCommand("gcc "+ filePath + "-o " + "test.out");
            // For Java: 
            // we simply use javac filename 
            // then java filename.class
            // check if os is window: 
            // else if linux 
            // else if mac ....etc
            // String ret = commandExecutor1.executeCommand("/bin/sh", "-c", "ls -l"); // linux
            //String ret = commandExecutor1.executeCommand("cmd", "/c", "dir /a"); // windows
            String OS = System.getProperty("os.name");

            if(OS.equals("Windows 11"))
            {
                String [] ret = commandExecutor1.executeCommand("cmd", "/c", "javac -d "+ Utils.filePath + " " + Utils.filePath + Utils.filename);//windows
                System.out.println(ret);
                String jsonLike = "{" + 
                "\"error\":\"" + ret[0] + "\"," +
                "\"output\":\"" + ret[1] +"\"," +
                "}";
                System.out.println(jsonLike);
                return jsonLike ;
            }
            else if(OS.equals("Linux"))
            {
                String [] ret = commandExecutor1.executeCommand("bash", "/c", "javac -d "+ Utils.filePath + " " + Utils.filePath + Utils.filename);//Linux
                System.out.println(ret);
                String jsonLike = "{" + 
                "\"error\":\"" + ret[0] + "\"," +
                "\"output\":\"" + ret[1] +"\"," +
                "}";
                System.out.println(jsonLike);
                return jsonLike ;
            }
            else if(OS.equals("MacOS"))
            {
                String [] ret = commandExecutor1.executeCommand("bash", "/c", "javac -d "+ Utils.filePath + " " + Utils.filePath+Utils.filename);// MacOS
                System.out.println(ret);
                String jsonLike = "{" + 
                "\"error\":\"" + ret[0] + "\"," +
                "\"output\":\"" + ret[1] +"\"," +
                "}";
                System.out.println(jsonLike);
                return jsonLike ;
            }
            else
            {
                System.out.println("IDFK");
            }  
            return OS;
        } catch (IOException e) {
            System.out.print(e);
            return "Error writing to file";
        }        
    }

    @PostMapping("/execute")
    public String ExecuteCode(@RequestBody User user) {
        CommandExecutor1 commandExecutor1 = new CommandExecutor1();
        // For CPP: check which c compiler is present 
        // 1. gcc
        // 2. Windows 
        // 3. mac clang
        // etc and then execute that command here only giving command for linux: 
        //String ret = commandExecutor1.executeCommand("gcc "+ filePath + "-o " + "test.out");
        // For Java: 
        // we simply use javac filename 
        // then java filename.class
        // check if os is window: 
        // else if linux 
        // else if mac ....etc
        // String ret = commandExecutor1.executeCommand("/bin/sh", "-c", "ls -l"); // linux
        //String ret = commandExecutor1.executeCommand("cmd", "/c", "dir /a"); // windows
        
        
        String [] ret = commandExecutor1.executeCommand("cmd", "/c", "java -classpath "+ Utils.filePath+ " " + Utils.className);// + " 2> " + filePath+"_output.txt"); // windows
        String jsonLike = "{" + 
        "\"error\":\"" + ret[0] + "\"," +
        "\"output\":\"" + ret[1] +"\"," +
        "}";

        System.out.println(jsonLike);
        return jsonLike;
    }

    public String cleanUpString(String inputString) {
        // Example cleanup operations
        String cleanedString = inputString.trim(); // Remove leading/trailing whitespace
        cleanedString = cleanedString.replaceAll("[^a-zA-Z0-9]", ""); // Remove special characters
    
        // Add more cleanup operations as needed
        // ...
    
        return cleanedString;
    }

}
