package cn.nulladev.hmlib;

public class HammingPacket {
	
	public static final int HEADER_BYTES = 5;
	public static final int DATA_BITS = 32768;
	public static final int DATA_BYTES = 4096;
	public static final int DATA_BYTES_VALID = 4094;
	public static final int TOTAL_BYTES = HEADER_BYTES + DATA_BYTES;
	
	private short _index = 0;
	private short _size = 4094;
	private byte _fragflag = 1;
	private byte _headerBytes[] = new byte[HEADER_BYTES];
	private byte _dataBytes[] = new byte[DATA_BYTES];
	
	/** 私有构造器，请使用工厂方法初始化。 */
	private HammingPacket() {}
	
	/** 获取pos位置的bit值，返回'0'或'1'。 */
	public char getBitAtPos(int pos) {
		int index = pos / 8;
		String byteStr = Bytelib.byte2bits(this._dataBytes[index]);
		return byteStr.charAt(pos % 8);
	}
	
	/** 将pos位置的bit值设置为'0'或'1'。 */
	public void setBitAtPos(int pos, char c) throws Exception {
		if (c != '0' && c != '1') {
			throw new Exception("Invalid bit char.");
		}
		int index = pos / 8;
		StringBuffer SB = new StringBuffer(Bytelib.byte2bits(this._dataBytes[index]));
		SB.setCharAt(pos % 8, c);
		this._dataBytes[index] = Bytelib.bits2byte(SB.toString());
	}
	
	/** 拼好汉明码处理过的数据的StringBuffer，方便进一步处理。 */
	public StringBuffer rawDataString() {
		StringBuffer dataBuf = new StringBuffer();
		for (int i = 0; i < DATA_BYTES; i++) {
			dataBuf.append(Bytelib.byte2bits(this._dataBytes[i]));
		}
		return dataBuf;
	}
	
	/** 直接输出整个包。 */
	public byte[] toRawBytes() {
		return Bytelib.connect(this._headerBytes, this._dataBytes);
	}
	
	/** 初始化包的header。 */
	public HammingPacket initHeader(short index, byte fragflag) {
		this._index = index;
		this._fragflag = fragflag;
		this.createHeader();
		return this;
	}
	
	/** 根据index和size生成包的header。 */
	private void createHeader() {
		this._headerBytes = Bytelib.connect(Bytelib.short2byte(_index), Bytelib.short2byte(_size));
		this._headerBytes = Bytelib.connect(this._headerBytes, new byte[] {this._fragflag});
	}
	
	public boolean isFinal() {
		return this._fragflag == 0;
	}
	
	public short getIndex() {
		return this._index;
	}
	
	public String info() {
		return "packet index:" + this._index + ", size:" + this._size + ", fragflag:" + this._fragflag;
	}
	
	/** 根据汉明码计算错误位置。 */
	public int calcErrPos() {
		int flag = 0;
		for (int i = 0; i < DATA_BITS; i++) {
			if (getBitAtPos(i) == '1') {
				flag = flag ^ i;
			}
		}
		return flag;
	}
	
	/** 试图使用汉明码自我修正。 */
	public void selfCorrect() throws Exception {
		int pos = this.calcErrPos();
		if (pos == 0) {
			System.out.println("no error");
			return;
		} else {
			int flag = 0;
			for (int i = 0; i < DATA_BITS; i++) {
				if (getBitAtPos(i) == '1') {
					flag++;
				}
			}
			if (flag % 2 == 0) {
				throw new Exception("more than 2 error detected, cannot correct");
			} else {
				char c = this.getBitAtPos(pos) == '0'? '1' : '0';
				this.setBitAtPos(pos, c);
				System.out.println("1 error detected at packet" + this._index);
				System.out.println("wrong bit at pos" + pos + ", corrected");
			}
		}
	}
	
	/** 输出真实数据。 */
	public byte[] toRealBytes() throws Exception {
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
			bytes[i] = Bytelib.bits2byte(bits);
		}
		return bytes;
	}
	
	/** 工厂方法，使用汉明码处理过的看不懂数据实例化。 */
	public static HammingPacket fromRawBytes(byte[] bytes) throws Exception {
		if (bytes.length != TOTAL_BYTES) {
			throw new Exception("Invalid data number.");
		}
		HammingPacket data = new HammingPacket();
		for (int i = 0; i<HEADER_BYTES; i++) {
			data._headerBytes[i] = bytes[i];
    	}
		data._index = Bytelib.byte2short(new byte[]{data._headerBytes[0], data._headerBytes[1]});
		data._size = Bytelib.byte2short(new byte[]{data._headerBytes[2], data._headerBytes[3]});
		data._fragflag = data._headerBytes[4];
		for (int i = 0; i<DATA_BYTES; i++) {
			data._dataBytes[i] = bytes[i+HEADER_BYTES];
    	}
		return data;
	}
	
	/** 工厂方法，使用正常数据实例化。 */
	public static HammingPacket fromBytes(byte[] dataBytes) throws Exception {
		if (dataBytes.length > DATA_BYTES_VALID) {
			throw new Exception("Too many bytes in one packet.");
		}
		HammingPacket data = new HammingPacket();
		data._size = (short) dataBytes.length;
		//拼接原始数据
		StringBuffer dataBuf = new StringBuffer();
		for (byte b : dataBytes) {
			dataBuf.append(Bytelib.byte2bits(b));
		}
		for (int i = 0; i < DATA_BYTES_VALID - dataBytes.length; i++) {
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
		for (int i = 0; i < DATA_BITS; i++) {
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
		for (int i = 1; i < DATA_BITS; i++) {
			if (SB.charAt(i) == '1') {
				flag2++;
			}
		}
		if (flag2 % 2 == 1) {
			SB.setCharAt(0, '1');
		}
		//填充
		for (int i = 0; i < DATA_BYTES; i++) {
			String bits = SB.substring(8 * i, 8 * i + 8);
			data._dataBytes[i] = Bytelib.bits2byte(bits);
		}
		return data;
	}

}
