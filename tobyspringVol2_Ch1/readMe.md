
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
  
 
      
   
 