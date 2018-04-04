import java.io.File;
/*SEG2106 Assignment 4
Justin Huynh
7745112
 */


public class SearchJob {

	private String name;
	private File file;
	
	public SearchJob (File file){
		name = file.getName();
		
		this.file = file;
	}
	
	public String getName (){
		return name;
	}
	
	public File getFile(){
		return file;
	}
}
