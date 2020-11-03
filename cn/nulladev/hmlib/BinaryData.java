package cn.nulladev.hmlib;

public class BinaryData {
	
	public static final int PACKET_BITS = 32768;
	public static final int PACKET_BYTES = 4096;
	public static final int VALID_BYTES = 4094;
	
	private int _size;
	private byte _dataBytes[] = new byte[PACKET_BYTES];
	
	private BinaryData(int size) {
		this._size = size;
	}
	
	public char getBitAtPos(int pos) {
		int index = pos / 8;
		String byteStr = ByteLib.byte2Bits(this._dataBytes[index]);
		return byteStr.charAt(pos % 8);
	}
	
	public void setBitAtPos(int pos, char c) throws Exception {
		if (c != '0' && c != '1') {
			throw new Exception("Invalid bit char.");
		}
		int index = pos / 8;
		StringBuffer SB = new StringBuffer(ByteLib.byte2Bits(this._dataBytes[index]));
		SB.setCharAt(pos % 8, c);
		this._dataBytes[index] = ByteLib.Bits2Byte(SB.toString());
	}
	
	public StringBuffer rawDataString() {
		StringBuffer dataBuf = new StringBuffer();
		for (int i = 0; i < PACKET_BYTES; i++) {
			dataBuf.append(ByteLib.byte2Bits(this._dataBytes[i]));
		}
		return dataBuf;
	}
	
	public int calcErrPos() {
		int flag = 0;
		for (int i = 0; i < PACKET_BITS; i++) {
			if (getBitAtPos(i) == '1') {
				flag = flag ^ i;
			}
		}
		return flag;
	}
	
	public void selfCorrect() throws Exception {
		int pos = this.calcErrPos();
		if (pos == 0) {
			System.out.println("no error");
			return;
		} else {
			int flag = 0;
			for (int i = 0; i < PACKET_BITS; i++) {
				if (getBitAtPos(i) == '1') {
					flag++;
				}
			}
			if (flag % 2 == 0) {
				throw new Exception("more than 2 error, cannot correct");
			} else {
				char c = this.getBitAtPos(pos) == '0'? '1' : '0';
				this.setBitAtPos(pos, c);
				System.out.println("1 error at pos" + pos + ", corrected");
			}
		}
	}
	
	public byte[] toRawBytes() {
		return this._dataBytes;
	}
	
	public byte[] toBytes() throws Exception {
		if (this.calcErrPos() != 0)
			this.selfCorrect();
		StringBuffer SB = this.rawDataString();
		for (int i = 14; i >= 0; i--) {
			SB.deleteCharAt((int)Math.pow(2, i));
		}
		SB.deleteCharAt(0);
		byte bytes[] = new byte[this._size];
		for (int i = 0; i < this._size; i++) {
			String bits = SB.substring(8 * i, 8 * i + 8);
			bytes[i] = ByteLib.Bits2Byte(bits);
		}
		return bytes;
	}
	
	public static BinaryData fromRawBytes(byte[] dataBytes, int size) throws Exception {
		if (dataBytes.length != PACKET_BYTES) {
			throw new Exception("Invalid data number.");
		}
		BinaryData data = new BinaryData(dataBytes.length);
		data._dataBytes = dataBytes;
		return data;
	}
	
	public static BinaryData fromBytes(byte[] dataBytes) throws Exception {
		if (dataBytes.length > VALID_BYTES) {
			throw new Exception("Too many bytes in one packet.");
		}
		BinaryData data = new BinaryData(dataBytes.length);
		//拼接原始数据
		StringBuffer dataBuf = new StringBuffer();
		for (byte b : dataBytes) {
			dataBuf.append(ByteLib.byte2Bits(b));
		}
		for (int i = 0; i < VALID_BYTES - dataBytes.length; i++) {
			dataBuf.append("00000000");
		}
		//在汉明码位置先填0
		StringBuffer SB = new StringBuffer(dataBuf.toString());
		SB.insert(0, "0");
		for (int i = 0; i < 15; i++) {
			SB.insert((int)Math.pow(2, i), "0");
		}
		//计算汉明码
		int flag = 0;
		for (int i = 0; i < PACKET_BITS; i++) {
			if (SB.charAt(i) == '1') {
				flag = flag ^ i;
			}
		}
		for (int i = 0; i < 15; i++) {
			if ((byte)((flag >> i) & 0x1) == 1) {
				SB.setCharAt((int)Math.pow(2, i), '1');
			}
		}
		//计算checksum
		int flag2 = 0;
		for (int i = 1; i < PACKET_BITS; i++) {
			if (SB.charAt(i) == '1') {
				flag2++;
			}
		}
		if (flag2 % 2 == 1) {
			SB.setCharAt(0, '1');
		}
		//填充
		for (int i = 0; i < PACKET_BYTES; i++) {
			String bits = SB.substring(8 * i, 8 * i + 8);
			data._dataBytes[i] = ByteLib.Bits2Byte(bits);
		}
		return data;
	}

}

class ByteLib {
	
	static String byte2Bits(byte b) {
		return "" 
		+ (byte)((b >> 7) & 0x1) 
		+ (byte)((b >> 6) & 0x1) 
		+ (byte)((b >> 5) & 0x1) 
		+ (byte)((b >> 4) & 0x1) 
		+ (byte)((b >> 3) & 0x1) 
		+ (byte)((b >> 2) & 0x1) 
		+ (byte)((b >> 1) & 0x1) 
		+ (byte)((b >> 0) & 0x1);
	}
	
	static byte Bits2Byte(String bits) {
		if (bits == null)
			return 0;
		if (bits.length() != 8) {
			return 0;
		} else {
			if (bits.charAt(0) == '0') {
				return (byte) Integer.parseInt(bits, 2);
			} else {
				return (byte) (Integer.parseInt(bits, 2) - 256);
			}
		}
	}
	
}
