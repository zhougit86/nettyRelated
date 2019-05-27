package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import proxy.imp.AOPMethod;

public class AOPHandle implements InvocationHandler{
	//�������
	private AOPMethod method;
	private Object o;
	public AOPHandle(Object o,AOPMethod method) {
		this.o=o;
		this.method=method;
	}
	/**
	 * ����������Զ�����,Java��̬�������
	 * �ᴫ�������Ǹ�����
	 * @param Object proxy	�������Ľӿ�,��ͬ�ڶ���
	 * @param Method method	�����÷���
	 * @param Object[] args	��������
	 * ����ʹ��invokeʱʹ��proxy��Ϊ�������ʱ,��Ϊ�������Ľӿ�,��ͬ�ڶ���
	 * ���ִ������������ӿڣ��������������
	 **/
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object ret=null;
		//�޸ĵĵط�������Ŷ
		this.method.before(proxy, method, args);
		ret=method.invoke(o, args);
		//�޸ĵĵط�������Ŷ
		this.method.after(proxy, method, args);
		return ret;
	}
}
