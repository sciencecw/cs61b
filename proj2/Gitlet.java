import java.io.File;
import java.util.Date;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Gitlet {

    private static State state;
    private static String[] argm;
    private static final String GITLET_DIR = ".gitlet/";

    private static void init() {
        if (isInit()){
            System.out.println("A gitlet version control system already exists in the current directory.");
        }else {
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
        boolean hasAdd = state.addFile(f); 
        return;
    }

    private static void commit(String msg) {
        state.commit(msg);
    }
    
    private static void rm(String fileString) {
        File f = new File(fileString);
        boolean hasrm = state.removeFile(f); 
        return;
    }
    private static void log() {
        state.log();
    }   
    private static void globalLog() {
        state.glog();
    }   

    private static void find() {

    }   

    private static void status() {

    }   

    private static void ckout(String s, int id) {

    } 

    private static void ckout(String s) {
        state.checkout(s);
    }   

    private static void branch() {

    }   

    private static void rmBranch() {

    }   

    private static void reset() {

    }   

    //Helper methods
    private static void writeState() {
        try {
            File sfile = new File(".Gitlet/State.ser");
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
            File sfile = new File(".Gitlet/State.ser");
            FileInputStream fis = new FileInputStream(sfile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object sObj = ois.readObject();
            state = (State) sObj;
            ois.close();            
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("fail to read state");
        }
    }

    private static boolean isInit() {
        return new File(".Gitlet/State.ser").exists(); 
    }

    private static boolean checklength(int lg) {
        return argm.length >= lg;
    }

    private static void danger(){
        System.out.println("Warning: The command you entered may alter the files"
         + "in your working directory. Uncommitted changes may be lost." 
         + " Are you sure you want to continue? (yes/no)");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String textLine = reader.readLine();
            if (textLine.startsWith("yes")){
                return;
            } else {
                System.exit(0);
            }
        } catch (IOException e) {
            System.out.println("error reading your response.");
            System.out.println(e);
            System.exit(0);
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
                case "add": if (checklength(2)) add(args[1]);
                            break;
                case "commit": if (checklength(2)) commit(args[1]);
                                else System.out.println("Please enter a commit message.");
                            break;
                case "rm": if (checklength(2)) rm(args[1]);
                            break;
                case "log": log();
                            break;
                case "global-log": globalLog();
                            break;
                case "checkout": danger();
                        System.out.println(argm.length);
                        if (checklength(3)) ckout(args[2],Integer.parseInt(args[1]));
                        else if (checklength(2)) ckout(args[1]);
                        break;
            }
            writeState();
        } else {
            System.out.println("Gitlet is not initialized.");
        }
    }
}