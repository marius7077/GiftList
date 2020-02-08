package client;

import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {
	
	public ArgumentParser() {}
	
	public Map<String, String> parseArgs(String command) {
		Map<String, String> argsMap  = new HashMap<>();
		String[] args = command.split(" ");
		int iterator = 0;
		
		if(args.length > 0) argsMap.put("method", getMethod(args[iterator++]));
		
		if(args.length > 1) {
			if('-' == args[iterator].charAt(0)) {
				argsMap.putAll(setArgs(args[iterator++].substring(1)));
			}
		}
		
		fillArgs(argsMap, args, iterator);
		
		return argsMap;
	}

	private String getMethod(String method) {
		switch(method) {
			case "look": return "look";
			case "see": return "see";
			case "book": return "book";
			case "connect": return "connect";
			case "disconnect": return "disconnect";
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
			if(options.contains("l")) optionsMap.put("options", optionsMap.get("options") + "l");
		}

		if(options.contains("d")) optionsMap.put("date", "");
		if(options.contains("t")) optionsMap.put("duree", "");
		if(options.contains("n")) optionsMap.put("nbplaces", "");
		return optionsMap;
	}
	
	private void fillArgs(Map<String, String> argsMap, String[] args, int iterator) {
		if("see".equals(argsMap.get("method")) || "book".equals(argsMap.get("method"))) argsMap.put("room", args[iterator++]);
		if(argsMap.containsKey("date") || "book".equals(argsMap.get("method"))) argsMap.put("date", args[iterator++]);
		if(argsMap.containsKey("duree") || "book".equals(argsMap.get("method"))) argsMap.put("duree", args[iterator++]);
		if(argsMap.containsKey("nbplaces")) argsMap.put("nbplaces", args[iterator++]);	
		System.out.println(argsMap);
	}
}
