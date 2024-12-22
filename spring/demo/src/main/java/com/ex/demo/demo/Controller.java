package com.ex.demo.demo;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String CompileCode(@RequestBody User user, @RequestParam String language) {

        try{
            
            Utils.fileService = new FileService(); 
            Utils.fileService.createFile(Utils.filePath+Utils.fileName);
            String code = user.getCode();
            Utils.fileService.writeToFile(Utils.filePath+Utils.fileName, code);
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

            String[] ret;

            if(language.equalsIgnoreCase("java"))
            {
                if(OS.contains("Windows"))
                {
                    ret = commandExecutor1.executeCommand("cmd", "/c", "javac -d " + Utils.filePath + " " + Utils.filePath + Utils.fileName);
                    ret = commandExecutor1.executeCommand("cmd", "/c", "javac -cp " + Utils.filePath + " " + Utils.fileName.replace(" .java", ""));
                } 
                else 
                {
                    ret = commandExecutor1.executeCommand("bash", "/c", "javac -d " + Utils.filePath + " " + Utils.filePath + Utils.fileName);
                    ret = commandExecutor1.executeCommand("bash", "/c", "javac -cp " + Utils.filePath + " " + Utils.fileName.replace(" .java", ""));
                }
            }
            else if(language.equalsIgnoreCase("cpp"))
            {
                if(OS.contains("Windows"))
                {
                    ret = commandExecutor1.executeCommand("cmd", "/c", "g++ " + Utils.filePath + Utils.fileName + " -o " + Utils.filePath + "a.exe");
                    ret = commandExecutor1.executeCommand("cmd", "/c", Utils.filePath + "a.exe");
                }
                else
                {
                    ret = commandExecutor1.executeCommand("bash", "-c", "g++ " + Utils.filePath + Utils.fileName + " -o " + Utils.filePath + "a.out");
                    ret = commandExecutor1.executeCommand("bash", "-c", Utils.filePath + "a.out");
                }
            }
            else if (language.equalsIgnoreCase("python"))
            {
                if(OS.contains("Windows"))
                {
                    ret = commandExecutor1.executeCommand("cmd", "/c", "python " + Utils.filePath + Utils.fileName); 
                }
                else
                {
                    ret = commandExecutor1.executeCommand("bash", "/c", "python " + Utils.filePath + Utils.fileName);
                }
            }
            else
            {
                return "{\"error\":\"Unsupported language.\"}";
            }

            String error = ret[0].replace("\n", " ");
            String output = ret[1].replace("\n", " ");

            String jsonLike = "{" +
                "\"error\":\"" + error + "\"," +
                "\"output\":\"" + output + "\"" +
                "}";
            
        } catch (IOException e) {
            e.printStackTrace();
            return "Error writing to file";
        }
                return language;        
    }

    @PostMapping("/execute")
    public String ExecuteCode(@RequestBody User user, @RequestParam String language) {
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

        try {
            String[] ret;
            if (language.equalsIgnoreCase("java")) {
                if (OS.contains("Windows")) {
                    ret = commandExecutor1.executeCommand("cmd", "/c", "javac " + Utils.filePath + Utils.fileName);
                    ret = commandExecutor1.executeCommand("cmd", "/c", "java -cp " + Utils.filePath + " " + Utils.fileName.replace(".java", ""));
                } else {
                    ret = commandExecutor1.executeCommand("bash", "-c", "javac " + Utils.filePath + Utils.fileName);
                    ret = commandExecutor1.executeCommand("bash", "-c", "java -cp " + Utils.filePath + " " + Utils.fileName.replace(".java", ""));
                }
            } else if (language.equalsIgnoreCase("cpp")) {
                if (OS.contains("Windows")) {
                    ret = commandExecutor1.executeCommand("cmd", "/c", "g++ " + Utils.filePath + Utils.fileName + " -o " + Utils.filePath + "a.exe");
                    ret = commandExecutor1.executeCommand("cmd", "/c", Utils.filePath + "a.exe");
                } else {
                    ret = commandExecutor1.executeCommand("bash", "-c", "g++ " + Utils.filePath + Utils.fileName + " -o " + Utils.filePath + "a.out");
                    ret = commandExecutor1.executeCommand("bash", "-c", Utils.filePath + "a.out");
                }
            } else if (language.equalsIgnoreCase("python")) {
                if (OS.contains("Windows")) {
                    ret = commandExecutor1.executeCommand("cmd", "/c", "python " + Utils.filePath + Utils.fileName);
                } else {
                    ret = commandExecutor1.executeCommand("bash", "-c", "python3 " + Utils.filePath + Utils.fileName);
                }
            } else {
                return "{\"error\":\"Unsupported language.\"}";
            }
    
            // Replace newline characters with spaces
            String error = ret[0].replace("\n", " ");
            String output = ret[1].replace("\n", " ");
    
            String jsonLike = "{" + 
                "\"error\":\"" + error + "\"," +
                "\"output\":\"" + output + "\"" +
                "}";
            System.out.println(jsonLike);
            return jsonLike;
    
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"An error occurred during execution.\"}";
        }
    }

/**
 * Cleans up the input string by performing several operations.
 * 
 * This method trims leading and trailing whitespace from the input string
 * and removes any special characters, leaving only alphanumeric characters.
 * Additional cleanup operations can be added as needed.
 * 
 * @param inputString The string to be cleaned up.
 * @return The cleaned-up string with whitespace trimmed and special characters removed.
 */

    public String cleanUpString(String inputString) {
        // Example cleanup operations
        String cleanedString = inputString.trim(); // Remove leading/trailing whitespace
        cleanedString = cleanedString.replaceAll("[^a-zA-Z0-9]", ""); // Remove special characters
    
        // Add more cleanup operations as needed
        // ...
    
        return cleanedString;
    }

}
