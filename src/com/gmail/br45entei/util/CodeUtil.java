/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gmail.br45entei.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

/** Uses code from Apache Commons lang3.
 * 
 * @author br45e */
public class CodeUtil {
	
	/** Causes the currently executing thread to sleep (temporarily cease
	 * execution) for the specified number of milliseconds, subject to
	 * the precision and accuracy of system timers and schedulers. The thread
	 * does not lose ownership of any monitors.
	 *
	 * @param millis
	 *            the length of time to sleep in milliseconds
	 *
	 * @throws IllegalArgumentException
	 *             if the value of {@code millis} is negative */
	public static final void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	/** @param data The data that the buffer will hold. The new buffer will
	 *            <b>not</b> be backed by the given byte array; that is,
	 *            modifications to the buffer will <b>not</b> cause the array to
	 *            be modified, or vice versa.
	 * @return The resulting buffer */
	public static final ByteBuffer createByteBuffer(byte[] data) {
		ByteBuffer buf = createByteBuffer(data.length);
		buf.put(data);
		buf.rewind();
		return buf;
	}
	
	/** @param size The number of bytes that the buffer will be able to hold
	 * @return The resulting buffer */
	public static final ByteBuffer createByteBuffer(int size) {
		while(true) {
			try {
				return ByteBuffer.allocateDirect((Byte.SIZE / 8) * size).order(ByteOrder.nativeOrder());
			} catch(OutOfMemoryError e) {
				System.err.println(e.getClass().getName() + ": Failed to allocate space for int buffer(size=" + Integer.toString(size) + "); Running garbage collector and trying again...");
				System.gc();
				sleep(10L);
			}
		}
	}
	
	/** @param data The data that the buffer will hold. The new buffer will
	 *            <b>not</b> be backed by the given short array; that is,
	 *            modifications to the buffer will <b>not</b> cause the array to
	 *            be modified, or vice versa.
	 * @return The resulting buffer */
	public static final ShortBuffer createShortBuffer(short[] data) {
		ShortBuffer buf = createShortBuffer(data.length);
		buf.put(data);
		buf.rewind();
		return buf;
	}
	
	/** @param size The number of shorts that the buffer will be able to hold
	 * @return The resulting buffer */
	public static final ShortBuffer createShortBuffer(int size) {
		while(true) {
			try {
				return ByteBuffer.allocateDirect((Short.SIZE / 8) * size).order(ByteOrder.nativeOrder()).asShortBuffer();
			} catch(OutOfMemoryError e) {
				System.err.println(e.getClass().getName() + ": Failed to allocate space for short buffer(size=" + Integer.toString(size) + "); Running garbage collector and trying again...");
				System.gc();
				sleep(10L);
			}
		}
	}
	
	/** @param data The data that the buffer will hold. The new buffer will
	 *            <b>not</b> be backed by the given char array; that is,
	 *            modifications to the buffer will <b>not</b> cause the array to
	 *            be modified, or vice versa.
	 * @return The resulting buffer */
	public static final CharBuffer createCharBuffer(char[] data) {
		CharBuffer buf = createCharBuffer(data.length);
		buf.put(data);
		buf.rewind();
		return buf;
	}
	
	/** @param size The number of chars that the buffer will be able to hold
	 * @return The resulting buffer */
	public static final CharBuffer createCharBuffer(int size) {
		while(true) {
			try {
				return ByteBuffer.allocateDirect((Character.SIZE / 8) * size).order(ByteOrder.nativeOrder()).asCharBuffer();
			} catch(OutOfMemoryError e) {
				System.err.println(e.getClass().getName() + ": Failed to allocate space for char buffer(size=" + Integer.toString(size) + "); Running garbage collector and trying again...");
				System.gc();
				sleep(10L);
			}
		}
	}
	
	/** @param data The data that the buffer will hold. The new buffer will
	 *            <b>not</b> be backed by the given int array; that is,
	 *            modifications to the buffer will <b>not</b> cause the array to
	 *            be modified, or vice versa.
	 * @return The resulting buffer */
	public static final IntBuffer createIntBuffer(int[] data) {
		IntBuffer buf = createIntBuffer(data.length);
		buf.put(data);
		buf.rewind();
		return buf;
	}
	
	/** @param size The number of integers that the buffer will be able to hold
	 * @return The resulting buffer */
	public static final IntBuffer createIntBuffer(int size) {
		while(true) {
			try {
				return ByteBuffer.allocateDirect((Integer.SIZE / 8) * size).order(ByteOrder.nativeOrder()).asIntBuffer();
			} catch(OutOfMemoryError e) {
				System.err.println(e.getClass().getName() + ": Failed to allocate space for int buffer(size=" + Integer.toString(size) + "); Running garbage collector and trying again...");
				System.gc();
				sleep(10L);
			}
		}
	}
	
