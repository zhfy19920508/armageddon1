package com.zhfy.game.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.IntArray;
import com.zhfy.game.config.ResDefaultConfig;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class ComUtil {
   // static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");


    //test rule="({0}+{1})/{2}+{1}" min="0" max="100"
    public static int getIntergerValueByRuleStr(String ruleStr,int min,int max,int... args){


        for(int i=0;i<args.length;i++){
            String str0="\\{"+i+"\\}"   ;
            str0=  str0.replaceAll("\\\\","");
            ruleStr= ruleStr.replace(str0,args[i]+"");
        }
      //  ruleStr="parseInt("+ruleStr+")";
        int v=-1;
        try {
          //  Object result = jse.eval(ruleStr);
           // v=ComUtil.transForInt(result,min);
            v=getResult(ruleStr,-1);
        } catch (Exception e) {
            if(ResDefaultConfig.ifDebug){
                Gdx.app.error("getIntergerValueByRuleStr",ruleStr);
                e.printStackTrace();
            }else {
             v=-1;
            }
        }
        return limitValue(v,min,max);
    }

    public static int getResult(String expr,int defaultValue) throws Exception {
        try {
            System.out.println("计算"+expr);
            /*数字栈*/
            Stack<BigDecimal> number = new Stack<BigDecimal>();
            /*符号栈*/
            Stack<String> operator = new Stack<String>();
            operator.push(null);// 在栈顶压人一个null，配合它的优先级，目的是减少下面程序的判断

            /* 将expr打散为运算数和运算符 */
            Pattern p = Pattern.compile("(?<!\\d)-?\\d+(\\.\\d+)?|[+\\-*/()]");// 这个正则为匹配表达式中的数字或运算符
            Matcher m = p.matcher(expr);
            while(m.find()) {
                String temp = m.group();
                if(temp.matches("[+\\-*/()]")) {//遇到符号
                    if(temp.equals("(")) {//遇到左括号，直接入符号栈
                        operator.push(temp);
                     //   System.out.println("符号栈更新："+operator);
                    }else if(temp.equals(")")){//遇到右括号，"符号栈弹栈取栈顶符号b，数字栈弹栈取栈顶数字a1，数字栈弹栈取栈顶数字a2，计算a2 b a1 ,将结果压入数字栈"，重复引号步骤至取栈顶为左括号，将左括号弹出
                        String b = null;
                        while(!(b = operator.pop()).equals("(")) {
                          //  System.out.println("符号栈更新："+operator);
                            BigDecimal a1 = number.pop();
                            BigDecimal a2 = number.pop();
                          //  System.out.println("数字栈更新："+number);
                          //  System.out.println("计算"+a2+b+a1);
                            number.push(bigDecimalCal(a2, a1, b.charAt(0)));
                        //    System.out.println("数字栈更新："+number);
                        }
                        System.out.println("符号栈更新："+operator);
                    }else {//遇到运算符，满足该运算符的优先级大于栈顶元素的优先级压栈；否则计算后压栈
                        while(getPriority(temp) <= getPriority(operator.peek())) {
                            BigDecimal a1 = number.pop();
                            BigDecimal a2 = number.pop();
                            String b = operator.pop();
                        //    System.out.println("符号栈更新："+operator);
                       //     System.out.println("数字栈更新："+number);
                      //      System.out.println("计算"+a2+b+a1);
                            number.push(bigDecimalCal(a2, a1, b.charAt(0)));
                     //       System.out.println("数字栈更新："+number);
                        }
                        operator.push(temp);
                     //   System.out.println("符号栈更新："+operator);
                    }
                }else {//遇到数字，直接压入数字栈
                    number.push(new BigDecimal(temp));
                  //  System.out.println("数字栈更新："+number);
                }
            }

            while(operator.peek()!=null) {//遍历结束后，符号栈数字栈依次弹栈计算，并将结果压入数字栈
                BigDecimal a1 = number.pop();
                BigDecimal a2 = number.pop();
                String b = operator.pop();
                //System.out.println("符号栈更新："+operator);
               // System.out.println("数字栈更新："+number);
               // System.out.println("计算"+a2+b+a1);
                number.push(bigDecimalCal(a2, a1, b.charAt(0)));
               // System.out.println("数字栈更新："+number);
            }
            return number.pop().intValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }



    //四则运算方法
    public static BigDecimal bigDecimalCal(BigDecimal a1, BigDecimal a2, char operator) throws Exception {
        switch (operator) {
            case '+':
                return a1.add(a2);
            case '-':
                return a1.subtract(a2);
            case '*':
                return a1.multiply(a2);
            case '/':
                return a1.divide(a2,5,BigDecimal.ROUND_HALF_UP);//防止小数出现无限循环的情况下，取5位小数
            default:
                break;
        }
        throw new Exception("illegal operator!");
    }

    //获得优先级
    private static int getPriority(String s) throws Exception {
        if(s==null) return 0;
        switch(s) {
            case "(":return 1;
            case "+":;
            case "-":return 2;
            case "*":;
            case "/":return 3;
            default:break;
        }
        throw new Exception("illegal operator!");
    }

    //结果转为int
    private static int transForInt(Object result,int defaultValue) {
        if (result instanceof Integer) {

           return Integer.parseInt(result.toString());

        }
        if (result instanceof Double) {
            return (int) Double.parseDouble(result.toString());
        }
        return defaultValue;
    }


    //本工具类主要涉及通常使用的工具类
    private ComUtil() {
        throw new IllegalStateException("ComUtil class");
    }

    public static int getCharacterPosition(String url, String s, int i) {
        //这里是获取"/"符号的位置 lastindexof从字符串末尾开始检索，检索到子字符
        Matcher slashMatcher = Pattern.compile(s).matcher(url);
        int mIdx = 0;
        while (slashMatcher.find()) {
            mIdx++;
            //当"/"符号第i次出现的位置
            if (mIdx == i) {
                break;
            }
        }
        int rs;
        try {
            rs = slashMatcher.start();
        } catch (Exception e) {
            rs = -1;
        }
        return rs;
    }

    //对象转Map
    public static Map JBeanToMap(Object obj) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        if (obj == null)
            return null;
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (int i = 0, iMax = fields.length; i < iMax; i++) {
                try {
                    Field f = obj.getClass().getDeclaredField(fields[i].getName());
                    f.setAccessible(true);
                    Object o = f.get(obj);
                    reMap.put(fields[i].getName(), o);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return reMap;
    }

    //获得符号 如果是正数,返回+v,否则就是 -v
    public static String getSymbolNumer(int value){
        if(value>=0){
            return "+"+value;
        }
        return ""+value;
    }

    //示例 setVal(obj,"setUpdateUser","修改人"); 给对象赋值
    public static void setVal(Object obj, String methodName, Object value) {
        if (value == null || obj == null) {
            return;
        }
        //MethodAccess access = MethodAccess.get(obj.getClass());
        //access.invoke(obj, methodName, value);


        String method_name = methodName;
        Method[] methods = obj.getClass().getMethods();
        for (Method method : methods) {
            /**
             * 因为这里只是调用bean中属性的set方法，属性名称不能重复 所以set方法也不会重复，所以就直接用方法名称去锁定一个方法
             * （注：在java中，锁定一个方法的条件是方法名及参数）
             **/
            if (method.getName().equals(method_name)) {
                Class[] parameterC = method.getParameterTypes();
                try {
                    /**
                     * 如果是基本数据类型时（如int、float、double、byte、char、boolean）
                     * 需要先将Object转换成相应的封装类之后再转换成对应的基本数据类型 否则会报
                     * ClassCastException
                     **/
                    if (parameterC[0] == int.class) {
                        method.invoke(obj, ((Integer) value).intValue());
                        break;
                    } else if (parameterC[0] == float.class) {
                        method.invoke(obj, ((Float) value).floatValue());
                        break;
                    } else if (parameterC[0] == double.class) {
                        method.invoke(obj, ((Double) value).doubleValue());
                        break;
                    } else if (parameterC[0] == byte.class) {
                        method.invoke(obj, ((Byte) value).byteValue());
                        break;
                    } else if (parameterC[0] == char.class) {
                        method.invoke(obj, ((Character) value).charValue());
                        break;
                    } else if (parameterC[0] == boolean.class) {
                        method.invoke(obj, ((Boolean) value).booleanValue());
                        break;
                    } else {
                        method.invoke(obj, parameterC[0].cast(value));
                        break;
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //map转对象
    public static Object mapToJBean(Class<?> clazz, Map<String, Object> map) throws Exception {
        Object javabean = clazz.newInstance(); // 构建对象
        Method[] methods = clazz.getMethods(); // 获取所有方法
        //MethodAccess access = MethodAccess.get(clazz);
        for (Method method : methods) {
            if (method.getName().startsWith("set")) {
                String field = method.getName(); // 截取属性名
                field = field.substring(field.indexOf("set") + 3);
                field = field.toLowerCase().charAt(0) + field.substring(1);
                if (map.containsKey(field)) {
                    method.invoke(javabean, map.get(field));
                    //access.invoke(javabean, method.getName(), map.get(field));
                }
            }
        }
        return javabean;
    }

    //将第一个字母大写
    public static String UpperInitial(String str) {
        if (str != null && str != "") {
            str = str.substring(0, 1).toUpperCase() + str.substring(1);
        }
        return str;
    }

    //List 集合去除null元素
    public static <T> List<T> removeNull(List<? extends T> oldList) {
        // 你没有看错，真的是有 1 行代码就实现了
        oldList.removeAll(Collections.singleton(null));
        return (List<T>) oldList;
    }


    //获取单个对象的值
    public static Map<String, Object> getKeyAndValue(Object obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 得到类对象
        Class userCla = (Class) obj.getClass();
        /* 得到类中的所有属性集合 */
        Field[] fs = userCla.getDeclaredFields();
        for (int i = 0, iMax = fs.length; i < iMax; i++) {
            Field f = fs[i];
            f.setAccessible(true); // 设置些属性是可以访问的
            Object val = new Object();
            try {
                val = f.get(obj);
                // 得到此属性的值
                if (val != null) {
                    map.put(f.getName(), val);// 设置键值
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("单个对象的所有键值==反射==" + map.toString());
        return map;
    }

    /**
     * 计算字符串的显示长度，半角算１个长度，全角算两个长度
     * @param
     * @return
     */
    public static int computeDisplayLen( String str ) {
        if(str==null || str.length()<0){
            return 0;
        }
        int len=0;
        char c;
        for(int i=str.length()-1;i>=0;i--){
            c=str.charAt(i);
            if (c > 255) {
                /*
                 * GBK 编码格式 中文占两个字节
                 * UTF-8 编码格式中文占三个字节 len += 3;
                 */
                len += 2;
            } else {
                len++;
            }
        }
        return len;
    }

    /*
     * 处理字符串，进行前后补位 resultString ,表原字符串 length，处理后要求长度 flag，1表示前面增加，0表示后增加
     * str1,要补位的字符串
     */
    public static String formmatString(String str, int length,  boolean ifLeft, String str1) {
       // int num = computeDisplayLen(str);
       // int numj=computeDisplayLen(str1);
        int num = str.getBytes().length;
        int numj=str1.getBytes().length;
        if (num == length) {
            return str;
        } else if (num< length) {
            for (int i = 0, iMax = length - num; i < iMax; i+=numj) {
                if (ifLeft) {
                    str = str1 + str;
                } else {
                    str = str + str1;
                }
            }
            Gdx.app.log("formmatString:"+ num,str);
            return str;
        } else {
            return str.substring(0, length);
        }
    }

    public static String formmatNumber(int value, int length, boolean ifLeft, String str1) {
        String str = value + "";
        if (str.length() == length) {
            return str;
        } else if (str.length() < length) {
            for (int i = 0, iMax = length - str.length(); i < iMax; i++) {
                if (ifLeft) {
                    str = str1 + str;
                } else {
                    str = str + str1;
                }
            }
            return str;
        } else {
            return str.substring(0, length);
        }
    }


    //使用反射获取list大小
    public static int getArrayListCapacity(ArrayList<?> arrayList) {
        Class<ArrayList> arrayListClass = ArrayList.class;
        try {
            //获取 elementData 字段
            Field field = arrayListClass.getDeclaredField("elementData");
            //开始访问权限
            field.setAccessible(true);
            //把示例传入get，获取实例字段elementData的值
            Object[] objects = (Object[]) field.get(arrayList);
            //返回当前ArrayList实例的容量值
            return objects.length;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean isEmpty(CharSequence cs) {
        return (cs == null) || (cs.length() == 0);
    }

    //清空右边
    public static String rightTrim(String str) {
        if (str == null) {
            return "";
        }
        int length = str.length();
        for (int i = length - 1; i >= 0; i--) {
            if (str.charAt(i) != 0x20) {
                break;
            }
            length--;
        }
        return str.substring(0, length);
    }

    //将首字母大写
    public static String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    //获得list出现最多的数据
    public static String getListMostRepeatData(List<?> l) {
        String regex;
        Pattern p;
        Matcher m;
        String tmp = "";
        String tot_str = l.toString();
        int max_cnt = 0;
        String max_str = "";
        for (Object str : l) {
            if (tmp.equals(str)) continue;
            tmp = str.toString();
            regex = str.toString();
            p = Pattern.compile(regex);
            m = p.matcher(tot_str);
            int cnt = 0;
            while (m.find()) {
                cnt++;
            }
            //System.out.println(str + ":" + cnt);
            if (cnt > max_cnt) {
                max_cnt = cnt;
                max_str = str.toString();
            }
        }
        //System.out.println(" 出现的最大次数的字符串是 " + max_str) ;
        return max_str;
    }

    //list值交换位置
    public static void swap(List<?> list, int i, int j) {
        final List l = list;
        l.set(i, l.set(j, l.get(i)));
    }

   /* public static void swap(Object a,Object b) {
        Object c=a;
        a=b;
        b=c;
    }
*/

    //遍历获取map的key值
    public static IntArray getKeyByMap(Map map) {
        IntArray rs = new IntArray();
        for (Object key : map.keySet()) {
            rs.add(Integer.parseInt(key.toString()));
        }
        return rs;
    }

    //计算概率 100为100%出现 //例如20 s:1000 y:220 n:780
    public static boolean ifGet(int chance) {
        if (chance <= 0) {
            return false;
        } else if (chance >= 100) {
            return true;
        }
        return (chance > ((int) (Math.random() * (100 - 0) + 0)));
    }

    public static int getRandomForXIsMinValue(int x, int y) {
        if (x > y) {
            x = y - 1;
        }
        return getRandom(x, y);
    }


    //获得两个整数范围之间的随机数 左闭右闭 [x,y]  https://blog.csdn.net/dj741/article/details/69666661
    public static int getRandom(int x, int y) {
        return x + (int) (Math.random() * (y - x + 1));
    }

    //只能是 "v1,v2"或 "v1" 这种格式
    public static int getRandomByStr(String str) {
        if(isNumeric(str)){
            return Integer.parseInt(str);
        }
        String[] strs = str.split(",");
        int v1=Integer.parseInt(strs[0]);
        if(strs.length!=2){
            return v1;
        }

        int v2=Integer.parseInt(strs[1]);
        return getRandom(v1,v2);
    }


    //list copy
    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    /**
     * 判断字符串是否为数字(包含负数)
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Boolean flag = false;
        String tmp;
        if(!isEmpty(str)){
            if(str.startsWith("-")){
                tmp = str.substring(1);
            }else{
                tmp = str;
            }
            flag = tmp.matches("^[0.0-9.0]+$");
        }
        return flag;
    }
    private static boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static byte[] File2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static void byte2File(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //从一堆list中获取一列
    public static <T> List<Object> listToList(Collection<T> list, String fieldName) throws Exception {
        List<Object> ret = new ArrayList();
        List<String> getStrs = null;
        List<Method> getMethods = new ArrayList<Method>();
        for (T t : list) {
            if (getStrs == null) {
                getStrs = new ArrayList<String>();
                for (String s : fieldName.split("\\.")) {
                    getStrs.add("get" + s.substring(0, 1).toUpperCase() + s.substring(1));
                }
            }
            Object value = t;
            for (int i = 0, iMax = getStrs.size(); i < iMax; i++) {

                if (getMethods == null || getMethods.size() <= i || getMethods.get(i) == null) {
                    getMethods.add(value.getClass().getDeclaredMethod(getStrs.get(i)));
                }
                value = getMethods.get(i).invoke(value);
            }
            ret.add(value);
        }
        return ret;
    }

    //List<Integer> 去重
    public static List<Integer> getNewList(List<Integer> list) {
        if (null == list || list.size() < 2) return list;
        Set<Integer> set = new HashSet<Integer>();
        List<Integer> newList = new ArrayList<Integer>();
        for (Integer i : list) {
            if (set.add(i)) {
                newList.add(i);
            }
        }
        list = null;
        return newList;
    }

    //判断a是否完全包含b
    public static boolean ifListContainList(List<Object> a, List<Object> b) {
        boolean rs = true;
        for (Object i : b) {
            if (!a.contains(i)) {
                rs = false;
            }
        }
        return rs;
    }

    public static boolean ifListContainListByInteger(List<Integer> a, List<Integer> b) {
        boolean rs = true;
        for (int i : b) {
            if (!a.contains(i)) {
                rs = false;
            }
        }
        return rs;
    }

    //随机从list中取一个  regionGrid.get(ComUtil.getOneByListRand(regionGrid))
    public static Object getOneByListRand(List list) {
        if (list.size() < 1) {
            return -1;
        }
        return list.get(getRandom(0, list.size() - 1));
    }

    public static int getOneByArrayRand(IntArray list) {
        if (list.size < 1) {
            return -1;
        }
        return list.get(getRandom(0, list.size - 1));
    }


    //返回的是角标 /随机从list中取一个
    public static int getOneIndexByListRand(List list) {
        if (list.size() < 1) {
            return -1;
        }
        return getRandom(0, list.size() - 1);
    }

    //随机从list中取n个
    public static List<Integer> getNByListRand(List list, int n) {
        List<Integer> rs = new ArrayList<Integer>();
        int c;
        for (int i = 0; i < n; ) {
            c = getRandom(0, list.size() - 1);
            if (!rs.contains(list.get(c))) {
                rs.add((Integer) list.get(c));
                i++;
            }
        }
        return rs;
    }

    //list去重添加 返回true说明添加成功,否则失败
    //LinkedHashSet<Object> set 使用mainGame的set,防止过多重复
    public static boolean listAddNoDup(List list, Object o, LinkedHashSet<Object> set) {
        int s = list.size();

        if (o instanceof List) {
            list.addAll((List) o);
        } else {
            list.add(o);
        }
        set.clear();
        //LinkedHashSet<Object> set=new LinkedHashSet<Object>(list.size());
        set.addAll(list);
        list.clear();
        list.addAll(set);
        if (list.size() == s) {
            return false;
        } else {
            return true;
        }

    }




    //获得距离环状数据的中心值
    public static int getRingValue(int ringMax, int vA, int vB) {
        int halfRing = ringMax / 2;
        int rs = 0;
        if (vA > halfRing && vB < halfRing) {
            //a:120  b:15  rs=15+180-120=75
            rs = vB + halfRing - vA;
        } else if (vA < halfRing && vB > halfRing) {
            //a:10  b:130  rs=10+180-130=60
            rs = vA + halfRing - vB;
        } else {
            rs = Math.abs(vA - vB);
        }
        return rs;
    }

    public static int limitValue(int v, int min, int max) {
        if (v >= max) {
            v = max;
        }
        if (v <= min) {
            v = min;
        }
        return v;
    }

    public static float limitFValue(float v, float min, float max) {
        if (v >= max) {
            v = max;
        } else if (v <= min) {
            v = min;
        }
        return v;
    }


    public static int max(int a, int b) {
        return a >= b ? a : b;
    }

    public static int min(int a, int b) {
        return a <= b ? a : b;
    }

    public static float max(float a, float b) {
        return a >= b ? a : b;
    }

    public static float min(float a, float b) {
        return a <= b ? a : b;
    }

    //获得两点距离
    public static double getDistance(float p1_x, float p1_y, float p2_x, float p2_y) {
        return Math.sqrt(Math.abs((p1_x - p2_x) * (p1_x - p2_x) + (p1_y - p2_y) * (p1_y - p2_y)));
    }

    //弧度
    public static double getAngle(float p1_x, float p1_y, float p2_x, float p2_y) {
        return Math.atan2((p2_x - p1_x), (p2_y - p1_y));

    }

    //角度
    public static double getRadian(double degree) {
        return degree * (180 / Math.PI);
    }

    //角度  注意 p1_y,p2_y 要求是上面为原点,不要搞错
    public static double getRadian(float p1_x, float p1_y, float p2_x, float p2_y) {
        //-90~90
        return Math.atan2((p2_x - p1_x), (p2_y - p1_y)) * (180 / Math.PI);
    }

    public static String getRandOne(String[] rs) {
        int index = (int) (Math.random() * rs.length);
        return rs[index];

    }


    public static int getRandOne(String rsS) {
        String[] rs = rsS.split(",");
        int index = (int) (Math.random() * rs.length);
        if (isNumeric(rs[index])) {
            return Integer.parseInt(rs[index]);
        } else {
            return 0;
        }
    }

    public static int getRandOneIndex(String rsS) {
        String[] rs = rsS.split(",");
        int index = (int) (Math.random() * rs.length);
        if (isNumeric(rs[index])) {
            return index;
        } else {
            return -1;
        }
    }

    public static int getValueByStr(String rsS, int index) {
        String[] rs = rsS.split(",");
        //int index = (int) (Math.random() * rs.length);
        if (isNumeric(rs[index])) {
            return Integer.parseInt(rs[index]);
        } else {
            return 0;
        }
    }

    public static int getRandOne(int[] rs) {
        int index = (int) (Math.random() * rs.length);
        return rs[index];
    }





    //判断两个数区间是否重合
    //sx 开始的数 ex截止的数
    public static boolean ifIntervalOverlap(int sx1, int ex1, int sx2, int ex2) {
        if (Math.max(sx1, sx2) <= Math.min(ex1, ex2)) {
            return true;
        }
        return false;
    }

    //获得指定区间的和
    public static int getIntervalSum(int min, int max) {
        int v = 0;
        for (int i = min; i <= max; i++) {
            v += i;
        }
        return v;
    }

    //ifHave true:>=,<= false:>,<
    public static boolean ifValueBetween(int v, int m1, int m2, boolean ifHave) {
        if (ifHave) {
            if (v >= m1 && v <= m2) {
                return true;
            } else if (v >= m2 && v <= m1) {
                return true;
            } else {
                return false;
            }
        } else {
            if (v > m1 && v < m2) {
                return true;
            } else if (v > m2 && v < m1) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static String getSpace(int length) {
        switch (length) {
            case 1:
                return ResDefaultConfig.StringName.space1;
            case 2:
                return ResDefaultConfig.StringName.space2;
            case 3:
                return ResDefaultConfig.StringName.space3;
            case 4:
                return ResDefaultConfig.StringName.space4;
            case 5:
                return ResDefaultConfig.StringName.space5;
            case 6:
                return ResDefaultConfig.StringName.space6;
            case 7:
                return ResDefaultConfig.StringName.space7;
            case 8:
                return ResDefaultConfig.StringName.space8;
            case 9:
                return ResDefaultConfig.StringName.space9;
            case 10:
                return ResDefaultConfig.StringName.space10;
            case 11:
                return ResDefaultConfig.StringName.space11;
            case 12:
                return ResDefaultConfig.StringName.space12;
            case 13:
                return ResDefaultConfig.StringName.space13;
            case 14:
                return ResDefaultConfig.StringName.space14;
            case 15:
                return ResDefaultConfig.StringName.space15;
            case 16:
                return ResDefaultConfig.StringName.space16;
            case 17:
                return ResDefaultConfig.StringName.space17;
            case 18:
                return ResDefaultConfig.StringName.space18;
            case 19:
                return ResDefaultConfig.StringName.space19;
            case 20:
                return ResDefaultConfig.StringName.space20;
            case 21:
                return ResDefaultConfig.StringName.space21;
            case 22:
                return ResDefaultConfig.StringName.space22;
            case 23:
                return ResDefaultConfig.StringName.space23;
            case 24:
                return ResDefaultConfig.StringName.space24;
            case 25:
                return ResDefaultConfig.StringName.space25;
            case 26:
                return ResDefaultConfig.StringName.space26;
            case 27:
                return ResDefaultConfig.StringName.space27;
            case 28:
                return ResDefaultConfig.StringName.space28;
            case 29:
                return ResDefaultConfig.StringName.space29;
            case 30:
                return ResDefaultConfig.StringName.space30;
            default:
                Gdx.app.error("no length space", length + "");
        }
        return ResDefaultConfig.StringName.space1;
    }


    //判断在用","分割的字符串中是否包含某个值
    public static boolean ifHaveValueInStr(String str, int v) {
        if(isEmpty(str)){
            return false;
        }
        if(isNumeric(str)&&Integer.parseInt(str)==v){
            return true;
        }
        String[] strs = str.split(",");
        for (int i = 0; i < strs.length; i++) {
            if (ComUtil.isNumeric(strs[i]) && Integer.parseInt(strs[i]) == v) {
                return true;
            }
        }
        //Gdx.app.log("no value",v+":"+str.toString());
        return false;
    }
    public static boolean ifHaveValueInStrArrays(String[] strs, int v) {
        for (int i = 0; i < strs.length; i++) {
            if (ComUtil.isNumeric(strs[i]) && Integer.parseInt(strs[i]) == v) {
                return true;
            }
        }
        //Gdx.app.log("no value",v+":"+str.toString());
        return false;
    }

    public static boolean ifHaveValueIntegerArrays(int[] vs, int v) {
        if(vs==null||vs.length==0){
            return false;
        }
        for (int i = 0; i < vs.length; i++) {
            if (vs[i]== v) {
                return true;
            }
        }
        //Gdx.app.log("no value",v+":"+str.toString());
        return false;
    }
    public static  boolean ifValueBetWeenStrs(String str, int v, Boolean ifHave){
        if(isNumeric(str)&&Integer.parseInt(str)==v){
            return true;
        }
        String[] strs = str.split(",");
        if(strs.length!=2||!isNumeric(strs[0])||!isNumeric(strs[1])){
            return false;
        }
        int v1=Integer.parseInt(strs[0]);
        int v2=Integer.parseInt(strs[1]);
        return ifValueBetween(v,v1,v2,ifHave);
    }



    public static String converNumToRoman(int number) {
        StringBuilder strBuilder = new StringBuilder();
        //int number = Integer.parseInt(strNubmer);

        strBuilder.append(ResDefaultConfig.StringName.RomanStr[3][number / 1000 % 10]);
        strBuilder.append(ResDefaultConfig.StringName.RomanStr[2][number / 100 % 10]);
        strBuilder.append(ResDefaultConfig.StringName.RomanStr[1][number / 10 % 10]);
        strBuilder.append(ResDefaultConfig.StringName.RomanStr[0][number % 10]);
        return strBuilder.toString();

    }

    //随机打乱数组
    public static void shuffle(int[] arr) {
        Random mRandom = new Random();
        for (int i = arr.length; i > 0; i--) {
            int rand = mRandom.nextInt(i);
            swap(arr, rand, i - 1);
        }
    }

    //交换两个值
    private static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }




    public static byte[] toByteArray(InputStream input)
            throws IOException
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static int copy(InputStream input, OutputStream output)
            throws IOException
    {
        long count = copyLarge(input, output);
        if (count > 2147483647L) {
            return -1;
        }
        return (int)count;
    }

    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException
    {
        byte[] buffer = new byte[4096];
        long count = 0L;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;

    }

    public static String removeRepeatChar(String str) {
        List<String> data = new ArrayList<String>();

        for (int i = 0; i < str.length(); i++) {
            String s = str.substring(i, i + 1);
            if (!data.contains(s)) {
                data.add(s);
            }
        }
        StringBuilder result = new StringBuilder();
        for (String s : data) {
            result.append(s);
        }
        return result.toString();
    }


     /**
     * 字符串转数组 可以先将字符串转成字符串数组，在将字符串数组中的每个元素转成int类型复制给字符串数组长度的int数组 str格式为{0,1,2}
     * @param commentLabelIds
     * @return
     */
    public static int[] stringTransIntArray(String  commentLabelIds){
        String[] ids = commentLabelIds.split(",");//字符串转成字符串数组
        if(ids.length==0){
            return null;
        }
        int[] ret = new int[ids.length];//创建一个字符串数组长度的int数组
        int i = 0;
        for(String s : ids){
            //去的掉字符串数组中的特殊字符，获取每个元素的值
            if(i == 0){
                s = s.substring(1,s.length());
            }
            if(i == ids.length-1){
                s = s.replace("\n","").trim();
                s = s.substring(0,s.length()-1);
            }
            s = s.replace("\n","").trim();
         /*if("\n".contains(s)){
            s = s.replace("\n","");
         }*/
         //将字符串数组中的元素转换成int类型，复制给int数组的元素
            if(isNumeric(s)){
                ret[i] = Integer.valueOf(s);
            }

            i++;
        }
        return ret;
    }

    //str为字符串,用,风格值
    public static int[] strTransIntArray(String  str){
       //创建一个字符串数组长度的int数组
        String[] strs = str.split(",");
        int[] ret = new int[strs.length];
        for (int i = 0; i < strs.length; i++) {
            if (ComUtil.isNumeric(strs[i]) ) {
                ret[i]=Integer.parseInt(strs[i]);
            }
        }
        return ret;
    }

    public static IntArray stringTransGdxIntArray(String  commentLabelIds){
        String[] ids = commentLabelIds.split(",");//字符串转成字符串数组
       // int[] ret = new int[ids.length];//创建一个字符串数组长度的int数组
        IntArray rs=new IntArray();
        int i = 0;
        for(String s : ids){
            //去的掉字符串数组中的特殊字符，获取每个元素的值
            if(i == 0){
                s = s.substring(1,s.length());
            }
            if(i == ids.length-1){
                s = s.replace("\n","").trim();
                s = s.substring(0,s.length()-1);
            }
            s = s.replace("\n","").trim();
         /*if("\n".contains(s)){
            s = s.replace("\n","");
         }*/
            //将字符串数组中的元素转换成int类型，复制给int数组的元素
            if(isNumeric(s)){
                // ret[i] = Integer.valueOf(s);
                rs.add(Integer.valueOf(s));
            }
            i++;
        }
        return rs;
    }


    //获得第一个数字
    public static int getFirstNumber(String str,int defaultValue) {
        if(isNumeric(str)){
            return Integer.parseInt(str);
        }
        String[] ids = str.split(",");
        if(isNumeric(ids[0])){
            return Integer.parseInt(ids[0]);
        }
        return defaultValue;
    }

    static boolean isSymbol(char ch)
    {
        if(isCnSymbol(ch)) return true;
        if(isEnSymbol(ch))return true;

        if(0x2010 <= ch && ch <= 0x2017) return true;
        if(0x2020 <= ch && ch <= 0x2027) return true;
        if(0x2B00 <= ch && ch <= 0x2BFF) return true;
        if(0xFF03 <= ch && ch <= 0xFF06) return true;
        if(0xFF08 <= ch && ch <= 0xFF0B) return true;
        if(ch == 0xFF0D || ch == 0xFF0F) return true;
        if(0xFF1C <= ch && ch <= 0xFF1E) return true;
        if(ch == 0xFF20 || ch == 0xFF65) return true;
        if(0xFF3B <= ch && ch <= 0xFF40) return true;
        if(0xFF5B <= ch && ch <= 0xFF60) return true;
        if(ch == 0xFF62 || ch == 0xFF63) return true;
        if(ch == 0x0020 || ch == 0x3000) return true;
        return false;

    }
    static boolean isCnSymbol(char ch) {
        if (0x3004 <= ch && ch <= 0x301C) return true;
        if (0x3020 <= ch && ch <= 0x303F) return true;
        return false;
    }
    static boolean isEnSymbol(char ch){

        if (ch == 0x40) return true;
        if (ch == 0x2D || ch == 0x2F) return true;
        if (0x23 <= ch && ch <= 0x26) return true;
        if (0x28 <= ch && ch <= 0x2B) return true;
        if (0x3C <= ch && ch <= 0x3E) return true;
        if (0x5B <= ch && ch <= 0x60) return true;
        if (0x7B <= ch && ch <= 0x7E) return true;

        return false;
    }

    static boolean isPunctuation(char ch){
        if(isCjkPunc(ch)) return true;
        if(isEnPunc(ch)) return true;

        if(0x2018 <= ch && ch <= 0x201F) return true;
        if(ch == 0xFF01 || ch == 0xFF02) return true;
        if(ch == 0xFF07 || ch == 0xFF0C) return true;
        if(ch == 0xFF1A || ch == 0xFF1B) return true;
        if(ch == 0xFF1F || ch == 0xFF61) return true;
        if(ch == 0xFF0E) return true;
        if(ch == 0xFF65) return true;

        return false;
    }
    static boolean isEnPunc(char ch){
        if (0x21 <= ch && ch <= 0x22) return true;
        if (ch == 0x27 || ch == 0x2C) return true;
        if (ch == 0x2E || ch == 0x3A) return true;
        if (ch == 0x3B || ch == 0x3F) return true;

        return false;
    }
    static boolean isCjkPunc(char ch){
        if (0x3001 <= ch && ch <= 0x3003) return true;
        if (0x301D <= ch && ch <= 0x301F) return true;

        return false;
    }

    public static void copyStrForPCClipboard(String str) {
        StringSelection stsel = new StringSelection(str);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stsel, stsel);
    }

    //获得一个除了xx的随机数
    public static int getRandom(int min, int max, int excludeValue) {
        int v=getRandom(min,max-1);
        if(v==excludeValue){
            v=max;
        }
        return v;
    }

    //邮箱是否合法
    public static boolean isEmail(String string) {
        if (string == null)
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        if (m.matches())
            return true;
        else
            return false;
    }


    //取字符串分割以后的该位置的数字
    public static float getIntByStrs(String strs, int index) {
        String[] ids = strs.split(",");
        if(index<ids.length&&isNumeric(ids[index])){
            return Integer.parseInt(ids[index]);
        }
        Gdx.app.error("getIntByStrs",strs+":"+index);
        return 0;
    }
    //取字符串分割以后的该位置的浮动类型
    public static float getFloatByStrs(String strs, int index) {
        String[] ids = strs.split(",");
        if(index<ids.length&&isDouble(ids[index])){
            return Float.parseFloat(ids[index]);
        }
        Gdx.app.error("getFloatByStrs",strs+":"+index);
        return 0;
    }

    public static boolean IsOverlap(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2) {
        return x1<x2+w2 && x2<x1+w1 && y1<y2+h2 && y2<y1+h1;
    }


    public static void main(String[] argv) {
       /* Integer[] indexArrays={1,2,3,4,5,6};
        Integer[] valueArrays={16,25,34,13,22,31};
        sortIntegerArrays(indexArrays,valueArrays,true);
        System.out.println("indexArrays:"+Arrays.toString(indexArrays));
        System.out.println("valueArrays:"+ Arrays.toString(valueArrays));
*/
        System.out.println("getCountByStrSplit:"+getCountByStrSplit("123"));
    }

    //ifSortOrder true 从小到大  false
    public static void sortIntegerArrays(Integer[] indexArrays, Integer[] valueArrays,boolean ifSortOrder) {
        if(ifSortOrder){//
            for(int i = 0; i < valueArrays.length - 1; i++){
                // 初始化一个布尔值
                boolean flag = true;
                for(int j = 0; j < valueArrays.length - i - 1 ; j++){
                    if(valueArrays[j] > valueArrays[j+1]){
                        // 调换
                        int temp;
                        temp = valueArrays[j];
                        valueArrays[j] = valueArrays[j+1];
                        valueArrays[j+1] = temp;
                        temp = indexArrays[j];
                        indexArrays[j] = indexArrays[j+1];
                        indexArrays[j+1] = temp;
                        // 改变flag
                        flag = false;
                    }
                }
                if(flag){
                    break;
                }
            }
        }else{//从大到小
            for(int i = 0; i < valueArrays.length - 1; i++){
                // 初始化一个布尔值
                boolean flag = true;
                for(int j = 0; j < valueArrays.length - i - 1 ; j++){
                    if(valueArrays[j] < valueArrays[j+1]){
                        // 调换
                        int temp;
                        temp = valueArrays[j];
                        valueArrays[j] = valueArrays[j+1];
                        valueArrays[j+1] = temp;
                        temp = indexArrays[j];
                        indexArrays[j] = indexArrays[j+1];
                        indexArrays[j+1] = temp;
                        // 改变flag
                        flag = false;
                    }
                }
                if(flag){
                    break;
                }
            }
        }
    }

    public static int getTime(){
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMdd");
        return Integer.parseInt(timeFormat.format(new Date(System.currentTimeMillis())));
    }

    public static int getCountByStrSplit(String strs) {
        if(ComUtil.isEmpty(strs)){return 0;}
        String[] ids = strs.split(",");
        return ids.length;
    }

    public static String transStrForProperty(Properties save) {
        String str=save.toString();
        str= str.replaceAll("\\{","");
        str=str.replaceAll("\\}","");
        str=str.replaceAll(", ","\n");
        return str;
    }
}
