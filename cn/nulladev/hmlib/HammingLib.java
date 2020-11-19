package cn.nulladev.hmlib;

public class HammingLib {
	
	/** 获取单个汉明码处理过的数据包。 */
	public static HammingPacket handleSinglePacket(short index, byte fragflag, byte[] data) {
		try {
			HammingPacket bdata = HammingPacket.fromBytes(index, fragflag, data);
			return bdata;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	/** 获取单个汉明码处理过的数据包。 */
	public static HammingPacket handleSinglePacket(byte[] data) {
		return handleSinglePacket((short)0, (byte)0, data);
	}
	
	/** 还原单个汉明码处理过的数据包。 */
	public static byte[] restoreSinglePacket(HammingPacket packet) {
		try {
			return packet.toRealBytes();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	/** 还原单个汉明码处理过的数据包。 */
	public static byte[] restoreSinglePacket(byte[] data) {
		try {
			return restoreSinglePacket(HammingPacket.fromRawBytes(data));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
		return null;
	}
	
	/** 获取汉明码处理过的数据包的数组，此处data长度大于4094。 */
	public static HammingPacket[] handleMultiplePackets(byte[] data) {
		try {
			int size = (data.length / 4089) + 1;
			HammingPacket packets[] = new HammingPacket[size];
			for (int i = 0; i<size; i++) {
				int packet_size = i==size-1? data.length%4089 : 4089;
				byte packet_data[] = new byte[packet_size];
				for (int j = 0; j<packet_data.length; j++) {
					packet_data[j] = data[i*4089+j];
				}
				packets[i] = HammingPacket.fromBytes((short)i, i==size-1? (byte)0:(byte)1, packet_data);
			}
			return packets;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
		return null;
	}

}
