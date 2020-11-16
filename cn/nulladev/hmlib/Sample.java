package cn.nulladev.hmlib;

import java.util.Random;

public class Sample {
	
	public static void main(String args[]) {
		Random ran = new Random();
		byte bs[] = new byte[15000];
		ran.nextBytes(bs);
		try {
			HammingPacket[] packets = HammingLib.handleMultiplePackets(bs);
			for (int i = 0; i< packets.length; i++) {
				int pos = ran.nextInt(32768);
				char c = packets[i].getBitAtPos(pos) == '0'? '1' : '0';
				packets[i].setBitAtPos(pos, c);
				System.out.println(packets[i].info());
			}
			
			boolean flag = true;
			for (int i = 0; i< packets.length; i++) {
				byte bs1[] = HammingLib.restoreSinglePacket(packets[i].toRawBytes());
				for (int j = 0; j < bs1.length; j++) {
					if(bs[4094*i+j] != bs1[j]) {
						System.out.println("error at pos" + i);
						System.out.println("bs[i] =" + bs[i]);
						System.out.println("but bs1[i] =" + bs1[i]);
						flag = false;
					}
				}
			}
			if(flag) {
				System.out.println("excelent!");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
