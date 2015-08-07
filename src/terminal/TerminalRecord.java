package terminal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TerminalRecord {
	
	private String path=".\\text.txt";
	private FileWriter writer;
	private File file;
	public TerminalRecord(){
		file=new File(path);
		/*try {
			writer=new FileWriter(file, false);
			writer.write("");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	public void append(String cmd){
		try {
			writer=new FileWriter(file,true);
			writer.append(cmd);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
