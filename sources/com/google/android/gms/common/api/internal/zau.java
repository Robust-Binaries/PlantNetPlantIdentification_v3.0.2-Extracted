package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;

final class zau implements zabt {
    private final /* synthetic */ zas zaeq;

    private zau(zas zas) {
        this.zaeq = zas;
    }

    public final void zab(@Nullable Bundle bundle) {
        
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0006: INVOKE  (wrap: java.util.concurrent.locks.Lock
              0x0002: INVOKE  (r0v1 java.util.concurrent.locks.Lock) = (wrap: com.google.android.gms.common.api.internal.zas
              0x0000: IGET  (r0v0 com.google.android.gms.common.api.internal.zas) = (r1v0 'this' com.google.android.gms.common.api.internal.zau A[THIS]) com.google.android.gms.common.api.internal.zau.zaeq com.google.android.gms.common.api.internal.zas) com.google.android.gms.common.api.internal.zas.zaa(com.google.android.gms.common.api.internal.zas):java.util.concurrent.locks.Lock type: STATIC) java.util.concurrent.locks.Lock.lock():void type: INTERFACE in method: com.google.android.gms.common.api.internal.zau.zab(android.os.Bundle):void, dex: classes.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:245)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:213)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:109)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
            	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
            	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:210)
            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:203)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:316)
            	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:262)
            	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:225)
            	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:110)
            	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:76)
            	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:44)
            	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:32)
            	at jadx.core.codegen.CodeGen.generate(CodeGen.java:20)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
            Caused by: jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0002: INVOKE  (r0v1 java.util.concurrent.locks.Lock) = (wrap: com.google.android.gms.common.api.internal.zas
              0x0000: IGET  (r0v0 com.google.android.gms.common.api.internal.zas) = (r1v0 'this' com.google.android.gms.common.api.internal.zau A[THIS]) com.google.android.gms.common.api.internal.zau.zaeq com.google.android.gms.common.api.internal.zas) com.google.android.gms.common.api.internal.zas.zaa(com.google.android.gms.common.api.internal.zas):java.util.concurrent.locks.Lock type: STATIC in method: com.google.android.gms.common.api.internal.zau.zab(android.os.Bundle):void, dex: classes.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:245)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:105)
            	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:88)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:682)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:357)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:239)
            	... 19 more
            Caused by: java.lang.ArrayIndexOutOfBoundsException: arraycopy: length -1 is negative
            	at java.base/java.lang.System.arraycopy(Native Method)
            	at java.base/java.util.ArrayList.shiftTailOverGap(ArrayList.java:777)
            	at java.base/java.util.ArrayList.removeIf(ArrayList.java:1720)
            	at java.base/java.util.ArrayList.removeIf(ArrayList.java:1689)
            	at jadx.core.dex.instructions.args.SSAVar.removeUse(SSAVar.java:86)
            	at jadx.core.dex.instructions.args.SSAVar.use(SSAVar.java:79)
            	at jadx.core.dex.nodes.InsnNode.attachArg(InsnNode.java:87)
            	at jadx.core.dex.nodes.InsnNode.addArg(InsnNode.java:73)
            	at jadx.core.dex.nodes.InsnNode.copyCommonParams(InsnNode.java:335)
            	at jadx.core.dex.instructions.IndexInsnNode.copy(IndexInsnNode.java:24)
            	at jadx.core.dex.instructions.IndexInsnNode.copy(IndexInsnNode.java:9)
            	at jadx.core.dex.nodes.InsnNode.copyCommonParams(InsnNode.java:333)
            	at jadx.core.dex.nodes.InsnNode.copy(InsnNode.java:350)
            	at jadx.core.codegen.InsnGen.inlineMethod(InsnGen.java:880)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:669)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:357)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:223)
            	... 24 more
            */
        /*
            this = this;
            com.google.android.gms.common.api.internal.zas r0 = r1.zaeq
            java.util.concurrent.locks.Lock r0 = r0.zaeo
            r0.lock()
            com.google.android.gms.common.api.internal.zas r0 = r1.zaeq     // Catch:{ all -> 0x0024 }
            r0.zaa(r2)     // Catch:{ all -> 0x0024 }
            com.google.android.gms.common.api.internal.zas r2 = r1.zaeq     // Catch:{ all -> 0x0024 }
            com.google.android.gms.common.ConnectionResult r0 = com.google.android.gms.common.ConnectionResult.RESULT_SUCCESS     // Catch:{ all -> 0x0024 }
            r2.zael = r0     // Catch:{ all -> 0x0024 }
            com.google.android.gms.common.api.internal.zas r2 = r1.zaeq     // Catch:{ all -> 0x0024 }
            r2.zax()     // Catch:{ all -> 0x0024 }
            com.google.android.gms.common.api.internal.zas r2 = r1.zaeq
            java.util.concurrent.locks.Lock r2 = r2.zaeo
            r2.unlock()
            return
        L_0x0024:
            r2 = move-exception
            com.google.android.gms.common.api.internal.zas r0 = r1.zaeq
            java.util.concurrent.locks.Lock r0 = r0.zaeo
            r0.unlock()
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zau.zab(android.os.Bundle):void");
    }

    public final void zac(@NonNull ConnectionResult connectionResult) {
        this.zaeq.zaeo.lock();
        try {
            this.zaeq.zael = connectionResult;
            this.zaeq.zax();
        } finally {
            this.zaeq.zaeo.unlock();
        }
    }

    public final void zab(int i, boolean z) {
        this.zaeq.zaeo.lock();
        try {
            if (!this.zaeq.zaen && this.zaeq.zaem != null) {
                if (this.zaeq.zaem.isSuccess()) {
                    this.zaeq.zaen = true;
                    this.zaeq.zaeg.onConnectionSuspended(i);
                    this.zaeq.zaeo.unlock();
                    return;
                }
            }
            this.zaeq.zaen = false;
            this.zaeq.zaa(i, z);
        } finally {
            this.zaeq.zaeo.unlock();
        }
    }

    /* synthetic */ zau(zas zas, zat zat) {
        this(zas);
    }
}
