package proxy;

import java.lang.reflect.Method;

import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import proxy.imp.AOPMethod;
import proxy.imp.AnimalInterface;

@RunWith(BlockJUnit4ClassRunner.class)
public class AOPTest {

	public static void main(String[] args) {

		AnimalInterface dog = AnimalFactory.getAnimal(DogImp.class, new AOPMethod() {
			// ����д����ִ��ǰ��AOP���뷽��
			public void before(Object proxy, Method method, Object[] args) {
				if (method.getName().equals("getProperty")) {
					System.err.println("�ɹ�����" + method.getName() + "����,����");
				}
			}

			// ����ϵ����ִ�к��AOP���뷽��
			public void after(Object proxy, Method method, Object[] args) {
				if (method.getName().equals("getProperty"))
					System.err.println("�ɹ�����" + method.getName() + "����,����");

			}
		});
		dog.say();
		String name1 = "�ҵ�������" + dog.getName();
		System.out.println(name1);
		dog.setName("������");
		String name2 = "�ҵ�������" + dog.getName();
		System.out.println(name2);
		dog.getProperty();
	}
}
