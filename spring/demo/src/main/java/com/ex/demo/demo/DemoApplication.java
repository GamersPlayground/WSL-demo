package com.ex.demo.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
	
	public static void main(String[] args) {
		try {
			Utils.fileService.deleteFile(Utils.filePath+Utils.fileNameJava);
			Utils.fileService.deleteFile(Utils.filePath+Utils.fileNameCpp);	
			Utils.fileService.deleteFile(Utils.filePath+Utils.fileNamePy);	

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SpringApplication.run(DemoApplication.class, args);
	}

}
