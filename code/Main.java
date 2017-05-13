import java.util.ArrayList;
import java.util.Random;


public class Main {

	public static void main(String[] args) {
		ArrayList<Words> allWords = new ArrayList<Words>();
		ArrayList<Words> forPer = new ArrayList<Words>();
		FileFounder ff = new FileFounder();
		ff.fileFounder("data/comedies",allWords);
		ff.fileFounder("data/historical", forPer);
		ArrayList<BiGram> biGramWords = new ArrayList<BiGram>();
		ArrayList<TriGram> triGramWords = new ArrayList<TriGram>();
		ArrayList<String> uniGramSentence = new ArrayList<String>();
		ArrayList<String> biGramSentence = new ArrayList<String>();
		ArrayList<String> triGramSentence = new ArrayList<String>();
		ArrayList<String> probabilitySentence = new ArrayList<String>();
		probabilitySentence.add("To work or not to work, that is the problem!");
		probabilitySentence.add("Shall sleep more, Theodore shall sleep more.");
		probabilitySentence.add("It does not matter how slowly you go so long as you do not stop.");
		probabilitySentence.add("Imagination is more important than knowledge...");
		probabilitySentence.add("Thou seest, the heavens, as troubled with man's act"); 
		System.out.println("For task One ");
		
		biGramModelForTaskOne(probabilitySentence, allWords);
		
		taskTwo(allWords,biGramWords,triGramWords,uniGramSentence,biGramSentence,triGramSentence);
		System.out.println("Generate sentences for uni gram ");
		uniGramProbability(uniGramSentence, allWords);
		System.out.println("Generate sentences for bi gram ");
		biGramModelForTaskOne(biGramSentence,allWords);
		System.out.println("Generate sentences for tri gram ");
		triGramProbability(triGramSentence, allWords);
		/*double perUni = perplexityUni(forPer);		// When calculate perplexity , run time so hard 
		System.out.println(perUni);
		double perBi = perplexityBi(forPer);
		System.out.println(perBi);
		double perTri = perplexityTri(forPer);
		System.out.println(perTri);*/
	}
	
	public static double perplexityUni(ArrayList<Words> forPer){
		double sum = 0.0;
		for(int i = 0;i<forPer.size();i++){
			sum += Math.log((double)oneWordCount(forPer.get(i).getWord(), forPer)/(double)forPer.size());
		}
		
		return Math.pow(2, (1/forPer.size()*sum));
	}
	
	public static double perplexityBi(ArrayList<Words> forPer){
		double sum = 0.0;
		for(int i = 0;i<forPer.size();i++){
			sum += Math.log((double)twoWordCount(forPer.get(i).getWord(),forPer.get(i+1).getWord(), forPer)/(double)oneWordCount(forPer.get(i).getWord(),forPer));
		}
		
		return Math.pow(2, (1/forPer.size()*sum));
	}
	
	public static double perplexityTri(ArrayList<Words> forPer){
		double sum = 0.0;
		for(int i = 0;i<forPer.size();i++){
			sum += Math.log((double)threeWordCount(forPer.get(i).getWord(),forPer.get(i+1).getWord(),forPer.get(i+2).getWord(), forPer)/(double)twoWordCount(forPer.get(i).getWord(),forPer.get(i+1).getWord(), forPer));
		}
		
		return Math.pow(2, (1/forPer.size()*sum));
	}
	
