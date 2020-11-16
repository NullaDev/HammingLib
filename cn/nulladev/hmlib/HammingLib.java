package cn.nulladev.hmlib;

public class HammingLib {
	
	/** 获取单个汉明码处理过的数据包。 */
	public static HammingPacket handleSinglePacket(byte[] data) {
		try {
			HammingPacket bdata = HammingPacket.fromBytes(data);
			return bdata;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	/** 还原单个汉明码处理过的数据包。 */
	public static byte[] restoreSinglePacket(HammingPacket packet) {
		try {
			return packet.toRealBytes();
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	/** 还原单个汉明码处理过的数据包。 */
	public static byte[] restoreSinglePacket(byte[] data) {
		try {
			return restoreSinglePacket(HammingPacket.fromRawBytes(data));
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	/** 获取汉明码处理过的数据包的数组，此处data长度大于4094。 */
	public HammingPacket[] handleMultiplePackets(byte[] data) {
		//TODO 还没写。
		return null;
	}

}
