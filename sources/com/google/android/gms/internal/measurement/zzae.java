package com.google.android.gms.internal.measurement;

import android.os.RemoteException;

final class zzae extends zza {
    private final /* synthetic */ String val$id;
    private final /* synthetic */ zzaa zzar;

    zzae(zzaa zzaa, String str) {
        this.zzar = zzaa;
        this.val$id = str;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x000a: INVOKE  (wrap: com.google.android.gms.internal.measurement.zzn
              0x0002: INVOKE  (r0v1 com.google.android.gms.internal.measurement.zzn) = (wrap: com.google.android.gms.internal.measurement.zzaa
              0x0000: IGET  (r0v0 com.google.android.gms.internal.measurement.zzaa) = (r4v0 'this' com.google.android.gms.internal.measurement.zzae A[THIS]) com.google.android.gms.internal.measurement.zzae.zzar com.google.android.gms.internal.measurement.zzaa) com.google.android.gms.internal.measurement.zzaa.zzc(com.google.android.gms.internal.measurement.zzaa):com.google.android.gms.internal.measurement.zzn type: STATIC), (wrap: java.lang.String
              0x0006: IGET  (r1v0 java.lang.String) = (r4v0 'this' com.google.android.gms.internal.measurement.zzae A[THIS]) com.google.android.gms.internal.measurement.zzae.val$id java.lang.String), (wrap: long
              0x0008: IGET  (r2v0 long) = (r4v0 'this' com.google.android.gms.internal.measurement.zzae A[THIS]) com.google.android.gms.internal.measurement.zzae.timestamp long) com.google.android.gms.internal.measurement.zzn.setUserId(java.lang.String, long):void type: INTERFACE in method: com.google.android.gms.internal.measurement.zzae.zzl():void, dex: classes.dex
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
            Caused by: jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0002: INVOKE  (r0v1 com.google.android.gms.internal.measurement.zzn) = (wrap: com.google.android.gms.internal.measurement.zzaa
              0x0000: IGET  (r0v0 com.google.android.gms.internal.measurement.zzaa) = (r4v0 'this' com.google.android.gms.internal.measurement.zzae A[THIS]) com.google.android.gms.internal.measurement.zzae.zzar com.google.android.gms.internal.measurement.zzaa) com.google.android.gms.internal.measurement.zzaa.zzc(com.google.android.gms.internal.measurement.zzaa):com.google.android.gms.internal.measurement.zzn type: STATIC in method: com.google.android.gms.internal.measurement.zzae.zzl():void, dex: classes.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:245)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:105)
            	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:88)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:682)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:357)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:239)
            	... 19 more
            Caused by: java.util.ConcurrentModificationException
            	at java.base/java.util.ArrayList.removeIf(ArrayList.java:1714)
            	at java.base/java.util.ArrayList.removeIf(ArrayList.java:1689)
            	at jadx.core.dex.instructions.args.SSAVar.removeUse(SSAVar.java:86)
            	at jadx.core.utils.InsnRemover.unbindArgUsage(InsnRemover.java:90)
            	at jadx.core.dex.nodes.InsnNode.replaceArg(InsnNode.java:130)
            	at jadx.core.dex.nodes.InsnNode.replaceArg(InsnNode.java:134)
            	at jadx.core.codegen.InsnGen.inlineMethod(InsnGen.java:892)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:669)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:357)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:223)
            	... 24 more
            */
        /*
            this = this;
            com.google.android.gms.internal.measurement.zzaa r0 = r4.zzar
            com.google.android.gms.internal.measurement.zzn r0 = r0.zzan
            java.lang.String r1 = r4.val$id
            long r2 = r4.timestamp
            r0.setUserId(r1, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzae.zzl():void");
    }
}
