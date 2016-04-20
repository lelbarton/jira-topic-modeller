package main;

import cc.mallet.types.Instance;

public interface Issue {

	public void setInstance(Instance instance);

	public Instance getInstance();

	public String getDate();

	public void setDate(String date);

	public String getKey();

	public void setKey(String key);

	public int getId();

	public void setId(int id);

	public String getSummary();

	public void setSummary(String summary);

	public String getContent();

	public void setContent(String contentText);

	public String getDisplayName();

	public void setDisplayName(String displayName);

}
