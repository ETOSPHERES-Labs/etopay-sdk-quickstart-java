package com.example;

import com.cawaena.Wallet;

/**
 * Hello world!
 */
public class App {
	public String getBuildInfo() {
		Wallet w = new Wallet();
		try {
			return w.getBuildInfo();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		App app = new App();
		System.out.println(app.getBuildInfo());
	}

}
