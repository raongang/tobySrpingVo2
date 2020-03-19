package springbook.learningtest.spring.ioc.bean;

//pojo
public class StringPrinter implements Printer{

	private StringBuffer buffer = new StringBuffer();
	
	@Override
	public void print(String message) {
		// TODO Auto-generated method stub
		this.buffer.append(message);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.buffer.toString();
	}

}
