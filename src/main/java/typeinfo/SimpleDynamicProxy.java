//: typeinfo/SimpleDynamicProxy.java
package typeinfo; /* Added by Eclipse.py */
import java.lang.reflect.*;
//调用处理器
class DynamicProxyHandler implements InvocationHandler {
  private Object proxied;
  //向构造器传递一个实际对象的引用，使得调用处理器在执行其中介任务时，可以将其转发
  public DynamicProxyHandler(Object proxied) {
    this.proxied = proxied;
  }
  //处理代理实例上的方法调用并返回结果。
  public Object
  invoke(Object proxy, Method method, Object[] args)
  throws Throwable {
    System.out.println("**** proxy: " + proxy.getClass() +
      ", method: " + method + ", args: " + args);
    if(args != null)
      for(Object arg : args)
        System.out.println("  " + arg);
    //返回实际对象方法的运行结果
    return method.invoke(proxied, args);
  }
}	

class SimpleDynamicProxy {
  public static void consumer(Interface iface) {
    iface.doSomething();
    iface.somethingElse("bonobo");
  }
  public static void main(String[] args) {
    RealObject real = new RealObject();
    consumer(real);
    // Insert a proxy and call again:
    Interface proxy = (Interface)Proxy.newProxyInstance(
      Interface.class.getClassLoader(),
      new Class[]{ Interface.class },
      new DynamicProxyHandler(real));
    consumer(proxy);
  }
} /* Output: (95% match)	
doSomething
somethingElse bonobo
**** proxy: class $Proxy0, method: public abstract void Interface.doSomething(), args: null
doSomething
**** proxy: class $Proxy0, method: public abstract void Interface.somethingElse(java.lang.String), args: [Ljava.lang.Object;@42e816
  bonobo
somethingElse bonobo
*///:~
