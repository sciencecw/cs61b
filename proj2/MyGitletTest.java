import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Class that provides JUnit tests for Gitlet, as well as a couple of utility
 * methods.
 * 
 * @author Joseph Moghadam
 * 
 *         Some code adapted from StackOverflow:
 * 
 *         http://stackoverflow.com/questions
 *         /779519/delete-files-recursively-in-java
 * 
 *         http://stackoverflow.com/questions/326390/how-to-create-a-java-string
 *         -from-the-contents-of-a-file
 * 
 *         http://stackoverflow.com/questions/1119385/junit-test-for-system-out-
 *         println
 * 
 */
public class MyGitletTest {
    private static final String GITLET_DIR = ".gitlet/";
    private static final String TESTING_DIR = "test_files/";

    /* matches either unix/mac or windows line separators */
    private static final String LINE_SEPARATOR = "\r\n|[\r\n]";

    /**
     * Deletes existing gitlet system, resets the folder that stores files used
     * in testing.
     * 
     * This method runs before every @Test method. This is important to enforce
     * that all tests are independent and do not interact with one another.
     */
    @Before
    public void setUp() {
        File f = new File(GITLET_DIR);
        if (f.exists()) {
            recursiveDelete(f);
        }
        f = new File(TESTING_DIR);
        if (f.exists()) {
            recursiveDelete(f);
        }
        f.mkdirs();
    }

    /**
     * Tests that init creates a .gitlet directory. Does NOT test that init
     * creates an initial commit, which is the other functionality of init.
     */
    @Test
    public void testBasicInitialize() {
        gitlet("init");
        File f = new File(GITLET_DIR);
        assertTrue(f.exists());
    }

    /**
     * Tests that checking out a file name will restore the version of the file
     * from the previous commit. Involves init, add, commit, and checkout.
     */
    @Test
    public void testBasicCheckout() {
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("init");
        gitlet("add", wugFileName);
        gitlet("commit", "added wug");
        writeFile(wugFileName, "This is not a wug.");
        gitlet("checkout", wugFileName);
        assertEquals(wugText, getText(wugFileName));
    }

    /**
     * Tests that log prints out commit messages in the right order. Involves
     * init, add, commit, and log.
     */
    @Test
    public void testBasicLog() {
        gitlet("init");
        String commitMessage1 = "initial commit";

        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("add", wugFileName);
        String commitMessage2 = "added wug";
        gitlet("commit", commitMessage2);

        String logContent = gitlet("log");
        assertArrayEquals(new String[] { commitMessage2, commitMessage1 },
                extractCommitMessages(logContent));
    }

    @Test
    public void testInherit() {
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("init");
        gitlet("add", wugFileName);
        gitlet("commit", "added wug");
        writeFile(wugFileName, "This is not a wug.");

        String extraFileName = TESTING_DIR + "extrafile.txt";
        createFile(extraFileName, "extra");
        gitlet("commit", "second");
        gitlet("checkout", wugFileName);
        assertEquals(wugText, getText(wugFileName));
        assertEquals("extra", getText(extraFileName));
    }

    @Test
    public void testRemoveBasic() {
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("init");
        gitlet("add", wugFileName);
        gitlet("rm", wugFileName);
        gitlet("add", wugFileName);
        gitlet("commit", "added wug");
        writeFile(wugFileName, "This is not a wug.");
        gitlet("checkout", wugFileName);
        assertEquals(wugText, getText(wugFileName));
    }

    @Test
    public void testRemove1() {
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("init");
        gitlet("add", wugFileName);
        gitlet("commit", "number 1");

        String extraFileName = TESTING_DIR + "extrafile.txt";
        createFile(extraFileName, "extra");
        gitlet("add", extraFileName);
        gitlet("commit", "number 2");
        writeFile(wugFileName, "This is not a wug.");
        writeFile(extraFileName, "additional");
        gitlet("checkout", wugFileName);
        gitlet("checkout", extraFileName);
        assertEquals(wugText, getText(wugFileName));
        assertEquals("extra", getText(extraFileName));

        gitlet("rm", wugFileName);
        gitlet("commit", "number 3");
        writeFile(wugFileName, " ");
        writeFile(extraFileName, "additional");
        String log = gitlet("checkout", wugFileName);
        gitlet("checkout", extraFileName);
        assertEquals(" ", getText(wugFileName));
        assertEquals("extra", getText(extraFileName));
    }