	public static void triGramProbability(ArrayList<String> triGramSentence , ArrayList<Words> allWords){
		for(String s : triGramSentence ){
			double sum = 0;
			String[] splitString = s.split("[\\,\\;\\\t\\:\\-\\\n]");
			String sentence = null ;
			for(int i=0 ; i<splitString.length ; i++){
				if(i==0){
					sentence = splitString[i];				/* Again creating sentence */
				}
				else{
					sentence += splitString[i];
				}
			}
			System.out.println(sentence);
			
			String[] splitSentence = sentence.split("\\ ");		/*	Split sentence for whitespace	*/
			
			for(int i=0 ; i<splitSentence.length ; i++){
				if(i+2<splitSentence.length){
					if(splitSentence[i].equals("...")){			/*	only . , ... , ! , ? , calculating probability	*/
						sum += Math.log10((double)oneWordCount("...", allWords)/(double)allWords.size());
					}else if(splitSentence[i].equals(".")){
						sum += Math.log10((double)oneWordCount(".", allWords)/(double)allWords.size());
					}else if(splitSentence[i].equals("!")){
						sum += Math.log10((double)oneWordCount("!", allWords)/(double)allWords.size());
					}else if(splitSentence[i].equals("?")){
						sum += Math.log10((double)oneWordCount("?", allWords)/(double)allWords.size());
					}
					else if(splitSentence[i].equals("I")){		/* adding I to i , control this that */
						if(threeWordCount("i",splitSentence[i+1],splitSentence[i+1], allWords) != 0){
							sum += Math.log10((double)threeWordCount("i",splitSentence[i+1],splitSentence[i+2], allWords)/(double)twoWordCount("i", splitSentence[i+1], allWords));
						}else{
							sum += Math.log10((double)1/(double)allWords.size());	/*	Smooth	*/
						}
					}else if(splitSentence[i].contains("...")){
						String[] split3 = splitSentence[i].split("\\...");
						for(int j=0; j<split3.length ; j++){		/* calculating probability */
							if(threeWordCount(split3[j],splitSentence[i+1],splitSentence[i+2], allWords) != 0){
								sum += Math.log10((double)threeWordCount(split3[j],splitSentence[i+1],splitSentence[i+2], allWords)/(double)twoWordCount("...", splitSentence[i+1], allWords));
							}else{
								sum += Math.log10((double)1/(double)allWords.size());	/*	Smooth	*/
							}
						}
						if(oneWordCount("...", allWords) != 0){		/* calculating probability */
							sum += Math.log10((double)threeWordCount("...",splitSentence[i+1],splitSentence[i+2], allWords)/(double)twoWordCount("...", splitSentence[i+1], allWords));
						}else{
							sum += Math.log10((double)1/(double)allWords.size());	/*	Smooth	*/
						}
					}else if(splitSentence[i].contains(".")){
						String[] split2 = splitSentence[i].split("\\.");
						for(int j=0; j<split2.length ; j++){		/* calculating probability */
							if(threeWordCount(split2[j],splitSentence[i+1],splitSentence[i+2], allWords) != 0){
								sum += Math.log10((double)threeWordCount(split2[j],splitSentence[i+1],splitSentence[i+2], allWords)/(double)twoWordCount(".", splitSentence[i+1], allWords));
							}else{
								sum += Math.log10((double)1/(double)allWords.size());	/*	Smooth	*/
							}
						}
						if(oneWordCount(".", allWords) != 0){		/* calculating probability */
							sum += Math.log10((double)threeWordCount(".",splitSentence[i+1],splitSentence[i+2], allWords)/(double)twoWordCount(".", splitSentence[i+1], allWords));
						}else{
							sum += Math.log10((double)1/(double)allWords.size());	/*	Smooth	*/
						}
					}else if(splitSentence[i].contains("!")){
						String[] split4 = splitSentence[i].split("\\!");
						for(int j=0; j<split4.length ; j++){		/* calculating probability */
							if(threeWordCount(split4[j],splitSentence[i+1],splitSentence[i+2], allWords) != 0){
								sum += Math.log10((double)threeWordCount(split4[j],splitSentence[i+1],splitSentence[i+2], allWords)/(double)twoWordCount("!", splitSentence[i+1], allWords));
							}else{
								sum += Math.log10((double)1/(double)allWords.size());
							}
						}
						if(oneWordCount("!", allWords) != 0){		/* calculating probability */
							sum += Math.log10((double)threeWordCount("!",splitSentence[i+1],splitSentence[i+2], allWords)/(double)twoWordCount("!", splitSentence[i+1], allWords));
						}else{
							sum += Math.log10((double)1/(double)allWords.size());	/*	Smooth	*/
						}
					}else if(splitSentence[i].contains("?")){
						String[] split5 = splitSentence[i].split("\\?");
						for(int j=0; j<split5.length ; j++){		/* calculating probability */
							if(threeWordCount(split5[j],splitSentence[i+1],splitSentence[i+2], allWords) != 0){
								sum += Math.log10((double)threeWordCount(split5[j],splitSentence[i+1],splitSentence[i+2], allWords)/(double)twoWordCount("?", splitSentence[i+1], allWords));
							}else{
								sum += Math.log10((double)1/(double)allWords.size());	/*	Smooth	*/
							}
						}
						if(oneWordCount("!"
								+ "?", allWords) != 0){		/* calculating probability */
							sum += Math.log10((double)threeWordCount("?",splitSentence[i+1],splitSentence[i+2], allWords)/(double)twoWordCount("?", splitSentence[i+1], allWords));
						}else{
							sum += Math.log10((double)1/(double)allWords.size());	/*	Smooth	*/
						}
					}else{
						if(threeWordCount(splitSentence[i],splitSentence[i+1],splitSentence[i+2], allWords) != 0){
							sum += Math.log10((double)threeWordCount(splitSentence[i],splitSentence[i+1],splitSentence[i+2], allWords)/(double)twoWordCount(splitSentence[i], splitSentence[i+1], allWords));
						}else{
							sum += Math.log10((double)1/(double)allWords.size());	/*	Smooth	*/
						}
					}
				}
			}
			System.out.println("\nThis sentence create probability : log10(" + sum + ")\n");
		}
	}
	
