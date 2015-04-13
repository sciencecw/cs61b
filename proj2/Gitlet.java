import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Gitlet {

    private static State state;
    private static String[] argm;
    private static final String GITLET_DIR = ".gitlet/";
    private static final String STATE_DIR = ".gitlet/State.ser";

    private static void init() {
        if (isInit()) {
            System.out.println("A gitlet version control system already"
                + " exists in the current directory.");
        } else {
            state = new State();
            File f1 = new File(GITLET_DIR);
            if (!f1.exists()) {
                f1.mkdir();
            } else {
                System.out.println("warning: gitlet folder already exists");
            }
            writeState();    
        }
    }

    private static void add(String fileString) {
        File f = new File(fileString);
        state.addFile(f); 
        return;
    }

    private static void commit() {
        if (checklength(2)) {
            state.commit(argm[1]);
        } else {
            System.out.println("Please enter a commit message.");
        } 
    }
    
    private static void rm(String fileString) {
        File f = new File(fileString);
        boolean hasrm = state.remove(f); 
        return;
    }
    private static void log() {
        state.log();
    }   
    private static void globalLog() {
        state.glog();
    }   

    private static void find(String msg) {
        state.find(msg);
    }   

    private static void status() {
        state.status();
    }   

    private static void checkout() {
        if (noDanger()) {
            if (checklength(3)) {
                ckout(argm[2], Integer.parseInt(argm[1]));
            } else if (checklength(2)) {
                ckout(argm[1]);   
            }
        }
    }

    private static void ckout(String s, int id) {
        state.checkout(s, id);
    } 

    private static void ckout(String s) {
        state.checkout(s);
    }   

    private static void branch(String b) {
        state.branch(b);
    }   

    private static void rmBranch(String b) {
        state.rmBranch(b);
    }   

    private static void reset() {
        if (checklength(2)) {
            state.reset(Integer.parseInt(argm[1]));
        } 
    }   

    private static void merge() {
        if (checklength(2)) {
            state.merge(argm[1]);
        } 
    }   

    private static void rebase(String b) {
        state.rebase(b, false);
    }   

    private static void iRebase(String b) {
        state.rebase(b, true);
    }   

    private static void test() {
        state.test();
    }



    //Helper methods
    private static void writeState() {
        try {
            File sfile = new File(STATE_DIR);
            FileOutputStream fos = new FileOutputStream(sfile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            //System.out.println(state);
            oos.writeObject(state);
            oos.close();            
        } catch (IOException e) {
            System.out.println("fail to write state!");
            System.out.println(e);
        }
    }

    private static void readState() {
        try {
            File sfile = new File(STATE_DIR);
            FileInputStream fis = new FileInputStream(sfile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object sObj = ois.readObject();
            state = (State) sObj;
            ois.close();            
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("fail to read state");
        }
    }

    private static boolean isInit() {
        return new File(STATE_DIR).exists(); 
    }

    private static boolean checklength(int lg) {
        return argm.length >= lg;
    }

    private static boolean noDanger() {
        System.out.println("Warning: The command you entered may alter the files"
            + " in your working directory. Uncommitted changes may be lost." 
            + " Are you sure you want to continue? (yes/no)");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String textLine = reader.readLine();
            return textLine.startsWith("yes");
        } catch (IOException e) {
            System.out.println("error reading your response.");
            System.out.println(e);
            return false;
        }
    }

    public static void main(String[] args) {
        argm = args;
        if (!checklength(1)) {
            System.out.println("not enough arguments");
            return;
        }
        if ("init".equals(args[0])) {
            init();
            return;
        }
        if (isInit()) {
            readState();
            switch (args[0]) {
                case "add": 
                    if (checklength(2)) {
                        add(args[1]);
                    } 
                    break;
                case "commit": 
                    commit();
                    break;
                case "rm": 
                    if (checklength(2)) {
                        rm(args[1]);  
                    } 
                    break;
                case "log": 
                    log();
                    break;
                case "global-log": 
                    globalLog();
                    break;
                case "find": 
                    if (checklength(2)) {
                        find(args[1]);  
                    } 
                    break;
                case "status": 
                    status();
                    break;
                case "checkout": 
                    checkout();
                    break;
                case "branch": 
                    if (checklength(2)) {
                        branch(args[1]);
                    } 
                    break;                                
                case "rm-branch": 
                    if (checklength(2)) {
                        rmBranch(args[1]);
                    } 
                    break;
                case "reset": 
                    reset();
                    break;
                case "merge": 
                    merge();
                    break;
                case "rebase": 
                    if (checklength(2)) {
                        rebase(args[1]);
                    } 
                    break;
                case "i-rebase": 
                    if (checklength(2)) {
                        iRebase(args[1]);
                    } 
                    break;
                case "test": 
                    test();
                    break;
                default:
                    break;
            }
            writeState();
        } else {
            System.out.println("Gitlet is not initialized.");
        }
    }
}
