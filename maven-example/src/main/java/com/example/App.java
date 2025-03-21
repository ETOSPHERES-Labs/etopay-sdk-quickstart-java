package com.example;

import com.etospheres.etopay.ETOPaySdk;

/**
 * Hello world!
 */
public class App {
	public String getBuildInfo() {
		ETOPaySdk w = new ETOPaySdk();
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
