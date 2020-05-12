package com.raon.web.atmvc;


/*
 *  prototypeEditor는 싱글톤 불가
 *  다른 빈의 di를 받기 위해서는 자기자신도 bean이 되어야 하지만 싱글톤이 되므로, prototype으로 사용한다.
 *  
 * 
 * */
public class PrototypePropertyEditorTest {
	
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
