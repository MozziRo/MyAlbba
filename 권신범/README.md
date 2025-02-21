**WebRTC API**
WebRTC 표준은 크게 두 가지 기술, 즉 미디어 캡처 기기와 P2P 연결을 다룹니다.

미디어 캡처 기기에는 동영상 카메라와 마이크는 물론 화면 캡처 및 기기도 포함됩니다. 카메라와 마이크의 경우 navigator.mediaDevices.getUserMedia()를 사용하여 MediaStreams를 캡처합니다. 화면 녹화에는 navigator.mediaDevices.getDisplayMedia()를 대신 사용합니다.

P2P 연결은 RTCPeerConnection 인터페이스에서 처리합니다. 이는 WebRTC에서 두 피어 간의 연결을 설정하고 제어하는 중심점입니다.



**미디어 기기 시작하기**
웹용으로 개발할 때 WebRTC 표준은 컴퓨터 또는 스마트폰에 연결된 카메라 및 마이크입니다. 이 기기 일반적으로 미디어 기기라고 하며 JavaScript를 사용하여 액세스할 수 있습니다. MediaDevices를 구현하는 navigator.mediaDevices 객체를 통해 인터페이스에 추가되었습니다. 이 객체에서 연결된 모든 장치를 열거하고 기기가 변경되거나 (기기가 연결되거나 연결 해제되었을 때) 기기가 열릴 때 를 호출하여 미디어 스트림을 검색합니다 (아래 참조).

가장 일반적인 방법은 getUserMedia() 함수를 통하는 것입니다. 일치하는 미디어의 MediaStream로 확인되는 프로미스를 반환합니다. 기기에서 사용할 수 있습니다. 이 함수는 단일 MediaStreamConstraints 객체를 사용합니다. Google의 요구사항을 나타냅니다 예를 들어 간단히 기본 마이크와 카메라가 있다면 다음과 같이 합니다.

- promise 사용용
const constraints = {
    'video': true,
    'audio': true
}
navigator.mediaDevices.getUserMedia(constraints)
    .then(stream => {
        console.log('Got MediaStream:', stream);
    })
    .catch(error => {
        console.error('Error accessing media devices.', error);
    });

- ASYNC/AWAIT 사용
const openMediaDevices = async (constraints) => {
    return await navigator.mediaDevices.getUserMedia(constraints);
}

try {
    const stream = openMediaDevices({'video':true,'audio':true});
    console.log('Got MediaStream:', stream);
} catch(error) {
    console.error('Error accessing media devices.', error);
}

getUserMedia()를 호출하면 권한 요청이 트리거됩니다. 사용자가 권한을 허용하면 프로미스가 다음을 포함하는 MediaStream로 확인됩니다. 동영상 1개와 오디오 트랙 1개가 있어야 합니다. 권한이 거부되면 PermissionDeniedError이 발생합니다. 일치하는 기기가 없는 경우 연결되면 NotFoundError이 발생합니다.


## NAT(Network Address Translation)
'나'는 누구인지 '이름'으로 구별할 수 있듯이 각 기기에도 자신만의 고유한 이름이 있다. 그것이 바로 IP이고 이 IP는 고정IP, 유동IP 로 나뉘어서
실제 고유의 값일 수도 있고 아닐 수도 있다. 회사망/내부망 (LAN)은 Private IP이기 때문에 다른 네트워크 (구글, 외부 웹사이트)등 에서는 통용되지 않는다.
그렇기 때문에 네트워크에서 데이터를 주고 받기 위해선  Public IP가 필요하다.

**NAT은 Private IP를 Public IP로 1대1 대응시켜 변환하는 장치를 말한다.**
또한, IP주소를 재기록 하면서 라우트를 통해 네트워크 트래픽을 주고 받는 매커니즘이다.

WebRTC 통신은 Peer to Peer 방식으로 서로 데이터를 주고 받아야 하기 때문에 보내고 받는 Peer의 정보 (Public IP)를 알고 있어야 한다.
그러나 Public IP는 요청을 보낼때마다 Private IP -> Public IP로 NAT에 의해 바뀌기 때문에 동일한 Public IP로 통신할 수 없는 문제점이 발생한다.

