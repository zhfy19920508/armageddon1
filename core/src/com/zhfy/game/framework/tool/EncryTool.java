package com.zhfy.game.framework.tool;

import com.alibaba.fastjson.JSONObject;
import com.zhfy.game.framework.ComUtil;
import com.zhfy.game.framework.tool.base64.BASE64Decoder;
import com.zhfy.game.framework.tool.base64.BASE64Encoder;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class EncryTool {
    private final static String ENCODE = "GBK";
    private final static String DES = "DES";
    private final static int keyLengthMax=24;

    //对数据进行加密
    //type:pay,refund
    public static String encryFpayData(String key,String data) throws Exception {
        //将数据转为json验证数据有效性

        JSONObject jsonStr = JSONObject.parseObject(data);
        if(jsonStr.get("type").equals("pay")){
            if(jsonStr.get("order_amount")==null||!ComUtil.isNumeric(jsonStr.get("order_amount").toString())){
                return null;
            }
            if(jsonStr.get("product_name")==null||(jsonStr.get("product_name")).toString().length()>100){
                return null;
            }
            if(jsonStr.get("order_info")==null||(jsonStr.get("order_info")).toString().length()>100){
                return null;
            }
            if(jsonStr.get("auth_code")==null||(jsonStr.get("auth_code")).toString().length()>128){
                return null;
            }
            if(jsonStr.get("order_no")==null||(jsonStr.get("order_no")).toString().length()>30){
                return null;
            }
        }else if(jsonStr.get("type").equals("refund")){
            if(jsonStr.get("order_no")==null||(jsonStr.get("order_no")).toString().length()>30){
                return null;
            }
            if(jsonStr.get("ref_desc")==null||(jsonStr.get("ref_desc")).toString().length()>30){
                return null;
            }
        }else if(jsonStr.get("type").equals("result")){

        }else{
            return null;
        }
        return encrypt(data, key);

    }

    //对数据进行解密
    public static String decrypFpayData(String key,String data) throws Exception {
        return decrypt(data,key);
    }



    /**
     * Description 根据键值进行加密
     * @param data 待加密数据
     * @param key 密钥
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes(ENCODE), key.getBytes(ENCODE));
        String strs = new BASE64Encoder().encode(bt);
        return strs;
    }

    /**
     * 根据键值进行解密
     * @param data 待解密数据
     * @param key    密钥
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws IOException,
            Exception {
        if (data == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decrypt(buf, key.getBytes(ENCODE));
        return new String(bt, ENCODE);
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key
     *            加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        key=extendKey(key);

        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

    public static byte[] extendKey(byte[] key) {
        //System.out.println("length:"+key.length);
        byte[] tmpKey = new byte[keyLengthMax];
        if (key.length < keyLengthMax) { // short key ? .. extend to 24 byte key
            int i;int iMax=(int)(keyLengthMax/key.length);int iRem=(int)(keyLengthMax%key.length);
            System.arraycopy(key, 0, tmpKey, 0,iRem+1);
            //System.out.println(" begI:"+0+" endI:"+(iRem));
            for(i=0;i<iMax;i++) {
                //System.out.println("i:"+i+" begI:"+(i*key.length+iRem)+" endI:"+(i*key.length+iRem+key.length));
                //System.out.println("keyL:"+key.length+" temL:"+tmpKey.length);
                System.arraycopy(key, 0, tmpKey, i*key.length+iRem, key.length);
            }
        }else {
            System.arraycopy(key, 0, tmpKey, 0,keyLengthMax);
        }
        return tmpKey;
    }


    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key 加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        key=extendKey(key);
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

    public static void main(String[] args) throws Exception {
        /*String data = "12AUism810jsqASI08";
        String key ="qwerrewq";
        System.out.println("加密前===>"+data);
        try {
            //System.err.println(encrypt(data, key));
            //System.err.println(decrypt(encrypt(data, key), key));
            String jiamihou = encrypt(data,key);
            System.out.println("加密后===>"+jiamihou);
            System.out.println("解密后===>"+decrypt(jiamihou,key));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        /*String temp;
        // temp="{'order_amount':'1','product_name':'测试','order_info':'测试加解密','auth_code':'285192674239050687','order_no':'201905140953','type':'pay'}";
        temp="{'order_no':'TY_YH_ali_1557814483393_61','ref_desc':'测试退款退款','type':'refund'}";
        String key="544646";

        String str1=null;
        String str2=null;
        try {
            str1= EncryTool.encryFpayData(key,temp);
            str2= EncryTool.decrypFpayData(key,str1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonStr = JSONObject.parseObject(str2);
        System.out.println("str1:"+str1);
        System.out.println("str1转译:"+ URLEncoder.encode(str1));
        System.out.println("str2:"+str2);
        System.out.println("type:"+jsonStr.get("type"));*/

        String key="naCO0oS4NPyz7wtdBtyb3Q==";
        String code=decrypFpayData(ComUtil.getTime()+"",key);
        System.out.println("test code:"+code);
    }

    //将结果对特殊字符转译
}
