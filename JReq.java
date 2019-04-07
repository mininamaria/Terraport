package com.example.maria.terraport;

import com.google.gson.Gson;

public class JReq {

    public Pairs[] Request;

    public class Pairs{
        public String Action;
        public String Param;
        public String PVal;

        public Pairs(String Action,String Param,String PVal){
            this.Action=Action;
            this.Param=Param;
            this.PVal=PVal;
        }
        public Pairs(){
            this.Action=null;
            this.Param=null;
            this.PVal=null;
        }
    }

    public JReq(int count){
        this.Request = new Pairs[count];
    }
    public void setPair(int num,String Action,String Param,String PVal){
        Request[num]=new Pairs(Action,Param,PVal);
    }
    public String getJSON(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static JReq parseRequest(String fromServer) {
        Gson gson = new Gson();
        JReq request;
        request=gson.fromJson(fromServer,JReq.class);
        return request;
    }
}
