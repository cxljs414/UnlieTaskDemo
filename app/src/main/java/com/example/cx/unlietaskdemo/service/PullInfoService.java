package com.example.cx.unlietaskdemo.service;

/**
 * 从服务器获取取件单信息服务
 * Created by yao.zhai on 2017/7/5.
 */

//import android.support.v7.app.NotificationCompat;


public class PullInfoService /*extends Service8*/{
//    private long timeGape = 10000;
//    private String getPullInfoUrl;
//    public NotificationManager mNotificationManager;
//    private PullInfoService pullInfoService;
//    private SharedPreferences sharedPreferences;
//    private SharedPreferences cookieSharedPreferences;
//    private int notifycont=80;
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//
//
//        String time_Gape = getString(R.string.time_gape);
//        getPullInfoUrl = getString(R.string.getPullInfo_url);
//        timeGape = Long.parseLong(time_Gape);
//        pullInfoService=this;
//        Log.i("PullInfoService","拉取通知栏信息URL"+getPullInfoUrl+"  间隔时间："+time_Gape);
//        sharedPreferences = this.getSharedPreferences("common",0);//Android7.0以后因为安全限制modo应填0，只有本app可访问
//        cookieSharedPreferences = this.getSharedPreferences("cookie",0);
//        Thread thread = new Thread(new GetDate());
//        thread.start();
//        super.onCreate();
//    }
//
//    private class GetDate extends Thread{
//        public void run(){
//            PrintWriter out = null;
//            BufferedReader in = null;
//            try {
//                while (1==1){
//                    try {
//
//
//
//                        String carrierPin= cookieSharedPreferences.getString("carrierPin","");
//                        String token= cookieSharedPreferences.getString("token","");
//                        String timestamp = sharedPreferences.getString("pullInfoTimestamp","");
//                        URL url = null;
//                        String urlParam = getPullInfoUrl+"?token="+token+"&carrierPin="+carrierPin+"&timeStamp="+timestamp;
//                        url = new URL(urlParam);
//                        HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
//                        urlConnection.setRequestProperty("accept","*/*");
//                        urlConnection.setRequestProperty("connection"," keep-alive");
//                        urlConnection.setRequestProperty("user-agent",
//                                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//                        urlConnection.setRequestMethod("GET");
//                        urlConnection.setDoInput(true);
//                        urlConnection.setDoOutput(true);
//                        in = new BufferedReader(
//                                new InputStreamReader(urlConnection.getInputStream()));
//                        String line;
//                        Log.i("PullInfoService","请求接口路径："+url);
//
//                        StringBuffer stringBuffer=new StringBuffer();
//                        while ((line = in.readLine()) != null) {
//                            stringBuffer.append(line);
//                        }
//                        if(stringBuffer.length()>0){
//                            Msg msg = (Msg)JSON.parseObject(stringBuffer.toString(), Msg.class);
//                            //验证接口请求是否成功
//                            if(msg.getFlag().equals("success")){
//                                JSONObject jsonObject = JSON.parseObject(msg.getContent());
//                                String timeStamp = jsonObject.get("timeStamp").toString();
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                editor.putString("pullInfoTimestamp",timeStamp);
//                                editor.commit();
//                                List<PushMessageResult> list = JSONArray.parseArray(jsonObject.get("messageList").toString(), PushMessageResult.class);
//                                if(!list.isEmpty()){
//                                    Intent intent = new Intent(pullInfoService, MainActivity.class);
//                                    mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(pullInfoService);
//                                    mBuilder.setContentIntent(PendingIntent.getActivity(pullInfoService, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT))
//                                            .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
//                                            .setDefaults(Notification.DEFAULT_VIBRATE)
//                                            .setSmallIcon(ic_launcher_round)
//                                            .setAutoCancel(true);
//                                    for (PushMessageResult pushMessageResult:list){
//                                        mBuilder.setContentTitle(pushMessageResult.getTitle())
//                                                .setContentText(pushMessageResult.getContent());
//                                        mNotificationManager.notify(notifycont, mBuilder.build());
//                                        notifycont++;
//                                    }
//                                }
//                            }else {
//                                Log.e("PullInfoService","接口请求失败："+msg.toString());
//                            }
//                        }
//                    }catch (MalformedURLException e){
//                        Log.e("PullInfoService",e.getMessage());
//                    }catch (IOException e){
//                        Log.e("PullInfoService",e.getMessage());
//                    }catch (JSONException e){
//                        Log.e("PullInfoService",e.getMessage());
//                    }catch (Exception e){
//                        Log.e("PullInfoService",e.getMessage());
//                    }
//                    sleep(timeGape);
//                }
//            }
//            catch (InterruptedException e) {
//                Log.e("PullInfoService",e.getMessage());
//            }
//            finally {
//                try {
//                    if(out!=null){
//                        out.close();
//                    }
//                    if (in!=null){
//                        in.close();
//                    }
//                }catch (IOException e){
//                    Log.e("PullInfoService",e.getMessage());
//                }
//            }
//        }
//    }
}
