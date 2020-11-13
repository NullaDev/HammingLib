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
			return packet.toBytes();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	/** 还原单个汉明码处理过的数据包。 */
	public static byte[] restoreSinglePacket(byte[] data, int size) {
		try {
			return restoreSinglePacket(HammingPacket.fromRawBytes(data, size));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public HammingPacket[] handleMultiplePackets(byte[] data) {
		//这里的data长度大于4094，不能用一个包装下，所以返回多个包的数组。
		//TODO 还没写。
		return null;
	}
	
	public HammingPacket[] reorder(byte[] data) {
		//为了避免传输过程中经常出现的连续n个比特位错误，需要把包打乱顺序后进行分装，以包的数量对抗连续错误。
		//TODO 还没写。
		return null;
	}

}
