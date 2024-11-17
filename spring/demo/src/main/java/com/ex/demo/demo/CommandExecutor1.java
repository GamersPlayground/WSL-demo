package com.ex.demo.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

@Service
public class CommandExecutor1 {

    public String executeCommand(String... command) {
        try {
            //ProcessBuilder processBuilder = new ProcessBuilder();
            //processBuilder.command(command);
            Process process = Runtime.getRuntime().exec(command);

            //Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            process.waitFor();
            System.out.println("Waiting over.");

            return output.toString();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error executing command: " + command, e);
        }
    }
}