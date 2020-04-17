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
      - HttpMessageConverter 타입의 메세지 변환기(message converter)가 여러개 등록되어 있음
         └ StringHttpMessageConverter (String)
         └ MarshallingHttpMessageConverter (XML)
         └ MappingJacksonHttpMessageConverter (JSON)
      
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

 3.4 뷰 
  컨트롤러가 작업을 마친후 뷰정보를 ModelAndView 타입 오브젝트에 담아서 DispatcherServlet에 돌려주는 방법2가지
    - View 타입의 오브젝트 반환
    - 뷰 이름 반환(뷰 이름으로부터 실제 사용할 뷰를 결정해주는 view resolver 필요하며 디폴트로 등록된 뷰리졸버(InternalResourceViewResolver)를 사용한다.) 
  
  3.4.1 뷰 사용방법
    - 스프링이 제공하는 기반 뷰 클래스를 확장해서 코드로 뷰를 만듬
    - 스프링이 제공하는 뷰를 사용하되 뷰 클래스 자체를 상속하거나 코드를 작성하지 않고, JSP나 프리마커 같은 템플릿 파일을 사용하거나
      모델을 자동으로 뷰로 전환하는 로직을 적용하는 방법
    - 스프링의 view 구현 클래스는 모두 멀티스레드에서 공유가능하니 싱글톤 빈으로 등록해서 사용해도 된다.

    ● InternalResourceView (DEFAULT)
     - UrlBasedViewResolver의 서브클래스
     - JSTL 라이브러리가 존재하지 않을 경우
     - RequestDispatcher의 forward()나 include()를 이용.
     - Controller가 돌려준 뷰이름을 포워딩할 jsp의 이름으로 사용하고 모델정보를 요청 attribute에 넣어주는 작업을 InternalResourceView와 DispatcherServlet이 대신함.

    ● JstlView (DEFAULT)
     - Jstl로 기술한 JSP를 표시할 경우 사용.
     - InternalResourceView 을 상속한 View 클래스
     
	● InternalResourceViewResolver
	 - InternalResourceView와 JstlView를 이용하기 위한 View Resolver
	 
	● RedirectView 
	 - HttpServletResponse의 sendRedirect()를 호출해주는 기능을 가진 view
	 - 실제 View가 생성되는게 아니라 url만 만들어져 다른 페이지로 리다이렉트 된다.
	 
	● VelocityView, FreeMarkerView
	 
	● MarshallingView
	  - spring 3.0에 새롭게 등장한 OXM(object-XML Mapping) 추상화 기능을 활용해서 application/xml 타입의 XML콘텐트를 작성하게 해줌.
	  	   
	● AbstractExcelView, AbstractJExcelView, AbstractPdfView
	  - 엑셀과 pdf를 만들어 주는 view
      - AbstractExcelView : Apache POI 라이브러리를 이용해 엑셀 뷰 생성
      - AbstractJExcelView : JExcelAPI 이용 엑셀 문서 생성
      - AbstractPdfView : iText 프레임워크 API로 PDF문서를 생성해줌.
      
	● TilesView
	
	● MappingJacksonJsonView
	  - Ajax에서 많이 사용되는 JSON 타입의 콘텐트를 작성해주는 뷰.
	  - 모델의 모든 오브젝트를 JSON으로 변환한다.
	  - 
	
  3.4.2 뷰 리졸버
     - HandlerMapping이 요청 URL로부터 컨트롤러를 찾아주는 것처럼, 뷰 이름으로부터 사용할 뷰 오브젝트를 찾아준다.
     - 뷰 리졸버는 ViewResolver인터페이스를 구현해서 만든다.
     - 뷰 리졸버를 여러 개 빈으로 등록해서 이용할 경우에는 order 프로퍼티를 이용해서 적용 순서 지정 가능.
 
 	● InternalResourceViewResolver
 	 - order 프로퍼티에는 기본적으로 Integer.MAX값이 있어서 order를 지정하지 않아도 제일 마지막에 사용된다.
 	 
 	● VelocityViewResolver, FreeMarkerViewResolver
	 	      
 3.5 기타 전략
   3.5.1 핸들러 예외 리졸버(HandlerExceptionResolver)
    - 총4개의 HandlerExceptionResolver 구현 전략을 제공하며 3개는 디폴트로 설정되어 있음.
    
 	● AnnotationMethodHandlerExceptionResolver
 	  - 예외가 발생한 컨트롤러 내의 메소드중에서 @ExceptionHandler 애노테이션이 붙은 메소드를 찾아 예외처리를 맡겨주는 핸들러 예외 리졸버
 	  - Spring3.0에 새로 추가
 	  
 	● ResponseStatusExceptionResolver
 	  - 예외를 특정 Http 응답상태 코드로 전환.
 	  - 단점은 직접 @ResponseStatus를 붙일수 있는 예외클래스를 만들어야 한다. 따라서 기존에 정의된 예외클래스에는 바로 적용할 수 없음.
 	  
 	● DefaultHandlerExceptionResolver
 	  - 스프링에서 내부적으로 발생하는 주요 예외를 처리 해주는 표준예외처리 로직을 담고 있음.
 	  ex) NoSuchRequestHandleMethodException -> HTTP404 - Not Found 
 	      TypeMismatchException -> HTTP 400 - Bad Request
 	  - 스프링 내부에서 발생하므로 크게 신경쓰지 않아도 되지만 다른 핸들러 예외 리졸버를 빈으로 등록해서 DefaultHandlerExceptionResolver 가
 	    작동하지 않을 경우에는 함께 등록 해주는 것이 좋다.

 	● SimpleMappingExceptionResolver
 	  - web.xml의 <error-page>와 비슷하게 예외를 처리할 뷰를 지정할수 있게 해준다.
 	  - default전략이 아니므로 직접 빈으로 등록해야 한다.
 	  - 실제로 사용하기 편함.
 	  
	3.5.2 지역정보 리졸버 (LocalResovler)
	3.5.3 멀티파트 리졸버	  
	  - 파일업로드와 같이 멀티파트 포맷의 요청정보를 처리하는 전략
	  - Apache Commons의  FileUpload 라이브러리를 사용하는 CommonsMultipartResolver 한가지만 지원됨.
	  - bean으로 등록해 줘야함.
	  
 	● RequestToViewNameTranslator
 	  - 컨트롤러에서 뷰이름이나 뷰 오브젝트를 돌려주지 않았을 경우 Http요청정보를 참고하여 뷰 이름을 생성해준다.
 	  - 디폴트로 DefaultRequestToViewNameTranslator가 등록되어 있음.
 	  - URL을 기준으로 뷰 이름을 결정
 	    
