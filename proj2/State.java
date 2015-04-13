import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;


/*
1. check all boundary case. null pointer exceptions, out of bounds,
commit/pointer does not exist
2. check that all pointer change is valid, especially change of HEAD and current branch
3. what about empty branch names? empty file names?
4. consider if each code works right after init
*/

public class State implements Serializable {
    private Commit root;  
    // List of addresses to the files to be copied
    private HashSet<File> addSet;
    private HashSet<File> rmSet;
    // pointers to end of each branch as well as HEAD pointer
    private HashMap<String, Commit> branches;
    private String curBranch;
    // list of all commits according to their ID
    private ArrayList<Commit> commitList;
    // commit id of the next commit
    private int cid = 0;
    private static final String GITLET_DIR = ".gitlet/";

    public State() {
        root = new Commit();
        addSet = new HashSet<File>();
        rmSet = new HashSet<File>();
        branches = new HashMap<String, Commit>();
        curBranch = "master";
        commitList = new ArrayList<Commit>();
        commitAdvance(root);
        return;
    }

    public void addFile(File f) {
        if (f.exists() && f.isFile()) {
            if (isModified(f)) {
                addSet.add(f);
                return;
            } else {
                System.out.println("File has not been "
                    + "modified since the last commit.");
            }
        } else {
            System.out.println("File does not exist, or the address is not a file.");
        }
        return;
    }