	/** @param data The data that the buffer will hold. The new buffer will
	 *            <b>not</b> be backed by the given float array; that is,
	 *            modifications to the buffer will <b>not</b> cause the array to
	 *            be modified, or vice versa.
	 * @return The resulting buffer */
	public static final FloatBuffer createFloatBuffer(float[] data) {
		FloatBuffer buf = createFloatBuffer(data.length);
		buf.put(data);
		buf.rewind();
		return buf;
	}
	
	/** @param size The number of floats that the buffer will be able to hold
	 * @return The resulting buffer */
	public static final FloatBuffer createFloatBuffer(int size) {
		while(true) {
			try {
				return ByteBuffer.allocateDirect((Float.SIZE / 8) * size).order(ByteOrder.nativeOrder()).asFloatBuffer();
			} catch(OutOfMemoryError e) {
				System.err.println(e.getClass().getName() + ": Failed to allocate space for float buffer(size=" + Integer.toString(size) + "); Running garbage collector and trying again...");
				System.gc();
				sleep(10L);
			}
		}
	}
	
	/** @param data The data that the buffer will hold. The new buffer will
	 *            <b>not</b> be backed by the given double array; that is,
	 *            modifications to the buffer will <b>not</b> cause the array to
	 *            be modified, or vice versa.
	 * @return The resulting buffer */
	public static final DoubleBuffer createDoubleBuffer(double[] data) {
		DoubleBuffer buf = createDoubleBuffer(data.length);
		buf.put(data);
		buf.rewind();
		return buf;
	}
	
	/** @param size The number of doubles that the buffer will be able to hold
	 * @return The resulting buffer */
	public static final DoubleBuffer createDoubleBuffer(int size) {
		while(true) {
			try {
				return ByteBuffer.allocateDirect((Double.SIZE / 8) * size).order(ByteOrder.nativeOrder()).asDoubleBuffer();
			} catch(OutOfMemoryError e) {
				System.err.println(e.getClass().getName() + ": Failed to allocate space for double buffer(size=" + Integer.toString(size) + "); Running garbage collector and trying again...");
				System.gc();
				sleep(10L);
			}
		}
	}
	
	/** @param data The data that the buffer will hold. The new buffer will
	 *            <b>not</b> be backed by the given long array; that is,
	 *            modifications to the buffer will <b>not</b> cause the array to
	 *            be modified, or vice versa.
	 * @return The resulting buffer */
	public static final LongBuffer createLongBuffer(long[] data) {
		LongBuffer buf = createLongBuffer(data.length);
		buf.put(data);
		buf.rewind();
		return buf;
	}
	
	/** @param size The number of longs that the buffer will be able to hold
	 * @return The resulting buffer */
	public static final LongBuffer createLongBuffer(int size) {
		while(true) {
			try {
				return ByteBuffer.allocateDirect((Long.SIZE / 8) * size).order(ByteOrder.nativeOrder()).asLongBuffer();
			} catch(OutOfMemoryError e) {
				System.err.println(e.getClass().getName() + ": Failed to allocate space for long buffer(size=" + Integer.toString(size) + "); Running garbage collector and trying again...");
				System.gc();
				sleep(10L);
			}
		}
	}
	
	//======================================================================================
	
	/** @param d The decimal to round
	 * @param radix The amount of decimal places to round to
	 * @return The resulting decimal in String form */
	public static final String roundToString(double d, int radix) {
		String format = "#.";
		for(int i = 0; i < radix; i++) {
			format += "#";
		}
		DecimalFormat df = new DecimalFormat(format);
		df.setRoundingMode(RoundingMode.HALF_EVEN);
		String result = df.format(d);
		if(radix == 0 && result.endsWith(".")) {
			return result.substring(0, result.length() - 1);
		}
		return result;
	}
	
	/** @param decimal The decimal to limit
	 * @param numOfPlaces The number of places to limit the decimal to(radix)
	 * @return The limited decimal */
	public static final String limitDecimalNoRounding(double decimal, int numOfPlaces) {
		return limitDecimalNoRounding(decimal, numOfPlaces, false);
	}
	
