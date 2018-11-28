package com.example.cx.unlietaskdemo.core.net;

import android.util.Log;

import com.xstore.tms.android.entity.LoginedCarrier;
import com.xstore.tms.android.utils.AesUtils;
import com.xstore.tms.android.utils.LogUtils;
import com.xstore.tms.android.utils.RSAUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by wangwenming1 on 2018/7/1.
 */
public class E_DInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        boolean encryptionContent = request.url().toString().contains("&source=app");
        boolean loginContent = request.url().toString().contains("/login/newLogin?");
        if (encryptionContent) {
            RequestBody requestBody = chain.request().body();
            if (requestBody != null) {
                okio.Buffer buffer = new okio.Buffer();
                requestBody.writeTo(buffer);
                Charset charset = Charset.forName("UTF-8");
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(charset);
                }
                String encryptStr = null;
                if (charset != null) {
                    encryptStr = buffer.readString(charset);
                }
                if (!loginContent) {
                    String str = null;
                    for (int i = 0; i < 10; i++) {
                        str = LoginedCarrier.INSTANCE.getSfKeyMD5();
                        LogUtils.i("interceptor","keymd5="+str);
                        if(StringUtils.isNotBlank(str))
                            break;
                        // - 登入后立即获取用户信息时，短暂拿不到刚缓存的数据。
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if(str!=null && encryptStr != null) {
                        try {
                            encryptStr = AesUtils.aesEncrypt(encryptStr, str);
                        } catch (Exception e) {
                            Log.e("E", e.getMessage(), e);
                        }
                    }
                }
                if(encryptStr != null){
                    try {
                        encryptStr = RSAUtil.encrypt(encryptStr, RSAUtil.PUBLIC_KEY);
                    } catch (Exception e) {
                        Log.e("E", e.getMessage(), e);
                    }
                    LogUtils.i("interceptor","rquest="+encryptStr);
                    RequestBody body = MultipartBody.create(contentType, encryptStr);
                    request = chain.request().newBuilder() //.addHeader("Cookie","carrierPin=liuyuan10; token=7444e313e0934b868891b60d79a3d5c8")
                            .post(body)
                            .build();
                }else{
                    request = chain.request().newBuilder() //.addHeader("Cookie","carrierPin=liuyuan10; token=7444e313e0934b868891b60d79a3d5c8")
                            .build();
                }

            }
        }
        if (!loginContent && LoginedCarrier.INSTANCE.isLogined()) {
            request = request.newBuilder().addHeader("Cookie",
                    "carrierPin="+ LoginedCarrier.INSTANCE.getCarrierPin() +"; token=" +LoginedCarrier.INSTANCE.getToken() )
                    .addHeader("Content-Type", "application/json; charset=UTF-8")
                    .build();

            LogUtils.i("interceptor","token="+LoginedCarrier.INSTANCE.getToken());
        }
        Response response = chain.proceed(request);
        if (encryptionContent) {
            String bodyStr = response.body().string();
            try {
                bodyStr = RSAUtil.decrypt(bodyStr, RSAUtil.PUBLIC_KEY);
            } catch (Exception e) {
                Log.e("E", e.getMessage(), e);
            }
            if (!loginContent) {
                try {
                    LogUtils.i("interceptor","KeyMD5="+LoginedCarrier.INSTANCE.getSfKeyMD5());
                    bodyStr = AesUtils.aesDecrypt(bodyStr, LoginedCarrier.INSTANCE.getSfKeyMD5());
                }catch (Exception e){
                    LogUtils.e("interceptor",e.getMessage(),e);
                }

            }
            LogUtils.i("interceptor","responsebody="+bodyStr);
            ResponseBody rpb = ResponseBody.create(request.body().contentType(), bodyStr);
            response = response.newBuilder().body(rpb).build();
        }
        return response;
    }

}
