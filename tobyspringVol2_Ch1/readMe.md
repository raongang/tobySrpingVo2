
※ 프로젝트 설명 : IoC 컨테이너와 DI
 1 IoC컨테이너 : bean Factory 와 application Context 
   - 스프링 애플리케이션에서 오브젝트의 생성과 관계설정, 사용, 제거 등의 작업을 애플리케이션 코드 대신 독립된 컨테이너가 담당
   - 컨테이너가 코드 대신 오브젝트에 대한 제어권을 가지고 있다고 해서 IoC라고 부름
   - bean Factory 와 application Context는 IoC를 담당하는 컨테이너임.
   
  1.1 bean Factory
    - 오브젝트의 생성과 오브젝트 사이의 런타임 관계를 설정하는 DI관점의 컨테이너 
  
  1.2 application Context
    - DI를 위한 bean factory에 엔터프라이즈 애플리케이션을 개발하는데 필요한 여러가지 컨테이너 기능을 추가한 것.
    - 그 자체로 IoC와 DI를 위한 bean factory이면서, 그 이상을 기능을 가짐.
    
  1-3. bean
    - 스프링 컨테이너가 관리하는 오브젝트
    - bean은 오브젝트 단위로 등록되고 만들어지기 때문에, 같은 클래스 타입이라고 할지라도 두개를 등록하면 서로 다른 빈 오브젝트가 생성된다.
    
  1-4. IoC컨테이너의 종류 ( ApplicationContext interface 구현 ) 
    ● StaticApplicationContext 
      - 실전에서 사용금지, 테스트용 
    ● GeneralApplicationContext
      - 가장 일반적 구현클래스
    ● GeneralXmlApplicationContext
    ● WebApplicationContext 
      - Spring application에서 가장 많이 사용되는 application context , application context를 확장한 interface
      - 웹어플리케이션에서 만들어지는 스프링 IoC 컨테이너는 WebApplicationContext 인터페이스를 구현한 것.  
 
