package main;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.StringEscapeUtils;;

public class TopicWriter {

	private String fileName;
	private FileWriter writer;

	public TopicWriter(String fileName) {
		this.fileName = fileName;

		try {
			this.writer = new FileWriter(fileName);
			writer.append("Issue Id");
			writer.append(",");
			writer.append("Subject");
			writer.append(",");
			writer.append("Topic #");
			writer.append(",");
			writer.append("Weight");
			writer.append(",");
			writer.append("Main Topic");
			writer.append(",");
			writer.append("Content");
			writer.append("\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Could not write to file " + fileName);
			e.printStackTrace();
		}
	}

	public void writeTopicModel(String id, String subject, String topics, String content) {
		// TODO
		try {
			writer.append(id);
			writer.append(",");
			writer.append(StringEscapeUtils.escapeCsv(subject));
			writer.append(",");
			writer.append(topics);
			writer.append(",");
			writer.append(StringEscapeUtils.escapeCsv(content));
			writer.append("\n");
			// TODO add content for all issues

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Could not add issue " + id);
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
