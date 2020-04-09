ㄴ개요
 토비의 스프링3.1 Vol.2 스프링의 기술과 선택 3장 스프링웹기술과 스프링 MVC Cahpter
 
 3. 스프링 웹 프레젠테이션 계층 기술 
  root-context   - 웹 기술에서 완전히 독립적인 비지니스계층(service),데이터액세스계층(dao)
  servlet-context - 스프링 웹기술을 기반으로 동작하는 웹 관련 빈을 담음 ( jsp, controller )
  
  - context 분리의 이유는 스프링 웹 서블릿 컨텍스트를 통째로 다른 기술로 대체하기 위해서.
   
  3.1 스프링 제공 웹프레임 워크
    - spring servlet/mvc ( front controller 역할을 하는 DispatcherServlet을 핵심 엔진으로 이용 ) 
  
  3.1.2 스프링 MVC와 DispatcherServlet 전략
     DispatcherServlet
       - 스프링 웹 기술의 핵심이자 기반.
       - 스프링 웹 기술을 구성하는 다양한 전략을 DI로 구성해서 확장하도록 만들어진 서블릿/MVC의 엔진과 같은 역할.
       - 핸들러 어댑터 전략을 통해서 적절한 controller를 찾고 모든 웹 요청 정보가 담긴 HttpServletRequest 타입의 오브젝트를 전달한다.
       - 서블릿 컨테이너가 생성하고 관리하는 오브젝트이지, 스프링의 컨텍스트에서 관리하는 빈 오브젝트가 아님.
     
     ModelAndView
       - Controller는 DispatcherServlet(front controller)에게 최종적으로 뷰와 프레젠테이션 계층의 구성요소를 정보로 담은 모델을 리턴 시켜 주는데, 
         ModelAndView 이러한 컨트롤러로부터 돌려받는 오브젝트
         
     DispatcherServlet DI 가능한 전략
       ● HandlerMapping ( interface ) 
          - URL과 요청정보를 기준으로, 어떤 Controller를 이용할 것인지를 결정하는 로직 담당
          - DEFAULT : BeanNamedUrlHandlerMapping , DefaultAnnotationHandlerMapping 
       ● HandlerAdapter
          - Controller 타입에 적합한 어댑터를 이용해서 Controller호출.
          - DEFAULT : HttpRequestHandlerAdapter, SimpleControllerHandlingAdapter, AnnotationMethodHandlerAdapter
       ● HandlerExceptionResolver
           - DEFAULT : AnnotationMethodHandlerExceptionResolver, ResponseStatusExceptionResolver, DefaultHanlderExceptionResolver
       ● ViewResolver
           - Controller가 리턴한 뷰이름을 참고하여 뷰오브젝트를 찾아줌.
           - DEFAULT : InternalResourceViewResolver 
       ● LocaleResolver
       ● ThemeResolver
       ● RequestToViewNameTranslator
       
     SimpleControllerHandlingAdapter
       - Controller Interface를 구현한 핸들러/컨트롤러를 사용함.

     @Controller, @RequestMapping
       - DefaultAnnotationHandlerMapping 에 의해 Controller가 결정되고 AnnotationMethodHandlerAdapter에 의해 DispatcherServlet에서 호출이 일어남.
     
     InternalResourceViewResolver
       - Servlet이나 JSP같이 RequestDispatcher에 의해 포워딩 될 수 있는 리소스를 뷰로 사용    
       
3.2 스프링 웹 학습테스트
  - 서블릿 테스트용 목 오브젝트가 제공된다.
  - API : org.springframework.mock.web package
         
