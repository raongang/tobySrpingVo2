package com.raon.web.atmvc;


/*
 *  
 *  @InitBinder나 WebBindingInitializer는 매번 new를 이용한다. 이를 해결하기 위해 싱글톤으로 할려고 했으나, prototypeEditor는 싱글톤 불가
 *  다른 빈의 di를 받기 위해서는 자기자신도 bean이 되어야 하지만 싱글톤이 되므로, prototype으로 사용한다.
 *  
 *  prototypeEditor 가 상태값을 가지기 때문에 멀티스레드 환경에서 여러 오브젝트가 공유해서 사용하면 안된다.
 * 
 * */
public class PrototypePropertyEditorTest {

	/**
	 * ====================================================================================================
	 * 
	 */
	
	
	static class User {
		int id; String name; Code userType;
		public int getId() {	return id;	}
		public void setId(int id) { this.id = id; }
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		public Code getUserType() { return userType; }
		public void setUserType(Code userType) { this.userType = userType; }
		@Override
		public String toString() { return "User [id=" + id + ", name=" + name + ", userType=" + userType + "]";
		}		
	}//end User
	
	static class Code {
		int id;
		String name;
		public int getId() { return id; }
		public void setId(int id) { this.id = id; }
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		public String toString() {
			return "Code [id=" + id + ", name=" + name + "]";
		}
	}//end Code
	
}
