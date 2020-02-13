package client;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {
	private final static String[] METHOD = { "list", "describe", "book" };
	private final static String[] LIST_OPTIONS = { "a", "c", "i", "d", "t", "n" };
	private final static String[] DESC_OPTIONS = { "a", "d" };

	public ArgumentParser() {
	}

	public Map<String, String> parseArgs(String command) throws CommandException {
		Map<String, String> argsMap = new HashMap<>();
		String[] args = command.split(" ");
		int iterator = 0;

		if (args.length > 0 && Arrays.asList(METHOD).contains(args[iterator])) {
			argsMap.put("method", args[iterator++]);
		} else throw new CommandException();
		if (args.length > 1 && '-' == args[iterator].charAt(0)) {
			argsMap.putAll(setArgs(args[iterator++].substring(1), argsMap.get("method")));
		}
		fillArgs(argsMap, args, iterator);

		return argsMap;
	}

	private Map<String, String> setArgs(String options, String method) throws CommandException {
		Map<String, String> optionsMap  = new HashMap<>();
		if(Arrays.asList(options.split("")).stream().anyMatch(o -> !String.join("", LIST_OPTIONS).contains(o)) 
			|| (method.equals(METHOD[1]) && Arrays.asList(options.split("")).stream().anyMatch(o -> !String.join("", DESC_OPTIONS).contains(o)))
			|| method.equals(METHOD[2]) || options.length() == 0) throw new CommandException(method);
				if(options.contains("d")) optionsMap.put("date", "");
		if(options.contains("t")) optionsMap.put("time", "");
		if(options.contains("n")) optionsMap.put("nbpers", "");
		optionsMap.put("options", options);
		
		return optionsMap;
	}

	private void fillArgs(Map<String, String> argsMap, String[] args, int iterator) throws CommandException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if(METHOD[2].equals(argsMap.get("method"))) df = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
		
		try {
			if (METHOD[1].equals(argsMap.get("method")) || METHOD[2].equals(argsMap.get("method"))) argsMap.put("room", args[iterator++]);
			if (argsMap.containsKey("date") || METHOD[2].equals(argsMap.get("method"))) {
				String[] dates = args[iterator++].split(":");
				long d2, d1 = df.parse(dates[0]).getTime();
				argsMap.put("date", d1 + "");
				if(dates.length == 2) {
					d2 = df.parse(dates[1]).getTime();
					argsMap.put("date_end", d2 + "");
				}
			}
			if (argsMap.containsKey("time") || METHOD[2].equals(argsMap.get("method"))) argsMap.put("time", Integer.parseInt(args[iterator++]) + "");
			if (argsMap.containsKey("nbpers")) argsMap.put("nbers", args[iterator++]);
		} catch(IndexOutOfBoundsException | ParseException | NumberFormatException e) { throw new CommandException(argsMap.get("method")); }
	}
}