3.3 컨트롤러(controller)
  - 스프링에서는 컨트롤러를 handler라고 부르기도 함.
  - 사용자 요청을 기준으로 어떤 컨트롤러(handler)에게 작업을 위임할지를 결정 해주는 것을 handler mapping 전략이라고 함.
  
 3.3.1 컨트롤러의 종류와 핸들러 어댑터
  - 사용자 요청을 dispatcherServlet이 handler mapping을 통해 선정되어야 할 controller를 정한다. -> handler Mapping으로 controller가 정해지면 이를 HandlerAdapter를 통해
     컨트롤러를 호출하게 됨.
    
  - Hanlder Adapter를 빈으로 등록시 DispatcherServlet은 이를 자동으로 감지하여 디폴트 핸들러 어댑터 대신 사용.
    
    ● Servlet과 SimpleServletHandlerAdapter
       - javax.servlet.Servlet을 구현한 서블릿 클래스도 스프링MVC의 controller로 사용될 수 있음.
       
    ● HttpRequestHandler와 HttpRequestHandlerAdapter
       - HttpRequestHandler는 interface로 정의된 컨트롤러 타입이므로 이를 구현해서컨트롤러를 만든다.
       - Servet Interfac와 비슷하며, 실제로 서블릿처럼 동작하는 컨트롤러를 만들때 이용한다.
       - DEFAULT전략이라 별도의 빈 등록이 필요 없다.
     
    ● Controller와 SimpleControllerHandlerAdapter  
       - Controller Interface를 직접 구현하는 것은 권장되지 않음
       - 적어도 웹브라우저를 클라이언트로 갖는 컨트롤러의 필수 기능이 구현되어 있는 AbstractController를 구현해서 만드는게 나음.
     
    ● AnnotationMehtodHandlerAdatper
      - 지원하는 컨트롤러 타입이 정해져 있지 않다.
      - 메소드에 붙은 몇가지 애노테이션 정보와 메소드 이름, 파라미터, 리턴 타입에 대한 규칙 등을 종합적으로 분석해서 컨트롤러를 선발하고 호출 방식을 결정.
      - 컨트롤러 하나가 하나 이상의 url에 매핑될 수 있음.
      - DefaultAnnotationHandlerMapping 와 같이 사용해야 함.
      - 제일 많이 이용됨.
      - ex) 
      @Controller
      public class HelloController{
      	@RequestMapping("/hello")
      	public String hello(@RequestParam("name") String name, ModelMap map){
      		map.put("message","hello " + name);
      		return "hello";
      	}
      }
      
 3.3.2 핸들러 매핑
    - HTTP 요청정보를 이용해서 이를 처리할 핸들러오브젝트 즉, 컨트롤러를 찾고 핸들러 실행 체인(HandlerExecutionChain) 을 돌려준다.
    - HandlerMapping은 컨트롤러의 타입과 상관없이 여러 가지 타입의 컨트롤러를 선택할 수 있다.
    

    3.3.2.1 스프링이 제공하는 5가지 핸들러매핑.
     DEFAULT : BeanNamedUrlHandlerMapping DefaultAnnotationHandlerMapping
    
    ● BeanNamedUrlHandlerMapping 
      - 빈의 이름에 들어 있는 URL을 HTTP 요청의 URL과 비교해서 일치하는 빈을 찾는다.
      - bean이름에 매핑정보를 넣기 때문에 매핑정보를 관리하기 불편하다는 단점 발생.     
      - 복잡한 application에서는 사용하지 않는다.
      
    ● ControllerBeanNameHandlerMapping
       - 빈의 아이디나 빈 이름을 이용해 매핑한다.
       - 디폴트 빈이 아니므로 XML에 별도로 설정해줘야 하며, 특정 handlerMapping을 빈으로 등록하게 되면 default hanlder mapping은 적용되지 않는다.
       
    ● ControllerClassNameHandlerMapping 
      - 빈 이름 대신 클래스 이름을 URL에 매핑.
                     
    ● SimpleUrlHandlerMapping 
      - URL과 컨트롤러의 매핑정보를 한곳에 모아놓을  수 있음.
      - 매핑정보는 SimpleUrlHandlerMapping빈의 프로퍼티에 넣는다.
      
    ● DefaultAnnotationHandlerMapping
      - @RequestMapping이라는 어노테이션을 컨트롤러 클래스나 메소드에 직접 부여하고 이를 이용해 매핑하는 전략.
 
  3.3.3 핸들러 인터셉터(Handler Interceptor)
      - DispatcherServlet이 컨트롤러를 호출하기 전과 후에 요청과 응답을 참조하거나 가공할 수 있는 일종의 Filter.
      - Handling Mapping의 결과 HandlingExecutionChain을 돌려받고 핸들러 실행 채인은 하나 이상의 핸들러 인터셉터를 거쳐서 컨트롤러가 실행될 수 있도록 구성되어 있다.  
      - 핸들러 인터셉터 자체가 bean
      - HttpServletRequest,HttpServletResponse, 실행될 컨트롤러 빈 오브젝트, 컨트롤러가 돌려주는 ModelAndView, 발생한 예외등을 제공받을 수 있기 때문에 서블릿필터보다 훨씬 정교하고 편함.               

		      
      
