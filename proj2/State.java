import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.nio.file.Files;
import java.util.Date;
import java.io.IOException;
import java.io.Serializable;
import java.lang.RuntimeException;
import java.text.SimpleDateFormat;
import java.nio.file.StandardCopyOption;


public class State implements Serializable {
	protected Commit root;  // not sure how useful is this; might review later
							// Stores the initial commit address
	// List of IO addresses to the files to be copied
	protected HashSet<File> addSet;
	protected HashSet<File> rmSet;
	// pointers to end of each branch as well as HEAD pointer
	public HashMap<String,Commit> pointers;
	// list of all commits according to their ID
	protected ArrayList<Commit> commitList;
	// commit id of the next commit
	protected int cid = 0;
	private static final String GITLET_DIR = ".Gitlet/";

	public State() {
		root = new Commit();
		addSet = new HashSet<File>();
		rmSet = new HashSet<File>();
		pointers = new HashMap<String, Commit>();
		pointers.put("HEAD", root);
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
				System.out.println("File has not been modified since the last commit.");
			}
		} else {
			System.out.println("File does not exist, or the address is not a file.");
		}
		return false;
	}

	public void commit(String msg) {
		// !!! case where no add occurs
		if (addSet.isEmpty() && rmSet.isEmpty()) {
			System.out.println("No changes added to the commit.");
			return;
		}
		Commit prev = pointers.get("HEAD");
		// make new dir, move files
		HashMap<File,File> ads = commitHelper();
		Commit c = new Commit(prev, cid, ads, msg);
		commitList.add(c);
		pointers.put("HEAD", c);
		addSet.clear(); // clear add set
		rmSet.clear();
		cid += 1;		// update commitid
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
		// only file case for now
		File fnow = new File(s);
		File fi = getheadFile(fnow);
		try {
			// replace current file by old file
			Files.copy(fi.toPath(),fnow.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			System.out.println(e);
			System.out.println("checkout failure");
		}
	}



	// HELPER METHODS

	// Fix this!
	private boolean isModified(File f) {
		File prevFile = getheadFile(f);
		if (prevFile == null) {
			return true;
		}
		try {
			byte[] encodeNew = Files.readAllBytes(f.toPath());
			byte[] encoded = Files.readAllBytes(getheadFile(f).toPath()); // !!!what's file name?
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
			System.exit(1);
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