    @Test
    public void testRemove2() {
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("init");
        gitlet("add", wugFileName);
        gitlet("commit", "number 1");

        String extraFileName = TESTING_DIR + "extrafile.txt";
        createFile(extraFileName, "extra");
        writeFile(wugFileName, "incorrect wug");
        gitlet("add", extraFileName);
        gitlet("add", wugFileName);
        gitlet("rm", wugFileName);
        gitlet("commit", "number 2");
        writeFile(wugFileName, "new wug");
        assertEquals("new wug", getText(wugFileName));
        gitlet("checkout", "2", wugFileName);
        assertEquals(wugText, getText(wugFileName));
    }

    @Test
    public void testRemove3() {
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("init");
        gitlet("add", wugFileName);
        gitlet("commit", "number 1");

        recursiveDelete(new File(wugFileName));
        gitlet("rm", wugFileName);
        gitlet("commit", "number 2");
        assertEquals("", getText(wugFileName));
        gitlet("checkout", wugFileName);
        assertEquals("", getText(wugFileName));        
    }

    @Test
    public void testBranch() {
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("init");
        gitlet("add", wugFileName);
        gitlet("commit", "added wug");
        writeFile(wugFileName, "This is not a wug.");

        String masterfile = TESTING_DIR + "masterfile.txt";
        String branchfile = TESTING_DIR + "branchfile.txt";
        String bftext = "This is branch.";
        String mftext = "Here is the master.";
        // make new branch branching and commit
        gitlet("branch", "branching");
        createFile(branchfile, bftext);
        gitlet("add", branchfile);
        gitlet("commit", "branch commit");

        // commit at master branch
        gitlet("checkout", "master");
        createFile(masterfile, mftext);
        gitlet("add", masterfile);
        gitlet("commit", "master commit");
        String logContent = gitlet("log");
        assertArrayEquals(new String[] { "master commit", "added wug", "initial commit"},
                extractCommitMessages(logContent));

        // make changes to bfile and mfile, checkout "branching", only bfile changes
        writeFile(branchfile, "no branch");
        writeFile(masterfile, "not master");
        gitlet("checkout", "branching");
        assertEquals("not master", getText(masterfile));
        assertEquals(bftext, getText(branchfile));

        // test remove branch
        String log = gitlet("rm-branch", "branching");
        assertEquals(log, "Cannot remove the current branch.\n");
        gitlet("checkout", "master");
        log = gitlet("rm-branch", "branching");
        assertEquals(log, "");
        assertEquals(mftext, getText(masterfile));
    }