	public static void uniGramProbability(ArrayList<String> uniGramSentence , ArrayList<Words> allWords){
		/* This function same triGramProbability */
		for(String s : uniGramSentence ){
			double sum = 0;
			String[] splitString = s.split("[\\,\\;\\\t\\:\\-\\\n]");
			String sentence = null ;
			for(int i=0 ; i<splitString.length ; i++){
				if(i==0){
					sentence = splitString[i];
				}
				else{
					sentence += splitString[i];
				}
			}
			System.out.println(sentence);
			
			String[] splitSentence = sentence.split("\\ ");
			
			for(int i=0 ; i<splitSentence.length ; i++){
				if(i+1<splitSentence.length){
					if(splitSentence[i].equals("I")){
						if(oneWordCount("i", allWords) != 0){
							sum += Math.log10((double)oneWordCount("i", allWords)/(double)allWords.size());
						}else{
							sum += Math.log10((double)1/(double)allWords.size());
						}
					}else if(splitSentence[i].contains("...")){
						String[] split3 = splitSentence[i].split("\\...");
						for(int j=0; j<split3.length ; j++){
							if(oneWordCount(split3[j], allWords) != 0){
								sum += Math.log10((double)oneWordCount(split3[j],allWords)/(double)allWords.size());
							}else{
								sum += Math.log10((double)1/(double)allWords.size());
							}
						}
						if(oneWordCount("...", allWords) != 0){
							sum += Math.log10((double)oneWordCount("...",allWords)/(double)allWords.size());
						}else{
							sum += Math.log10((double)1/(double)allWords.size());
						}
					}else if(splitSentence[i].contains(".")){
						String[] split2 = splitSentence[i].split("\\.");
						for(int j=0; j<split2.length ; j++){
							if(oneWordCount(split2[j], allWords) != 0){
								sum += Math.log10((double)oneWordCount(split2[j],allWords)/(double)allWords.size());
							}else{
								sum += Math.log10((double)1/(double)allWords.size());
							}
						}
						if(oneWordCount(".", allWords) != 0){
							sum += Math.log10((double)oneWordCount(".",allWords)/(double)allWords.size());
						}else{
							sum += Math.log10((double)1/(double)allWords.size());
						}
					}else if(splitSentence[i].contains("!")){
						String[] split4 = splitSentence[i].split("\\!");
						for(int j=0; j<split4.length ; j++){
							if(oneWordCount(split4[j], allWords) != 0){
								sum += Math.log10((double)oneWordCount(split4[j],allWords)/(double)allWords.size());
							}else{
								sum += Math.log10((double)1/(double)allWords.size());
							}
						}
						if(oneWordCount("!", allWords) != 0){
							sum += Math.log10((double)oneWordCount("!",allWords)/(double)allWords.size());
						}else{
							sum += Math.log10((double)1/(double)allWords.size());
						}
					}else if(splitSentence[i].contains("?")){
						String[] split5 = splitSentence[i].split("\\?");
						for(int j=0; j<split5.length ; j++){
							if(oneWordCount(split5[j], allWords) != 0){
								sum += Math.log10((double)oneWordCount(split5[j],allWords)/(double)allWords.size());
							}else{
								sum += Math.log10((double)1/(double)allWords.size());
							}
						}
						if(oneWordCount("?", allWords) != 0){
							sum += Math.log10((double)oneWordCount("?",allWords)/(double)allWords.size());
						}else{
							sum += Math.log10((double)1/(double)allWords.size());
						}
					}else{
						if(oneWordCount(splitSentence[i], allWords) != 0){
							sum += Math.log10((double)oneWordCount(splitSentence[i], allWords)/(double)allWords.size());
						}else{
							sum += Math.log10((double)1/(double)allWords.size());
						}
					}
				}
			}
			System.out.println("\nThis sentence create probability : log10(" + sum + ")\n");
		}
	}
	
