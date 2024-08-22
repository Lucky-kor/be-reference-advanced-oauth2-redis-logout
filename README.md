# Redis를 통한 OAuth2, 토큰 기반 인증 방식에서의 로그아웃 구현

### Description
Redis를 이용한 로그아웃 기능을 구현한 코드는 기본적으로 Spring Security에서 OAuth2 기반의 코드를 이용해 로그아웃 기능을 구현하고 있습니다.
해당 기능을 구현하기 위해서는, 로컬 환경에 Redis가 설치되어 있어야 합니다.
* [로그아웃 구현 관련 코드](#로그아웃-구현-관련-코드)

> 예제 코드에 대한 더 구체적인 정보는 아래에서 확인하세요.

---

### 로그아웃 구현 관련 코드
Redis를 적용해 로그아웃 기능을 구현한 예제 코드입니다.
추가된 내용이 많으니, 직접 구현하시려면 아래 작성된 순서를 잘 따라가며 코드를 작성하셔야 합니다.
* 소스 코드 경로
    * Spring Data Redis 의존성 추가
        * [build.gradle](https://github.com/Lucky-kor/be-reference-advanced-oauth2-redis-logout/blob/main/build.gradle)
    * Redis 실행을 위한 정보를 application.yml에 추가하는 부분
        * [src/main/resources/application-local.yml](https://github.com/Lucky-kor/be-reference-advanced-oauth2-redis-logout/blob/main/src/main/resources/application.yml)
    * Redis와의 연결을 설정하고, Redis 템플릿을 통해 Redis 서버와 데이터를 주고받을 수 있도록 구성하기 위한 클래스
        * [src/main/java/com/springboot/redis/RedisRepositoryConfig.java](https://github.com/Lucky-kor/be-reference-advanced-oauth2-redis-logout/blob/main/src/main/java/com/springboot/redis/RedisRepositoryConfig.java)
    * Jwt Token을 관리하는 클래스입니다.
      1. generateAccessToken, generateRefreshToken 메서드에서 토큰을 생성 후, Redis에 토큰을 저장하는 로직이 추가되었습니다.
      2. 로그아웃시, Redis에서 토큰을 삭제하는 deleteRegisterToken 메서드가 추가되었습니다.
      * [src/main/java/com/springboot/oauth2_jwt/jwt/JwtTokenizer.java]([https://github.com/codestates-seb/be-reference-send-email/blob/28e43990e300dd06487ba153c4ec64d98278292c/src/main/java/com/codestates/helper/email/SimpleEmailSendable.java](https://github.com/Lucky-kor/be-reference-advanced-oauth2-redis-logout/blob/main/src/main/java/com/springboot/oauth2_jwt/jwt/JwtTokenizer.java))
    * OAuth2 인증 후, Tokenizer 객체를 사용해 토큰을 생성하는 로직이 담긴 필터 클래스입니다.
      1. refreshToken 생성시, Redis에서 Key로 사용하기 위해 accessToken을 함께 전달하는 코드가 추가되었습니다.
      2. 다른 키를 사용한다면 추가하지 않아도 괜찮습니다. 다만, JwtTokenizer.java 파일도 함께 수정해야 합니다.
      * [src/main/java/com/springboot/oauth2_jwt/auth/handler/OAuth2MemberSuccessHandler.java](https://github.com/Lucky-kor/be-reference-advanced-oauth2-redis-logout/blob/main/src/main/java/com/springboot/oauth2_jwt/auth/handler/OAuth2MemberSuccessHandler.java)
    * 로그인 후, 전달하는 토큰을 검증하는 JwtVerificationFilter 필터 클래스입니다.
      1. redis에서 추가 검증을 위해 RedisTemplate를 생성자 주입을 통해 전달받습니다.
      2. Redis에서 토큰을 검증하는 isTokenValidInRedis 메서드 추가
      3. doFilterInternal 메서드에 토큰의 시그니처를 검증 후, isTokenValidInRedis 메서드를 통해 레디스에 토큰 여부까지 검증
      * [src/main/java/com/springboot/oauth2_jwt/auth/filter/JwtVerificationFilter.java](https://github.com/Lucky-kor/be-reference-advanced-oauth2-redis-logout/blob/main/src/main/java/com/springboot/oauth2_jwt/auth/filter/JwtVerificationFilter.java)
    * Spring Security 관련 설정을 관리하는 클래스입니다.
      1. RedisTemplate 객체를 생성자를 통해 DI받고 있습니다.
      2. JwtVerificationFilter 생성자에 RedisTemplate 전달하는 코드가 추가되었습니다.
      * [src/main/java/com/springboot/oauth2_jwt/config/SecurityConfigurationV2.java](https://github.com/Lucky-kor/be-reference-advanced-oauth2-redis-logout/blob/main/src/main/java/com/springboot/oauth2_jwt/config/SecurityConfigurationV2.java)
    * 로그아웃을 위한 AuthController 구현 클래스입니다.
      1. logout 핸들러 메서드 생성 
      2. authService.logout() 메서드의 호출 결과에 따라 HttpStatus 코드로 분기처리
      * [src/main/java/com/springboot/oauth2_jwt/auth/controller/AuthController.java](https://github.com/Lucky-kor/be-reference-advanced-oauth2-redis-logout/blob/main/src/main/java/com/springboot/oauth2_jwt/auth/controller/AuthController.java)
    * 로그아웃을 위한 AuthService 구현 클래스입니다.
      1. logout의 비즈니스 로직이 담긴 메서드 추가
      * [src/main/java/com/springboot/oauth2_jwt/auth/controller/AuthService.java](https://github.com/Lucky-kor/be-reference-advanced-oauth2-redis-logout/blob/main/src/main/java/com/springboot/oauth2_jwt/auth/service/AuthService.java)

---
