package com.install.tests.dependency;

import java.io.IOException;

import org.junit.Test;

/**
 * Place holder for your unit tests
 */
public class YourUnitTest extends BaseTest{

	@Test
    //DEPEND: no output for DEPEND
    public void testDepend() throws IOException {
        String[] input = {
                "DEPEND A B\n",
                "DEPEND B C\n",
                "DEPEND C B\n",
                "END\n"
        };

        String expectedOutput = "DEPEND A B\n" +
        		 "DEPEND B C\n"+
        		 "DEPEND C B\n"+
        		 "B depends on C, ignoring command\n"+
        		 "END\n";

        runTest(expectedOutput, input);
    }
	
	@Test
    //INSTALL: output "Installing"
    public void testInstall() throws IOException {
        String[] input = {
                "DEPEND A B\n",
                "INSTALL B\n",
                "END\n"
        };

        String expectedOutput = "DEPEND A B\n" +
                "INSTALL B\n" +
                "Installing B\n" +
                "END\n";

        runTest(expectedOutput, input);
    }
	
	@Test
    //INSTALL: output "Installing"
    public void testInstall1() throws IOException {
		String[] input = {
                "DEPEND A B\n",
                "DEPEND B C\n",
                "INSTALL A\n",
                "END\n"
        };

        String expectedOutput = "DEPEND A B\n" +
                "DEPEND B C\n" +
                "INSTALL A\n"+
                "Installing C\n"+
                "Installing B\n"+
                "Installing A\n"+
                "END\n";

        runTest(expectedOutput, input);
    }
	
	@Test
    //INSTALL: output "Installing"
    public void testInstall2() throws IOException {
		String[] input = {
                "DEPEND A B\n",
                "DEPEND B C\n",
                "INSTALL C\n",
                "END\n"
        };

        String expectedOutput = "DEPEND A B\n" +
                "DEPEND B C\n" +
        		"INSTALL C\n" +
                "Installing C\n"+
                "END\n";

        runTest(expectedOutput, input);
    }
	
	@Test
    //INSTALL: output "Installing"
    public void testList() throws IOException {
		String[] input = {
                "DEPEND A B\n",
                "DEPEND B C\n",
                "INSTALL A\n",
                "LIST\n",
                "END\n"
        };

        String expectedOutput = "DEPEND A B\n" +
                "DEPEND B C\n" +
                "INSTALL A\n"+
                "Installing C\n"+
                "Installing B\n"+
                "Installing A\n"+
                "LIST\n"+
                "C\n"+
                "B\n"+
                "A\n"+
                "END\n";

        runTest(expectedOutput, input);
    }
}
