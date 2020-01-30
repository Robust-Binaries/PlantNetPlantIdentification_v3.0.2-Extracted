package okio;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;
import kotlin.UByte;

public final class Options extends AbstractList<ByteString> implements RandomAccess {
    final ByteString[] byteStrings;
    final int[] trie;

    private Options(ByteString[] byteStringArr, int[] iArr) {
        this.byteStrings = byteStringArr;
        this.trie = iArr;
    }

    /* renamed from: of */
    public static Options m194of(ByteString... byteStringArr) {
        if (byteStringArr.length == 0) {
            return new Options(new ByteString[0], new int[]{0, -1});
        }
        ArrayList arrayList = new ArrayList(Arrays.asList(byteStringArr));
        Collections.sort(arrayList);
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(Integer.valueOf(-1));
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList2.set(Collections.binarySearch(arrayList, byteStringArr[i2]), Integer.valueOf(i2));
        }
        if (((ByteString) arrayList.get(0)).size() != 0) {
            int i3 = 0;
            while (i3 < arrayList.size()) {
                ByteString byteString = (ByteString) arrayList.get(i3);
                int i4 = i3 + 1;
                int i5 = i4;
                while (i5 < arrayList.size()) {
                    ByteString byteString2 = (ByteString) arrayList.get(i5);
                    if (!byteString2.startsWith(byteString)) {
                        continue;
                        break;
                    } else if (byteString2.size() == byteString.size()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("duplicate option: ");
                        sb.append(byteString2);
                        throw new IllegalArgumentException(sb.toString());
                    } else if (((Integer) arrayList2.get(i5)).intValue() > ((Integer) arrayList2.get(i3)).intValue()) {
                        arrayList.remove(i5);
                        arrayList2.remove(i5);
                    } else {
                        i5++;
                    }
                }
                i3 = i4;
            }
            Buffer buffer = new Buffer();
            buildTrieRecursive(0, buffer, 0, arrayList, 0, arrayList.size(), arrayList2);
            int[] iArr = new int[intCount(buffer)];
            for (int i6 = 0; i6 < iArr.length; i6++) {
                iArr[i6] = buffer.readInt();
            }
            if (buffer.exhausted()) {
                return new Options((ByteString[]) byteStringArr.clone(), iArr);
            }
            throw new AssertionError();
        }
        throw new IllegalArgumentException("the empty byte string is not a supported option");
    }

    private static void buildTrieRecursive(long j, Buffer buffer, int i, List<ByteString> list, int i2, int i3, List<Integer> list2) {
        int i4;
        int i5;
        int i6;
        Buffer buffer2;
        int i7;
        Buffer buffer3 = buffer;
        int i8 = i;
        List<ByteString> list3 = list;
        int i9 = i2;
        int i10 = i3;
        List<Integer> list4 = list2;
        if (i9 < i10) {
            int i11 = i9;
            while (i11 < i10) {
                if (((ByteString) list3.get(i11)).size() >= i8) {
                    i11++;
                } else {
                    throw new AssertionError();
                }
            }
            ByteString byteString = (ByteString) list.get(i2);
            ByteString byteString2 = (ByteString) list3.get(i10 - 1);
            int i12 = -1;
            if (i8 == byteString.size()) {
                i12 = ((Integer) list4.get(i9)).intValue();
                int i13 = i9 + 1;
                byteString = (ByteString) list3.get(i13);
                i4 = i13;
            } else {
                i4 = i9;
            }
            if (byteString.getByte(i8) != byteString2.getByte(i8)) {
                int i14 = 1;
                for (int i15 = i4 + 1; i15 < i10; i15++) {
                    if (((ByteString) list3.get(i15 - 1)).getByte(i8) != ((ByteString) list3.get(i15)).getByte(i8)) {
                        i14++;
                    }
                }
                long intCount = j + ((long) intCount(buffer)) + 2 + ((long) (i14 * 2));
                buffer3.writeInt(i14);
                buffer3.writeInt(i12);
                for (int i16 = i4; i16 < i10; i16++) {
                    byte b = ((ByteString) list3.get(i16)).getByte(i8);
                    if (i16 == i4 || b != ((ByteString) list3.get(i16 - 1)).getByte(i8)) {
                        buffer3.writeInt((int) b & UByte.MAX_VALUE);
                    }
                }
                Buffer buffer4 = new Buffer();
                int i17 = i4;
                while (i17 < i10) {
                    byte b2 = ((ByteString) list3.get(i17)).getByte(i8);
                    int i18 = i17 + 1;
                    int i19 = i18;
                    while (true) {
                        if (i19 >= i10) {
                            i6 = i10;
                            break;
                        } else if (b2 != ((ByteString) list3.get(i19)).getByte(i8)) {
                            i6 = i19;
                            break;
                        } else {
                            i19++;
                        }
                    }
                    if (i18 == i6 && i8 + 1 == ((ByteString) list3.get(i17)).size()) {
                        buffer3.writeInt(((Integer) list4.get(i17)).intValue());
                        i7 = i6;
                        buffer2 = buffer4;
                    } else {
                        buffer3.writeInt((int) ((((long) intCount(buffer4)) + intCount) * -1));
                        i7 = i6;
                        buffer2 = buffer4;
                        buildTrieRecursive(intCount, buffer4, i8 + 1, list, i17, i6, list2);
                    }
                    buffer4 = buffer2;
                    i17 = i7;
                }
                Buffer buffer5 = buffer4;
                buffer3.write(buffer5, buffer5.size());
                return;
            }
            int min = Math.min(byteString.size(), byteString2.size());
            int i20 = i8;
            int i21 = 0;
            while (i20 < min && byteString.getByte(i20) == byteString2.getByte(i20)) {
                i21++;
                i20++;
            }
            long intCount2 = 1 + j + ((long) intCount(buffer)) + 2 + ((long) i21);
            buffer3.writeInt(-i21);
            buffer3.writeInt(i12);
            int i22 = i8;
            while (true) {
                i5 = i8 + i21;
                if (i22 >= i5) {
                    break;
                }
                buffer3.writeInt((int) byteString.getByte(i22) & UByte.MAX_VALUE);
                i22++;
            }
            if (i4 + 1 != i10) {
                Buffer buffer6 = new Buffer();
                buffer3.writeInt((int) ((((long) intCount(buffer6)) + intCount2) * -1));
                buildTrieRecursive(intCount2, buffer6, i5, list, i4, i3, list2);
                buffer3.write(buffer6, buffer6.size());
            } else if (i5 == ((ByteString) list3.get(i4)).size()) {
                buffer3.writeInt(((Integer) list4.get(i4)).intValue());
            } else {
                throw new AssertionError();
            }
        } else {
            throw new AssertionError();
        }
    }

    public ByteString get(int i) {
        return this.byteStrings[i];
    }

    public final int size() {
        return this.byteStrings.length;
    }

    private static int intCount(Buffer buffer) {
        return (int) (buffer.size() / 4);
    }
}
