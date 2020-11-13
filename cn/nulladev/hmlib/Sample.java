package cn.nulladev.hmlib;

import java.util.Random;

public class Sample {
	
	public static void main(String args[]) {
		Random ran = new Random();
		byte bs[] = new byte[4094];
		ran.nextBytes(bs);
		try {
			HammingPacket packet = HammingLib.handleSinglePacket(bs);
			int pos = ran.nextInt(32768);
			char c = packet.getBitAtPos(pos) == '0'? '1' : '0';
			packet.setBitAtPos(pos, c);
			byte bs1[] = HammingLib.restoreSinglePacket(packet.toRawBytes(), 4094);
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
