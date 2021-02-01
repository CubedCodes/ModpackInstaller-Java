package com.rishabhroy.ModpackInstaller_Java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App 
{
    public void Main() {
    	String os = System.getProperty("os.name");
    	System.out.println(os);
    }
    
    
    public void Windows() {
    	
    }
    
    public void MacOS() throws IOException {
    	Process process = Runtime.getRuntime().exec("ping www.stackabuse.com");
    	printResults(process);
    	
    }
    
    public static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
