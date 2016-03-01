package main;

public class JiraIssue implements Issue {

	private String key;
	private int id;
	private String date;
	private String summary;
	private String contentText;
	private String authorName;
	private String userName;

	public JiraIssue(String key, int id, String summary, String desc, String date, String displayName,
			String userName) {
		this.id = id;
		this.key = key;
		this.date = date;
		this.summary = summary;
		this.contentText = desc;
		this.authorName = displayName;
		this.userName = userName;
	}
}
