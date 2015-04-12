import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.io.File;
import java.nio.file.Files;
import java.util.Date;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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
    protected Commit root;  // not sure how useful is this; might review later
                            // Stores the initial commit address
    // List of IO addresses to the files to be copied
    protected HashSet<File> addSet;
    protected HashSet<File> rmSet;
    // pointers to end of each branch as well as HEAD pointer
    public HashMap<String,Commit> pointers;
    protected String curBranch;
    // list of all commits according to their ID
    protected ArrayList<Commit> commitList;
    // commit id of the next commit
    protected int cid = 0;
    private static final String GITLET_DIR = ".gitlet/";

    public State() {
        root = new Commit();
        addSet = new HashSet<File>();
        rmSet = new HashSet<File>();
        pointers = new HashMap<String, Commit>();
        pointers.put("HEAD", root);
        pointers.put("master", root);
        curBranch = "master";
        commitList = new ArrayList<Commit>();
        commitList.add(root);
        cid = 1;
        return;
    }

    public boolean addFile(File f) {
        if (f.exists() && f.isFile()) {
            if (isModified(f)) {
                addSet.add(f);
                return true;
            } else {
                System.out.println("File has not been "
                    + "modified since the last commit.");
            }
        } else {
            System.out.println("File does not exist, or the address is not a file.");
        }
        return false;
    }

    public void commit(String msg) {
        if (addSet.isEmpty() && rmSet.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        Commit prev = pointers.get("HEAD");
        // make new dir, move files
        HashMap<File,File> ads = commitHelper();
        Commit c = new Commit(prev, cid, ads, msg);
        commitList.add(c);
        // move head and current branch pointer
        pointers.put("HEAD", c);
        pointers.put(curBranch, c);
        addSet.clear(); // clear add set
        rmSet.clear();
        cid += 1;       // update commitid
    }

    public boolean removeFile(File f) {
        if (!f.exists()) {
            System.out.println("File does not exist.");
            return false;
        }
        if (!f.isFile()) {
            System.out.println("This is not a file.");
            return false;
        }
        boolean isRemoved = false;
        if (addSet.contains(f)) {
            addSet.remove(f);
            isRemoved = true;
        } 
        HashMap<File,File> oldmap = getAdss("HEAD");
        if (oldmap.containsKey(f)) {
            rmSet.add(f);
            isRemoved = true;
        }
        if (isRemoved) {
            return true;
        }
        System.out.println("No reason to remove the file.");
        return false;
    }

    public void log() {
        Commit ptr = pointers.get("HEAD");
        for (;ptr != null; ptr = ptr.getPrev()) {
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

    public void checkout(String s){
        // if it is current branch
        if (s.equals(curBranch)) {
            System.out.println("No need to checkout the current branch.");
            return;
        } 
        // branch case: check if such branch exists
        if (!s.equals("HEAD") && pointers.containsKey(s)) {
            Commit c = pointers.get(s);
            revertcommit(c);
            // change current branch to destination branch
            curBranch = s;
            pointers.put("HEAD", c);
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
            // for   Testing only (!!)
            if (c.getID() != id) {
                System.out.println("Wrong ID: " + c.getID());
            } //     Testing only (!!)
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
            if (c != null && c.getMessage().equals(msg)){
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
        for (String s: pointers.keySet()) {
            if (!s.equals("HEAD")) {
                if (s.equals(curBranch)){
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
        if (pointers.containsKey(b)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        curBranch = b;
        pointers.put(curBranch, pointers.get("HEAD"));
    }   

    public void rmBranch(String b) {
        if (!pointers.containsKey(b)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (b != null && b.equals(curBranch)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        pointers.remove(b);
    }   

    public void reset(int id) {
        if (id >= commitList.size()) {
            System.out.println("No commit with that id exists.");
        }
        Commit c = commitList.get(id);
        revertcommit(c);
        // change current branch to that commit node
        pointers.put("HEAD", c);
        pointers.put(curBranch, c);
    }   

    // must be checked by jUNIT and by hand (!!!!)
    public void merge(String b) {
        Commit cSplit = splitPoint(b);
        HashMap<File,File> mSplit = cSplit.getAddresses();
        HashMap<File,File> mNow = getAdss("HEAD");
        // search branch b for changed/new files from SP and check they are not changed in current
        HashMap<File,File> mGiven = getAdss(b);
        for (File f: mGiven.keySet()) {
            File fs = mSplit.get(f);
            File fg = mGiven.get(f);
            File fn = mNow.get(f);
            File fconflct = new File(f.getPath()+".conflict");
            // current file is not null >> check if given file has been changed
            if (fn == null) {
                // move fg over to f
                copyfile(fg, f);
            } else {
                if (fs != null && isDiff(fg,fs)) {
                    if (isDiff(fn,fs)) {
                        //conlfict case
                        copyfile(fg, fconflct);
                    } else {
                        copyfile(fg, f);
                    }
                } else if (fs == null) {
                    // conflict case
                    copyfile(fg, fconflct);
                }
            }
        }
    }


    private int[] prevCommitid(String b) {
        Commit c = pointers.get(b);
        int[] cArray = new int[commitList.size()];
        int i = 0; 
        for (;c != null; c = c.getPrev()) {
            cArray[i] = c.getID();
            i++;
        }
        return cArray;
    }

    private Commit splitPoint(String b) {
        int[] cArray = prevCommitid(b);
        Commit c = pointers.get("HEAD");
        int cid = c.getID();
        search:  
            while (cid > 0) {
                for (int i = 0; i < cArray.length && cArray[i] != 0; i++) {
                    if (cArray[i] == cid) {
                        break search;
                    }
                }
                c = c.getPrev();
                cid = c.getID();
            }
        return c;
    }


    //(for testing purpose only) (!!!!!!)
    public void test() {
        Commit c = pointers.get("HEAD");
        System.out.println("ArrayList index: " + commitList.indexOf(c));
        System.out.println("commit ID: " + c.getID());
        System.out.println("size: " + commitList.size());
        int[] cArray = new int[commitList.size()];
        System.out.println(Arrays.toString(cArray));
        System.out.println("commit id: " + Arrays.toString(prevCommitid("HEAD")));
        System.out.println();
        System.out.println("splitPoint commit ID: " + splitPoint("master").getID());
    }





    // HELPER METHODS


    private void revertcommit(Commit c) {
        HashMap<File,File> ad = c.getAddresses();
        for (Map.Entry<File,File> e: ad.entrySet()) {
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
            Files.copy(fi.toPath(),ff.toPath(), 
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
            System.out.println("Check modified: Exception reading file");
            System.out.println(e);
        }
        return false;
    }

    // get the address of immediate previous file at the HEAD pointer
    // may be different from current file since it may be changed
    private File getheadFile(File f) {
        Commit c = pointers.get("HEAD");
        File ff = c.getAddress(f);
        return ff;
    }

    private HashMap<File,File> getAdss(String ptr) {
        Commit c = pointers.get(ptr);
        HashMap<File,File> ff = c.getAddresses();
        return ff;
    }

    // use info in addSet and rmSet
    // return fileAddress map and copy files
    private HashMap<File,File> commitHelper() {
        HashMap<File,File> remap = new HashMap<File,File>();
        HashMap<File,File> oldmap = getAdss("HEAD");
        remap.putAll(oldmap); // put all old addresses in
        // create new folder
        File folder = getGitletfolder(cid);
        if (folder.exists()) {
            throw new RuntimeException("Gitlet folder already exist");
        } else {
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
                Files.copy(fi.toPath(),ff.toPath());
            } catch (IOException e) {
                System.out.println(e);
                System.out.println("Potential commit failure");
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
        return String.format("%05d",num);
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