**STUN/TURN 서버를 사용해서 문제를 해결할 수 있다.**
STUN(Session Traversal Utilities for NAT)
공개 주소를 발견하거나 peer간의 직접 연결을 막는 등 라우터의 제한을 결정하며 ICE를 보완하는 프로토콜이다.
즉, STUN 서버는 해당 Peer의 Public IP주소를 보내는 역할을 한다. 

## Annotation 종류
**@ComponentScan**
: @Component와 @Service, @Repository, @Controller, @Configuration이 붙은 클래스 Bean들을 찾아서 Context에 bean등록을 해주는 Annotation이다.
Spring에서 @Component로 다 쓰지 않고 @Repository, @Service, @Controller등을 사용하는 이유는, 예를들어 @Repository는 DAO의 메소드에서 발생할 수 있는 unchecked exception들을 스프링의 DataAccessException으로 처리할 수 있기 때문이다.


**@ComponentScan** : @Component와 @Service, @Repository, @Controller, @Configuration이 붙은 클래스 Bean들을 찾아서 Context에 bean등록을 해주는 Annotation이다.
@Component Annotation이 있는 클래스에 대하여 bean 인스턴스를 생성
ApplicationContext.xml에 <bean id="jeongpro" class="jeongpro" /> 과 같이 xml에 bean을 직접등록하는 방법도 있고 위와 같이 Annotation을 붙여서 하는 방법도 있다.

**@Component** : @Component와 @Service, @Repository, @Controller, @Configuration이 붙은 클래스 Bean들을 찾아서 Context에 bean등록을 해주는 Annotation이다.
@Component Annotation이 있는 클래스에 대하여 bean 인스턴스를 생성
ApplicationContext.xml에 <bean id="jeongpro" class="jeongpro" /> 과 같이 xml에 bean을 직접등록하는 방법도 있고 위와 같이 Annotation을 붙여서 하는 방법도 있다.

**@Bean**
@Bean은 개발자가 직접 제어가 불가능한 외부 라이브러리등을 Bean으로 만들려할 때 사용되는 Annotation이다. 1개의 외부 라이브러리로 부터 생성한 객체를 등록 시 사용 (new로 객체를 생성 후 직접 bean으로 등록할 때 사용)
@Configuration
public class ApplicationConfig {    
    @Bean
    public ArrayList<String> array(){
        return new ArrayList<String>();
    }   
}
ArrayList같은 라이브러리등을 Bean으로 등록하기 위해서는 별도로 해당 라이브러리 객체를 반환하는 Method를 만들고 @Bean Annotation을 사용하면 된다.
위의 경우 @Bean에 아무런 값을 지정하지 않았으므로 Method 이름을 camelCase로 변경한 것이 Bean id로 등록된다.method 이름이 arrayList()인 경우 arrayList가 Bean id
@Configuration
public class ApplicationConfig {    
    @Bean(name="myarray")
    public ArrayList<String> array(){
        return new ArrayList<String>();
    }   
}
위와 같이 @Bean에 name이라는 값을 이용하면 자신이 원하는 id로 Bean을 등록할 수 있다.Configuration 클래스에서 외부라이브러리 관련 객체를 미리 객체화 시켜놓을 때 많이 사용한다.

**@Autowired**
속성(field), setter method, constructor(생성자)에서 사용하며 Type에 따라 알아서 Bean을 주입 해준다.무조건적인 객체에 대한 의존성을 주입시킨다.이 Annotation을 사용할 시, 스프링이 자동적으로 값을 할당한다.Controller 클래스에서 DAO나 Service에 관한 객체들을 주입 시킬 때 많이 사용한다.
즉, Dependency Injection를 위한 곳에 사용된다.
 
필드, 생성자, 입력 파라미터가 여러 개인 메소드(@Qualifier는 메소드의 파라미터)에 적용 가능하다.
Type을 먼저 확인한 후 못 찾으면 Name에 따라 주입한다.

**@Controller와 @RestController의 차이**
: @ControllerAPI와 view를 동시에 사용하는 경우에 사용한다.대신 API 서비스로 사용하는 경우는 @ResponseBody를 사용하여 객체를 반환한다.view(화면) return이 주목적이다.
@RestControllerview가 필요없는 API만 지원하는 서비스에서 사용한다.Spring 4.0.1부터 제공@RequestMapping 메서드가 기본적으로 @ResponseBody 의미를 가정한다.data(json, xml 등) return이 주목적이다.

즉, @RestController = @Controller + @ResponseBody 이다.