	public static void biGramModelForTaskOne(ArrayList<String> probabilitySentence , ArrayList<Words> allWords){
		/* This function same triGramProbability */
		for(String s : probabilitySentence){
			double sum = 0;
			String[] splitString = s.split("[\\,\\;\\\t\\:\\-\\\n]");
			String sentence = null ;
			for(int i=0 ; i<splitString.length ; i++){
				if(i==0){
					sentence = splitString[i];
				}
				else{
					sentence += splitString[i];
				}
			}
			System.out.println(sentence);
			
			String[] splitSentence = sentence.split("\\ ");
			
			for(int i=0 ; i<splitSentence.length ; i++){
				if(i+1<splitSentence.length){
					if(splitSentence[i].equals("I")){
						if(twoWordCount("i", splitSentence[i+1], allWords) != 0){
							sum += Math.log10((double)twoWordCount("i", splitSentence[i+1], allWords)/(double)oneWordCount("i",allWords));
						}else{
							sum += Math.log10((double)1/(double)allWords.size());
						}
					}else if(splitSentence[i].contains("...")){
						String[] split3 = splitSentence[i].split("\\...");
						for(int j=0; j<split3.length ; j++){
							if(twoWordCount(split3[j],"...", allWords) != 0){
								sum += Math.log10((double)twoWordCount(split3[j],"...", allWords)/(double)oneWordCount(split3[j],allWords));
							}else{
								sum += Math.log10((double)1/(double)allWords.size());
							}
						}
					}else if(splitSentence[i].contains(".")){
						String[] split2 = splitSentence[i].split("\\.");
						for(int j=0; j<split2.length ; j++){
							if(twoWordCount(split2[j],"...", allWords) != 0){
								sum += Math.log10((double)twoWordCount(split2[j],"...", allWords)/(double)oneWordCount(split2[j],allWords));
							}else{
								sum += Math.log10((double)1/(double)allWords.size());
							}
						}
					}else if(splitSentence[i].contains("!")){
						String[] split4 = splitSentence[i].split("\\!");
						for(int j=0; j<split4.length ; j++){
							if(twoWordCount(split4[j],"!", allWords) != 0){
								sum += Math.log10((double)twoWordCount(split4[j],"!", allWords)/(double)oneWordCount(split4[j],allWords));
							}else{
								sum += Math.log10((double)1/(double)allWords.size());
							}
						}
					}else if(splitSentence[i].contains("?")){
						String[] split5 = splitSentence[i].split("\\?");
						for(int j=0; j<split5.length ; j++){
							if(twoWordCount(split5[j],"?", allWords) != 0){
								sum += Math.log10((double)twoWordCount(split5[j],"?", allWords)/(double)oneWordCount(split5[j],allWords));
							}else{
								sum += Math.log10((double)1/(double)allWords.size());
							}
						}
					}else{
						if(twoWordCount(splitSentence[i], splitSentence[i+1], allWords) != 0){
							sum += Math.log10((double)twoWordCount(splitSentence[i], splitSentence[i+1], allWords)/(double)oneWordCount(splitSentence[i],allWords));
						}else{
							sum += Math.log10((double)1/(double)allWords.size());
						}
					}
				}
			}
			System.out.println("\nThis sentence create probability : log10(" + sum + ")\n");
		}
	}
	
	public static int threeWordCount(String wordOne, String wordTwo ,String wordThree, ArrayList<Words> allWords){
		int countWord = 0;
		for(int i = 0 ; i<allWords.size() ; i++){
			if(i+2<allWords.size()){		/* Count for triGram probability */
				if(allWords.get(i).getWord().equals(wordOne) && allWords.get(i+1).getWord().equals(wordTwo)
						&& allWords.get(i+2).getWord().equals(wordThree)){
					countWord++;
				}
			}
		}
		
		return countWord;
	}
	
