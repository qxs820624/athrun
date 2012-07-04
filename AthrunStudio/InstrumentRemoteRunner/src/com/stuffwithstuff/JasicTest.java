package com.stuffwithstuff;

public class JasicTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//String contents = "text \"hello,\", \"hello,jin!\"";
		//String contents = "click \"hello\"";
		String contents = "sleep 10000" + "\n";
        
        // Run it.
        Jasic jasic = new Jasic();
        jasic.interpret(contents);
	}

}
