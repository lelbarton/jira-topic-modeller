package retrieve;
import java.io.IOException;

public interface IIssueFetcher {

	public String httpGet(String urlStr) throws IOException;

}