2. 스프링의 설정 메타 정보
  - BeanDefinition 인터페이스로 표현되는 순수한 추상정보
  - Ioc 컨테이너, 즉 application context는 BeanDefinition으로 만들어진 메타정보를 담은 오브젝트를 사용해서 IoC와 DI작업을 수행한다.
  
 3. 스프링의 context?
  - 어떤 객체를 핸들링하기 위한 접근 수단
     예 ) print를 하기 위해서는 printContext를 사용해야하고 Servlet을 수행하기 위해서는 Servlet Context를 사용해야함.
      
 4. Webapplication의 IoC Container 구성
    - web.xml 참고
    - root application context , servlet application context 
 
   
 5. IoC/DI를 위한 빈 설정 메타정보 작성 
   - 자동인식을 위한 빈 등록 : 스테레오타입 애노테이션과 빈 스캐너
   - 빈스캐닝을 통한 자동인식 빈 등록 기능 : 특정 애노테이션이 붙은 클래스를 자동으로 찾아서 빈으로 등록해주는 방식
   - 빈 스캐너 
      ㄴ 스캐닝 작업을 담당하는 오브젝트, 클래스이름의 앞글자를 소문자로 한 이름을 빈의 아이디로 사용한다.
      ㄴ 클래스패스의 모든 클래스를 다 검색하지만 비효율적이므로, 빈으로 등록된 클래스들이 있는 패키지를 지정 해 주는 것이 좋다.
      ㄴ 빈 스캐너에 내장된 디폴트 필터는 @Component 애노테이션 또는 @Component를 메타 애노테이션을 가진 애노테이션이 부여된 클래스.
   - 스테레오타입 애노테이션 : @Component 를 포함해 디폴트 필터에 적용되는 애노테이션
  
 5-1. 자동인식을 위한 빈 등록 방법																	
   ㄴ XML을 이용한 빈 스캐너 등록 ( component-scan 이용 ) 
   ㄴ 빈 스캐너를 내장한 application context 사용
   	
 5-2. 자바코드에 의한 빈등록
    ㄴ @Configuration 클래스의 @Bean 메소드
    ㄴ @Configuration은 빈 스캐닝을 통해 자동 인식 대상이 되므로 XML이 필요없음.
    ㄴ @Configuration의 메타 애노테이션에 @Component가 포함되어 있음.
    
 
 6. 빈 의존관계 설정 방법
   6-1. XML <property> , <constructor-arg> 
   6-2. XML 자동와이어링 ( byName , byType ) 
   6-3. 애노테이션 ( @Resource , @Autowired , @Inject ) 
     6-3-1. @Resource
        - 자바클래스의 수정자뿐만 아니라 필드에도 붙일 수 있음. ( 필드 주입을 할경우 setter는 필요없지만, 만약 setter를 넣게 되면 값 대입 외 다른 조작행위를 하면 안된다.)
        - XML대신 <property>와 같은 역할을 하는 애노테이션 
        - XML에 bean을 생성하고, 다음 3가지 설정중 하나를 선택해야 함
          ( <context:annotation-config /> , <context:component-scan /> , AnnotationConfigApplicationContext 또는 AnnotationConfigWebApplicationContext )
        - 이름으로 검색, 없을 경우 type을 검색하고 없을 경우 exception 발생.  
        
     6-3-2. @Autowired / @Inject    
        - 기본적으로 type에 의한 자동와이어링 방식으로 동작.
        - @Autowired 
           ㄴ 스프링 2.5부터 적용
           ㄴ 생성자, 필드, 수정자메소드, 일반메소드 네가지로 확장한 것이다.
           ㄴ 수정자 메소드와 필드일 경우에는, 이름 대신 필드나 프로퍼티 타입을 이용해 후보 빈을 찾는다.
           ㄴ 생성자일 경우에는, 타입에 의한 자동 와이어링이 적용. ( 단 하나의 생성자에만 사용 가능 ) 
           
        - @Inject
           ㄴ JavaEE6의 표준 스펙 JSR-330 정의.
           ㄴ @javax.inject.Qualifier , @javax.inject.inject
           ㄴ @Autowired 와 기능은 같지만 required=false 같은 설정은 없다.
           
        - @Qualifier 
          ㄴ 부여대상이 필드와 수상이 수정자와 메소드, 생성자
          
   6-4. 자바코드에 의한 의존관계 설정
 
 7. 프로퍼티 값 설정 방법
  7-1. 메타정보 종류에 따른 값 설정방법
       ● XML:<property>와 전용태그
       ● 애노테이션 : @Value
         - static final로 초기화를 하지 않고 자바코드의 외부의 리소스나 환경정보에 담긴 값을 사용하도록 지정 해주는 게 주요 용도.
            1. 매번 환경이 달라 질수 있는 값 ( ex. DataSource ) 
               └ 환경에 의존적인 정보이거나 작업에 대한 타임아웃처럼 상황에 따라 달라질 수 있는 값 ( ex. 파일경로 ) 
            2. 특별한 경우, 예를 들면 테스트나 특별한 이벤트 때는 초기값 대신 다른 값을 이용할 수 있음.
       ● 자바코드 : @Value
          1. @Configuration, @Bean을 사용하는 경우에도 프로퍼티값을 외부로 독립 시킬 수 있다.
          

8. PropertyEditor 와 ConversionService
 - @Value , XML의 value attribute는 String외 타입에 대해서는 타입을 변경하기 위한 두 가지 종류의 타입변환 서비스를 제공한다.
 8-1. PropertyEditor 
    - java.beans 구현
    - 자바빈에서 차용하여 스프링에서 제공되는 default로 사용되는 타입 변환기
   
 8-2. ConversionService 
    - Spring3.0부터 나옴.
    - 스프링에서 직접 제공하는 타입변환 API.
    - PropertyEditor와 달리 멀티 스레드 환경에서 공유해서 사용할 수 있다.
    
9. 컬렉션
  - @Value나 value attribute로 단순 오브젝트 및 일부 타입의 배열을 전달가능.
  - 컬렉션 타입으로 값을 넣기 위해서는 컬렉션 선언용 태그를 이용해야 한다.(<property>의 value attribte가 생략됨)
  - Collection.xml 참고.
  
       
      