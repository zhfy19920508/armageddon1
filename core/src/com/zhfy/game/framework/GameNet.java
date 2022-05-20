package com.zhfy.game.framework;

import com.alibaba.fastjson.JSONObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.Json;
import com.zhfy.game.config.ResDefaultConfig;

public class GameNet {

   private Net.HttpRequest request;
   private String account;
   private String password;
    private String email;
   private String uuid;
    private int status;//网络状态 -2维护 -1无网络或寻找不到服务器 0未登陆 1已登陆


        public GameNet(){
            request = new Net.HttpRequest("POST");
            uuid="";

            request.setUrl(ResDefaultConfig.Url.defaultUrl+"/getServerStatus");
            Net.HttpResponseListener httpResponseListener = new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    if (httpResponse.getStatus().getStatusCode() == 200) {
                   //     Gdx.app.log("Log httpResponse", httpResponse.getResultAsString());
                        JSONObject json =  JSONObject.parseObject(httpResponse.getResultAsString());
                        status=json.getInteger("data");
                        if(status==1){//服务器只有两种状态 0正常 1维护
                            status=-2;
                        }
                        Gdx.app.log("init GameNet", status+"");
                        //TODO status为0,则尝试登陆
                    }else {
                        Gdx.app.error("GameNet error","请求失败, 状态码: " + httpResponse.getStatus().getStatusCode());
                    }
                }

                @Override
                public void failed(Throwable t) {
                    Gdx.app.log("net test", "failed");
                    status=-1;
                }

                @Override
                public void cancelled() {
                    Gdx.app.log("handleHttpResponse","cancel");
                    status=-1;
                }
            };
            Gdx.net.sendHttpRequest(request,httpResponseListener);

        }


        //是否在登陆状态
        public boolean isLanding(){

            return false;
        }

        public void test(){
            request.setUrl(ResDefaultConfig.Url.defaultUrl+"/test");
            request.setContent( "account=test3");
            Net.HttpResponseListener httpResponseListener = new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    Gdx.app.log("Log httpResponse test", httpResponse.getResultAsString());
                }

                @Override
                public void failed(Throwable t) {
                    Gdx.app.log("net test", "failed");  status=-1;
                }

                @Override
                public void cancelled() {
                    Gdx.app.log("handleHttpResponse","cancel");  status=-1;
                }
            };
            Gdx.net.sendHttpRequest(request,httpResponseListener);
        }

    public void createAccount(String account,String name,String password,String email){
        request.setUrl(ResDefaultConfig.Url.defaultUrl+"/createAcount");
        String str= "account="+account+"&name="+name+"&password="+password+"&email="+email+"&projectId="+ResDefaultConfig.projectId ;
        Net.HttpResponseListener httpResponseListener = new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Gdx.app.log("Log httpResponse createAccount", httpResponse.getResultAsString());
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("net test", "failed");  status=-1;
            }

            @Override
            public void cancelled() {
                Gdx.app.log("handleHttpResponse","cancel");  status=-1;
            }
        };
        Gdx.net.sendHttpRequest(request,httpResponseListener);
    }

    public void updPassword(String newPassword){
        request.setUrl(ResDefaultConfig.Url.defaultUrl+"/updPassword");
        String str= "account="+account+"&o_password="+password+"&n_password="+newPassword;
        Net.HttpResponseListener httpResponseListener = new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Gdx.app.log("Log httpResponse updPassword", httpResponse.getResultAsString());
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("net test", "failed");  status=-1;
            }

            @Override
            public void cancelled() {
                Gdx.app.log("handleHttpResponse","cancel");  status=-1;
            }
        };
        Gdx.net.sendHttpRequest(request,httpResponseListener);
    }









        //验证用户信息
    public void login(String account,String password){
        request.setUrl(ResDefaultConfig.Url.defaultUrl+"/verify");
        //  var params = 'username=' + nameValue + '&age=' + ageValue;

        //  var params = 'account=test2&password=test2&projectId=1' ;
        String str= "account="+account+"&password="+password+"&projectId="+ResDefaultConfig.projectId ;
        request.setContent(str);

        Net.HttpResponseListener httpResponseListener = new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Gdx.app.log("Log httpResponse", httpResponse.getResultAsString());
                //TODO

            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("net test", "failed");
            }

            @Override
            public void cancelled() {
                Gdx.app.log("handleHttpResponse","cancel");
            }
        };
        Gdx.net.sendHttpRequest(request,httpResponseListener);
    }


    public void getGameInfo(){
        request.setUrl(ResDefaultConfig.Url.armageddonUrl+"/getGameInfo");
        String str= "uuid="+uuid;
        Net.HttpResponseListener httpResponseListener = new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Gdx.app.log("Log httpResponse", httpResponse.getResultAsString());
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("net test", "failed");
            }

            @Override
            public void cancelled() {
                Gdx.app.log("handleHttpResponse","cancel");
            }
        };
        Gdx.net.sendHttpRequest(request,httpResponseListener);
    }

    public void createGameInfo(){
        request.setUrl(ResDefaultConfig.Url.armageddonUrl+"/createGameInfo");
        String str= "account="+account;
        Net.HttpResponseListener httpResponseListener = new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Gdx.app.log("Log httpResponse", httpResponse.getResultAsString());
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("net test", "failed");
            }

            @Override
            public void cancelled() {
                Gdx.app.log("handleHttpResponse","cancel");
            }
        };
        Gdx.net.sendHttpRequest(request,httpResponseListener);
    }

    public void updGameInfo(int updId,int value){
        request.setUrl(ResDefaultConfig.Url.armageddonUrl+"/updGameInfo");
        String str= "uuid="+uuid+ "&updId="+updId+ "&value="+value;
        Net.HttpResponseListener httpResponseListener = new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Gdx.app.log("Log httpResponse", httpResponse.getResultAsString());
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("net test", "failed");
            }

            @Override
            public void cancelled() {
                Gdx.app.log("handleHttpResponse","cancel");
            }
        };
        Gdx.net.sendHttpRequest(request,httpResponseListener);
    }

    public void useCDKEY(String cdkey){
        request.setUrl(ResDefaultConfig.Url.armageddonUrl+"/useCDKEY");
        String str= "cdkey="+cdkey+ "&uuid="+uuid;
        Net.HttpResponseListener httpResponseListener = new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Gdx.app.log("Log httpResponse", httpResponse.getResultAsString());
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("net test", "failed");
            }

            @Override
            public void cancelled() {
                Gdx.app.log("handleHttpResponse","cancel");
            }
        };
        Gdx.net.sendHttpRequest(request,httpResponseListener);
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
