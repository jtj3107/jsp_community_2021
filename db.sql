DROP DATABASE IF EXISTS jsp_board;
CREATE DATABASE jsp_board;
USE jsp_board;

CREATE TABLE article (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    title CHAR(100) NOT NULL,
    `body` TEXT NOT NULL
);

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = '제목1',
`body` = '내용1';

INSERT INTO article
    SET regDate = NOW(),
    updateDate = NOW(),
    title = '제목2',
    `body` = '내용2';

# 회원 테이블 생성
CREATE TABLE `member` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    delDate DATETIME DEFAULT NULL, # 삭제날짜
    delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0, # 삭제상태(0:미삭제,1:삭제)
    loginId CHAR(50) UNIQUE NOT NULL,
    loginPw CHAR(100) NOT NULL,
    `name` CHAR(30) NOT NULL,
    nickname CHAR(30) UNIQUE NOT NULL,
    email CHAR(100) NOT NULL,
    cellphoneNo CHAR(20) NOT NULL,
    authLevel SMALLINT(2) UNSIGNED DEFAULT 3 NOT NULL COMMENT '(3=일반,7=관리자)'
);

# 회원 생성
INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = 'admin',
loginPw = '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918',
`name` = '조태중',
nickname = '관리자',
email = 'jtj3926@gmail.com',
cellphoneNo = '01012341234',
authLevel = 7;


# 게시판 테이블 생성
CREATE TABLE board (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '번호',
    regDate DATETIME NOT NULL COMMENT '작성날짜',
    updateDate DATETIME NOT NULL COMMENT '수정날짜',
    `code` CHAR(20) NOT NULL UNIQUE COMMENT '코드',
    `name` CHAR(20) NOT NULL UNIQUE COMMENT '이름',
    blindStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '블라인드여부',
    blindDate DATETIME COMMENT '블라인드날짜',
    delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '삭제여부',
    delDate DATETIME COMMENT '삭제날짜'
);

# 게시판 테스트 데이터 생성
## 공지사항 게시판 생성
INSERT INTO board
SET regDate = NOW(),
updateDate = NOW(),
`name` = 'NOTICE',
`code` = 'notice';

## 자유 게시판 생성
INSERT INTO board
SET regDate = NOW(),
updateDate = NOW(),
`name` = 'FREE',
`code` = 'free';

# 게시판 테이블 칼럼 추가
ALTER TABLE article
ADD COLUMN delDate DATETIME DEFAULT NULL AFTER updateDate;

ALTER TABLE article
ADD COLUMN delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 AFTER updateDate;

ALTER TABLE article
ADD COLUMN memberId INT(10) UNSIGNED NOT NULL AFTER updateDate;

ALTER TABLE article
ADD COLUMN boardId INT(10) UNSIGNED NOT NULL AFTER updateDate;

ALTER TABLE article
ADD COLUMN hitCount INT(10) UNSIGNED NOT NULL DEFAULT 0 AFTER `body`;

ALTER TABLE article
ADD COLUMN likeCount INT(10) UNSIGNED NOT NULL DEFAULT 0 AFTER `body`;

ALTER TABLE article
ADD COLUMN dislikeCount INT(10) UNSIGNED NOT NULL DEFAULT 0 AFTER `body`;

CREATE TABLE reply(
    id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    PRIMARY KEY(id),
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    memberId INT(10) UNSIGNED NOT NULL,
    articleId INT(10) UNSIGNED NOT NULL,
    `body` TEXT NOT NULL
);

# 좋아요 테이블 추가
CREATE TABLE islike (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    relTypeCode CHAR(30) NOT NULL,
    relId INT(10) UNSIGNED NOT NULL,
    memberId INT(10) UNSIGNED NOT NULL,
    `point` SMALLINT(1) NOT NULL
);

# 좋아요 인덱스
ALTER TABLE `jspCommunity`.`like` ADD INDEX (`relTypeCode` , `relId` , `memberId`); 

# 기존 게시물을 2번 회원이 쓴 자유게시물로 지정
UPDATE article
SET memberId = 2,
boardId = 2;


# 테스트 게시물 만들기
INSERT INTO article
(regDate, updateDate, boardId, memberId, title, `body`)
SELECT 
NOW(),
NOW(),
CAST(RAND() * 1 AS SIGNED) + 1,
CAST(RAND() * 1 AS SIGNED) + 1,
CONCAT('제목--', RAND()),
CONCAT('내용--', RAND())
FROM article;

# 부가정보테이블 
# 댓글 테이블 추가
CREATE TABLE attr (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    `relTypeCode` CHAR(20) NOT NULL,
    `relId` INT(10) UNSIGNED NOT NULL,
    `typeCode` CHAR(30) NOT NULL,
    `type2Code` CHAR(30) NOT NULL,
    `value` TEXT NOT NULL
);

# attr 유니크 인덱스 걸기
## 중복변수 생성금지
## 변수찾는 속도 최적화
ALTER TABLE `attr` ADD UNIQUE INDEX (`relTypeCode`, `relId`, `typeCode`, `type2Code`); 

## 특정 조건을 만족하는 회원 또는 게시물(기타 데이터)를 빠르게 찾기 위해서
ALTER TABLE `attr` ADD INDEX (`relTypeCode`, `typeCode`, `type2Code`);

# attr에 만료날짜 추가
ALTER TABLE `attr` ADD COLUMN `expireDate` DATETIME NULL AFTER `value`;

SELECT * FROM `reply`