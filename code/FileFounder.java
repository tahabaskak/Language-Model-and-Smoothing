import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class FileFounder {

	public void fileFounder(String path,ArrayList<Words> allWords){
		File paths = new File(path);
		File[] pathList = paths.listFiles();
		
		if(pathList == null){
			return;
		}
		
		for(File f : pathList){
			if(f.isDirectory()){
				fileFounder(f.getAbsolutePath(),allWords);
			}
			else{
				//System.out.println("File: "+ f.getAbsolutePath());
				
				readFile(f.getAbsolutePath(),allWords);
			}
		}
	}
	
	public void readFile (String fileName,ArrayList<Words> allWords){
		try{
			FileReader inputFile = new FileReader(fileName);
			BufferedReader  bufferReader = new BufferedReader(inputFile);
			String line;
			while((line = bufferReader.readLine()) != null){
				addAllWords(line,allWords);		
			}
			bufferReader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void addAllWords(String line,ArrayList<Words> allWords){
		Words newWord;
		Words newWord2;
		if(!line.contains("<") && !line.isEmpty()){
			String[] splitString = line.split("[\\,\\;\\\t\\ \\:\\-\\\n]");
			/* \\!\\?\\,\\. */
			for(int i=0 ; i<splitString.length ; i++){
				if(!splitString[i].isEmpty()){
					newWord = new Words();
					newWord2 = new Words();
					if(splitString[i].equals("I")){
						newWord.setWord("i");
						newWord.setTotalWordCount(1);
						allWords.add(newWord);
					}
					else if(splitString[i].contains("...")){
						String[] split3 = splitString[i].split("\\...");
						newWord.setWord(split3[0].toLowerCase());
						allWords.add(newWord);
						newWord2.setWord("...");
						allWords.add(newWord2);
					}
					else if(splitString[i].contains(".")){
						String[] split2 = splitString[i].split("\\.");
						for(int j=0; j<split2.length ; j++){
							newWord.setWord(split2[j].toLowerCase());
							allWords.add(newWord);
						}
						newWord2.setWord(".");
						allWords.add(newWord2);
					}
					
					else if(splitString[i].contains("!")){
						String[] split4 = splitString[i].split("\\!");
						newWord.setWord(split4[0].toLowerCase());
						allWords.add(newWord);
						newWord2.setWord("!");
						allWords.add(newWord2);
					}
					else if(splitString[i].contains("?")){
						//System.out.println(splitString[i]);
						String[] split5 = splitString[i].split("\\?");
						for(int j=0; j<split5.length ; j++){
							newWord.setWord(split5[j].toLowerCase());
							allWords.add(newWord);
							//System.out.println(split5[j]);
						}
						newWord2.setWord("?");
						allWords.add(newWord2);
					}
					else{
						newWord.setWord(splitString[i].toLowerCase());
						newWord.setTotalWordCount(1);
						allWords.add(newWord);
					}
					
				}				
			}
		}
	}

}
