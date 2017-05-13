
public class Words {
	private String word;
	private int totalWordCount;
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getTotalWordCount() {
		return totalWordCount;
	}
	public void setTotalWordCount(int totalWordCount) {
		this.totalWordCount = this.totalWordCount + totalWordCount;
	}
}
