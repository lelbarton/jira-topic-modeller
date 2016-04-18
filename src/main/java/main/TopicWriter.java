package main;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.StringEscapeUtils;

public class TopicWriter {

	private String fileName;
	private FileWriter writer;
	private static final String COLUMN_HEADERS = "Key, Author, Subject, Content, Main Topic, Topic #, Weight \n";
	private static final String DELIMITER = ",";

	/*
	 * Creates a new csv file with the given file name. Be sure to call the
	 * close method when all topic info has been written
	 */
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

	/*
	 * Writes a row of the CSV with the given issue info
	 */
	public void writeTopicModel(String key, String author, String subject, String content, String topicInfo) {
		try {
			writer.append(key + DELIMITER);
			writer.append(author + DELIMITER);
			writer.append(StringEscapeUtils.escapeCsv(subject) + DELIMITER);
			writer.append(StringEscapeUtils.escapeCsv(content) + DELIMITER);
			writer.append(topicInfo + "\n");

		} catch (IOException e) {
			System.err.println("Could not write issue " + key + " to csv");
			e.printStackTrace();
		}
	}

	/*
	 * Closes the output stream to the CSV when finished writing
	 */
	public void closeTopicModelCsv() {
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.err.println("Could not close file " + fileName);
			e.printStackTrace();
		}
	}
}
