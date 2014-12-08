package Common.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class Comfy {
	public Socket socket = null;
	public Map<String, Action> actionsMap = new HashMap<String, Action>();
	public BufferedReader in = null;
	public PrintStream out = null;

	public Comfy(Socket socket) throws IOException {
		this.socket = socket;
		initInAndOutStream();
		waitForCommand();
	}

	private void initInAndOutStream() throws IOException {
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintStream(socket.getOutputStream());
	}

	public void accept(String commandName, Action action) {
		actionsMap.put(commandName, action);
	}

	public void send(String commandName, JSONObject data) throws JSONException {
		data.put("command", commandName);
		out.println(data.toString());
	}

	private void waitForCommand() {
		Thread thread = new Thread(new CommandWaiter());
		thread.start();
	}

	private class CommandWaiter implements Runnable {
		@Override
		public void run() {
			while(true) {
				String dataStr;
				try {
					dataStr = in.readLine();
					JSONObject data = new JSONObject(dataStr);
					String commandName = data.getString("command");
					Action action = actionsMap.get(commandName);
					if (action != null) {
						// 接收到一个命令，开启新线程去启动该行为
						action.data = data;
						new Thread(action).start();
					} else {
						// 命令不存在则进行警告，并不报错
						System.out.println("Command: " + commandName + " is not found!");
					}
				} catch (IOException | JSONException e) {
					;
				}
			}
		}
	}
}
