/**
 * Main class where the execution will start from
 * @author Hussein Al Osman
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;

/*SEG2106 Assignment 4
Justin Huynh
7745112
 */

public class Main {

	

	/**
	 * Get all the files from a directory
	 * @param dir File instance representing the directory
	 * @return Array List of all the files in the directory
	 */
	private static ArrayList<File> getFiles (File dir){
		ArrayList<File> filesList = new ArrayList<File>();
		
		String [] fileNames = dir.list();
		
		for (String fileName: fileNames){
			File file = new File(dir.getAbsoluteFile()+"/"+fileName);
			if (file.isFile()){
				filesList.add(file);
			}

		}
		
		return filesList;
	} 
	
	
	public static void main(String args[]) throws IOException {
        
		if (args.length > 0){ // Make sure an argument was passed to this program
		
			// The argument should represent a directory
			File dir = new File (args[0]);
			
			// Check if dir is in fact a directory
			if (dir.isDirectory()){
				
				// Read the files in the directory
				ArrayList<File> filesList = getFiles(dir);

				//Ask user if they want to search using one thread per file or only 4 threads
				System.out.println("Enter X to search using one thread per file. Enter Y to search using 4 threads");
				Scanner scanIn = new Scanner(System.in);
				String searchMode = scanIn.nextLine();

				// Get the pattern that will be searched for by prompting the user
				System.out.println("Enter the pattern to look for:");
				String pattern  = scanIn.nextLine();
				//System.out.println(pattern);
				scanIn.close();

				// Create an array of search jobs
				SearchJob[] jobs = new SearchJob[filesList.size()];
				for (int i = 0; i < jobs.length; i++) {
					jobs[i] = new SearchJob(filesList.get(i));
					//System.out.println(filesList.get(i));
				}

				//Exercise 1: One search thread per file
				if (searchMode.equals("X")) {
					//Create a thread group
					ThreadGroup g = new ThreadGroup("thread group");

					SearchTask[] a_searchTasks = new SearchTask[jobs.length];

					//Create a SearchTask Thread for each file and assign it to the thread group
					for (int i = 0; i < jobs.length; i++) {
						a_searchTasks[i] = new SearchTask(g, jobs[i], pattern);
						a_searchTasks[i].start();
						//System.out.println(g.activeCount());

					}
				}

				//Exercise 2: Only 4 search threads at a time
				else if (searchMode.equals("Y")){
					ThreadGroup g = new ThreadGroup("thread group");
					SearchTask[] a_searchTasks = new SearchTask[4];

					//Instantiate and start the first four threads
					for (int j = 0; j < 4; j++) {
						a_searchTasks[j] = new SearchTask(g, jobs[j], pattern);
						a_searchTasks[j].start();
					}

					int i = 4; //Keeps track of the next job to add

					//Check if the four running threads are complete
					while(i <= jobs.length - 4){
						if(g.activeCount() < 1){
							//System.out.println("number of active threads:" + g.activeCount());

							//Add the next four jobs to the threads
							for (int j = 0; j < 4; j++){
								a_searchTasks[j] = new SearchTask(g, jobs[i++], pattern);
								a_searchTasks[j].start();
							}
						}
					}
				}

				/* Call the search task in order to search for the entered sequence
				SearchTask searchTask = new SearchTask(jobs, pattern);
				searchTask.runSearch();*/

			}
			else {
				// The argument specified is not a path for a directory
				System.err.println("Incorrect argument: must be a directory");
			}
		}
		else {
			// No argument were passed to the program
			System.err.println("Missing argument: files directory");
		}
    }
    
}