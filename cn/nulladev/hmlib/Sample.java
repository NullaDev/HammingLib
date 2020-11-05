package cn.nulladev.hmlib;

import java.util.Random;

public class Sample {
	
	public static void main(String args[]) {
		Random ran = new Random();
		byte bs[] = new byte[4094];
		ran.nextBytes(bs);
		try {
			HammingPacket bdata = HammingPacket.fromBytes(bs);
			//int pos = ran.nextInt(32768);
			int pos = 12345;
			char c = bdata.getBitAtPos(pos) == '0'? '1' : '0';
			bdata.setBitAtPos(pos, c);
			byte bs1[] = bdata.toBytes();
			for (int i = 0; i < 4094; i++) {
				if(bs[i] != bs1[i]) {
					System.out.println("error at pos" + i);
					System.out.println("bs[i] =" + bs[i]);
					System.out.println("but bs1[i] =" + bs1[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
