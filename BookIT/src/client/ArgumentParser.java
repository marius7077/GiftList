package client;

import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {
	
	public ArgumentParser() {}
	
	public Map<String, String> parseArgs(String command) {
		Map<String, String> argsMap  = new HashMap<>();
		String[] args = command.split(" ");
		
		if(args.length > 0) argsMap.put("method", getMethod(args[0]));
		
		if(args.length > 1) {
			if('-' == args[1].charAt(0)) {
				argsMap.putAll(setArgs(args[1].substring(1)));
			}
			else {
				argsMap.put("room", args[1]);
				return argsMap;
			}
		}
		
		fillArgs(argsMap, args);
		
		return argsMap;
	}

	private String getMethod(String method) {
		switch(method) {
			case "look": return "l";
			case "see": return "s";
			case "book": return "b";
			default: return null;
		}
	}

	private Map<String, String> setArgs(String options) {
		Map<String, String> optionsMap  = new HashMap<>();

		if(options.contains("a")) {
			optionsMap.put("options", "a");
			return optionsMap;
		} else {
			optionsMap.put("options", "");
			if(options.contains("f")) optionsMap.put("options", "f");
			if(options.contains("i")) optionsMap.put("options", optionsMap.get("options") + "i");
		}

		if(options.contains("d")) optionsMap.put("date", "");
		if(options.contains("t")) optionsMap.put("duree", "");
		if(options.contains("n")) optionsMap.put("nbplaces", "");
		return optionsMap;
	}
	
	private void fillArgs(Map<String, String> argsMap, String[] args) {
		int iterator = 2;
		
		if("s".equals(argsMap.get("method"))) argsMap.put("room", args[iterator++]);
		if(argsMap.containsKey("date")) argsMap.put("date", args[iterator++]);
		if(argsMap.containsKey("duree")) argsMap.put("duree", args[iterator++]);
		if(argsMap.containsKey("nbplaces")) argsMap.put("nbplaces", args[iterator++]);		
	}
}
