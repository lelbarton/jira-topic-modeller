package main;

import cc.mallet.types.Instance;

public class JiraIssue implements Issue {

	private String key;
	private int id;
	private String date;
	private String summary;
	private String contentText;
	private String displayName;
	private Instance instance; // topic model instance

	/*
	 * Constructor for JIRA issue object
	 * 
	 * @param key The key of the issue, e.g. JIRA-09
	 * 
	 * @param id The id number of the issue
	 * 
	 * @param summary The subject line
	 * 
	 * @param desc The content of the post
	 * 
	 * @param date The date this was posted
	 * 
	 * @param displayName The display name of the author
	 */
	public JiraIssue(String key, int id, String summary, String desc, String date, String displayName) {
		this.setId(id);
		this.setKey(key);
		this.setDate(date);
		this.setSummary(summary);
		this.setContent(desc);
		this.setDisplayName(displayName);
		// instance set by topic modeler class when this added to issues manager
		this.instance = null;
	}

	// *** Getters and Setters **

	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	public Instance getInstance() {
		return this.instance;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return contentText;
	}

	public void setContent(String contentText) {
		this.contentText = contentText;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String authorName) {
		this.displayName = authorName;
	}
}
