package Common.src;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class Configuration {
	public String HOST; // 部署的时候，这里需要换成本地的IP地址
	public int PORT;

	public Configuration() {
	    String path = Paths.get(".").toAbsolutePath().normalize().toString();
	    try {
            @SuppressWarnings("resource")
            String content = new Scanner(new File(path + "/" + "config.json")).useDelimiter("\\Z").next();
            JSONObject obj = new JSONObject(content);
            HOST = obj.getString("HOST");
            PORT = obj.getInt("PORT");
        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        }
    }

	public static void main(String[] args) {
        Configuration config = new Configuration();
        System.out.println(config.HOST + ":" + config.PORT);
    }
}
