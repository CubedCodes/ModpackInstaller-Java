package com.rishabhroy.ModpackInstaller;



public class Main{
	
	public static void main(String[] args) {
		String os = System.getProperty("os.name");
		System.out.println(os);
		if (os.contains("Mac")) {
			MacOS();
		}
		
		if (os.contains("Windows")) {
			Windows();
		}
		
		else if ((!(os.contains("Mac"))) && (!(os.contains("Windows")))) {
			System.out.println("Sorry, your system is not supported");
		}
	}
	
	public static void MacOS() {
		System.out.println("initiating Mac OS X installation");
	}
	
	public static void Windows() {
		System.out.println("initiating Windows 10 installation");
	}
}

