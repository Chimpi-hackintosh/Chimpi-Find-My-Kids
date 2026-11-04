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
 title(root,"Pedir ajuda");
 button(root,"🆘 PREPARAR SMS",0xffc92b46).setOnClickListener(v->prepare());
 note(root,"IMPORTANTE: abre seu app de mensagens; é necessário conferir os destinatários e tocar em ENVIAR");
 hold=button(root,"✋ SEGURE 2s, DEPOIS SOLTE",0xff2458e7);
 hold.setOnTouchListener((v,e)->{if(e.getActionMasked()==0){pressed=SystemClock.elapsedRealtime();hold.setText("Solte para iniciar SOS");return true;}
 if(e.getActionMasked()==1){hold.setText("✋ SEGURE 2s, DEPOIS SOLTE");if(SystemClock.elapsedRealtime()-pressed>=2000&&!active)count();return true;}
 if(e.getActionMasked()==3){hold.setText("✋ SEGURE 2s, DEPOIS SOLTE");return true;}return false;});
 note(root,"Após soltar, 8 segundos para cancelar antes de ABRIR o aplicativo SMS");
 cancel=button(root,"Cancelar contagem",0xff64748b);cancel.setVisibility(View.GONE);cancel.setOnClickListener(v->stop());
 status=new TextView(this);status.setText("Nenhuma mensagem preparada");root.addView(status);
 title(root,"Polícia local • referências");
 note(root,"Locais não organizados por proximidade. Verifique os endereços oficiais");
 for(String place:new String[]{"56ª DP, Comendador Soares, Nova Iguaçu","52ª DP, Centro de Nova Iguaçu","50ª DP, Itaguaí","59ª DP, Duque de Caxias"}){
 button(root,"📍 "+place,0xff2458e7).setOnClickListener(v->new AlertDialog.Builder(this).setMessage(place+"\nVerifique o endereço antes de ir").setPositiveButton("Mapas",(d,w)->{
 try{startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("geo:0,0?q="+Uri.encode(place))));}catch(Exception ignored){}
 }).setNeutralButton("190",(d,w)->dial()).setNegativeButton("Fechar",null).show());}
 note(root,"Sem aviso automático aos parentes ou à polícia. Mensagens SMS exigem rede celular; alarmes remotos não funcionam sem conexão");
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
 if(seconds--<=0){active=false;cancel.setVisibility(View.GONE);prepare();}else h.postDelayed(this,1000);}};h.post(run);}
 void stop(){active=false;if(run!=null)h.removeCallbacks(run);if(cancel!=null)cancel.setVisibility(View.GONE);if(status!=null)status.setText("Cancelado, nenhuma mensagem enviada");}
 @Override protected void onDestroy(){stop();super.onDestroy();}
 @Override public void onRequestPermissionsResult(int c,String[] p,int[] r){super.onRequestPermissionsResult(c,p,r);
 if(c==12&&checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)gps.setChecked(false);}
}