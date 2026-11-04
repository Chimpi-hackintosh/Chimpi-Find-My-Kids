package com.chimpi.kids;

import android.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;

public final class SosCrypto {
 private SosCrypto(){}
 public static String encrypt(String key, JSONObject event)throws Exception{
   byte[] secret=Base64.decode(key,Base64.URL_SAFE|Base64.NO_WRAP|Base64.NO_PADDING);
   if(secret.length!=32)throw new SecurityException("Chave de pareamento inválida");
   byte[] nonce=new byte[12];new SecureRandom().nextBytes(nonce);
   Cipher c=Cipher.getInstance("AES/GCM/NoPadding");
   c.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(secret,"AES"),new GCMParameterSpec(128,nonce));
   byte[] encrypted=c.doFinal(event.toString().getBytes(StandardCharsets.UTF_8));
   byte[] packet=new byte[nonce.length+encrypted.length];
   System.arraycopy(nonce,0,packet,0,nonce.length);
   System.arraycopy(encrypted,0,packet,nonce.length,encrypted.length);
   return Base64.encodeToString(packet,Base64.URL_SAFE|Base64.NO_WRAP|Base64.NO_PADDING);
 }
}
