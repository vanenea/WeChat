package com.chen.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读取工具
 * @author chen
 *
 */
public class IOUtils {

	/**
	 * 读取字符串
	 * @param in
	 * @return
	 */
	public static String readString(InputStream in) {
		try {
			int ch1 = in.read();
			int ch2 = in.read();
			if((ch1 | ch2) < 0) {
				throw new EOFException();
			}
			int len = (ch1<<8) + ch2;
			byte[] data = new byte[len];
			in.read(data);
			return new String(data,"UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 读取long值
	 * @param in
	 * @return
	 */
	public static long readLong(InputStream in) {
		DataInputStream dis = new DataInputStream(in);
		try {
			return dis.readLong();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 读取short值
	 * @param in
	 * @return
	 */
	public static short readShort(InputStream in) {
		DataInputStream dis = new DataInputStream(in);
		try {
			return dis.readShort();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 写short值
	 * @param out
	 */
	public synchronized static void writeShort(OutputStream out, short s) {
		DataOutputStream dos = new DataOutputStream(out);
		try {
			dos.writeShort(s);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 写字符串
	 * @param out
	 * @param str
	 */
	public synchronized static void writeString(OutputStream out, String str) {
		try {
			byte[] data = str.getBytes("UTF-8");
			writeShort(out, (short)data.length);
			out.write(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
