/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.socket.inv;

/**
 *
 * @author francescodigaudio
 */
public class CRC16 {

    /**
     * CRC16_CCITT: Polynomial x16+x12+x5+1 (0x1021), initial value 0x0000, low
     * bit first, high bit after, result is exclusive to 0x0000 0x8408 is the
     * result of 0x1021 bitwise reversal.
     *
     * @param buffer
     * @return
     */
    public static int CRC16_CCITT(byte[] buffer) {
        int wCRCin = 0x0000;
        int wCPoly = 0x8408;
        for (byte b : buffer) {
            wCRCin ^= ((int) b & 0x00ff);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x0001) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= wCPoly;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
//        wCRCin=(wCRCin<<8)|(wCRCin>>8);
//        wCRCin &= 0xffff;
        return wCRCin ^= 0x0000;

    }

    /**
     * CRC-CCITT (0xFFFF) CRC16_CCITT_FALSE: Polynomial x16+x12+x5+1 (0x1021),
     * initial value 0xFFFF, low bit is after, high bit is first, result is
     * exclusive to 0x0000
     *
     * @param buffer
     * @return
     */
    public static int CRC16_CCITT_FALSE(byte[] buffer) {
        int wCRCin = 0xffff;
        int wCPoly = 0x1021;
        for (byte b : buffer) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((wCRCin >> 15 & 1) == 1);
                wCRCin <<= 1;
                if (c15 ^ bit) {
                    wCRCin ^= wCPoly;
                }
            }
        }
        wCRCin &= 0xffff;
        return wCRCin ^= 0x0000;
    }

    /**
     * CRC-CCITT (XModem) CRC16_XMODEM: Polynomial x16+x12+x5+1 (0x1021),
     * initial value 0x0000, low bit after, high bit first, result is XOR0
     *
     * @param buffer
     * @return
     */
    public static int CRC16_XMODEM(byte[] buffer) {
        int wCRCin = 0x0000; // initial value 65535
        int wCPoly = 0x1021; // 0001 0000 0010 0001 (0, 5, 12)
        for (byte b : buffer) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((wCRCin >> 15 & 1) == 1);
                wCRCin <<= 1;
                if (c15 ^ bit) {
                    wCRCin ^= wCPoly;
                }
            }
        }
        wCRCin &= 0xffff;
        return wCRCin ^= 0x0000;
    }

    /**
     * CRC16_X25: Polynomial x16+x12+x5+1 (0x1021), initial value 0xffff, low
     * bit first, high bit after, result is XORIF 0xFFFF 0x8408 is the result of
     * 0x1021 bitwise reversal.
     *
     * @param buffer
     * @return
     */
    public static int CRC16_X25(byte[] buffer) {
        int wCRCin = 0xffff;
        int wCPoly = 0x8408;
        for (byte b : buffer) {
            wCRCin ^= ((int) b & 0x00ff);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x0001) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= wCPoly;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return wCRCin ^= 0xffff;
    }

    /**
     * CRC-16 (Modbus) CRC16_MODBUS: Polynomial x16+x15+x2+1 (0x8005), initial
     * value 0xFFFF, low bit first, high bit after, result is X2000000 exclusive
     * OR 0xA001 is the result of 0x8005 bitwise reversal
     *
     * @param buffer
     * @return
     */
    public static int CRC16_MODBUS(byte[] buffer) {
        int wCRCin = 0xffff;
        int POLYNOMIAL = 0xa001;
        for (byte b : buffer) {
            wCRCin ^= ((int) b & 0x00ff);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x0001) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= POLYNOMIAL;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return wCRCin ^= 0x0000;
    }

    /**
     * CRC-16 CRC16_IBM: Polynomial x16+x15+x2+1 (0x8005), initial value 0x0000,
     * low bit first, high bit after, result is XOR0 0xA001 is the result of
     * 0x8005 bitwise reversal
     *
     * @param buffer
     * @return
     */
    public static int CRC16_IBM(byte[] buffer) {
        int wCRCin = 0x0000;
        int wCPoly = 0xa001;
        for (byte b : buffer) {
            wCRCin ^= ((int) b & 0x00ff);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x0001) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= wCPoly;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return wCRCin ^= 0x0000;
    }

    /**
     * CRC16_MAXIM: Polynomial x16+x15+x2+1 (0x8005), initial value 0x0000, low
     * bit first, high bit after, result is XORIF 0xFFFF 0xA001 is the result of
     * 0x8005 bitwise reversal
     *
     * @param buffer
     * @return
     */
    public static int CRC16_MAXIM(byte[] buffer) {
        int wCRCin = 0x0000;
        int wCPoly = 0xa001;
        for (byte b : buffer) {
            wCRCin ^= ((int) b & 0x00ff);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x0001) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= wCPoly;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return wCRCin ^= 0xffff;
    }

    /**
     * CRC16_USB: Polynomial x16+x15+x2+1 (0x8005), initial value 0xFFFF, low
     * bit first, high bit after, result is XORIF 0xFFFF 0xA001 is the result of
     * 0x8005 bitwise reversal
     *
     * @param buffer
     * @return
     */
    public static int CRC16_USB(byte[] buffer) {
        int wCRCin = 0xFFFF;
        int wCPoly = 0xa001;
        for (byte b : buffer) {
            wCRCin ^= ((int) b & 0x00ff);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x0001) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= wCPoly;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return wCRCin ^= 0xffff;
    }

    /**
     * CRC16_DNP: Polynomial x16+x13+x12+x11+x10+x8+x6+x5+x2+1 (0x3D65), initial
     * value 0x0000, low bit first, high bit after, result is XORIF 0xFFFF
     * 0xA6BC is the result of 0x3D65 bitwise reversal
     *
     * @param buffer
     * @return
     */
    public static int CRC16_DNP(byte[] buffer) {
        int wCRCin = 0x0000;
        int wCPoly = 0xA6BC;
        for (byte b : buffer) {
            wCRCin ^= ((int) b & 0x00ff);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x0001) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= wCPoly;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return wCRCin ^= 0xffff;
    }
}
