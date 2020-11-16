package cn.nulladev.hmlib;

public class Bytelib {
	
	public static String byte2bits(byte b) {
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
	
	public static byte bits2byte(String bits) {
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
	
	public static byte[] int2byte(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }
 
    public static int byte2int(byte[] bytes) {
        int value=0;
        for(int i = 0; i < 4; i++) {
            int shift= (3-i) * 8;
            value +=(bytes[i] & 0xFF) << shift;
        }
        return value;
    }
    
    public static byte[] short2byte(short s){
        byte[] b = new byte[2]; 
        for(int i = 0; i < 2; i++){
            int offset = 16 - (i+1)*8; //因为byte占4个字节，所以要计算偏移量
            b[i] = (byte)((s >> offset)&0xff); //把16位分为2个8位进行分别存储
        }
        return b;
   }

   public static short byte2short(byte[] b){
       short l = 0;
       for (int i = 0; i < 2; i++) {
           l<<=8; //<<=和我们的 +=是一样的，意思就是 l = l << 8 
           l |= (b[i] & 0xff); //和上面也是一样的  l = l | (b[i]&0xff)
       }
       return l;
   }
    
    public static byte[] connect(byte[] bytes1, byte[] bytes2) {
    	int length1 = bytes1.length;
    	int length2 = bytes2.length;
    	byte[] result = new byte[length1+length2];
    	for (int i = 0; i<bytes1.length; i++) {
    		result[i] = bytes1[i];
    	}
    	for (int i = 0; i<bytes2.length; i++) {
    		result[i+length1] = bytes2[i];
    	}
    	return result;
    }

}
