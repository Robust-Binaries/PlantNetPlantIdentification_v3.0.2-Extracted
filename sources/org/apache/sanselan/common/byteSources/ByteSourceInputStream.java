package org.apache.sanselan.common.byteSources;

import android.support.p000v4.media.session.PlaybackStateCompat;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import kotlin.UByte;

public class ByteSourceInputStream extends ByteSource {
    private static final int BLOCK_SIZE = 1024;
    private CacheBlock cacheHead = null;

    /* renamed from: is */
    private final InputStream f162is;
    private Long length = null;
    private byte[] readBuffer = null;

    private class CacheBlock {
        public final byte[] bytes;
        private CacheBlock next = null;
        private boolean triedNext = false;

        public CacheBlock(byte[] bArr) {
            this.bytes = bArr;
        }

        public CacheBlock getNext() throws IOException {
            CacheBlock cacheBlock = this.next;
            if (cacheBlock != null) {
                return cacheBlock;
            }
            if (this.triedNext) {
                return null;
            }
            this.triedNext = true;
            this.next = ByteSourceInputStream.this.readBlock();
            return this.next;
        }
    }

    private class CacheReadingInputStream extends InputStream {
        private CacheBlock block;
        private int blockIndex;
        private boolean readFirst;

        private CacheReadingInputStream() {
            this.block = null;
            this.readFirst = false;
            this.blockIndex = 0;
        }

        public int read() throws IOException {
            if (this.block == null) {
                if (this.readFirst) {
                    return -1;
                }
                this.block = ByteSourceInputStream.this.getFirstBlock();
                this.readFirst = true;
            }
            CacheBlock cacheBlock = this.block;
            if (cacheBlock != null && this.blockIndex >= cacheBlock.bytes.length) {
                this.block = this.block.getNext();
                this.blockIndex = 0;
            }
            CacheBlock cacheBlock2 = this.block;
            if (cacheBlock2 == null || this.blockIndex >= cacheBlock2.bytes.length) {
                return -1;
            }
            byte[] bArr = this.block.bytes;
            int i = this.blockIndex;
            this.blockIndex = i + 1;
            return bArr[i] & UByte.MAX_VALUE;
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            if (bArr != null) {
                if (i >= 0 && i <= bArr.length && i2 >= 0) {
                    int i3 = i + i2;
                    if (i3 <= bArr.length && i3 >= 0) {
                        if (i2 == 0) {
                            return 0;
                        }
                        if (this.block == null) {
                            if (this.readFirst) {
                                return -1;
                            }
                            this.block = ByteSourceInputStream.this.getFirstBlock();
                            this.readFirst = true;
                        }
                        CacheBlock cacheBlock = this.block;
                        if (cacheBlock != null && this.blockIndex >= cacheBlock.bytes.length) {
                            this.block = this.block.getNext();
                            this.blockIndex = 0;
                        }
                        CacheBlock cacheBlock2 = this.block;
                        if (cacheBlock2 == null || this.blockIndex >= cacheBlock2.bytes.length) {
                            return -1;
                        }
                        int min = Math.min(i2, this.block.bytes.length - this.blockIndex);
                        System.arraycopy(this.block.bytes, this.blockIndex, bArr, i, min);
                        this.blockIndex += min;
                        return min;
                    }
                }
                throw new IndexOutOfBoundsException();
            }
            throw new NullPointerException();
        }
    }

    public ByteSourceInputStream(InputStream inputStream, String str) {
        super(str);
        this.f162is = new BufferedInputStream(inputStream);
    }

    /* access modifiers changed from: private */
    public CacheBlock readBlock() throws IOException {
        if (this.readBuffer == null) {
            this.readBuffer = new byte[1024];
        }
        int read = this.f162is.read(this.readBuffer);
        if (read < 1) {
            return null;
        }
        if (read < 1024) {
            byte[] bArr = new byte[read];
            System.arraycopy(this.readBuffer, 0, bArr, 0, read);
            return new CacheBlock(bArr);
        }
        byte[] bArr2 = this.readBuffer;
        this.readBuffer = null;
        return new CacheBlock(bArr2);
    }

    /* access modifiers changed from: private */
    public CacheBlock getFirstBlock() throws IOException {
        if (this.cacheHead == null) {
            this.cacheHead = readBlock();
        }
        return this.cacheHead;
    }

    public InputStream getInputStream() throws IOException {
        return new CacheReadingInputStream();
    }

    public byte[] getBlock(int i, int i2) throws IOException {
        InputStream inputStream = getInputStream();
        inputStream.skip((long) i);
        byte[] bArr = new byte[i2];
        int i3 = 0;
        do {
            int read = inputStream.read(bArr, i3, bArr.length - i3);
            if (read >= 1) {
                i3 += read;
            } else {
                throw new IOException("Could not read block.");
            }
        } while (i3 < i2);
        return bArr;
    }

    public long getLength() throws IOException {
        Long l = this.length;
        if (l != null) {
            return l.longValue();
        }
        InputStream inputStream = getInputStream();
        long j = 0;
        while (true) {
            long skip = inputStream.skip(PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
            if (skip > 0) {
                j += skip;
            } else {
                this.length = new Long(j);
                return j;
            }
        }
    }

    public byte[] getAll() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (CacheBlock firstBlock = getFirstBlock(); firstBlock != null; firstBlock = firstBlock.getNext()) {
            byteArrayOutputStream.write(firstBlock.bytes);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public String getDescription() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Inputstream: '");
        stringBuffer.append(this.filename);
        stringBuffer.append("'");
        return stringBuffer.toString();
    }
}
