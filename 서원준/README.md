
### AWS 아키텍쳐

제어플레인(Control Plane)

- 컨테이너를 관리하는 기능
- ECS
    - Elastic Container Service 으로 완전관리형 컨테이너 서비스 솔루션
    - 구성요소
        - 테스크(Task)
            
            컨테이너가 동작하는 컴포넌트
            
        - 테스크 정의(Task Definition)
            
            테스크를 만드는 템플릿 정의
            
            - 서비스(Service)
            
            지정한 수만큼 테스크를 유지하는 스케줄러
            
            테스크의 배포 방식 결정
            
        - 클러스터(Cluster)
            
            서비스와 테스크를 실행하는 논리 그룹
            
- EKS
    - Elastic Kubernets Service로 완전관리형 쿠버네티스 서비스
    - 

데이터 플레인(Data Plane)

- 컨테이너가 실제로 동작하는 지원환경
- EC2
    - Elastic Compute Cloud로 AWS에서 가상머신을 이용한 서비스
    - 필요에 따라 사양을 변경할 수 있음
- Fargate
    - 컨테이너용 서버리스 컴퓨팅 엔진
    - 서버 운영 오버헤드가 없음