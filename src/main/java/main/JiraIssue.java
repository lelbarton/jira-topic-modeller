package main;

public class JiraIssue implements Issue {

	private String key;
	private int id;
	private String date;
	private String summary;
	private String contentText;
	private String authorName;
	private String userName;
	private AnnotatedIssue annotations;
	private MALLETInstanceManager instanceManager;

	/*
	 * Constructor for JIRA issue object
	 * 
	 * @param key The key of the issue, e.g. JIRA-09
	 * 
	 * @param id The id number of the issue
	 * 
	 * @param summary The natural language summary
	 * 
	 * @param desc The content of the issue post
	 * 
	 * @param date The date this was posted
	 * 
	 * @param displayName The display name of the poster
	 * 
	 * @param userName The Atlassian user name of the poster
	 */
	public JiraIssue(String key, int id, String summary, String desc, String date, String displayName,
			String userName) {
		this.id = id;
		this.key = key;
		this.date = date;
		this.summary = summary;
		this.contentText = desc;
		this.authorName = displayName;
		this.userName = userName;
		this.instanceManager = MALLETInstanceManager.getInstance();
		instanceManager.addInstance(this);
	}

	public String getSummary() {
		return this.summary;
	}

	public String getKey() {
		return this.key;
	}

	public String getContent() {
		return this.contentText;
	}

	public int getID() {
		return this.id;
	}

	public String getAuthor() {
		return this.authorName;
	}
}
