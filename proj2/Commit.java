import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.util.Date;
import java.io.Serializable;

public class Commit implements Serializable  {
	private final Commit previous;
	private final long date; 
	private final int commitid;
	private final HashMap<File,File> fileAddress;
	private final String message;

	public Commit() {
		// default initial commit
		previous = null;
		date = System.currentTimeMillis();
		commitid = 0; 
		fileAddress = new HashMap<File,File>();
		message = "initial commit";
	}

	public Commit(Commit prev, int id, Map ads, String msg) {
		previous = prev;
		date = System.currentTimeMillis();
		commitid = id; 
		fileAddress = new HashMap<File,File>();
		fileAddress.putAll(ads);
		message = msg;
	}

	public int getID(){
		return commitid;
	}

	public HashMap<File,File> getAddresses() {
		HashMap<File,File> returnMap = new HashMap<File,File>();
		returnMap.putAll(fileAddress);
		return returnMap;
	}

	public File getAddress(File f) {
		return fileAddress.get(f);
	}

	public String getMessage() {
		return message;
	}

	public Commit getPrev() {
		return previous;
	}

	public Date getDate() {
		return new Date(date);
	}

}