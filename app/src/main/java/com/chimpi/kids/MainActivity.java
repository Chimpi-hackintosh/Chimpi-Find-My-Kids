package com.chimpi.kids;
import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.location.*;
import android.net.Uri;
import android.os.*;
import android.text.InputType;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {
 Handler h=new Handler(Looper.getMainLooper()); EditText msg; Spinner level; CheckBox gps; TextView status; Button cancel,hold; EditText[] contacts=new EditText[6];
 boolean active=false; Runnable run; int seconds; long pressed;
 final String[] kin={"Mãe","Pai","Tia","Tio","Avó","Avô"};
 void title(LinearLayout box,String s){TextView v=new TextView(this);v.setText(s);v.setTextSize(21);v.setTypeface(Typeface.DEFAULT,1);v.setPadding(0,18,0,8);box.addView(v);}
 void note(LinearLayout box,String s){TextView v=new TextView(this);v.setText(s);v.setTextSize(14);box.addView(v);}
 Button button(LinearLayout box,String s,int color){Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setTextColor(-1);b.setBackgroundTintList(ColorStateList.valueOf(color));box.addView(b);return b;}
 void dial(){try{startActivity(new Intent(Intent.ACTION_DIAL,Uri.parse("tel:190")));}catch(Exception e){Toast.makeText(this,"Ligue 190 por outro aparelho",1).show();}}
 @Override public void onCreate(Bundle state){super.onCreate(state);
 ScrollView sc=new ScrollView(this);LinearLayout root=new LinearLayout(this);root.setOrientation(1);root.setPadding(24,32,24,32);sc.addView(root);setContentView(sc);
 title(root,"🐒 Chimpi Find My Kids");note(root,"PRÉVIA EXPERIMENTAL • Não substitui atendimento de emergência");
 button(root,"📞 LIGAR 190",0xffc92b46).setOnClickListener(v->dial());
 title(root,"O que está acontecendo?");
 msg=new EditText(this);msg.setHint("Diga o que aconteceu e onde você está");msg.setMinLines(2);msg.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);root.addView(msg);
 level=new Spinner(this);level.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,new String[]{"MÍNIMO","ATENÇÃO","EXTREMO"}));root.addView(level);
 gps=new CheckBox(this);gps.setText("Incluir localização recente (opcional)");root.addView(gps);
 gps.setOnCheckedChangeListener((v,checked)->{if(checked&&checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},12);});
 title(root,"Família autorizada");
 for(int i=0;i<6;i++){EditText f=new EditText(this);f.setHint(kin[i]+" (DDD + telefone)");f.setInputType(InputType.TYPE_CLASS_PHONE);f.setSingleLine(true);f.setText(getPreferences(MODE_PRIVATE).getString("c"+i,""));contacts[i]=f;root.addView(f);}
 button(root,"💾 Salvar números",0xff2458e7).setOnClickListener(v->save());
 title(root,"Pedir ajuda");setupSmsChip(root);setupWifi(root);
 button(root,"🆘 PREPARAR SMS",0xffc92b46).setOnClickListener(v->prepare());
 note(root,"IMPORTANTE: abre seu app de mensagens; é necessário conferir os destinatários e tocar em ENVIAR");
 hold=button(root,"✋ SEGURE 2s, DEPOIS SOLTE",0xff2458e7);
 hold.setOnTouchListener((v,e)->{if(e.getActionMasked()==0){pressed=SystemClock.elapsedRealtime();hold.setText("Solte para iniciar SOS");return true;}
 if(e.getActionMasked()==1){hold.setText("✋ SEGURE 2s, DEPOIS SOLTE");if(SystemClock.elapsedRealtime()-pressed>=2000&&!active)count();return true;}
 if(e.getActionMasked()==3){hold.setText("✋ SEGURE 2s, DEPOIS SOLTE");return true;}return false;});
 note(root,"Após soltar, 8 segundos para cancelar antes de enviar pelo Wi-Fi local. Para avisar familiares distantes, use o botão SOS por chip");
 cancel=button(root,"Cancelar contagem",0xff64748b);cancel.setVisibility(View.GONE);cancel.setOnClickListener(v->stop());
 status=new TextView(this);status.setText("Nenhuma mensagem preparada");root.addView(status);
 title(root,"Polícia local • referências");
 note(root,"Locais não organizados por proximidade. Verifique os endereços oficiais");
 for(String place:new String[]{"56ª DP, Comendador Soares, Nova Iguaçu","52ª DP, Centro de Nova Iguaçu","50ª DP, Itaguaí","59ª DP, Duque de Caxias"}){
 button(root,"📍 "+place,0xff2458e7).setOnClickListener(v->new AlertDialog.Builder(this).setMessage(place+"\nVerifique o endereço antes de ir").setPositiveButton("Mapas",(d,w)->{
 try{startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("geo:0,0?q="+Uri.encode(place))));}catch(Exception ignored){}
 }).setNeutralButton("190",(d,w)->dial()).setNegativeButton("Fechar",null).show());}
 note(root,"SOS por chip: SMS para familiares cadastrados, inclusive internacionais se a operadora permitir. O envio não confirma entrega. Em perigo imediato, ligue 190");
 }
 void save(){SharedPreferences.Editor e=getPreferences(MODE_PRIVATE).edit();for(int i=0;i<6;i++)e.putString("c"+i,contacts[i].getText().toString().trim());e.apply();}
 String loc(){if(!gps.isChecked())return "Localização não compartilhada";
 if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)return "Localização não disponível";
 try{LocationManager m=(LocationManager)getSystemService(LOCATION_SERVICE);Location p=m.getLastKnownLocation(LocationManager.GPS_PROVIDER);
 if(p==null||System.currentTimeMillis()-p.getTime()>900000)return "Localização recente indisponível";
 return String.format(Locale.US,"Localização aproximada: https://maps.google.com/?q=%.6f,%.6f",p.getLatitude(),p.getLongitude());
 }catch(Exception e){return "Localização indisponível";}}
 void prepare(){save();LinkedHashSet<String> phones=new LinkedHashSet<>();
 for(EditText t:contacts){String s=t.getText().toString().replaceAll("[^+0-9]","");if(s.matches("\\+?\\d{10,13}"))phones.add(s);}
 if(phones.isEmpty()){status.setText("Cadastre um familiar. Em urgência, ligue 190");return;}
 String text=msg.getText().toString().trim();if(text.isEmpty())text="Preciso de ajuda. Entre em contato comigo";
 String body="CHIMPI-SOS | NÍVEL: "+level.getSelectedItem().toString()+"\nMensagem: "+text+"\n"+loc()+"\nEm emergência, ligue 190";
 Intent i=new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+android.text.TextUtils.join(";",phones)));i.putExtra("sms_body",body);
 try{startActivity(i);status.setText("SMS apenas PREPARADO. Confirme os números e toque Enviar");}catch(Exception e){status.setText("Sem aplicativo de SMS. Em emergência, ligue 190");}}
 void count(){active=true;seconds=8;cancel.setVisibility(View.VISIBLE);run=new Runnable(){public void run(){if(!active)return;status.setText("Abrir SMS em "+seconds+" segundos");
 if(seconds--<=0){active=false;cancel.setVisibility(View.GONE);sendWifi(false);}else h.postDelayed(this,1000);}};h.post(run);}
 void stop(){active=false;if(run!=null)h.removeCallbacks(run);if(cancel!=null)cancel.setVisibility(View.GONE);if(status!=null)status.setText("Cancelado, nenhuma mensagem enviada");}
 @Override protected void onDestroy(){stop();super.onDestroy();}
 @Override public void onRequestPermissionsResult(int c,String[] p,int[] r){super.onRequestPermissionsResult(c,p,r);
 if(c==12&&checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)gps.setChecked(false);
 if(c==82){
   boolean granted=r.length>0&&r[0]==PackageManager.PERMISSION_GRANTED;
   if(granted&&smsApprovedAfterPermission){smsApprovedAfterPermission=false;sendSmsChipApproved();}
   else{smsApprovedAfterPermission=false;status.setText("Envio por chip não autorizado. Use o SMS manual ou ligue 190");}
 }}

 // =================== EventoSOS por Wi-Fi ====================
 EditText parentIp, parentKey;
 void setupWifi(LinearLayout root){
   title(root,"📶 EventoSOS: Enviar por Wi-Fi");
   note(root,"Conecte os 2 celulares na mesma rede Wi-Fi. No Chimpi Parents, ative o recebimento e copie IP e chave segura");
   parentIp=new EditText(this);parentIp.setSingleLine(true);parentIp.setHint("IP dos responsáveis (ex: 192.168.1.10)");
   parentIp.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_URI);
   parentIp.setText(getPreferences(MODE_PRIVATE).getString("wifi_ip",""));root.addView(parentIp);
   parentKey=new EditText(this);parentKey.setSingleLine(true);parentKey.setHint("Chave de pareamento de 43 caracteres");
   parentKey.setText(getPreferences(MODE_PRIVATE).getString("wifi_secret",""));root.addView(parentKey);
   button(root,"🔌 TESTAR CONEXÃO WI-FI (sem alarme)",0xff2458e7).setOnClickListener(v->sendWifi(true));
   button(root,"🚨 ENVIAR EVENTOSOS POR WI-FI",0xffc92b46).setOnClickListener(v->sendWifi(false));
   note(root,"Confirmação do app dos responsáveis ≠ confirmação de leitura ou de que ouviram. Se não conectar, tente ligação 190 ou SMS");
 }
 void sendWifi(boolean test){
   String host=parentIp.getText().toString().trim();
   String secret=parentKey.getText().toString().trim();
   if(!host.matches("(\\d{1,3}\\.){3}\\d{1,3}")){
     status.setText("Insira o endereço IPv4 local do celular dos responsáveis");
     return;
   }
   if(secret.length()<40){
     status.setText("Digite a chave de pareamento exibida no Chimpi Parents");
     return;
   }
   final String id=java.util.UUID.randomUUID().toString();
   String message=msg.getText().toString().trim();
   if(message.isEmpty())message="Preciso de ajuda! Entre em contato comigo";
   if(message.length()>650)message=message.substring(0,650);
   final String sosMessage=message+(gps.isChecked()?"\n"+loc():"");
   final String sosLevel=level.getSelectedItem().toString();
   saveWifiPair();
   status.setText(test?"Testando Wi-Fi e pareamento...":"Enviando EventoSOS ao app dos responsáveis...");
   new Thread(()->{
     try{
       android.net.ConnectivityManager cm=(android.net.ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
       android.net.Network nw=cm.getActiveNetwork();
       android.net.NetworkCapabilities c=cm.getNetworkCapabilities(nw);
       if(c==null||!c.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI))
         throw new java.io.IOException("É necessário conectar o celular ao Wi-Fi");
       java.net.InetAddress target=java.net.InetAddress.getByName(host);
       if(!target.isSiteLocalAddress())throw new java.io.IOException("Use um IP local da rede Wi-Fi");
       org.json.JSONObject obj=new org.json.JSONObject();
       obj.put("id",id);obj.put("type",test?"TEST":"SOS");
       obj.put("time",System.currentTimeMillis());
       obj.put("level",sosLevel);
       obj.put("message",sosMessage);
       String payload=SosCrypto.encrypt(secret,obj);
       try(java.net.Socket socket=new java.net.Socket()){
         socket.connect(new java.net.InetSocketAddress(target,45881),5000);
         socket.setSoTimeout(7000);
         java.io.DataOutputStream out=new java.io.DataOutputStream(new java.io.BufferedOutputStream(socket.getOutputStream()));
         java.io.DataInputStream in=new java.io.DataInputStream(new java.io.BufferedInputStream(socket.getInputStream()));
         out.writeUTF(payload);out.flush();
         String ack=in.readUTF();
         if(!ack.equals((test?"TEST_OK:":"ACK:")+id))throw new java.io.IOException("Sem confirmação válida do responsável");
       }
       runOnUiThread(()->status.setText(test
         ?"✅ Conexão de teste confirmada pelo app dos responsáveis"
         :"✅ EventoSOS RECEBIDO pelo app dos responsáveis. A pessoa ainda precisa verificar o aviso"));
     }catch(Exception err){
       runOnUiThread(()->status.setText((test?"❌ Teste falhou":"❌ EventoSOS NÃO CONFIRMADO")
         +"\n"+err.getClass().getSimpleName()
         +"\nVerifique mesma rede, IP, chave e escuta ativa. Em perigo imediato, ligue 190 ou tente SMS"));
     }
   },"Chimpi-SOS-Sender").start();
 }


 // =================== EventoSOS celular/SIM =====================
 boolean smsApprovedAfterPermission=false;
 void setupSmsChip(LinearLayout root){
   title(root,"📱 EventoSOS por chip (Brasil e exterior)");
   note(root,"Cadastre acima os telefones familiares com DDI: Brasil +55, Reino Unido +44. Só envie a números de familiares que autorizaram o recebimento");
   note(root,"Usa SMS da operadora, não precisa de internet. Exige sinal de rede, chip/eSIM ativo e pode cobrar por mensagens internacionais ou divididas");
   button(root,"🚨 SOS PARA TIA + STATUS AOS PAIS",0xffc92b46).setOnClickListener(v->confirmSmsChip());
   note(root,"Tia/tio/avó/avô recebe o SOS completo. Mãe e pai recebem somente: familiar acionado, aguardando confirmação. Sem garantia de entrega");
 }
 java.util.ArrayList<String> validSmsRecipients(){
   java.util.LinkedHashSet<String> phones=new java.util.LinkedHashSet<>();
   for(EditText f:contacts){
     String digits=f.getText().toString().trim().replaceAll("[^0-9+]","");
     if(digits.matches("\\+\\d{10,15}")||digits.matches("\\d{10,13}"))
       phones.add(digits);
   }
   return new java.util.ArrayList<>(phones);
 }

 void confirmSmsChip(){
   save();
   String caregiver="";
   for(int index:new int[]{2,3,4,5}){
     String phone=normalizedContact(index);
     if(!phone.isEmpty()){caregiver=kin[index]+": "+phone;break;}
   }
   if(caregiver.isEmpty()){
     status.setText("Cadastre uma tia, tio, avó ou avô e tente novamente. Se for urgente, ligue 190");return;
   }
   String parentNumbers="";
   for(int index:new int[]{0,1}){
     String phone=normalizedContact(index);
     if(!phone.isEmpty())parentNumbers+=kin[index]+": "+phone+"\n";
   }
   final String names="SOS detalhado -> "+caregiver+"\n\nPais (somente status, sem detalhes):\n"
      +(parentNumbers.isEmpty()?"Nenhum pai cadastrado":parentNumbers);
   new AlertDialog.Builder(this)
     .setTitle("Enviar EventoSOS para a família?")
     .setMessage(names+"\nConfirme quem receberá. SMS para exterior pode ter custo. Recebimento e ajuda NÃO são garantidos")
     .setPositiveButton("AUTORIZAR ENVIO",(dialog,which)->{
       if(checkSelfPermission(Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){
         smsApprovedAfterPermission=true;
         requestPermissions(new String[]{Manifest.permission.SEND_SMS},82);
       }else sendSmsChipApproved();
     }).setNegativeButton("Cancelar",null).show();
 }
 String normalizedContact(int index){
   if(index<0||index>=contacts.length)return "";
   String phone=contacts[index].getText().toString().trim().replaceAll("[^0-9+]","");
   if(phone.matches("\\+\\d{10,15}")||phone.matches("\\d{10,13}"))return phone;
   return "";
 }
 // Família recebe apenas o andamento; cuidador local recebe o SOS detalhado
 void sendSmsChipApproved(){
   if(checkSelfPermission(Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){
     status.setText("Permissão de SMS não concedida. Ligue 190 se estiver em perigo");return;
   }
   int caregiver=-1;
   for(int index:new int[]{2,3,4,5}){
     if(!normalizedContact(index).isEmpty()){caregiver=index;break;}
   }
   if(caregiver<0){
     status.setText("Cadastre uma tia, tio, avó ou avô como cuidador principal. Sem isso, contate seus pais ou ligue 190");
     return;
   }
   String primary=normalizedContact(caregiver);
   java.util.LinkedHashSet<String> parentPhones=new java.util.LinkedHashSet<>();
   for(int index:new int[]{0,1}){
     String phone=normalizedContact(index);
     if(!phone.isEmpty()&&!phone.equals(primary))parentPhones.add(phone);
   }
   String incident=java.util.UUID.randomUUID().toString().substring(0,8).toUpperCase(java.util.Locale.US);
   String message=msg.getText().toString().trim();
   if(message.isEmpty())message="Preciso de ajuda! Ligue para mim";
   if(message.length()>240)message=message.substring(0,240);
   String detail="CHIMPI-SOS | NÍVEL: "+level.getSelectedItem().toString()
       +"\nID: "+incident+"\nPreciso de ajuda: "+message+"\n"+loc()
       +"\nSe houver perigo imediato, ligue 190";
   String pending="CHIMPI-STATUS | AGUARDANDO"
       +"\nID: "+incident
       +"\nUm familiar foi acionado para ajudar a criança"
       +"\nA ajuda AINDA NÃO foi confirmada"
       +"\nConfira diretamente com a criança";
   int requested=0;
   StringBuilder errors=new StringBuilder();
   try{
     android.telephony.SmsManager sms=android.telephony.SmsManager.getDefault();
     // SOS completo somente ao cuidador local
     try{
       sendSmsParts(sms,primary,detail);
       requested++;
     }catch(Exception ex){errors.append("Não foi possível encaminhar ao familiar prioritário; ");}
     // Pais recebem APENAS status, nunca o relato detalhado
     for(String destination:parentPhones){
       try{
         sendSmsParts(sms,destination,pending);requested++;
       }catch(Exception ex){errors.append("Falha ao solicitar envio para um dos pais; ");}
     }
   }catch(Exception ex){errors.append("Rede SMS indisponível ou chip não configurado");}
   status.setText("EventoSOS "+incident+"\nPedidos de SMS aceitos pelo Android: "+requested
       +" de "+(parentPhones.size()+1)
       +"\nO cuidador recebe o SOS; seus pais recebem somente o status PENDENTE"
       +"\nNenhuma entrega ou ajuda foi confirmada"
       +(errors.length()==0?"":"\n"+errors.toString())
       +"\nSe houver perigo imediato, ligue 190");
 }
 void sendSmsParts(android.telephony.SmsManager sms,String number,String body){
   java.util.ArrayList<String> parts=sms.divideMessage(body);
   if(parts==null||parts.isEmpty())throw new IllegalArgumentException("Mensagem vazia");
   if(parts.size()==1)sms.sendTextMessage(number,null,body,null,null);
   else sms.sendMultipartTextMessage(number,null,parts,null,null);
 }
 void saveWifiPair(){
   if(parentIp==null||parentKey==null)return;
   getPreferences(MODE_PRIVATE).edit().putString("wifi_ip",parentIp.getText().toString().trim())
     .putString("wifi_secret",parentKey.getText().toString().trim()).apply();
 }
 @Override protected void onPause(){saveWifiPair();super.onPause();}

}