	/** @param decimal The decimal to limit
	 * @param numOfPlaces The number of places to limit the decimal to(radix)
	 * @param pad Whether or not the decimal should be padded with trailing
	 *            zeros if the resulting length is less than
	 *            <code>numOfPads</code>
	 * @return The limited decimal */
	public static final String limitDecimalNoRounding(double decimal, int numOfPlaces, boolean pad) {
		String padStr = pad ? lineOf('0', numOfPlaces) : "0";
		if(Double.doubleToLongBits(decimal) == Double.doubleToLongBits(0.0)) {
			return "0" + (numOfPlaces != 0 ? "." + padStr : "");
		}
		if(Double.doubleToLongBits(decimal) == Double.doubleToLongBits(-0.0)) {
			return "-0" + (numOfPlaces != 0 ? "." + padStr : "");
		}
		numOfPlaces += 1;
		String whole = Double.isFinite(decimal) ? getWholePartOf(decimal) : Double.isInfinite(decimal) ? "Infinity" : "NaN";
		if(numOfPlaces == 0) {
			return whole;
		}
		String d = Double.isFinite(decimal) ? getDecimalPartOf(decimal) : "";
		if(d.length() == 1 || d.equals(".0")) {
			return whole + (numOfPlaces != 0 ? "." + padStr : "");
		}
		if(d.length() > numOfPlaces) {
			d = d.substring(d.indexOf('.') + 1, numOfPlaces);
		}
		if(d.startsWith(".")) {
			d = d.substring(1);
		}
		String restore = d;
		if(d.endsWith("9")) {//Combat weird java rounding
			int chopIndex = -1;
			char[] array = d.toCharArray();
			boolean lastChar9 = false;
			for(int i = array.length - 1; i >= 0; i--) {
				boolean _9 = array[i] == '9';
				array[i] = _9 ? '0' : array[i];
				chopIndex = i;
				if(!_9 && lastChar9) {
					array[i] = Integer.valueOf(Integer.valueOf(new String(new char[] {array[i]})).intValue() + 1).toString().charAt(0);
					chopIndex = i + 1;
					break;
				}
				lastChar9 = _9;
			}
			d = new String(array, 0, (chopIndex == -1 ? array.length : chopIndex));
		}
		if(d.endsWith("0")) {
			while(d.endsWith("0")) {
				d = d.substring(0, d.length() - 1);
			}
		}
		if(pad && (numOfPlaces - d.length()) > 0) {
			d += lineOf('0', numOfPlaces - d.length());
		}
		if(d.length() > numOfPlaces - 1) {
			d = d.substring(0, numOfPlaces - 1);
		}
		if(d.isEmpty()) {
			d = restore;
		}
		//System.out.println("\"" + whole + "." + d + "\"");
		return whole + "." + d;//(d.isEmpty() ? "" : ("." + d));
	}
	
	private static final String lineOf(char c, int length) {
		char[] str = new char[length];
		for(int i = 0; i < length; i++) {
			str[i] = c;
		}
		return new String(str);
	}
	
	/** @param decimal The decimal
	 * @return The whole number portion of the given decimal */
	public static final String getWholePartOf(double decimal) {
		if(decimal != decimal) {
			return Long.toString(Double.doubleToLongBits(decimal));
		}
		String d = new BigDecimal(decimal).toPlainString();
		int indexOfDecimalPoint = d.indexOf(".");
		if(indexOfDecimalPoint != -1) {
			return d.substring(0, indexOfDecimalPoint);
		}
		return Long.toString((long) decimal);
	}
	
	/** @param decimal The decimal
	 * @return The given decimal without */
	public static final String getDecimalPartOf(double decimal) {
		if(decimal != decimal) {
			return Double.toString(decimal);
		}
		String d = new BigDecimal(decimal).toPlainString();
		int indexOfDecimalPoint = d.indexOf(".");
		if(indexOfDecimalPoint == -1) {
			d = Double.toString(decimal);
			indexOfDecimalPoint = d.indexOf(".");
		}
		if(indexOfDecimalPoint != -1) {
			return d.substring(indexOfDecimalPoint);
		}
		return d;
	}
	
	/** @param decimal The decimal to limit
	 * @param numOfPlaces The number of places to limit the decimal to
	 * @return The resulting String */
	public static final String limitDecimalToNumberOfPlaces(double decimal, int numOfPlaces) {
		return String.format("%." + numOfPlaces + "f", Double.valueOf(decimal));
	}
	
	/** @param decimal The decimal to limit
	 * @param numOfPlaces The number of places to limit the decimal to
	 * @return The resulting String */
	public static final String limitDecimalToNumberOfPlaces(float decimal, int numOfPlaces) {
		return String.format("%." + numOfPlaces + "f", Float.valueOf(decimal));
	}
	
	//======================================================================================
	
