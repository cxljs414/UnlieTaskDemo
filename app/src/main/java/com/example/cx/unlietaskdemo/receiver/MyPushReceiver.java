package com.example.cx.unlietaskdemo.receiver;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.jd.push.lib.MixPushMessageReceiver;
import com.jingdong.jdpush_new.entity.JDPushMessage;
import com.xstore.tms.android.R;
import com.xstore.tms.android.core.Const;
import com.xstore.tms.android.core.MyLifecycleHandler;
import com.xstore.tms.android.core.event.EventDispatchManager;
import com.xstore.tms.android.entity.LoginedCarrier;
import com.xstore.tms.android.entity.PushMessageEntity;
import com.xstore.tms.android.ui.PushSkipActivity;
import com.xstore.tms.android.ui.homedelivery.HomeDeliveryActivity;
import com.xstore.tms.android.ui.ro.ReturnOrderActivity;
import com.xstore.tms.android.utils.JDMaUtils;
import com.xstore.tms.android.utils.LogUtils;
import com.xstore.tms.android.utils.MonitorUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class MyPushReceiver extends MixPushMessageReceiver {
    private static final String TAG = MyPushReceiver.class.getSimpleName();

    @Override
    public void onPushMessage(Context context, String msg) {
        Log.i(TAG,"onMessageArrived");
        Log.d(TAG, msg);

        JDPushMessage message = JDPushMessage.parseJson(msg);
        try {
            JSONObject jsonObject = new JSONObject(message.getMsg());
            String title = jsonObject.optString("TITLE");
            String alert = jsonObject.optString("ALERT");
            PushMessageEntity pushMessageEntity = JSON.parseObject(alert, PushMessageEntity.class);
            dispatchStrategy(context, title, pushMessageEntity);
            JSONObject json= new JSONObject();
            json.put("pin",LoginedCarrier.INSTANCE.getCarrierPin());
            json.put("msg",alert);
            JDMaUtils.sendCustomData(context,"xstore_tms_1540273172816|3","MyPushReceiver",json.toString(),"接收push消息");
        } catch (Exception e) {
            e.printStackTrace();
            MonitorUtil.INSTANCE.addMonitor(Const.FLAG_MONITEOR_LOCATION, "收到消息但解析出错",1L);
        }        //消息到达，msg是透传过来的消息数据，消息体格式参考JDPushMessage类
    }

    @Override
    public void onClickMessage(Context context, String s, int i) {

    }

    @Override
    public void onToken(Context context, String s, int i) {

    }

    private void dispatchStrategy(Context context, String title, PushMessageEntity pushMessageEntity) {
        switch (pushMessageEntity.getMsgType()){
            case 1://宅配
                EventDispatchManager.getInstance().dispatchEvent(
                        new EventDispatchManager.Event(EventDispatchManager.EventType.EVENT_HINT_HOME_DELIVERY));
                if(!MyLifecycleHandler.isApplicationInForeground()){
                    showNotification(context, title, pushMessageEntity);
                }
                break;
            case 2://取件
                EventDispatchManager.getInstance().dispatchEvent(
                        new EventDispatchManager.Event(EventDispatchManager.EventType.EVENT_HINT_RECEVING));
                if(!MyLifecycleHandler.isApplicationInForeground()){
                    showNotification(context, title, pushMessageEntity);
                }
                break;

            case 3://配送员定位推送消息，收到消息后激活实时定位并上传
                EventDispatchManager.Event event = new EventDispatchManager.Event(EventDispatchManager.EventType.EVENT_HINT_PUSHLOCATION);
                event.data = pushMessageEntity.getContent();
                EventDispatchManager.getInstance().dispatchEvent(event);

                break;
            default:
                LogUtils.i(TAG,"不处理该消息类型："+pushMessageEntity.getMsgType());
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void showNotification(Context context, String title, PushMessageEntity pushMessageEntity) {
        if (TextUtils.isEmpty(title)) {
            title = context.getApplicationInfo()
                    .loadLabel(context.getPackageManager()).toString();
        }
        String content = pushMessageEntity.getContent();
        Intent show = new Intent();
        switch (pushMessageEntity.getMsgType()) {
            //宅配
            case 1:
                show.setClass(context, HomeDeliveryActivity.class);
                break;
            //取件
            case 2:
                show.setClass(context, ReturnOrderActivity.class);
                break;
        }
        show.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建通知渠道
            NotificationChannel mChannel = new NotificationChannel("notification", "通知", NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription("这是四季骑手的通知");
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;

            notificationManager.createNotificationChannel(mChannel);
        }

        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title).setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentText(content);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("notification");
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, show, PendingIntent.FLAG_CANCEL_CURRENT);

        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        //notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notification.vibrate = new long[] {0, 1000L, 1000L, 1000L};
        RingtoneManager.getRingtone(context.getApplicationContext(),
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).play();
        assert notificationManager != null;
        notificationManager.notify(1, notification);
    }

    @NotNull
    public static Intent showIntent(@NotNull PushSkipActivity context, @NotNull String extras) {
        JDPushMessage message = JDPushMessage.parseJson(extras);
        try {
            JSONObject jsonObject = new JSONObject(message.getMsg());
            String alert = jsonObject.optString("ALERT");
            PushMessageEntity pushMessageEntity = JSON.parseObject(alert, PushMessageEntity.class);
            Intent show = new Intent();
            switch (pushMessageEntity.getMsgType()) {
                //宅配
                case 1:
                    show.setClass(context, HomeDeliveryActivity.class);
                    break;
                //取件
                case 2:
                    show.setClass(context, ReturnOrderActivity.class);
                    break;
            }
            return show;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
