package com.facebook.soloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import kotlin.UByte;
import kotlin.UShort;

public final class MinElf {
    public static final int DT_NEEDED = 1;
    public static final int DT_NULL = 0;
    public static final int DT_STRTAB = 5;
    public static final int ELF_MAGIC = 1179403647;
    public static final int PN_XNUM = 65535;
    public static final int PT_DYNAMIC = 2;
    public static final int PT_LOAD = 1;

    private static class ElfError extends RuntimeException {
        ElfError(String str) {
            super(str);
        }
    }

    public static String[] extract_DT_NEEDED(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            return extract_DT_NEEDED(fileInputStream.getChannel());
        } finally {
            fileInputStream.close();
        }
    }

    public static String[] extract_DT_NEEDED(FileChannel fileChannel) throws IOException {
        long j;
        long j2;
        int i;
        long j3;
        long j4;
        boolean z;
        long j5;
        long j6;
        long j7;
        long j8;
        long j9;
        long j10;
        long j11;
        int i2;
        long j12;
        long j13;
        long j14;
        long j15;
        long j16;
        long j17;
        FileChannel fileChannel2 = fileChannel;
        ByteBuffer allocate = ByteBuffer.allocate(8);
        allocate.order(ByteOrder.LITTLE_ENDIAN);
        if (getu32(fileChannel2, allocate, 0) == 1179403647) {
            boolean z2 = true;
            if (getu8(fileChannel2, allocate, 4) != 1) {
                z2 = false;
            }
            if (getu8(fileChannel2, allocate, 5) == 2) {
                allocate.order(ByteOrder.BIG_ENDIAN);
            }
            if (z2) {
                j = getu32(fileChannel2, allocate, 28);
            } else {
                j = get64(fileChannel2, allocate, 32);
            }
            if (z2) {
                j2 = (long) getu16(fileChannel2, allocate, 44);
            } else {
                j2 = (long) getu16(fileChannel2, allocate, 56);
            }
            if (z2) {
                i = getu16(fileChannel2, allocate, 42);
            } else {
                i = getu16(fileChannel2, allocate, 54);
            }
            if (j2 == 65535) {
                if (z2) {
                    j17 = getu32(fileChannel2, allocate, 32);
                } else {
                    j17 = get64(fileChannel2, allocate, 40);
                }
                if (z2) {
                    j2 = getu32(fileChannel2, allocate, j17 + 28);
                } else {
                    j2 = getu32(fileChannel2, allocate, j17 + 44);
                }
            }
            long j18 = j;
            long j19 = 0;
            while (true) {
                if (j19 >= j2) {
                    j3 = 0;
                    j4 = 0;
                    break;
                }
                if (z2) {
                    j16 = getu32(fileChannel2, allocate, j18 + 0);
                } else {
                    j16 = getu32(fileChannel2, allocate, j18 + 0);
                }
                if (j16 == 2) {
                    if (z2) {
                        j3 = getu32(fileChannel2, allocate, j18 + 4);
                    } else {
                        j3 = get64(fileChannel2, allocate, j18 + 8);
                    }
                    j4 = 0;
                } else {
                    j18 += (long) i;
                    j19++;
                }
            }
            if (j3 != j4) {
                long j20 = j3;
                long j21 = j4;
                int i3 = 0;
                while (true) {
                    if (z2) {
                        z = z2;
                        j5 = getu32(fileChannel2, allocate, j20 + j4);
                    } else {
                        z = z2;
                        j5 = get64(fileChannel2, allocate, j20 + j4);
                    }
                    if (j5 == 1) {
                        if (i3 != Integer.MAX_VALUE) {
                            i3++;
                            j6 = j5;
                        } else {
                            throw new ElfError("malformed DT_NEEDED section");
                        }
                    } else if (j5 == 5) {
                        if (z) {
                            j6 = j5;
                            j15 = getu32(fileChannel2, allocate, j20 + 4);
                        } else {
                            j6 = j5;
                            j15 = get64(fileChannel2, allocate, j20 + 8);
                        }
                        j21 = j15;
                    } else {
                        j6 = j5;
                    }
                    long j22 = 16;
                    j20 += z ? 8 : 16;
                    j4 = 0;
                    if (j6 != 0) {
                        z2 = z;
                    } else if (j21 != 0) {
                        long j23 = j3;
                        int i4 = 0;
                        while (true) {
                            if (((long) i4) >= j2) {
                                j7 = 0;
                                j8 = 0;
                                break;
                            }
                            if (z) {
                                j11 = getu32(fileChannel2, allocate, j + j4);
                            } else {
                                j11 = getu32(fileChannel2, allocate, j + j4);
                            }
                            if (j11 == 1) {
                                if (z) {
                                    j12 = getu32(fileChannel2, allocate, j + 8);
                                } else {
                                    j12 = get64(fileChannel2, allocate, j + j22);
                                }
                                if (z) {
                                    i2 = i4;
                                    j13 = getu32(fileChannel2, allocate, j + 20);
                                } else {
                                    i2 = i4;
                                    j13 = get64(fileChannel2, allocate, j + 40);
                                }
                                if (j12 <= j21 && j21 < j13 + j12) {
                                    if (z) {
                                        j14 = getu32(fileChannel2, allocate, j + 4);
                                    } else {
                                        j14 = get64(fileChannel2, allocate, j + 8);
                                    }
                                    j7 = j14 + (j21 - j12);
                                    j8 = 0;
                                }
                            } else {
                                i2 = i4;
                            }
                            j += (long) i;
                            i4 = i2 + 1;
                            j22 = 16;
                            j4 = 0;
                        }
                        if (j7 != j8) {
                            String[] strArr = new String[i3];
                            int i5 = 0;
                            while (true) {
                                if (z) {
                                    j9 = getu32(fileChannel2, allocate, j23 + j8);
                                } else {
                                    j9 = get64(fileChannel2, allocate, j23 + j8);
                                }
                                if (j9 == 1) {
                                    if (z) {
                                        j10 = getu32(fileChannel2, allocate, j23 + 4);
                                    } else {
                                        j10 = get64(fileChannel2, allocate, j23 + 8);
                                    }
                                    strArr[i5] = getSz(fileChannel2, allocate, j10 + j7);
                                    if (i5 != Integer.MAX_VALUE) {
                                        i5++;
                                    } else {
                                        throw new ElfError("malformed DT_NEEDED section");
                                    }
                                }
                                j23 += z ? 8 : 16;
                                if (j9 != 0) {
                                    j8 = 0;
                                } else if (i5 == strArr.length) {
                                    return strArr;
                                } else {
                                    throw new ElfError("malformed DT_NEEDED section");
                                }
                            }
                        } else {
                            throw new ElfError("did not find file offset of DT_STRTAB table");
                        }
                    } else {
                        throw new ElfError("Dynamic section string-table not found");
                    }
                }
            } else {
                throw new ElfError("ELF file does not contain dynamic linking information");
            }
        } else {
            throw new ElfError("file is not ELF");
        }
    }

    private static String getSz(FileChannel fileChannel, ByteBuffer byteBuffer, long j) throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            long j2 = 1 + j;
            short u8Var = getu8(fileChannel, byteBuffer, j);
            if (u8Var == 0) {
                return sb.toString();
            }
            sb.append((char) u8Var);
            j = j2;
        }
    }

    private static void read(FileChannel fileChannel, ByteBuffer byteBuffer, int i, long j) throws IOException {
        byteBuffer.position(0);
        byteBuffer.limit(i);
        while (byteBuffer.remaining() > 0) {
            int read = fileChannel.read(byteBuffer, j);
            if (read == -1) {
                break;
            }
            j += (long) read;
        }
        if (byteBuffer.remaining() <= 0) {
            byteBuffer.position(0);
            return;
        }
        throw new ElfError("ELF file truncated");
    }

    private static long get64(FileChannel fileChannel, ByteBuffer byteBuffer, long j) throws IOException {
        read(fileChannel, byteBuffer, 8, j);
        return byteBuffer.getLong();
    }

    private static long getu32(FileChannel fileChannel, ByteBuffer byteBuffer, long j) throws IOException {
        read(fileChannel, byteBuffer, 4, j);
        return ((long) byteBuffer.getInt()) & 4294967295L;
    }

    private static int getu16(FileChannel fileChannel, ByteBuffer byteBuffer, long j) throws IOException {
        read(fileChannel, byteBuffer, 2, j);
        return byteBuffer.getShort() & UShort.MAX_VALUE;
    }

    private static short getu8(FileChannel fileChannel, ByteBuffer byteBuffer, long j) throws IOException {
        read(fileChannel, byteBuffer, 1, j);
        return (short) (byteBuffer.get() & UByte.MAX_VALUE);
    }
}
