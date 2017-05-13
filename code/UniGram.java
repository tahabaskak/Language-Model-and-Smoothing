
public class UniGram {

	private String word;
	private int wordCount;
	private double wordProbabilityForUniGram;
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getWordCount() {
		return wordCount;
	}
	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}
	public void incrementWordCount(){
		setWordCount(getWordCount() +1 );
	}
	public double getWordProbabilityForUniGram() {
		return wordProbabilityForUniGram;
	}
	public void setWordProbabilityForUniGram(double wordProbabilityForUniGram) {
		this.wordProbabilityForUniGram = wordProbabilityForUniGram;
	}
	public void incrementProbabilityForUniGram(double allWordCount){
		setWordProbabilityForUniGram((double)getWordCount() / allWordCount);
	}
	
	
}
