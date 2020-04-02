개요
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
          - DEFAULT : BeanNameUrlHandlerMapping , DefaultAnnotationHandlerMapping 
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
       
     @Controller, @RequestMapping
       - DefaultAnnotationHandlerMapping 에 의해 Controller가 결정되고 AnnotationMethodHandlerAdapter에 의해 DispatcherServlet에서 호출이 일어남.
     
     InternalResourceViewResolver
       - Servlet이나 JSP같이 RequestDispatcher에 의해 포워딩 될 수 있는 리소스를 뷰로 사용    
       