    public void commit(String msg) {
        if (addSet.isEmpty() && rmSet.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        Commit prev = branches.get("HEAD");
        // make new dir, move files
        HashMap<File, File> ads = commitHelper();
        Commit c = new Commit(prev, cid, ads, msg);
        commitAdvance(c);
        addSet.clear(); // clear add set
        rmSet.clear();
    }

    /* Put commit into place in commitList and increment cID
     * Also update Head and branch pointers
     */
    private void commitAdvance(Commit c) {
        commitList.add(c);
        // move head and current branch pointer
        branches.put("HEAD", c);
        branches.put(curBranch, c);
        cid += 1;       // update commitid
    }

    public boolean remove(File f) {
        if (addSet.contains(f)) {
            addSet.remove(f);
            return true;
        } 
        HashMap<File, File> oldmap = getAdss("HEAD");
        if (oldmap.containsKey(f)) {
            rmSet.add(f);
            return true;
        }
        System.out.println("No reason to remove the file.");
        return false;
    }

    public void log() {
        Commit ptr = branches.get("HEAD");
        for (; ptr != null; ptr = ptr.getPrev()) {
            printcommit(ptr);
        }
        return;
    }

    public void glog() {
        for (Commit c: commitList) {
            printcommit(c);
        }
        return;
    }

    public void checkout(String s) {
        // if it is current branch
        if (s.equals(curBranch)) {
            System.out.println("No need to checkout the current branch.");
            return;
        } 
        // branch case: check if such branch exists
        if (!s.equals("HEAD") && branches.containsKey(s)) {
            Commit c = branches.get(s);
            revertcommit(c);
            // change current branch to destination branch
            curBranch = s;
            branches.put("HEAD", c);
            return;
        }
        // single file case
        File fnow = new File(s);
        File fi = getheadFile(fnow);
        if (fi != null) {
            copyfile(fi, fnow);
        } else {
            System.out.println("File does not exist in the most"
                + " recent commit, or no such branch exists.");
        }
        
    }

    public void checkout(String s, int id) {
        // checkout file in particular commit
        File fnow = new File(s);
        if (id >= commitList.size()) {
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit c = commitList.get(id);
        File fi = c.getAddress(fnow);
        if (fi != null) {
            copyfile(fi, fnow);
        } else {
            System.out.println("File does not exist in that commit.");
        }
    } 


    public void find(String msg) {
        boolean found = false;
        for (Commit c: commitList) {
            if (c != null && c.getMessage().equals(msg)) {
                System.out.println("" + c.getID());
                found = true;
            }
        }
        if (!found) {
            System.out.println("Found no commit with that message.");
        }
    }   

    public void status() {
        System.out.println("=== Branches ===");
        for (String s: branches.keySet()) {
            if (!s.equals("HEAD")) {
                if (s.equals(curBranch)) {
                    System.out.println("*" + s);
                } else {
                    System.out.println(s);
                }
            }
        }
        System.out.println("");
        System.out.println("=== Staged Files ===");
        for (File f: addSet) {
            System.out.println(f.getPath());
        }
        System.out.println("");
        System.out.println("=== Files Marked for Removal ===");
        for (File f: rmSet) {
            System.out.println(f.getPath());
        }
    }   

    public void branch(String b) {
        if (branches.containsKey(b)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        curBranch = b;
        branches.put(curBranch, branches.get("HEAD"));
    }   

    public void rmBranch(String b) {
        if (!branches.containsKey(b)) {
            System.out.println("A branch with that name does not exist.");
            return;
        } else if (b != null && b.equals(curBranch)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        branches.remove(b);
    }   

    public void reset(int id) {
        if (id >= commitList.size()) {
            System.out.println("No commit with that id exists.");
        }
        Commit c = commitList.get(id);
        revertcommit(c);
        // change current branch to that commit node
        branches.put("HEAD", c);
        branches.put(curBranch, c);
    }   

    public void merge(String b) {
        Commit cSplit = splitPoint(b);
        HashMap<File, File> mSplit = cSplit.getAddresses();
        HashMap<File, File> mNow = getAdss("HEAD");
        // search branch b for changed/new files from SP and check they are not changed in current
        HashMap<File, File> mGiven = getAdss(b);
        for (File f: mGiven.keySet()) {
            File fs = mSplit.get(f);
            File fg = mGiven.get(f);
            File fn = mNow.get(f);
            File fconflct = new File(f.getPath() + ".conflict");
            // current file is not null >> check if given file has been changed
            if (fn == null) {
                // move fg over to f
                copyfile(fg, f);
            } else {
                if (fs == null) {
                    // conflict case
                    copyfile(fg, fconflct);
                } else if (!fg.equals(fs)) {
                    if (!fn.equals(fs)) {
                        //conlfict case
                        copyfile(fg, fconflct);
                    } else {
                        copyfile(fg, f);
                    }
                }
            }
        }
    }

    public void rebase(String b, boolean isInteractive) {
        if (b == null || !branches.containsKey(b)) {
            System.out.println(" A branch with that name does not exist.");
            return;
        } else if (b.equals(curBranch)) {
            System.out.println("Cannot rebase a branch onto itself.");
            return;
        }
        Commit cSplit = splitPoint(b);
        Commit cGiven = branches.get(b);
        if (cSplit == cGiven) {
            System.out.println("Already up-to-date.");
            return;
        } else if (cSplit == branches.get("HEAD")) {
            // move pointer to b
            System.out.println("pointer moves");
            branches.put("HEAD", cGiven);
            branches.put(curBranch, cGiven);
            revertcommit(cGiven);
            return;
        }
        if (isInteractive) {
            System.out.println("Currently replaying:");
        }
        Commit newestnode = replay(cGiven, cSplit, branches.get("HEAD"), isInteractive);
        revertcommit(newestnode);
    }

    private Commit replay(Commit prev, Commit cSplit, Commit cNow, boolean isInteractive) {
        boolean isFirstLast = (cNow == branches.get("HEAD"));
        if (cNow == cSplit) {
            return null;
        }   
        Commit retnCommit = replay(prev, cSplit, cNow.getPrev(), isInteractive);
        /* returned commit is the commit this commit should be attached to
         * if no commit is returned then prev = given branch is used
         */
        if (retnCommit != null) {
            prev = retnCommit;
        } else {
            isFirstLast = true;
        }
        String msg = cNow.getMessage();  
        char replay = 'c';   // default is continue
        if (isInteractive) { 
            // print commit and take input from user
            printcommit(cNow);
            replay = ireplayCheck(isFirstLast);
            // skipping case
            if (replay == 's') { 
                return retnCommit;
            // changing message
            } else if (replay == 'm') {
                msg = ireplayMessage();
            }
        }
        // Move files to new node
        HashMap<File, File> mPlayed = replayHelper(cSplit, cNow, prev);
        Commit newNode = new Commit(prev, cid, mPlayed, msg);
        commitAdvance(newNode);
        return newNode;
    }


    private HashMap<File, File> replayHelper(Commit cSplit, Commit cNow, Commit cGiven) {
        HashMap<File, File> mPlayed = new HashMap<File, File>();
        HashMap<File, File> mSplit = cSplit.getAddresses();
        HashMap<File, File> mNow = cNow.getAddresses();
        HashMap<File, File> mGiven = cGiven.getAddresses();
        mPlayed.putAll(mNow);
        for (File f: mGiven.keySet()) {
            File fs = mSplit.get(f);
            File fg = mGiven.get(f);
            File fn = mNow.get(f);
            if (fn == null) {
                // propagate fgiven if file does not exist in current branch
                mPlayed.put(f, fg);
            } else {
                // only given is changed from split, then propagate
                if (fs != null && !fg.equals(fs) && fn.equals(fs)) {
                    mPlayed.put(f, fg);
                }
            }
        }
        return mPlayed;
    }

    private int[] prevCommitid(String b) {
        Commit c = branches.get(b);
        int[] cArray = new int[commitList.size()];
        int i = 0; 
        for (; c != null; c = c.getPrev()) {
            cArray[i] = c.getID();
            i++;
        }
        return cArray;
    }

    private Commit splitPoint(String b) {
        int[] cArray = prevCommitid(b);
        Commit c = branches.get("HEAD");
        int id = c.getID();
        search:  
            while (id > 0) {
                for (int i = 0; i < cArray.length && cArray[i] != 0; i++) {
                    if (cArray[i] == id) {
                        break search;
                    }
                }
                c = c.getPrev();
                id = c.getID();
            }
        return c;
    }

    //(for testing purpose only) 
    public void test() {
        Commit c = branches.get("HEAD");
        System.out.println("ArrayList index: " + commitList.indexOf(c));
        System.out.println("commit ID: " + c.getID());
        System.out.println("size: " + commitList.size());
        int[] cArray = new int[commitList.size()];
        System.out.println(Arrays.toString(cArray));
        System.out.println("commit id: " + Arrays.toString(prevCommitid("HEAD")));
        System.out.println();
        System.out.println("splitPoint commit ID: " + splitPoint("master").getID());
    }



    // MINOR HELPER METHODS
    private String readInput() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            return reader.readLine();
        } catch (IOException e) {
            System.out.println(e);
            return "";
        }
    }

    private char ireplayCheck(boolean isFirstLast) {
        String textLine = ""; 
        while (ireplayCheckHelper(textLine, isFirstLast)) {
            System.out.println("Would you like to (c)ontinue, (s)kip this " 
                + "commit, or change this commit's (m)essage?");
            textLine = readInput();
        }
        return textLine.charAt(0);
    }

    private String ireplayMessage() {
        String textLine = ""; 
        while (textLine.equals("")) {
            System.out.println("Please enter a new message for this commit.");
            textLine = readInput();
        }
        return textLine;
    }

    private boolean ireplayCheckHelper(String textLine, boolean isFirstLast) {
        if (isFirstLast) {
            return !(textLine.startsWith("c") || textLine.startsWith("m"));
        } 
        return !(textLine.startsWith("c") || textLine.startsWith("s") 
            || textLine.startsWith("m"));
    }


    private void revertcommit(Commit c) {
        HashMap<File, File> ad = c.getAddresses();
        for (Map.Entry<File, File> e: ad.entrySet()) {
            File fi = e.getKey();
            File ff = e.getValue();
            // copy archive to local
            copyfile(ff, fi);
        }
    }

    // might be generalized to accomodate non-checkout functions (!!)
    private void copyfile(File fi, File ff) {
        try {
            // replace current file by old file
            Files.copy(fi.toPath(), ff.toPath(), 
                StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("checkout failure");
        }
    }

    // if f has been changed since last commit
    private boolean isModified(File f) {
        File prevFile = getheadFile(f);
        if (prevFile == null) {
            return true;
        }
        return isDiff(f, prevFile);
    }

    // if f1 and f2 have different contents?
    private boolean isDiff(File f1, File f2) {
        try {
            byte[] encodeNew = Files.readAllBytes(f1.toPath());
            byte[] encoded = Files.readAllBytes(f2.toPath());
            if (encoded.length != encodeNew.length) {
                return true;
            }
            for (int i = 0; i < encodeNew.length; i++) {
                if (encoded[i] != encodeNew[i]) {
                    return true;
                }
            }
        } catch (IOException | SecurityException e) {
            System.out.println(e);
        }
        return false;
    }

    // get the address of immediate previous file at the HEAD pointer
    // may be different from current file since it may be changed
    private File getheadFile(File f) {
        Commit c = branches.get("HEAD");
        File ff = c.getAddress(f);
        return ff;
    }

    private HashMap<File, File> getAdss(String ptr) {
        Commit c = branches.get(ptr);
        HashMap<File, File> ff = c.getAddresses();
        return ff;
    }

    // use info in addSet and rmSet
    // return fileAddress map and copy files
    private HashMap<File, File> commitHelper() {
        HashMap<File, File> remap = new HashMap<File, File>();
        HashMap<File, File> oldmap = getAdss("HEAD");
        remap.putAll(oldmap); // put all old addresses in
        // create new folder
        File folder = getGitletfolder(cid);
        if (!folder.exists()) {
            folder.mkdir();
        }
        // add all addset stuff into new commit
        for (File fi: addSet) {
            File ff = new File(folder, fi.getPath());
            remap.put(fi, ff);
            if (!ff.getParentFile().exists()) {
                ff.getParentFile().mkdirs();
            }
            //copy files into gitlet folder
            try {
                Files.copy(fi.toPath(), ff.toPath());
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        for (File fi: rmSet) {
            remap.remove(fi);
        }
        return remap;
    }

    private File getGitletfolder(int id) {
        return new File(GITLET_DIR + commitName(id) + "/");
    }

    private String commitName(int num) {
        return String.format("%05d", num);
    }

    private void printcommit(Commit c) {
        System.out.println("====");
        System.out.println("Commit " + c.getID() + ".");
        System.out.println(new SimpleDateFormat(
                "YYYY-MM-dd  HH:mm:ss").format(c.getDate()));
        System.out.println(c.getMessage());
        System.out.println();
    }
}

