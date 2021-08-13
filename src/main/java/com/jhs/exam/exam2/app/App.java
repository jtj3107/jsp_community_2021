package com.jhs.exam.exam2.app;

import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.container.ContainerComponent;
import com.jhs.exam.exam2.util.Ut;
import com.jhs.mysqliutil.MysqlUtil;

import lombok.Getter;

public class App implements ContainerComponent {
	@Getter
	private boolean ready = false;
	private String smtpGmailPw;

	@Override
	public void init() {
		// 해당 위치의 파일 값을 가져와 저장
		smtpGmailPw = Ut.getFileContents("c:/work/jtj/SmtpGmailPw.txt");

		// 파일값이 있을 경우 ready를 true로 변경
		if (smtpGmailPw != null && smtpGmailPw.trim().length() > 0) {
			ready = true;
		}
	}

	public static boolean isDevMode() {
		// 이 부분을 false로 바꾸면 production 모드 이다.
		return true;
	}

	// 서버 배포 여부를 리턴해주는 함수
	public static boolean isProductMode() {
		return isDevMode() == false;
	}

	// 정적 요소 세팅
	public static void start() {
		// DB 세팅
		MysqlUtil.setDBInfo("localhost", "geotjeoli", "gjl123414", "jsp_board");
		MysqlUtil.setDevMode(isDevMode());

		// 공용 객체 세팅
		Container.init();

	}

	// 커뮤니티 이름 리턴 함수
	public String getSiteName() {
		return "레몬 커뮤니티";
	}

	// 발송할 메일 주소 리턴 하는 함수
	public String getSmtpGmailId() {
		return "jtj3926@gmail.com";
	}

	//발송할 메일 비밀번호(파일) 리턴 하는 함수
	public String getSmtpGmailPw() {
		return smtpGmailPw;
	}

	// 베이스 Uri에 로그인페이지를 더해 리턴 하는 함수
	public String getLoginUri() {
		return getBaseUri() + "/usr/member/login";
	}

	// 베이스 Uri를 리턴 하는 함수
	private String getBaseUri() {
		String appUri = getSiteProtocol() + "://" + getSiteDomain();

		if (getSitePort() != 80 && getSitePort() != 443) {
			appUri += ":" + getSitePort();
		}

		if (getContextName().length() > 0) {
			appUri += "/" + getContextName();
		}

		return appUri;
	}

	// 커뮤니티 이름을 리턴 하는 함수
	private String getContextName() {
		if (isProductMode()) {
			return "";
		}

		return "jsp_community_2021";
	}

	// 사이트 포트번호를 리턴하는 함수
	private int getSitePort() {
		return 8080;
	}

	// 사이트 도메인을 리턴하는 함수
	private String getSiteDomain() {
		return "localhost";
	}

	// 사이트의 프로토콜을 리턴하는 함수
	private String getSiteProtocol() {
		if (isProductMode()) {
			return "https";
		}

		return "http";
	}

	// 메일 발송시 작성자이름을 리턴하는 함수
	public String getNotifyEmailFromName() {
		return "레몬 커뮤니티 알림봇";
	}
}
