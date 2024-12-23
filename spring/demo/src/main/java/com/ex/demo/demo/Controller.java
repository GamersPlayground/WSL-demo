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
    public String CompileCode(@RequestBody User user, @RequestParam(required = false) String language) {
        if (language == null || language.isEmpty()) {
            return "{\"error\":\"Language parameter is missing.\"}";
        }

        try {
            Utils.fileService = new FileService();
            String fileName = getFileNameByLanguage(language);
            Utils.fileService.createFile(Utils.filePath + fileName);
            String code = user.getCode();
            Utils.fileService.writeToFile(Utils.filePath + fileName, code);
            CommandExecutor1 commandExecutor1 = new CommandExecutor1();

            String OS = System.getProperty("os.name");
            String[] ret;

            if (language.equalsIgnoreCase("java")) {
                if (OS.contains("Windows")) {
                    ret = commandExecutor1.executeCommand("cmd", "/c", "javac -d " + Utils.filePath + " " + Utils.filePath + fileName);
                } else {
                    ret = commandExecutor1.executeCommand("bash", "-c", "javac -d " + Utils.filePath + " " + Utils.filePath + fileName);
                }
            } else if (language.equalsIgnoreCase("cpp")) {
                if (OS.contains("Windows")) {
                    ret = commandExecutor1.executeCommand("cmd", "/c", "g++ " + Utils.filePath + fileName + " -o " + Utils.filePath + "a.exe");
                } else {
                    ret = commandExecutor1.executeCommand("bash", "-c", "g++ " + Utils.filePath + fileName + " -o " + Utils.filePath + "a.out");
                }
            } else if (language.equalsIgnoreCase("python")) {
                // No compilation needed for Python
                ret = new String[]{"", "Python code is ready to execute."};
            } else {
                return "{\"error\":\"Unsupported language.\"}";
            }

            String error = ret[0].replace("\n", " ");
            String output = ret[1].replace("\n", " ");

            String jsonLike = "{" +
                "\"error\":\"" + error + "\"," +
                "\"output\":\"" + output + "\"" +
                "}";
            return jsonLike;

        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\":\"Error writing to file\"}";
        }
    }

    @PostMapping("/execute")
    public String ExecuteCode(@RequestBody User user, @RequestParam(required = false) String language) {
        if (language == null || language.isEmpty()) {
            return "{\"error\":\"Language parameter is missing.\"}";
        }

        CommandExecutor1 commandExecutor1 = new CommandExecutor1();
        String OS = System.getProperty("os.name");

        try {
            String[] ret;
            String fileName = getFileNameByLanguage(language);
            if (language.equalsIgnoreCase("java")) {
                if (OS.contains("Windows")) {
                    ret = commandExecutor1.executeCommand("cmd", "/c", "java -cp " + Utils.filePath + " " + Utils.className);
                } else {
                    ret = commandExecutor1.executeCommand("bash", "-c", "java -cp " + Utils.filePath + " " + Utils.className);
                }
            } else if (language.equalsIgnoreCase("cpp")) {
                if (OS.contains("Windows")) {
                    ret = commandExecutor1.executeCommand("cmd", "/c", Utils.filePath + "a.exe");
                } else {
                    ret = commandExecutor1.executeCommand("bash", "-c", Utils.filePath + "a.out");
                }
            } else if (language.equalsIgnoreCase("python")) {
                if (OS.contains("Windows")) {
                    ret = commandExecutor1.executeCommand("cmd", "/c", "python " + Utils.filePath + fileName);
                } else {
                    ret = commandExecutor1.executeCommand("bash", "-c", "python3 " + Utils.filePath + fileName);
                }
            } else {
                return "{\"error\":\"Unsupported language.\"}";
            }

            String error = ret[0].replace("\n", " ");
            String output = ret[1].replace("\n", " ");

            String jsonLike = "{" +
                "\"error\":\"" + error + "\"," +
                "\"output\":\"" + output + "\"" +
                "}";
            return jsonLike;

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"An error occurred during execution.\"}";
        }
    }

    private static String getFileNameByLanguage(String language) {
        if (language == null) {
            throw new NullPointerException("Language cannot be null");
        }
        switch (language.toLowerCase()) {
            case "java":
                return Utils.fileNameJava;
            case "cpp":
                return Utils.fileNameCpp;
            case "python":
                return Utils.fileNamePy;
            default:
                throw new IllegalArgumentException(
                        String.format("Unsupported language: %s", language));
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
