package com.gmail.br45entei.util;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

public class StringUtil {
	
	public static final String undoFilesystemSafeReplaced(String s) {
		return s.replace(";", ":").replace("''", "\"").replace("[ast]", "*").replace("[pipe]", "|").replace("&lt;", "<").replace("&gt;", ">");
	}
	
	public static final String makeStringFilesystemSafeReplaced(String s) {
		int len = s.length();
		StringBuilder sb = new StringBuilder(len);
		for(int i = 0; i < len; i++) {
			char ch = s.charAt(i);
			if(ch < 0x10 || ch >= 0x7F) {
				sb.append('0');
				continue;
			}
			if(ch == ':') {
				sb.append(";");
			} else if(ch == '"') {
				sb.append("''");
			} else if(ch == '*') {
				sb.append("[ast]");
			} else if(ch == '|') {
				sb.append("[pipe]");
			} else if(ch == '<') {
				sb.append("&lt;");
			} else if(ch == '>') {
				sb.append("&gt;");
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}
	
	private static final boolean isCharIllegal(String str) {// Why is ' illegal here...???
		return(str.equals("\n") || str.equals("\r") || str.equals("\t") || str.equals("\0") || str.equals("\f") || str.equals("`") || str.equals(":") || str.equals("'") || str.equals("?") || str.equals("*") || str.equals("<") || str.equals(">") || str.equals("|") || str.equals("\""));
	}
	
	public static final String makeStringFilesystemSafe(String s) {
		char escape = '%'; // ... or some other legal char.
		int len = s.length();
		StringBuilder sb = new StringBuilder(len);
		for(int i = 0; i < len; i++) {
			char ch = s.charAt(i);
			if(ch < ' ' || ch >= 0x7F || ch == '/' || ch == '\\' || ch == '?' || ch == ':' || ch == '"' || ch == '*' || ch == '|' || ch == '<' || ch == '>' || (ch == '.' && i == 0) || ch == escape) {
				sb.append(escape);
				if(ch < 0x10) {
					sb.append('0');
				}
				sb.append(Integer.toHexString(ch));
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}
	
	public static final String makeFilePathURLSafe(String filePath) {
		return filePath.replace("%", "%25").replace("+", "%2b").replace("#", "%23").replace(" ", "%20");
	}
	
	public static final String makeFilePathFileSystemSafe(String fileName) {
		StringBuilder sb = new StringBuilder();
		for(char c : fileName.toCharArray()) {
			if(isCharIllegal(new String(new char[] {c}))) {
				sb.append('-');
				continue;
			}
			sb.append(c);
		}
		fileName = FilenameUtils.normalize(sb.toString() + File.separator);
		fileName = fileName == null ? "" : fileName;
		return fileName.endsWith(File.separator) ? fileName.substring(0, fileName.length() - 1) : fileName;
	}
	
	public static final String makeFileNameFileSystemSafe(String fileName) {
		StringBuilder sb = new StringBuilder();
		for(char c : fileName.toCharArray()) {
			if(c == '/' || c == '\\' || isCharIllegal(new String(new char[] {c}))) {
				sb.append('-');
				continue;
			}
			sb.append(c);
		}
		return sb.toString();
	}
	
}