    @Test
    public void testMergeBasic() {
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        String wugText2 = "This is a changed wug.";
        String delFileName = TESTING_DIR + "del.txt";
        String delText = "should be deleted";
        String f3Name = TESTING_DIR + "file_3.txt";
        String f3Text = "stay the same";
        String newName = TESTING_DIR + "newfile.txt";
        String newText = "This is new";
        String f5Name = TESTING_DIR + "file_5.txt";
        String f5Text = "inherited";
        createFile(wugFileName, wugText);
        createFile(delFileName, delText);
        createFile(f3Name, f3Text);
        createFile(f5Name, f5Text);
        assertEquals(f5Text, getText(f5Name));
        gitlet("init");
        // make commit 1
        gitlet("add", wugFileName); // f1
        gitlet("add", f3Name);      // f3
        gitlet("add", delFileName); // f4
        gitlet("add", f5Name);      // f5
        gitlet("commit", "number 1");
        recursiveDelete(new File(delFileName));
        gitlet("rm", delFileName);
        
        // commit 2 at branch b_2
        gitlet("branch", "b_2");
        writeFile(wugFileName, wugText2);
        assertEquals(wugText2, getText(wugFileName)); 
        gitlet("add", wugFileName);
        createFile(newName, newText);
        gitlet("add", newName);     // f2
        gitlet("rm", f3Name);
        assertEquals(f5Text, getText(f5Name));
        gitlet("commit", "com2");

        gitlet("checkout", "master");
        gitlet("branch", "b_3");
        assertEquals(wugText, getText(wugFileName));
        assertEquals(delText, getText(delFileName));
        recursiveDelete(new File(delFileName));
        recursiveDelete(new File(f5Name));
        gitlet("rm", delFileName);
        gitlet("rm", f5Name);
        assertEquals("", getText(f5Name));
        gitlet("commit", "com3");
        gitlet("checkout", f5Name);
        assertEquals("", getText(f5Name));

        //merge
        String mlog = gitlet("merge", "b_2");
        System.out.println(mlog);
        assertEquals(wugText2, getText(wugFileName));
        assertEquals(newText, getText(newName));
        assertEquals(f3Text, getText(f3Name));
        assertEquals("", getText(delFileName));
        assertEquals(f5Text, getText(f5Name));
    }   

    /*@Test
     * public void testRebaseBasic() {
     *
     *
     *}
     */

    @Test
    public void testRebaseAdvanced() {
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        String wugText2 = "This is a changed wug.";
        String delFileName = TESTING_DIR + "del.txt";
        String delText = "should be deleted";
        String extraFileName = TESTING_DIR + "extrafile.txt";
        String extraText = "changed extra";
        String bcFileName = TESTING_DIR + "bothchangedfile.txt";
        String bcText = "the original text";

        createFile(wugFileName, wugText);
        createFile(delFileName, delText);
        createFile(extraFileName, "extra");
        createFile(bcFileName, bcText);
        gitlet("init");
        // make commit 1
        gitlet("add", wugFileName);         // f1
        gitlet("add", extraFileName);       // f2
        gitlet("add", bcFileName);          // f3
        gitlet("add", delFileName);         // f4
        gitlet("commit", "number 1");

        recursiveDelete(new File(delFileName));
        gitlet("rm", delFileName);
        writeFile(bcFileName, "given text");
        gitlet("add", bcFileName);

        gitlet("branch", "given");          // create branch named "given"
        writeFile(wugFileName, wugText2);
        gitlet("add", wugFileName);
        for (int i = 2; i < 11; i++) {
            String newwugFileName = TESTING_DIR + "wug"+ i + ".txt";
            createFile(newwugFileName, wugText + " (" + i + "th version)");
            gitlet("add", newwugFileName);
            gitlet("commit", "number " + i);
        }
        File f = new File(TESTING_DIR);
        if (f.exists()) {
            recursiveDelete(f);
        } else {
            System.out.println("testing file does not exist.");
        }
        f.mkdir();
        String newwugFileName = TESTING_DIR + "wug10.txt";
        gitlet("checkout", newwugFileName);
        gitlet("checkout", wugFileName);
        // verify add text
        assertEquals(wugText + " (10th version)", getText(newwugFileName));
        assertEquals(wugText2, getText(wugFileName));
        recursiveDelete(new File(newwugFileName));
        recursiveDelete(new File(wugFileName));

        //return to master branch
        gitlet("checkout", "master");
        // confirming content is as expected
        assertEquals(getText(wugFileName), wugText);             //f1
        assertEquals(getText(extraFileName), "extra");           //f2
        assertEquals(getText(bcFileName), bcText);               //f3
        assertEquals(delText, getText(delFileName));             //f4
        // delete f4 again
        gitlet("rm", delFileName);
        recursiveDelete(new File(delFileName));
        String logContent = gitlet("log");
        assertArrayEquals(new String[] { "number 1", "initial commit" },
                extractCommitMessages(logContent));
        // change extra and bcfile
        writeFile(extraFileName, extraText);
        writeFile(bcFileName, "master text");
        gitlet("add", extraFileName);
        gitlet("add", bcFileName);
        for (int i = 1; i < 6; i++) {
            writeFile(bcFileName, "master text " + i);
            gitlet("add", bcFileName);
            gitlet("commit", "master commit" + i);            
        }
        // branching case here?
        System.out.println("stopping before rebase");
        stopping();
        // REBASE
        String rebaselog = gitlet("rebase", "given");
        System.out.println("rebaselog" + rebaselog);
        stopping();
        assertEquals(wugText2, getText(wugFileName));
        for (int i = 2; i < 11; i++) {
            newwugFileName = TESTING_DIR + "wug"+ i + ".txt";
            assertEquals(wugText + " (" + i + "th version)", getText(newwugFileName));
        }
        assertEquals(extraText, getText(extraFileName));
        assertEquals("master text 5", getText(bcFileName));
        assertEquals("", getText(delFileName));
        logContent = gitlet("log");
        assertTrue(logContent.contains("master commit1"));
        assertTrue(logContent.contains("master commit5"));
        assertTrue(logContent.contains("initial commit"));
        assertTrue(logContent.contains("number 9"));
        System.out.println(logContent);

    }

