### Java 변형체스게임

### 목적
* java언어로 네트워크 기반 체스게임을 개발
* DB와 연동하여 데이터를 관리
* Swing을 사용하여 GUI환경에서 동작
* 사용자들이 2명 이상 모이는 곳은 채팅이 되어야함
* 위 항목들을 개발 해봄으로써 java를 학습함.

### 개발 환경
* [Java SE 1.8](https://www.oracle.com/)
* [Oracle 11g 64bit](https://www.oracle.com/)
* [bootstrap 3](http://www.getbootstrap.com)

### 기능 목록
* 체스게임
* 채팅
* 회원가입, 로그인, ID/Pw찾기, 패스워드 변경


### 회원관련 화면
프로그램 실행시 나타나는 로그인화면, 회원가입, Id/pw 찾기 화면

![board list](https://dl.dropbox.com/s/hdlpm33e6ftuzo0/signin.png)
<p style="text-align:center">그림1. 로그인 화면</p>

![board content](https://dl.dropboxusercontent.com/u/31464666/blog/php-portfolio/board_v1_content.jpg)
<p style="text-align:center">그림2. 게시판 내용 출력</p>

### 데이터베이스 설계
다음으로 데이터 저장을 위해 데이터베이스에 테이블 생성을 하겠습니다.

```sql
CREATE TABLE board (
    no INT UNSIGNED NOT NULL AUTO_INCREMENT,
	title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    created_date INT UNSIGNED NOT NULL,
    modified_date INT UNSIGNED,
    deleted_date INT UNSIGNED,
    readed_count MEDIUMINT NOT NULL,
    enabled BOOLEAN NOT NULL,
    PRIMARY KEY(no)
);
```

* 게시판의 기본적인 기능만을 테스트 하기 위해 게시글에 대한 내용만 저장하는 테이블을 만들었습니다.
* date 컬럼 들을 int 타입으로 하였는데 이는 다른 데이터 베이스로 전환시 데이터의 호환성을 위해 작성하였습니다.

### CRUD 를 위한 클래스 설계
데이터의 CRUD를 위하여 데이터를 처리하는 서비스 클래스를 설계 하겠습니다.
게시판 v1 을 설계 하면서 생각한 것이 모델과 컨트롤러 뷰의 분리에 중점을 두고 설계하였고 데이터베이스와의 통신은 전적으로 서비스 클래스만을 이용하여 처리 하였습니다.
쿼리에 대해서는 별도의 파일로 작성하여 쿼리의 변경에 따른 부담을 최소화 하였습니다.
데이터베이스 접속 정보도 별도의 파일로 관리를 하게 됩니다.
그럼 하나씩 코드를 살펴 보겠습니다. 참고로 너무 내용이 길어지는것을 방지 하기 위해 모든 소스의 전체 내용을 보여드리지는 않습니다. 전체 소스를 보고 싶으신분은 [github](https://github.com/Hana-Lee/php-portfolio) 저장소에서 확인 하실 수 있습니다. 물론 내려받아서 실행까지 해볼 수도 있습니다.

* 게시판 Article 모델 클래스 입니다.

```php
<?php
class BoardArticle {
	public $no;
	public $title;
	public $content;
	public $user_id;
	public $created_date;
	public $modified_date;
	public $deleted_date;
	public $readed_count;
	public $enabled;
}
?>
```

* 데이터베이스와 통신을하는 DAO 서비스 클래스 (PDO 를 사용하여 Object Mapping 을 합니다)

```php
<?php
class BoardService {
	// return_type 으로 article 클래스의 이름을 적어주면 article 오브젝트가 리턴됩니다.
    public static function query($query, $params=NULL, $return_type=NULL){}
    // update, insert, delete 를 위한 공통 함수 입니다.
    public static function update($query, $params=NULL) {}
    private static function setParameters($stmt, $params) {}
    private static function getPDO() {}
?>
```

* 뷰에 데이터를 전달해주는 컨트롤러 클래스 입니다.

```php
<?php
class BoardController {
	function readArticle($b_no) {}
	function readAllArticle($page_no=1) {}
	function createArticle($article) {}
	function updateArticle($article) {}
	function deleteArticle($article) {}
	function getTotalArticleCount() {}
}
?>
```

* 쿼리를 관리하는 PHP 파일입니다.

```php
<?php
$query['selectAllArticle'] = 'SELECT * FROM board INNER JOIN
(SELECT * FROM board ORDER BY no DESC LIMIT :start, :count) AS b USING(no)';

$query['selectArticleByNo'] = 'SELECT * FROM board WHERE no = :no';

$query['createArticle'] = 'INSERT INTO board VALUES (
NULL, :title, :content, :user_id, :c_date, 0, 0, 1, 1
)';

$query['updateArticle'] = 'UPDATE board SET
title = :title, content = :content, modified_date = :m_date
WHERE no = :no AND user_id = :user_id';

$query['deleteArticle'] = 'DELETE FROM board WHERE no = :no AND user_id = :user_id';

$query['selectArticleCount'] = 'SELECT COUNT(*) AS CNT FROM board';
?>
```

* 데이터베이스 접속 관리 PHP 파일입니다.

```php
<?php
$db['username'] = 'root';
$db['password'] = 'root';
$db['hostname'] = 'localhost';
$db['database'] = 'portfolio';
?>
```

### 후기

위와 같은 설계 구조를 가지고 가장 간단한 게시판을 만들어 봤습니다.
게시판은 아마도 가장 많이 사용되면서도 가장 생각을 많이 하게 만드는게 아닌가 생각됩니다.
개인적으로 bootstrap 을 이용하면서 디자인에 대한 고민을 많이 하지 않아도 되었던것과 PHP 와 같은 스크립트 언어의 장점인 수정 후 확인까지 딜레이가 없이 바로바로 된다는게 참 좋았던것 같습니다.
PHP 에는 웹 어플리케이션을 만들때 편리한 함수들이 미리 정의되어 있던것도 좋았습니다. 다만 손에 익숙하지 않아서 그런지 <?php ?> 이렇게 쓰는것과 변수선언때 $를 쓰는게 좀 힘들었습니다.
오랫동안 자바로 웹 개발을 하다 PHP 를 이용하여 게시판을 만들어 보았는데 서로 장단이 있는것 같습니다.
그리고 PHP 에 대하여 안좋은 이야기들을 인터넷상에서 본적이 있는데 그들이 하는 말에도 일리가 있긴 하지만 PHP 가 그들이 말하는것처럼 못쓸 언어라면 진즉에 없어지거나 거의 사용되지 않았겠지요.
하지만 여전히 제가 생각했던것보다 많은곳에서 PHP 를 주력으로 사용하는곳이 많다는건 그만큼 PHP 가 생각보다는 쓸만하고 PHP 에 대한 대안이 없다는 반증이 아닐까 조심스럽게 생각해봅니다.
이제 간단한 게시판 v1 을 완성하였으니 다음에는 외부 편집기를 도입하고, 로그인을 구현한 조금 더 보강된 게시판 v2 를 만들어 보도록 하겠습니다.