	/** @param s The String to test
	 * @return Whether or not the given String is a valid boolean
	 *         value(&quot;true&quot; or &quot;false&quot;) */
	public static final boolean isBoolean(String s) {
		return s != null && (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false"));
	}
	
	/** @param s The String to test
	 * @return Whether or not the given String is a valid short value */
	public static final boolean isShort(String s) {
		try {
			Short.parseShort(s);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	/** @param s The String to test
	 * @return Whether or not the given String is a valid integer value */
	public static final boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	/** @param s The String to test
	 * @return Whether or not the given String is a valid float value */
	public static final boolean isFloat(String s) {
		try {
			Float.parseFloat(s);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	/** @param s The String to test
	 * @return Whether or not the given String is a valid long value */
	public static final boolean isLong(String s) {
		try {
			Long.parseLong(s);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	/** @param s The String to test
	 * @return Whether or not the given String is a valid double value */
	public static final boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	//======================================================================================
	
	/** @return Whether or not a 64 bit system was detected */
	public static boolean isJvm64bit() {
		for(String s : new String[] {"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"}) {
			String s1 = System.getProperty(s);
			if((s1 != null) && s1.contains("64")) {
				return true;
			}
		}
		return false;
	}
	
	/** Enum class differentiating types of operating systems
	 * 
	 * @author Brian_Entei */
	public static enum EnumOS {
		/** Linux operating systems */
		LINUX,
		/** Unix operating systems */
		UNIX,
		/** Salaries operating systems */
		SOLARIS,
		/** Windows operating systems */
		WINDOWS,
		/** Mac/OSX */
		OSX,
		/** Android operating systems */
		ANDROID,
		/** An unknown operating system */
		UNKNOWN;
	}
	
	/** @return The type of operating system that java is currently running
	 *         on */
	public static EnumOS getOSType() {
		String s = System.getProperty("os.name").toLowerCase();
		return s.contains("win") ? EnumOS.WINDOWS : (s.contains("mac") ? EnumOS.OSX : (s.contains("solaris") ? EnumOS.SOLARIS : (s.contains("sunos") ? EnumOS.SOLARIS : (s.contains("linux") ? EnumOS.LINUX : (s.contains("unix") ? EnumOS.UNIX : (s.contains("android") ? EnumOS.ANDROID : EnumOS.UNKNOWN))))));
	}
	
	/** @return The platform-specific executable extension, if there is one */
	public static final String getExecutableExtension() {
		return getExecutableExtensionFor(getOSType());
	}
	
	/** @param os The operating system
	 * @return The executable extension for the given operating system, if it
	 *         has one */
	public static final String getExecutableExtensionFor(EnumOS os) {
		switch(os) {
		case ANDROID:
			return ".apk";
		case LINUX:
			return "";//"run";//or "out";
		case OSX:
			return ".ipa";
		case SOLARIS:
			return "";//"run";//or "out";
		case UNIX:
			return "";//"???";
		case UNKNOWN:
		default:
			return ".unknown";
		case WINDOWS:
			return ".exe";
		}
	}
	
	/** @param array The String[] array to convert
	 * @return The resulting string */
	public static final String stringArrayToString(String[] array) {
		return stringArrayToString(array, ' ', 0);
	}
	
	/** @param array The list to read from
	 * @param c The character to use as a separator
	 * @return The resulting string */
	public static final String stringArrayToString(char c, Collection<String> array) {
		return stringArrayToString(String.valueOf(c), array);
	}
	
	/** @param array The list to read from
	 * @param c The string to use as a separator
	 * @return The resulting string */
	public static final String stringArrayToString(String c, Collection<String> array) {
		if(array == null) {
			return "null";
		}
		String rtrn = "";
		int index = 0;
		for(String element : array) {
			rtrn += element + ((++index) == array.size() ? "" : c);
		}
		return rtrn.trim();
	}
	
	/** @param array The array/list/strings to read from
	 * @param c The character to use as a separator
	 * @return The resulting string */
	public static final String stringArrayToString(char c, String... array) {
		if(array == null) {
			return "null";
		}
		String rtrn = "";
		for(String element : array) {
			rtrn += element + c;
		}
		return rtrn.length() >= 2 ? rtrn.substring(0, rtrn.length() - 1) : rtrn;
	}
	
	/** @param array The String[] array to convert
	 * @param c The separator character to use
	 * @return The resulting string */
	public static final String stringArrayToString(String[] array, char c) {
		return stringArrayToString(array, c, 0);
	}
	
	/** @param array The String[] array to convert
	 * @param c The separator character to use
	 * @param startIndex The index to start at
	 * @return The resulting string */
	public static final String stringArrayToString(String[] array, char c, int startIndex) {
		return stringArrayToString(array, String.valueOf(c), startIndex);
	}
	
	/** @param array The array/list/strings to read from
	 * @param c The character to use as a separator
	 * @return The resulting string */
	public static final String stringArrayToString(String[] array, String c) {
		return stringArrayToString(array, c, 0);
	}
	
	/** @param array The array/list/strings to read from
	 * @param c The character to use as a separator
	 * @param startIndex The index to start at
	 * @return The resulting string */
	public static final String stringArrayToString(String[] array, String c, int startIndex) {
		if(array == null || startIndex >= array.length) {
			return "null";
		}
		String rtrn = "";
		int i = 0;
		for(String element : array) {
			if(i >= startIndex) {
				rtrn += element + (i + 1 == array.length ? "" : c);
			}
			i++;
		}
		return rtrn;
	}
	
	/** @param array The String[] array to convert
	 * @param c The separator character to use
	 * @param startIndex The index to start at
	 * @param endIndex The index to stop short at
	 * @return The resulting string */
	public static final String stringArrayToString(String[] array, char c, int startIndex, int endIndex) {
		return stringArrayToString(array, c + "", startIndex, endIndex);
	}
	
	/** @param array The array/list/strings to read from
	 * @param c The character to use as a separator
	 * @param startIndex The index to start at
	 * @param endIndex The index to stop short at
	 * @return The resulting string. If startIndex is greater than or equal to
	 *         the array's size, endIndex is greater than the array's size,
	 *         startIndex is greater than or equal to endIndex, and/or either
	 *         startIndex or endIndex are negative, "null" is returned. */
	public static final String stringArrayToString(String[] array, String c, int startIndex, int endIndex) {
		if(array == null || startIndex >= array.length || endIndex > array.length || startIndex >= endIndex || startIndex < 0 || endIndex < 0) {
			return "null";
		}
		String rtrn = "";
		int i = 0;
		for(String element : array) {
			if(i >= startIndex && i < endIndex) {
				rtrn += element + (i + 1 == endIndex ? "" : c);
			}
			i++;
		}
		return rtrn;
	}
	
	/** @param element The StackTraceElement
	 * @param identifier The identifier to use
	 * @return The resulting customized toString */
	public static final String stackTraceElementToStringCustom(StackTraceElement element, String identifier) {
		if(element != null) {
			return "(" + element.getFileName() + ":" + element.getLineNumber() + ")-> " + element.getMethodName() + "(" + identifier + ")";
		}
		return "null-> (" + identifier + ")";
	}
	
	/** @param stackTraceElements The elements to convert
	 * @return The resulting string */
	public static final String stackTraceElementsToStr(StackTraceElement[] stackTraceElements) {
		return stackTraceElementsToStr(stackTraceElements, "\r\n");
	}
	
	/** @param stackTraceElements The elements to convert
	 * @param lineSeparator The line separator to use
	 * @return The resulting string */
	public static final String stackTraceElementsToStr(StackTraceElement[] stackTraceElements, String lineSeparator) {
		String str = "";
		if(stackTraceElements != null) {
			for(StackTraceElement stackTrace : stackTraceElements) {
				str += (!stackTrace.toString().startsWith("Caused By") ? "     at " : "") + stackTrace.toString() + lineSeparator;
			}
		}
		return str;
	}
	
	/** @param stackTraceElements The elements to convert
	 * @return The resulting string */
	public static final String stackTraceCausedByElementsOnlyToStr(StackTraceElement[] stackTraceElements) {
		return stackTraceCausedByElementsOnlyToStr(stackTraceElements, "\r\n");
	}
	
	/** @param stackTraceElements The elements to convert
	 * @param lineSeparator The line separator to use
	 * @return The resulting string */
	public static final String stackTraceCausedByElementsOnlyToStr(StackTraceElement[] stackTraceElements, String lineSeparator) {
		String str = "";
		if(stackTraceElements != null) {
			for(StackTraceElement stackTrace : stackTraceElements) {
				str += (!stackTrace.toString().startsWith("Caused By") ? "" : stackTrace.toString() + lineSeparator);
			}
		}
		return str;
	}
	
	/** @param e The {@link Throwable} to convert
	 * @return The resulting String */
	public static final String throwableToStrNoStackTraces(Throwable e) {
		return throwableToStrNoStackTraces(e, "\r\n");
	}
	
	/** @param e The {@link Throwable} to convert
	 * @param lineSeparator The line separator to use
	 * @return The resulting String */
	public static final String throwableToStrNoStackTraces(Throwable e, String lineSeparator) {
		if(e == null) {
			return "null";
		}
		String str = e.getClass().getName() + ": ";
		if((e.getMessage() != null) && !e.getMessage().isEmpty()) {
			str += e.getMessage() + lineSeparator;
		} else {
			str += lineSeparator;
		}
		str += stackTraceCausedByElementsOnlyToStr(e.getStackTrace(), lineSeparator);
		if(e.getCause() != null) {
			str += "Caused by:" + lineSeparator + throwableToStrNoStackTraces(e.getCause(), lineSeparator);
		}
		return str;
	}
	
	/** @param e The {@link Throwable} to convert
	 * @return The resulting String */
	public static final String throwableToStr(Throwable e) {
		return throwableToStr(e, "\r\n");
	}
	
	/** @param e The {@link Throwable} to convert
	 * @param lineSeparator The line separator to use
	 * @return The resulting String */
	public static final String throwableToStr(Throwable e, String lineSeparator) {
		if(e == null) {
			return "null";
		}
		String str = e.getClass().getName() + ": ";
		if((e.getMessage() != null) && !e.getMessage().isEmpty()) {
			str += e.getMessage() + lineSeparator;
		} else {
			str += lineSeparator;
		}
		str += stackTraceElementsToStr(e.getStackTrace(), lineSeparator);
		if(e.getCause() != null) {
			str += "Caused by:" + lineSeparator + throwableToStr(e.getCause(), lineSeparator);
		}
		return str;
	}
	
	/** @return The stack trace element of the code that ran this method
	 * @author Brian_Entei */
	public static final StackTraceElement getCurrentStackTraceElement() {
		return ___gjdgerjkgnmFf_Xaf();
	}
	
	/** This methods name is ridiculous on purpose to prevent any other method
	 * names in the stack trace from potentially matching this one.
	 * 
	 * @return The stack trace element of the code that called the method that
	 *         called the method that called this method(Should only be called
	 *         by getCallingStackTraceElement()).
	 * @author Brian_Entei */
	private static final StackTraceElement ___gjdgerjkgnmFf_Xaf() {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		int index = -1;
		int target = -1;
		for(StackTraceElement element : elements) {
			index++;
			String methodName = element.getMethodName();
			if(methodName.equals("___gjdgerjkgnmFf_Xaf")) {
				target = index + 2;
				break;
			}
		}
		return(target > 0 && target < elements.length ? elements[target] : null);
	}
	
	/** @return The stack trace element of the code that ran this method
	 * @author Brian_Entei */
	public static final StackTraceElement getCallingStackTraceElement() {
		return ___ghdTdjsgd7t5c_Xaf();
	}
	
	/** This methods name is ridiculous on purpose to prevent any other method
	 * names in the stack trace from potentially matching this one.
	 * 
	 * @return The stack trace element of the code that called the method that
	 *         called the method that called this method(Should only be called
	 *         by getCallingStackTraceElement()).
	 * @author Brian_Entei */
	private static final StackTraceElement ___ghdTdjsgd7t5c_Xaf() {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		int index = -1;
		int target = -1;
		for(StackTraceElement element : elements) {
			index++;
			String methodName = element.getMethodName();
			if(methodName.equals("___ghdTdjsgd7t5c_Xaf")) {
				target = index + 3;
				break;
			}
		}
		return(target > 0 && target < elements.length ? elements[target] : null);
	}
	
	/** @return The line number of the code that ran this method
	 * @author Brian_Entei */
	public static final int getLineNumber() {
		return ___8drrd3148796d_Xaf();
	}
	
	/** This methods name is ridiculous on purpose to prevent any other method
	 * names in the stack trace from potentially matching this one.
	 * 
	 * @return The line number of the code that called the method that called
	 *         this method(Should only be called by getLineNumber()).
	 * @author Brian_Entei */
	private static final int ___8drrd3148796d_Xaf() {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		int index = -1;
		int target = -1;
		for(StackTraceElement element : elements) {
			index++;
			String methodName = element.getMethodName();
			if(methodName.equals("___8drrd3148796d_Xaf")) {
				target = index + 3;
				break;
			}
		}
		return(target > 0 && target < elements.length ? elements[target].getLineNumber() : -1);
	}
	
	/** @return The method name of the code that ran the code that ran this
	 *         method
	 * @author Brian_Entei */
	public static final String getMethodName() {
		return ___fbhfRghjprgGF_Xaf();
	}
	
	/** This methods name is ridiculous on purpose to prevent any other method
	 * names in the stack trace from potentially matching this one.
	 * 
	 * @return The method name of the code that called the method that called
	 *         the method that called this method(Should only be called by
	 *         getMethodName()).
	 * @author Brian_Entei */
	private static final String ___fbhfRghjprgGF_Xaf() {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		int index = -1;
		int target = -1;
		for(StackTraceElement element : elements) {
			index++;
			String methodName = element.getMethodName();
			if(methodName.equals("___fbhfRghjprgGF_Xaf")) {
				target = index + 3;
				break;
			}
		}
		return(target > 0 && target < elements.length ? elements[target].getMethodName() : null);
	}
	
	/** @param args Program command line arguments */
	public static final void main(String[] args) {
		System.out.println(getDecimalPartOf(Double.NaN));
		double piiiiii = 3.1415926535897932384626433832795028841971693993751058209749D;
		int size = (Double.SIZE / 4) - 1;
		System.out.println(limitDecimalToNumberOfPlaces(piiiiii, size - 2));
		System.out.println(limitDecimalNoRounding(0.0D, 4));
		System.out.println(limitDecimalNoRounding(-0.0D, 4));
		System.out.println(limitDecimalNoRounding(5, 4));
		System.out.println(limitDecimalNoRounding(-5, 4));
		System.out.println(limitDecimalNoRounding(piiiiii, size - 2));
		System.out.println(limitDecimalNoRounding(1.9969016, size));
		System.out.println(limitDecimalNoRounding(1.99690169, size));
		System.out.println(limitDecimalNoRounding(0.899999998, size));
		System.out.println(limitDecimalNoRounding(0.9999999996, size));
		System.out.println(limitDecimalNoRounding(0.99999999997, size));
		System.out.println(limitDecimalNoRounding(0.999999999998, size));
		System.out.println(limitDecimalNoRounding(0.9999999999996, size));
		System.out.println(limitDecimalNoRounding(0.99999999999999, size));
		System.out.println(limitDecimalNoRounding(0.9999969999999996, size));//Unfortunate, but I'll take it.
		System.out.println(limitDecimalNoRounding(1.9999969999999996, size));//Unfortunate, but I'll take it.
		System.out.println(limitDecimalToNumberOfPlaces(0.9999969999999996, size));//herpa derpa derpity derp
		System.out.println(limitDecimalNoRounding(0.999999999999996, size));
		System.out.println(limitDecimalNoRounding(0.9999999999999997, size));
		System.out.println(limitDecimalToNumberOfPlaces(0.9999999999999997, size));
		System.out.println(stackTraceElementToStringCustom(getCurrentStackTraceElement(), "args=\"" + stringArrayToString(args) + "\""));
		checkMethodName();
	}
	
	private static final void checkMethodName() {
		System.out.println(getCurrentStackTraceElement());
	}
	
	/** @param str The string to search for
	 * @param list The list of strings to search through
	 * @return True if the list contained any string starting with the given
	 *         string, ignoring case */
	public static final boolean startsWithIgnoreCase(String str, String... list) {
		if(str != null && list != null && list.length != 0) {
			str = str.toLowerCase();
			for(String s : list) {
				if(s != null && s.toLowerCase().startsWith(str)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/** @param list The list of strings to search through
	 * @param str The string to search for
	 * @return True if the list contained any string starting with the given
	 *         string, ignoring case */
	public static final boolean startsWithIgnoreCase(Collection<String> list, String str) {
		if(str != null && list != null && !list.isEmpty()) {
			str = str.toLowerCase();
			for(String s : new ArrayList<>(list)) {
				if(s.toLowerCase().startsWith(str)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/** @param str The string to search for
	 * @param list The list of strings to search through
	 * @return True if the list contained any instance of the given string,
	 *         ignoring case */
	public static final boolean containsIgnoreCase(String str, String... list) {
		if(str != null && list != null && list.length != 0) {
			for(String s : list) {
				if(s != null && s.equalsIgnoreCase(str)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/** @param list The list of strings to search through
	 * @param str The string to search for
	 * @return True if the list contained any instance of the given string,
	 *         ignoring case */
	public static final boolean containsIgnoreCase(Collection<String> list, String str) {
		if(str != null && list != null && !list.isEmpty()) {
			for(String s : new ArrayList<>(list)) {
				if(str.equalsIgnoreCase(s)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/** Case insensitive check if a String ends with a specified suffix.
	 * 
	 * <code>null</code>s are handled without exceptions. Two <code>null</code>
	 * references are considered to be equal. The comparison is case
	 * insensitive.
	 * 
	 * <pre>
	 * StringUtils.endsWithIgnoreCase(null, null)      = true
	 * StringUtils.endsWithIgnoreCase(null, "abcdef")  = false
	 * StringUtils.endsWithIgnoreCase("def", null)     = false
	 * StringUtils.endsWithIgnoreCase("def", "abcdef") = true
	 * StringUtils.endsWithIgnoreCase("def", "ABCDEF") = true
	 * </pre>
	 * 
	 * @see java.lang.String#endsWith(String)
	 * @param str the String to check, may be null
	 * @param suffix the suffix to find, may be null
	 * @return <code>true</code> if the String ends with the suffix, case
	 *         insensitive, or
	 *         both <code>null</code>
	 * @since 2.4 */
	public static boolean endsWithIgnoreCase(String str, String suffix) {
		return CodeUtil.endsWith(str, suffix, true);
	}
	
	/** Check if a String ends with a specified suffix (optionally case
	 * insensitive).
	 * 
	 * <pre>
	 * StringUtils.endsWithIgnoreCase(null, null)      = true
	 * StringUtils.endsWithIgnoreCase(null, "abcdef")  = false
	 * StringUtils.endsWithIgnoreCase("def", null)     = false
	 * StringUtils.endsWithIgnoreCase("def", "abcdef") = true
	 * StringUtils.endsWithIgnoreCase("def", "ABCDEF") = false
	 * </pre>
	 * 
	 * @see java.lang.String#endsWith(String)
	 * @param str the String to check, may be null
	 * @param suffix the suffix to find, may be null
	 * @param ignoreCase inidicates whether the compare should ignore case
	 *            (case insensitive) or not.
	 * @return <code>true</code> if the String starts with the prefix or
	 *         both <code>null</code> */
	private static boolean endsWith(String str, String suffix, boolean ignoreCase) {
		if((str == null) || (suffix == null)) {
			return((str == null) && (suffix == null));
		}
		if(suffix.length() > str.length()) {
			return false;
		}
		int strOffset = str.length() - suffix.length();
		return str.regionMatches(ignoreCase, strOffset, suffix, 0, suffix.length());
	}
	
	/** @param string The string whose contents will be replaced
	 * @param searchString The string to search for that will be replaced
	 * @param replacement The string that will replace the search string
	 * @return The resulting string */
	public static final String replaceOnce(String string, String searchString, String replacement) {
		return replace(string, searchString, replacement, 1);
	}
	
	/** <p>
	 * Replaces a String with another String inside a larger String,
	 * for the first {@code max} values of the search String.
	 * </p>
	 *
	 * <p>
	 * A {@code null} reference passed to this method is a no-op.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.replace(null, *, *, *)         = null
	 * StringUtils.replace("", *, *, *)           = ""
	 * StringUtils.replace("any", null, *, *)     = "any"
	 * StringUtils.replace("any", *, null, *)     = "any"
	 * StringUtils.replace("any", "", *, *)       = "any"
	 * StringUtils.replace("any", *, *, 0)        = "any"
	 * StringUtils.replace("abaa", "a", null, -1) = "abaa"
	 * StringUtils.replace("abaa", "a", "", -1)   = "b"
	 * StringUtils.replace("abaa", "a", "z", 0)   = "abaa"
	 * StringUtils.replace("abaa", "a", "z", 1)   = "zbaa"
	 * StringUtils.replace("abaa", "a", "z", 2)   = "zbza"
	 * StringUtils.replace("abaa", "a", "z", -1)  = "zbzz"
	 * </pre>
	 *
	 * @param text text to search and replace in, may be null
	 * @param searchString the String to search for, may be null
	 * @param replacement the String to replace it with, may be null
	 * @param max maximum number of values to replace, or {@code -1} if no
	 *            maximum
	 * @return the text with any replacements processed,
	 *         {@code null} if null String input */
	public static String replace(final String text, final String searchString, final String replacement, int max) {
		if(text == null || text.isEmpty() || searchString == null || searchString.isEmpty() || replacement == null || max == 0) {
			return text;
		}
		int start = 0;
		int end = text.indexOf(searchString, start);
		if(end == -1) {
			return text;
		}
		final int replLength = searchString.length();
		int increase = replacement.length() - replLength;
		increase = increase < 0 ? 0 : increase;
		increase *= max < 0 ? 16 : max > 64 ? 64 : max;
		final StringBuilder buf = new StringBuilder(text.length() + increase);
		while(end != -1) {
			buf.append(text.substring(start, end)).append(replacement);
			start = end + replLength;
			if(--max == 0) {
				break;
			}
			end = text.indexOf(searchString, start);
		}
		buf.append(text.substring(start));
		return buf.toString();
	}
	
	/** @param code The code to run with printing disabled
	 * @return Any bytes written to the standard output stream while executing
	 *         the given code */
	public static final byte[] runNoPrinting(Runnable code) {
		PrintStream out = System.out;
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			System.setOut(new PrintStream(baos));
			Throwable thrown = null;
			try {
				code.run();
			} catch(Throwable e) {
				thrown = e;
			}
			System.setOut(out);
			if(thrown != null) {
				throw new RuntimeException(thrown);
			}
			return baos.toByteArray();
		} catch(IOException ignored) {
			throw new Error(ignored);
		}
	}
	
	/** @param buf The buffer to get the data from
	 * @return The buffer's data */
	public static byte[] getData(ByteBuffer buf) {
		byte[] data = new byte[buf.capacity()];
		if(buf.hasArray()) {
			System.arraycopy(buf.array(), 0, data, 0, data.length);
			return buf.array();
		}
		buf.rewind();
		for(int i = 0; i < data.length; i++) {
			data[i] = buf.get();
		}
		return data;
	}
	
	/** @param buf The buffer to get the data from
	 * @return The buffer's data */
	public static short[] getData(ShortBuffer buf) {
		short[] data = new short[buf.capacity()];
		if(buf.hasArray()) {
			System.arraycopy(buf.array(), 0, data, 0, data.length);
			return buf.array();
		}
		buf.rewind();
		for(int i = 0; i < data.length; i++) {
			data[i] = buf.get();
		}
		return data;
	}
	
	/** @param buf The buffer to get the data from
	 * @return The buffer's data */
	public static char[] getData(CharBuffer buf) {
		char[] data = new char[buf.capacity()];
		if(buf.hasArray()) {
			System.arraycopy(buf.array(), 0, data, 0, data.length);
			return buf.array();
		}
		buf.rewind();
		for(int i = 0; i < data.length; i++) {
			data[i] = buf.get();
		}
		return data;
	}
	
	/** @param buf The buffer to get the data from
	 * @return The buffer's data */
	public static int[] getData(IntBuffer buf) {
		int[] data = new int[buf.capacity()];
		if(buf.hasArray()) {
			System.arraycopy(buf.array(), 0, data, 0, data.length);
			return buf.array();
		}
		buf.rewind();
		for(int i = 0; i < data.length; i++) {
			data[i] = buf.get();
		}
		return data;
	}
	
	/** @param buf The buffer to get the data from
	 * @return The buffer's data */
	public static long[] getData(LongBuffer buf) {
		long[] data = new long[buf.capacity()];
		if(buf.hasArray()) {
			System.arraycopy(buf.array(), 0, data, 0, data.length);
			return buf.array();
		}
		buf.rewind();
		for(int i = 0; i < data.length; i++) {
			data[i] = buf.get();
		}
		return data;
	}
	
	/** @param buf The buffer to get the data from
	 * @return The buffer's data */
	public static double[] getData(DoubleBuffer buf) {
		double[] data = new double[buf.capacity()];
		if(buf.hasArray()) {
			System.arraycopy(buf.array(), 0, data, 0, data.length);
			return buf.array();
		}
		buf.rewind();
		for(int i = 0; i < data.length; i++) {
			data[i] = buf.get();
		}
		return data;
	}
	
}
