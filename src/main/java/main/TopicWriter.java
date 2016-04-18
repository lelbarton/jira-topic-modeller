package main;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.StringEscapeUtils;

public class TopicWriter {

	private String fileName;
	private FileWriter writer;
	private static final String COLUMN_HEADERS = "Key, Author, Subject, Content, Main Topic, Topic #, Weight \n";

	public TopicWriter(String fileName) {
		this.fileName = fileName;

		try {
			this.writer = new FileWriter(fileName);
			writer.append(COLUMN_HEADERS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Could not write to file " + fileName);
			e.printStackTrace();
		}
	}

	public void writeTopicModel(String key, String author, String subject, String content, String topicInfo) {
		// TODO
		try {
			writer.append(key);
			writer.append(",");
			writer.append(author);
			writer.append(",");
			writer.append(StringEscapeUtils.escapeCsv(subject));
			writer.append(",");
			writer.append(StringEscapeUtils.escapeCsv(content));
			writer.append(topicInfo);
			writer.append("\n");
			// TODO add content for all issues

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Could not write issue " + key + " to csv");
			e.printStackTrace();
		}
	}

	public void closeTopicModelCsv() {
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Could not close file " + fileName);
			e.printStackTrace();
		}
	}
}
