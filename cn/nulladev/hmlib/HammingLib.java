package cn.nulladev.hmlib;

public class HammingLib {
	
	public static byte[] toHamming(byte[] data) {
		try {
			BinaryData bdata = BinaryData.fromBytes(data);
			return bdata.toRawBytes();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public static byte[] fromHamming(byte[] data, int size) {
		try {
			BinaryData bdata = BinaryData.fromRawBytes(data, size);
			return bdata.toBytes();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

}