4. 스프링 @MVC
    AnnotationMethodHandlerAdapter 가 호출하는 @Controller 메소드의 사용가능한 파라미터 타입과 애노테이션 종류
    
    4-2-1 메소드 파라미터의 종류
     ● HttpServletRequest, HttpServletResponse
     ● HttpSession
     ● WebRequest, NativeWebRequest
	 ● Locale
	 ● InputStream, Reader 
	 ● OutputStream, Writer
	 ● @PathVariable
	 ● @RequestParam
	 ● @CookieValue
	   - 쿠키값이 반드시 존재해야 하며, 없을 경우에도 예외가 발생하지 않게 할려면 required=false
	   - defaultValue선언도 가능.
	 ● @RequestHeader
	 ● Map,Model,ModelMap
	   - 파라미터로 정의해서 핸들러 어댑터에서 미리 만들어서 제공해준다.
	 ● @ModelAttribute 
	   - 별도의 설정 없이도 자동으로 뷰에 전달됨.
	   - 컨트롤러가 전달받는 오브젝트 형태의 정보를 가르킨다.
	   - 하나의 오브젝트에 클라이언트의 요청정보를 담아서 한번에 전달되는 것이기 때문에 이를 Command Pattern에서 말하는 Command Object라고도 함.
	 ● Erros, BindingResult
	 ● SessionStatus
	 ● @RequestBody
	 ● @Value
	 ● @Valid
	 	 
    4-2-2 리턴 타입의 종류	 
     - 컨트롤러가 핸들러 어댑터를 거쳐서 DispatcherServlet에 돌려줘야 하는 정보는 Model 과 View
     - handlerAdapter를 거쳐서 최종적으로 DispatcherServlet로 돌아갈 때는 ModelAndView 타입으로 리턴값이 전달됨. 
     - Model은 맵에 담긴 정보
         		
      4-2-2-1. 자동 추가 모델 오브젝트와 자동생성 뷰 이름 
        - 메소드 리턴 타입에 상관없이 조건만 맞으면 모델에 자동 추가됨
        
	 	1) @ModelAttribute 모델 오브젝트 또는 커맨드 오브젝트            
	    - 자동으로 컨트롤러가 리턴하는 모델에 추가됨.
	    - 기본적으로 모델 오브젝트 이름은 파라미터 타입이름.
	 
		 2) Map,Model,ModelMap 파라미터
	    - 컨트롤러 메소드에 Map,Model,ModelMap 타입의 파라미터를 사용하면 미리 생성된 모델 맵 오브젝트를 전달받아서 오브젝트를 추가가능.
	    - 파라미터에 추가한 오브젝트는 DispatcherServlet를 거쳐서 뷰에 전달되는 모델에 자동 추가된다.
	    
	 	3) @ModelAttribute 메소드
	    - @RequestMapping과 같이 사용하지 않는다.
	    - codes라는 이름으로 다른 컨트롤러가 실행될때 모델에 자동 추가된다.
	    
	    ex)
	      @ModelAttribute("codes")
	      public List<Code> codes(){
	      	return codeService.getAllCodes();
	      } 
	      
	     4) BidingResult	      
	      
	 ● ModelAndView	
	   - 자주 사용하지는 않음.
	 
	 ● String
	   - 메소드 리턴 타입이 String이면 리턴값은 뷰 이름으로 사용됨.	 
	 
	 ● void	 
	   - RequestToViewNameResolver전략을 통해 자동생성되는 뷰 이름이 사용됨.
	   - 디폴트로 등록된 RequestToViewNameResolver은 URL을 따라서 뷰이름을 만들어준다. 

	 ●  Model Object
	   - 뷰 이름은 RequestToViewNameResolver로 자동 생성하는 것을 사용하고 코드를 이용해 모델에 추가할 오브젝트가 하나뿐이라면, 오브젝트를 바로 리턴해도 가능.
  	   - 스프링은  리턴 타입이 미리 지정된 타입이나 void가 아닌 단순 오브젝트라면 이를 모델 오브젝트로 인식해서 모델에 자동으로 추가해준다.
  	   
  	  ex) "user" 이름으로 모델에 추가됨.
  	  
  	  @RequestMapping("/view")
  	  public User view(@RequestParam int id){
  	  	return userService.getUser(id);
  	  }
  	  
	 ●  Map/Model/ModelMap
	   - 메소드의 코드에서 Map,Model,ModelMap타입의 오브젝트를 직접 만들어서 리턴시 이 오브젝트는 모델로 사용된다.
	   - Map타입의 단일 오브젝트 리턴은 피해야함
	   
	 ●  View
	 
	 ●  @ResponseBody 
	    - 모델로 사용되는 대신 메세지 컨버터를 통해 바로 HTTP응답의 메세지 본문으로 전환된다.	
  
    4-2-3 @SessionAttribute와  SessionStatus
       - HTTP요청에 의해 동작하는 서블릿은 기본적으로 상태를 유지하지 않기 때문에, 메소드 요청이 독립적으로 처리된다.
       - 하나의 HTTP요청을 처리한 후에는 사용했던 모든 리소스를 정리함.
       - 여러 요청이 들어올 경우, 각 요청에 대해 서블릿은 스레드를 생성해서 service()메소드를 호출한다.
         └ WAS의 스레드풀을 이용하여 스레드를 생성하고, 해당 스레드에 서블릿을 할당하여 싱글톤 패턴의 서블릿 클래스의 service()메소드를 실행하여 서블릿이 쓰레드로 실행되도록 한다.
         
      4-2-3-1. 도메인 중심 프로그래밍 모델과 상태유지를 위한 세션 도입의 필요성
        - 스프링의 세션지원기능은 기본적으로 HTTP세션 사용.

		● @SessionAttribtes 
		   - ex) @SessionAttributes("user") -> users는 폼의 정보를 담을 모델 이름.
	       - 기능1. 컨트롤러 메소드가 생성하는 모델정보 중에서 @SessionAttributes에 지정한 이름과 동일한 것이 있다면 이를 세션에 저장한다.
	                           모델정보에 담긴 오브젝트중에서 세션 애트리뷰트라고 지정한 모델이 있으면 이를 자동으로 세션에 저장해준다.
	       - 기능2. @ModelAttribute가 지정된 파라미터가 있을때 이 파라미터에 전달해줄 오브젝트를 세션에서 가져오는 것.
	       - 클래스의 모든 메소드에 적용된다.
	       
		● SessionStatus 
		   - 더 이상 세션을 이용하지 않을경우 setComplete() 메소드를 호출해서 세션을 제거해 줘야 한다.
			  
  4.3 모델 바인딩과 검증
    Controller method에 @ModelAttribute가 지정된 파라미터를 @Controller 메소드에 추가시 동작방식.
      
      1. 파라미터 타입의 오브젝트 생성
        - @ModelAttribute User user 일 경우 User타입 오브젝트 생성 
        - default생성자 필수
        - @SessionAttributes에 의해 세션에 저장된 모델 오브젝트가 있을 경우에는 오브젝트 생성 대신, 세션에 저장된 오브젝트를 가져온다.
      2. 준비된 모델 오브젝트의 프로퍼티에  웹 파라미터를 바인딩.
        - 스프링의 기본에디터 이용 HTTP 파라미터 값을 모델의 프로퍼티 타입에 맞게 전환하고, 전환 불가능시 BindResult 오브젝트안에 바인딩 오류를 저장해서 컨트롤러로 넘겨주거나 예외를 발생시킨다.
      3. 모델의 값을 검증한다.
        - 바인딩에서 타입 검증뿐 아니라 그외 검증도 가능(ex. 필수프로퍼티에 null, 숫자지정범위초과)  
        - 여기서 바인딩은 오브젝트의 프로퍼티에 값을 넣는것을 의미한다.       
        
	4.3.1 스프링 바인딩 과정에서 변환작업을 위해 제공하는 2종류의 API.
	   ● PropertyEditor
	      - 변환을 위해 사용되는 메소드는 총4가지
	         └ HTTP요청 파라미터와 같은 문자열은 스트링 타입으로 서블릿에서 가져옴 
	          └ setAsText() : 메소드를 이용해 스트링 타입의 문자열을 넣고  getValue() : 변환된 오브젝트를 가져옴
	          └ setValue() : 오브젝트를 넣고 getAsText() 메소드로 변환된 문자열 가져옴.
	   
	      ※ 컨트롤러 메소드의 바인딩 원리
	       1. @Controller 메소드는 호출해줄 책임이 있는 AnnotationMethodHandlerAdapter는 @RequestParam, @ModelAttribute, @PathVariable 등처럼 HTTP요청을 파라미터 변수에
	                 바인딩 해주는 작업이 필요한 애노테이션을 만나면 먼저 WebDataBinder를 생성.
	       2. WebDataBinder는 HTTP요청으로부터 가져온 문자열을 파라미터 타입의 오브젝트로 변환하는 기능도 포함 ( PropertyEditor 를 이용 ) 
	       3. 커스텀 프로퍼티에디터를 @RequestParam같으 메소드 파라미터 바인딩에 적용할려면 WebDataBinder에 프로퍼티 에디터를 직접 등록해야 함. 
	       4. WebDataBinder는 내부적으로 만들어지기 때문에, 스프링이 제공하는 WebDaataBinder 초기화 메소드인 @InitBinder를 사용함.
	   
	    ● @InitBinder
	      - WebDataBinder 바인딩 적용 대상 - @RequestParam parameter, @CookieValue parameter, @RequestHeader parameter, @PathVariable Parameter, @ModelAttribute parameter
	      
	   
      
        
 	