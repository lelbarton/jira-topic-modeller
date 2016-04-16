package main;

import cc.mallet.types.Instance;

public interface Issue {

	public String getKey();

	public String getSummary();

	public String getContent();

	public int getID();

	public String getAuthor();

	public void setInstance(Instance instance);

	public Instance getInstance();

}