    private static void stopping(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String textLine = reader.readLine();
            return;
        } catch (IOException e) {
            System.out.println(e);
            return;
        }
    }

    /**
     * Convenience method for calling Gitlet's main. Anything that is printed
     * out during this call to main will NOT actually be printed out, but will
     * instead be returned as a string from this method.
     * 
     * Prepares a 'yes' answer on System.in so as to automatically pass through
     * dangerous commands.
     * 
     * The '...' syntax allows you to pass in an arbitrary number of String
     * arguments, which are packaged into a String[].
     */
    private static String gitlet(String... args) {
        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        ByteArrayOutputStream printingResults = new ByteArrayOutputStream();
        try {
            /*
             * Below we change System.out, so that when you call
             * System.out.println(), it won't print to the screen, but will
             * instead be added to the printingResults object.
             */
            System.setOut(new PrintStream(printingResults));

            /*
             * Prepares the answer "yes" on System.In, to pretend as if a user
             * will type "yes". You won't be able to take user input during this
             * time.
             */
            String answer = "yes";
            InputStream is = new ByteArrayInputStream(answer.getBytes());
            System.setIn(is);

            /* Calls the main method using the input arguments. */
            Gitlet.main(args);

        } finally {
            /*
             * Restores System.out and System.in (So you can print normally and
             * take user input normally again).
             */
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
        return printingResults.toString();
    }

    /**
     * Returns the text from a standard text file (won't work with special
     * characters).
     */
    private static String getText(String fileName) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(fileName));
            return new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Creates a new file with the given fileName and gives it the text
     * fileText.
     */
    private static void createFile(String fileName, String fileText) {
        File f = new File(fileName);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeFile(fileName, fileText);
    }

    /**
     * Replaces all text in the existing file with the given text.
     */
    private static void writeFile(String fileName, String fileText) {
        FileWriter fw = null;
        try {
            File f = new File(fileName);
            fw = new FileWriter(f, false);
            fw.write(fileText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes the file and all files inside it, if it is a directory.
     */
    private static void recursiveDelete(File d) {
        if (d.isDirectory()) {
            for (File f : d.listFiles()) {
                recursiveDelete(f);
            }
        }
        d.delete();
    }

    /**
     * Returns an array of commit messages associated with what log has printed
     * out.
     */
    private static String[] extractCommitMessages(String logOutput) {
        String[] logChunks = logOutput.split("====");
        int numMessages = logChunks.length - 1;
        String[] messages = new String[numMessages];
        for (int i = 0; i < numMessages; i++) {
            //System.out.println(logChunks[i + 1]);
            String[] logLines = logChunks[i + 1].split(LINE_SEPARATOR);
            messages[i] = logLines[3];
        }
        return messages;
    }
}