	public static int twoWordCount(String wordOne, String wordTwo , ArrayList<Words> allWords){
		int countWord = 0;
		for(int i = 0 ; i<allWords.size() ; i++){
			if(i+1<allWords.size()){	/* Count for biGram probability */
				if(allWords.get(i).getWord().equals(wordOne) && allWords.get(i+1).getWord().equals(wordTwo)){
					countWord++;
				}
			}
		}
		
		return countWord;
	}
	
	public static int oneWordCount(String word ,ArrayList<Words> allWords){
		int countWord = 0;
		for(Words w : allWords){
			if(w.getWord().equals(word))	/* Count for uniGram probability */
				countWord++;
		}
		return countWord;
	}

	public static void taskTwo(ArrayList<Words> allWords,ArrayList<BiGram> biGramWords,ArrayList<TriGram> triGramWords,
		ArrayList<String> uniGramSentence,ArrayList<String> biGramSentence,ArrayList<String> triGramSentence){
		for(int i = 0; i<10; i++){
			triGramSentence.add(generateSentenceForTriGram(allWords,triGramWords));
		}
		for(int i = 0; i<10; i++){
			biGramSentence.add(generateSentenceForBiGram(allWords,biGramWords));
		}
		for(int i = 0; i<10; i++){
			uniGramSentence.add(generateSentenceForUniGram(allWords));
		}
	}
	
	public static String generateSentenceForTriGram(ArrayList<Words> allWords, ArrayList<TriGram> triGramWords){
		String sentence = null;
		Random rand = new Random();
		/* firstly random location in arraylist, after generate sentence */
		for(int i = 0 ; i<7 ; i++){
			
			int number = rand.nextInt(allWords.size()-2);
			String newWord = otherWords(allWords.get(number).getWord(),allWords,triGramWords);
			if(i==0){
				if(allWords.get(number).getWord().contains("...")){
					sentence = allWords.get(number).getWord();
					break;
				}else if(allWords.get(number).getWord().contains(".")){
					sentence = allWords.get(number).getWord();
					break;
				}else if(allWords.get(number).getWord().contains("!")){
					sentence = allWords.get(number).getWord();
					break;
				}else if(allWords.get(number).getWord().contains("?")){
					sentence = allWords.get(number).getWord();
					break;
				}else if(newWord.contains("...")
						|| newWord.contains(".")
						|| newWord.contains("!")
						|| newWord.contains("?") ){
					sentence = allWords.get(number).getWord() + " " + newWord + " " ;
					break;
				}else{
					sentence = allWords.get(number).getWord() + " " +newWord + " " ;
				}
			}else{
				if(allWords.get(number).getWord().contains("...")){
					sentence += allWords.get(number).getWord() + " ";
					break;
				}else if(allWords.get(number).getWord().contains(".")){
					sentence += allWords.get(number).getWord() + " ";
					break;
				}else if(allWords.get(number).getWord().contains("!")){
					sentence += allWords.get(number).getWord() + " ";
					break;
				}else if(allWords.get(number).getWord().contains("?")){
					sentence += allWords.get(number).getWord() + " ";
					break;
				}else if(newWord.contains("...")
						|| newWord.contains(".")
						|| newWord.contains("!")
						|| newWord.contains("?") ){
					sentence =allWords.get(number).getWord() + " " + newWord  + " ";
					break;
				}else{
					sentence +=allWords.get(number).getWord() + " " + newWord + " ";
				}
			}
		}
		return sentence;
	}
	
	public static String otherWords(String word,ArrayList<Words> allWords,ArrayList<TriGram> triGramWords){
		TriGram newWord ;
		/* creating triGram model */
		for(int i = 0 ; i<allWords.size() ; i++){
			if(i+2<allWords.size()){
				if(allWords.get(i).getWord().equals(word)){
					newWord = new TriGram();
					newWord.setWord(allWords.get(i+1).getWord());
					newWord.setWordTwo(allWords.get(i+2).getWord());
					triGramWords.add(newWord);
				}
			}
		}
		Random rand = new Random();
		int number = rand.nextInt(triGramWords.size());
		if(triGramWords.get(number).getWord().contains("...") ||
				triGramWords.get(number).getWord().contains(".") ||
				triGramWords.get(number).getWord().contains("?") ||
				triGramWords.get(number).getWord().contains("!") ){
			return triGramWords.get(number).getWord();
		}else{
			return (triGramWords.get(number).getWord() + " " + triGramWords.get(number).getWordTwo());
		}
	}
	
