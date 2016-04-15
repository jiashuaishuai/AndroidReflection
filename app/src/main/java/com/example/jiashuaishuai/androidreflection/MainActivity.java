package com.example.jiashuaishuai.androidreflection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "打印结果";
    private Man man;
    private Class<?> temp;
    private TextView tv;
    private Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView);
        bt = (Button) findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getClassName();//获取classname
//                getConstructor();//反射构造方法
//                getMethod();
//                getField();
//               String s= (String) Load("com.example.jiashuaishuai.androidreflection.Man", "getTest", new String[]{"String", "int"}, new String[]{"name", "2"});
                /**
                 * 反射调用方法
                 */
                String s = (String) Load("com.example.jiashuaishuai.androidreflection.Man", "getTest", new String[]{"String", "int"}, new String[]{"name", "2"});
                Log.e(TAG, s);
            }
        });
        man = new Man();
        temp = man.getClass();

    }


//
//    /**
//     * 在运行时加载指定的类，并调用指定的方法
//     *
//     * @param cName      Java的类名
//     * @param MethodName 方法名
//     * @param params     方法的参数值
//     * @return
//     */
//    public Object Loadex(String cName, String MethodName, Object[] params) {
//
//        Object retObject = null;
//
//        try {
//            // 加载指定的类
//            Class cls = Class.forName(cName);    // 获取Class类的对象的方法之二
//
//            // 利用newInstance()方法，获取构造方法的实例
//            // Class的newInstance方法只提供默认无参构造实例
//            // Constructor的newInstance方法提供带参的构造实例
//            Constructor ct = cls.getConstructor(null);
//            Object obj = ct.newInstance(null);
//            //Object obj = cls.newInstance();
//
//            // 根据方法名获取指定方法的参数类型列表
//            Class paramTypes[] = this.getParamTypes(cls, MethodName);
//
//            // 获取指定方法
//            Method meth = cls.getDeclaredMethod(MethodName, paramTypes);
////            meth.setAccessible(true);
//            meth.setAccessible(true);
//
//            // 调用指定的方法并获取返回值为Object类型
//            retObject = meth.invoke(obj, params);
//
//        } catch (Exception e) {
//            System.err.println(e);
//        }
//
//        return retObject;
//    }
//
//    /**
//     * 获取参数类型，返回值保存在Class[]中
//     */
//    public Class[] getParamTypes(Class cls, String mName) {
//        Class[] cs = null;
//
//        /*
//         * Note: 由于我们一般通过反射机制调用的方法，是非public方法
//         * 所以在此处使用了getDeclaredMethods()方法
//         */
//        Method[] mtd = cls.getDeclaredMethods();
//        for (int i = 0; i < mtd.length; i++) {
//            if (!mtd[i].getName().equals(mName)) {    // 不是我们需要的参数，则进入下一次循环
//                continue;
//            }
//
//            cs = mtd[i].getParameterTypes();
//        }
//        return cs;
//    }


    /**
     * 在运行时加载指定的类，并调用指定的方法
     *
     * @param className  Java的类名
     * @param methodName 方法名
     * @param types      方法的参数类型
     * @param params     方法的参数值
     * @return
     */
    private Object Load(String className, String methodName, String[] types, String[] params) {
        Object retobject = null;
        try {
            //加载指定类
            Class cls = Class.forName(className);//获取Class类对象方法二
/**   一、
 * 创建类两种方法
 * 1.通过该类的Constructor下的newInstance创建（注：该类的构造方法的newInstance） 如果该构造方法有参数，则getConstructor（参数类型【】）newInstance(参数【】)
 * 2.通过该类本身的newInstance创建
 */
            Constructor constructor = cls.getConstructor(new Class[]{Integer.TYPE,String.class});
            Object obj = constructor.newInstance(new Object[]{2,"名字"});
//            Object obj = cls.newInstance();//利用类本身的newInstance创建该类
            /**
             * 二、构建该方法的参数类型
             */
            Class[] paramTypes = getMethodTypesClass(types);
/**
 * 三、在指定类中构建指定的方法
 */
            Method meth = cls.getDeclaredMethod(methodName, paramTypes);//私有方法需要使用DeclaredMethod
            meth.setAccessible(true);
            /**
             * 四、构建方法的参数值，
             */
            Object[] objParams = getMethodParamObject(types, params);

            /**
             * 五、调用指定的方法并获取返回值Object类型
             */
            retobject = meth.invoke(obj, objParams);
        } catch (Exception e) {
            System.err.println(e);
        }

        return retobject;
    }

    /**
     * 根据参数类型构建参数类型的type并保存到Class数组中
     *
     * @param types
     * @return
     */
    public Class[] getMethodTypesClass(String[] types) {
        Class[] classMethodTypes = new Class[types.length];

        for (int i = 0; i < classMethodTypes.length; i++) {
            if (!TextUtils.isEmpty(types[i])) {
                if (types[i].equals("int") || types[i].equals("Integer")) {
                    classMethodTypes[i] = Integer.TYPE;
                } else if (types[i].equals("float") || types[i].equals("Float")) {
                    classMethodTypes[i] = Float.TYPE;
                } else if (types[i].equals("double") || types[i].equals("Double")) {
                    classMethodTypes[i] = Double.TYPE;
                } else if (types[i].equals("boolean") || types[i].equals("Boolean")) {
                    classMethodTypes[i] = Boolean.TYPE;
                } else {
                    classMethodTypes[i] = String.class;
                }
            }
        }
        return classMethodTypes;
    }

    /**
     * 根据参数类型和 参数，构建方法的参数值
     */
    public Object[] getMethodParamObject(String[] types, String[] params) {

        Object[] retObjects = new Object[params.length];

        for (int i = 0; i < retObjects.length; i++) {
            if (!params[i].trim().equals("") || params[i] != null) {
                if (types[i].equals("int") || types[i].equals("Integer")) {
                    retObjects[i] = new Integer(params[i]);
                } else if (types[i].equals("float") || types[i].equals("Float")) {
                    retObjects[i] = new Float(params[i]);
                } else if (types[i].equals("double") || types[i].equals("Double")) {
                    retObjects[i] = new Double(params[i]);
                } else if (types[i].equals("boolean") || types[i].equals("Boolean")) {
                    retObjects[i] = new Boolean(params[i]);
                } else {
                    retObjects[i] = params[i];
                }
            }
        }

        return retObjects;
    }

    /**
     * 存在四种获取成员属性的方法，和获取构造方法成员方法类似：具有public和不区分public 以及是否包含父类
     * Field getField(String name)    根据变量名，返回一个具体的具有public属性的成员变量包含父类
     * Field[] getFields()    返回具有public属性的成员变量的数组包含父类
     * Field getDeclaredField(String name)    根据变量名，返回一个成员变量（不分public和非public属性）不包含父类
     * Field[] getDelcaredField()    返回所有成员变量组成的数组（不分public和非public属性）不包含父类
     */
    public void getField() {
//        Field[] fields = temp.getFields();//包含父类
        Field[] fields = temp.getDeclaredFields();//不包含父类
        for (int i = 0; i < fields.length; i++) {
            int mod = fields[i].getModifiers();
            String name = fields[i].getName();
            Loge(mod, name);
            Class type = fields[i].getType();
            Log.e(TAG, "type-字段类型" + type.getName());
            try {
                Field field = temp.getDeclaredField(fields[i].getName());//根据变量名字从temp中获取该变量不包含父类
//                Field field = temp.getField(fields[i].getName());//包含父类
                Log.e(TAG, "根据变量名称获取该变量，打印" + field.toGenericString());
                field.setAccessible(true);////类中的成员变量为private,故必须进行此操作 ，提高反射速度http://blog.csdn.net/devilkin64/article/details/7766792
                Object object = field.get(man);//反射变量值
                Log.e(TAG, "变量值--" + object);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * Method成员方法：
     * 与获取构造方法的方式相同，存在四种获取成员方法的方式。
     * 　　　　Method getMethod(String name, Class[] params)    根据方法名和参数，返回一个具体的具有public属性的方法
     * 　　　　Method[] getMethods()    返回所有具有public属性的方法数组，包括从父类继承的public方法和实现接口的public方法
     * 　　　　Method getDeclaredMethod(String name, Class[] params)    根据方法名和参数，返回一个具体的方法（不分public和非public属性）
     * 　　　　Method[] getDeclaredMethods()    返回该类中的所有的方法数组（不分public和非public属性）用于获取在当前类中定义的所有的成员方法和实现的接口方法，不包括从父类继承的方法。
     * 注意：
     * 在获取类的成员方法时，有一个地方值得大家注意，就是getMethods()方法和getDeclaredMethods()方法。
     * 　　　　getMethods()：用于获取类的所有的public修饰域的成员方法，包括从父类继承的public方法和实现接口的public方法；
     * 　　　　getDeclaredMethods()：用于获取在当前类中定义的所有的成员方法和实现的接口方法，不包括从父类继承的方法。
     */
    public void getMethod() {
//        Method[] methods = temp.getMethods();//带父类
        Method[] methods = temp.getDeclaredMethods();//不带父类
        for (int i = 0; i < methods.length; i++) {
            int mod = methods[i].getModifiers();
            String methodName = methods[i].getName();
            Loge(mod, methodName);
            Class[] parameterType = methods[i].getParameterTypes();
            for (int j = 0; j < parameterType.length; j++) {
                Log.e(TAG, "parameterType---" + "--" + j + "--" + parameterType[j]);
            }

        }
    }


    /**
     * 获取className
     */
    public void getClassName() {
        String className = temp.getName();
        tv.setText(className);
        Log.e(TAG, "className--" + className);
    }

    /**
     * 获得该类的构造方法
     * Constructor getConstructor(Class[] params) 根据构造函数的参数。返回一个具体的具有public属性的构造函数
     * Constructor getConstructors() 返回所有具有public属性的构造函数数组；
     * Constructor getDeclaredConstructor(Class[] params)   根据构造函数的参数，返回一个具体的构造函数（不分public和非public属性）
     * Constructor getDeclaredConstructor() 返回该类中所有的构造函数数组（不分public和非public属性）
     */
    public void getConstructor() {

//        Constructor[] constructors = temp.getDeclaredConstructors();//返回该类中所有的构造函数数组（不分public和非public属性）
        Constructor[] constructors = temp.getConstructors();//返回所有具有public属性的构造函数数组；
        for (int i = 0; i < constructors.length; i++) {
            int mod = constructors[i].getModifiers();
            String constructorName = constructors[i].getName();
            Loge(mod, constructorName);
            Class[] parameterType = constructors[i].getParameterTypes();
            for (int j = 0; j < parameterType.length; j++) {
                Log.e(TAG, "parameterType---" + "--" + j + "--" + parameterType[j]);
            }
        }
    }

    public void Loge(int mod, String name) {
        Log.e(TAG, "mod--修饰符" + mod);
        Log.e(TAG, "constructorName--" + name);
    }

    static void getInfo() {
        Man r = new Man();
        Class<?> temp = r.getClass();//获取指定类的class
        String className = temp.getName();//获取指定类的类名
        try {
            /**
             * 获得该类的构造方法
             * Constructor getConstructor(Class[] params) 根据构造函数的参数。返回一个具体的具有public属性的构造函数
             *Constructor getConstructors() 返回所有具有public属性的构造函数数组；
             *Constructor getDeclaredConstructor(Class[] params)   根据构造函数的参数，返回一个具体的构造函数（不分public和非public属性）
             *Constructor getDeclaredConstructor() 返回该类中所有的构造函数数组（不分public和非public属性）
             */
            Constructor[] theConstructorss = temp.getConstructors();//获取指定类的共有构造方法
            for (int i = 0; i < theConstructorss.length; i++) {
                int mod = theConstructorss[i].getModifiers();//返回该构造方法修饰符
                String constructorsName = theConstructorss[i].getName();//返回该构造方法的名字
                Class[] parameterTypes = theConstructorss[i].getParameterTypes();//返回该构造方法的参数集合
            }
/**
 * Method成员方法：
 *与获取构造方法的方式相同，存在四种获取成员方法的方式。
 *　　　　Method getMethod(String name, Class[] params)    根据方法名和参数，返回一个具体的具有public属性的方法
 *　　　　Method[] getMethods()    返回所有具有public属性的方法数组，包括从父类继承的public方法和实现接口的public方法
 *　　　　Method getDeclaredMethod(String name, Class[] params)    根据方法名和参数，返回一个具体的方法（不分public和非public属性）
 *　　　　Method[] getDeclaredMethods()    返回该类中的所有的方法数组（不分public和非public属性）用于获取在当前类中定义的所有的成员方法和实现的接口方法，不包括从父类继承的方法。
 *注意：
 *在获取类的成员方法时，有一个地方值得大家注意，就是getMethods()方法和getDeclaredMethods()方法。
 *　　　　getMethods()：用于获取类的所有的public修饰域的成员方法，包括从父类继承的public方法和实现接口的public方法；
 *　　　　getDeclaredMethods()：用于获取在当前类中定义的所有的成员方法和实现的接口方法，不包括从父类继承的方法。
 */
            Method[] methods = temp.getMethods();//获取该类包括父类和接口的public方法
//            Method[] methods = temp.getDeclaredMethods();//获取该类和接口实现的public方法，不包括父类


            Field[] fields = temp.getFields();
            for (int i = 0; i < fields.length; i++) {
                Class cl = fields[i].getType();//获取属性类型
                int md = fields[i].getModifiers();//获取修饰符
                Field field = temp.getField(fields[i].getName());//根据变量名获取变量
                field.setAccessible(true);//类中的成员变量为private,故必须进行此操作 ，提高反射速度http://blog.csdn.net/devilkin64/article/details/7766792
                Object object = field.get(r);
            }

            System.out.println("反射类中所有公有的属性");
            Field[] fb = temp.getFields();
            for (int j = 0; j < fb.length; j++) {
                Class<?> cl = fb[j].getType();
                System.out.println("fb:" + cl + "___" + fb[j].getName());
            }
            System.out.println("反射类中所有的属性");
            Field[] fa = temp.getDeclaredFields();
            for (int j = 0; j < fa.length; j++) {
                Class<?> cl = fa[j].getType();
                System.out.println("fa:" + cl + "____" + fa[j].getName());
            }

            System.out.println("反射类中所有的方法");
            Method[] fm = temp.getMethods();
            for (int i = 0; i < fm.length; i++) {
                System.out.println("fm:" + fm[i].getName() + "____" + fm[i].getReturnType().getName());
            }

            System.out.println("反射类中所有的接口");
            Class<?>[] fi = temp.getInterfaces();
            for (int i = 0; i < fi.length; i++) {
                System.out.println("fi:" + fi[i].getName());
            }

            System.out.println("反射类中私有属性的值");
            Field f = temp.getDeclaredField("var1");
            f.setAccessible(true);
            String i = (String) f.get(r);
            System.out.println(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改父类变量，调用父类方法
     */
    static void callSpuerMethod() {
        Man r = new Man();
        try {
            //修改私有变量；
            Field f = r.getClass().getSuperclass().getDeclaredField("age");
            f.setAccessible(true);
            f.set(r, 20);

            //调用私有方法,必须要用getDeclaredMethod，而不能用getMethod；
            Method mp = r.getClass().getSuperclass().getDeclaredMethod("priMethod", Integer.class);
            mp.setAccessible(true);
            mp.invoke(r, 18);

            //调用隐藏方法
            Method m = r.getClass().getSuperclass().getMethod("hideMethod", String.class);
            m.setAccessible(true);
            m.invoke(r, "Jerome");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改子类变量，调用子类方法
     */
    static void callCurrentMethod() {
        Man r = new Man();
        try {
            //修改私有变量；
            Field f = r.getClass().getDeclaredField("age");
            f.setAccessible(true);
            f.set(r, 20);

            //调用私有方法,必须要用getDeclaredMethod，而不能用getMethod；
            Method mp = r.getClass().getDeclaredMethod("getName");
            mp.setAccessible(true);
            mp.invoke(r);

            //调用隐藏私有方法
            Method m = r.getClass().getDeclaredMethod("getAge");
            m.setAccessible(true);
            m.invoke(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
