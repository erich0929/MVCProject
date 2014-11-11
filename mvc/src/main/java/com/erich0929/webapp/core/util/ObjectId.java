package com.erich0929.webapp.core.util;

import java.nio.ByteBuffer;
import java.io.*;

public class ObjectId {
	private static String local_pid = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
	private static Integer count = (int) (Math.random() * 1000000000);
	
	public static void main (String [] args) {
		//byte [] bytes = new byte [8];
		//ByteBuffer.wrap(bytes).putDouble (count);
		System.out.println (createId ("localhost"));
		try {
		Thread.sleep(1000);
		} catch (Exception e) {
			
		}
		System.out.println (createId ("localhost"));
		System.out.println (Math.random() * 100000);
	}
	
	public static String createId (String machineId) {
		String objectId = "";
		ByteBuffer bytes = ByteBuffer.allocate(12);
		
		/* put timestamp 4byte*/
		int timestamp = (int) (System.currentTimeMillis () / 1000);
		System.out.println ("timestamp : " + timestamp);
		bytes.putInt(timestamp);
		
		/* put machineId 3byte*/
		byte [] mid;
		//InputStream in = new ByteArrayInputStream (machineId.getBytes());
		try {
			mid = machineId.getBytes();
			//in.read(mid);
			System.out.println ("mid : " + machineId + ", " + "midhex : " + javax.xml.bind.DatatypeConverter.printHexBinary(mid));
		
			bytes.put(mid [1]);
			bytes.put(mid [2]);
			bytes.put(mid [3]);
			//in.close ();
			/* put pid 2byte*/
			byte [] pid = new byte [2];
			String pidStr = local_pid.substring (0, local_pid.lastIndexOf('@'));
			int pidInt = Integer.parseInt(pidStr);
			ByteBuffer pidByteBuffer = ByteBuffer.allocate (4).putInt (pidInt);
			//pidByteBuffer.rewind();
			byte [] pidByte = new byte [pidByteBuffer.remaining()];
			pidByte = pidByteBuffer.array();
			bytes.put(pidByte [2]);
			bytes.put(pidByte [3]);
			System.out.println ("pid : " + pidStr + ", " + javax.xml.bind.DatatypeConverter.printHexBinary (pidByte));
			//bytes.put(pid);
			//in.close ();
			/* put count */
			synchronized (count) {
				count ++;
				System.out.println ("count : " + count);
			}
			ByteBuffer buf = ByteBuffer.allocate(4).putInt(count);
			buf.rewind();
			System.out.println (buf.remaining ());
			byte [] countByte = new byte [buf.remaining()];
			countByte = buf.array();
			//in = new ByteArrayInputStream (countByte);
			//byte [] countvalue = new byte [3];
			//in.read(countvalue);
			//System.out.println ("count : " + javax.xml.bind.DatatypeConverter.printHexBinary (countvalue));
			bytes.put (countByte [1]);
			bytes.put(countByte [2]);
			bytes.put (countByte [3]);
			objectId = javax.xml.bind.DatatypeConverter.printHexBinary(bytes.array()) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectId;
	}
}
