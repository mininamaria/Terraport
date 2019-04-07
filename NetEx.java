package com.example.maria.terraport;
import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetEx extends AsyncTask<String, Void, Void> {
    public String URL;
    public JReq toServer;
    public JReq fromServer;
    public Boolean ready;
    public Boolean done;
    public long lastTime;
    public int sendTimeout;
    OkHttpClient client;

    public NetEx(String URL,JReq toSever,JReq fromServer,int sendTimeout)
    {
        this.URL=URL;
        this.toServer=toSever;
        this.fromServer=fromServer;
        this.sendTimeout=sendTimeout;
    }

    public NetEx(String URL,int sendTimeout)
    {
        this.URL=URL;
        this.toServer=null;
        this.fromServer=null;
        this.sendTimeout=sendTimeout;
    }
    public JReq.Pairs getPair(int num){
        return fromServer.Request[num];
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        lastTime=System.currentTimeMillis();
    }

    @Override
    protected Void doInBackground(String... strings) {
        // ждем пока пользователь подготовит нам данные и пройдет таймаут.
        while(!isCancelled()) {
            while (!ready && (System.currentTimeMillis() - lastTime) < sendTimeout) {}

            ready = false; //берем запрос в работу  - сбрасываем флаг готовности пользовательских данных.
            client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder().add("zapros", toServer.getJSON()).build();
            Request request = new Request.Builder().url(URL).post(formBody).build();
            try {
                Response response = client.newCall(request).execute();

                String serverAnswer = response.body().string();
                fromServer = JReq.parseRequest(serverAnswer);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {

            }

            lastTime=System.currentTimeMillis();
            done = true; //Говорим, что данные отправлены и получены.
        }
        return null;
    }
}