	public static String generateSentenceForBiGram(ArrayList<Words> allWords , ArrayList<BiGram> biGramWords){
		String sentence = null;
		Random rand = new Random();
		/* firstly random location in arraylist, after generate sentence */
		for(int i = 0 ; i<10 ; i++){
			int number = rand.nextInt(allWords.size()-1);
			String newWord =  otherWord(allWords.get(number).getWord(),allWords,biGramWords);
			if(i==0){
				if(allWords.get(number).getWord().contains("...")){
					sentence = allWords.get(number).getWord();
					break;
				}else if(allWords.get(number).getWord().contains(".")){
					sentence = allWords.get(number).getWord();
					break;
				}else if(allWords.get(number).getWord().contains("!")){
					sentence = allWords.get(number).getWord();
					break;
				}else if(allWords.get(number).getWord().contains("?")){
					sentence = allWords.get(number).getWord();
					break;
				}else if(newWord.contains("...")
						|| newWord.contains(".")
						|| newWord.contains("!")
						|| newWord.contains("?") ){
					sentence = allWords.get(number).getWord() + " " + newWord + " " ;
					break;
				}else{
					sentence = allWords.get(number).getWord() + " " +newWord + " " ;
				}
			}else{
				if(allWords.get(number).getWord().contains("...")){
					sentence += allWords.get(number).getWord() + " ";
					break;
				}else if(allWords.get(number).getWord().contains(".")){
					sentence += allWords.get(number).getWord() + " ";
					break;
				}else if(allWords.get(number).getWord().contains("!")){
					sentence += allWords.get(number).getWord() + " ";
					break;
				}else if(allWords.get(number).getWord().contains("?")){
					sentence += allWords.get(number).getWord() + " ";
					break;
				}else if(newWord.contains("...")
						|| newWord.contains(".")
						|| newWord.contains("!")
						|| newWord.contains("?") ){
					sentence =allWords.get(number).getWord() + " " + newWord  + " ";
					break;
				}else{
					sentence +=allWords.get(number).getWord() + " " + newWord + " ";
				}
			}
		}
		return sentence;
	}
	
	public static String otherWord(String word,ArrayList<Words> allWords,ArrayList<BiGram> biGramWords){
		BiGram newWord ;
		/* creating biGram model */
		for(int i = 0 ; i<allWords.size() ; i++){
			if(i+1<allWords.size()){
				if(allWords.get(i).getWord().equals(word)){
					newWord = new BiGram();
					newWord.setWord(allWords.get(i+1).getWord());
					biGramWords.add(newWord);
				}
			}
		}	
		Random rand = new Random();
		int number = rand.nextInt(biGramWords.size());
		return biGramWords.get(number).getWord();
	}

	public static String generateSentenceForUniGram(ArrayList<Words> allWords){
		String sentence = null;
		/* firstly random location in arraylist, after generate sentence */
		for(int i = 0 ; i<20 ; i++){
			Random rand = new Random();
			int number = rand.nextInt(allWords.size());
			
			if(i==0){
				if(allWords.get(number).getWord().contains("...")){
					sentence = allWords.get(number).getWord();
					break;
				}else if(allWords.get(number).getWord().contains(".")){
					sentence = allWords.get(number).getWord();
					break;
				}else if(allWords.get(number).getWord().contains("!")){
					sentence = allWords.get(number).getWord();
					break;
				}else if(allWords.get(number).getWord().contains("?")){
					sentence = allWords.get(number).getWord();
					break;
				}else{
					sentence = allWords.get(number).getWord();
				}
			}else{
				if(allWords.get(number).getWord().contains("...")){
					sentence += " "  + allWords.get(number).getWord();;
					break;
				}else if(allWords.get(number).getWord().contains(".")){
					sentence += " "  + allWords.get(number).getWord();
					break;
				}else if(allWords.get(number).getWord().contains("!")){
					sentence += " "  + allWords.get(number).getWord();
					break;
				}else if(allWords.get(number).getWord().contains("?")){
					sentence += " "  + allWords.get(number).getWord();
					break;
				}else{
					sentence += " "  + allWords.get(number).getWord();
				}
			}
		}
		return sentence;
	}
	
}
