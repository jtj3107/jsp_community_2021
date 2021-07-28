package com.jhs.exam.exam2.http.controller;

import java.util.List;

import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.dto.Member;
import com.jhs.exam.exam2.dto.ResultData;
import com.jhs.exam.exam2.http.Rq;
import com.jhs.exam.exam2.service.MemberService;
import com.jhs.exam.exam2.util.Ut;

public class UsrMemberController extends Controller {
	// memberService 사용하기 위해 Container에서 memberService객체 불러오기
	private MemberService memberService = Container.memberService;

	@Override
	public void performAction(Rq rq) {
		// uri에 ActionMethodName이 아래 case에 해당하는지 확인
		switch (rq.getActionMethodName()) {
		// 해당 함수 실행
		case "login":
			actionShowLogin(rq);
			break;
		case "doLogin":
			actionDoLogin(rq);
			break;
		case "doLogout":
			actionDoLogout(rq);
			break;
		case "join":
			actionShowJoin(rq);
			break;
		case "doJoin":
			actionDoJoin(rq);
			break;
		case "findLoginId":
			actionShowFindLoginId(rq);
			break;
		case "doFindLoginId":
			actionDoFindLoginId(rq);
			break;
		case "findLoginPw":
			actionShowFindLoginPw(rq);
			break;
		case "doFindLoginPw":
			actionDoFindLoginPw(rq);
			break;
		// 없을시 메세지 출력후 break;
		default:
			rq.println("존재하지 않는 페이지 입니다.");
			break;
		}
	}
	
	// 비밀번호 찾기 jsp에서 이동
	private void actionDoFindLoginPw(Rq rq) {
		// 해당 jsp에서 로그인아이디, 이메일 받아오기
		String loginId = rq.getParam("loginId", "");
		String email = rq.getParam("email", "");
		
		// 비정상적인 방법으로 작성없이 접근시 리턴
		if (loginId.length() == 0) {
			rq.historyBack("loginId를 입력해주세요.");
			return;
		}
		
		if (email.length() == 0) {
			rq.historyBack("email를 입력해주세요.");
			return;
		}
		
		// 받아온 로그인아이디, 이메일 이용해 해당 멤버값 얻어오는 함수
		ResultData memberRd = memberService.getMemberByLoginIdAndEmail(loginId, email);

		// 해당 값이 F-로 시작하면 오류 메세지 출력후 리턴
		if (memberRd.isFail()) {
			rq.historyBack(memberRd.getMsg());
			return;
		}
		
		// F-로 시작하지 않는 memberRd에서 저정한 member값 찾아 저장
		Member member = (Member)memberRd.getBody().get("member");
		
		// 해당 member로 임시비밀번호 생성후 이메일 보내는 함수
		ResultData sendeTempLoginPwToEmailRs = memberService.sendTempLoginPwToEmail(member);
		
		// 메일발송 실패시 오류 메세지 출력후 리턴
		if(sendeTempLoginPwToEmailRs.isFail()) {
			rq.historyBack(sendeTempLoginPwToEmailRs.getMsg());
			return;
		}
		
		// 성공메세지 출력후 로그인페이지로 이동
		rq.replace(sendeTempLoginPwToEmailRs.getMsg(), "../member/login");
		
	}

	private void actionShowFindLoginPw(Rq rq) {
		// 비밀번호 찾기 jsp로 이동
		rq.jsp("usr/member/findLoginPw");
	}

	// 로그인아이디 찾기 함수(로그인아이디 찾기 jsp에서 이동)
	private void actionDoFindLoginId(Rq rq) {
		// 로그인아이디 찾기 jsp에서 넘어온 name과 email 찾아 저장
		String name = rq.getParam("name", "");
		String email = rq.getParam("email", "");
		
		// 비정상적으로 접근하여 name, email 누락시 리턴
		if (name.length() == 0) {
			rq.historyBack("name를 입력해주세요.");
			return;
		}
		
		if (email.length() == 0) {
			rq.historyBack("email를 입력해주세요.");
			return;
		}
		
		// 기존에 만들었던 이름과 이메일로 member구하는 함수 사용
		Member oldMember = memberService.getMemberByNameAndEmail(name, email);

		// oldMember 없을시 오류 메세지 출력후 리턴
		if (oldMember == null) {
			rq.historyBack("일치화는 회원이 존재하지 않습니다.");
			return;
		}
		
		// 이동 주소 로그인 페이지에 해당 멤버 로그인 아이디 포함해서 세팅
		String redlaceUri = "../member/login?loginId=" + oldMember.getLoginId();
		
		// 해당 member 아이디를 보여주고 세팅된 주소로 이동
		rq.replace(Ut.f("해당 회원의 로그인아이디는 `%s` 입니다.", oldMember.getLoginId()), redlaceUri);
		
	}

	// 로그인아이디 찾기 jsp로 이동하는 함수
	private void actionShowFindLoginId(Rq rq) {
		rq.jsp("usr/member/findLoginId");
	}

	// 회원가입 함수(회원가입 jsp에서 연결)
	private void actionDoJoin(Rq rq) {
		// 회원가입 jsp에서 해당 값 해당 변수에 저장
		String loginId = rq.getParam("loginId", "");
		String loginPw = rq.getParam("loginPw", "");
		String name = rq.getParam("name", "");
		String nickname = rq.getParam("nickname", "");
		String email = rq.getParam("email", "");
		String cellphoneNo = rq.getParam("cellphoneNo", "");
		
		// 비정상적으로 접근하여 해당 값이 존재하지 않을시 리턴
		if (loginId.length() == 0) {
			rq.historyBack("loginId를 입력해주세요.");
			return;
		}

		if (loginPw.length() == 0) {
			rq.historyBack("loginPw를 입력해주세요.");
			return;
		}
		
		if (name.length() == 0) {
			rq.historyBack("name를 입력해주세요.");
			return;
		}

		if (nickname.length() == 0) {
			rq.historyBack("nickname를 입력해주세요.");
			return;
		}
		if (email.length() == 0) {
			rq.historyBack("email를 입력해주세요.");
			return;
		}

		if (cellphoneNo.length() == 0) {
			rq.historyBack("cellphoneNo를 입력해주세요.");
			return;
		}
	
		// 해당 변수를 이용해 아이디 중복과 이미 가입된 회원인지 판단해주는 함수
		ResultData joinRd = memberService.join(loginId, loginPw, name, nickname, email, cellphoneNo);

		// joinRd가 F-로 시작시 오류 메세지 출력후 리턴
		if (joinRd.isFail()) {
			rq.historyBack(joinRd.getMsg());
			return;
		}
		
		// 회원가입 jsp에서 redirectUri값 찾아 존재하면 redirectUri 저장 아니면 ../article/list값 저장
		String redirectUri = rq.getParam("redirectUri", "../member/login");
		
		// 성공 메세지 출력후 저장된 페이지로 이동하는 함수
		rq.replace(joinRd.getMsg(), redirectUri);
		
	}

	// 회원가입 jsp로 이동하는 함수
	private void actionShowJoin(Rq rq) {
		rq.jsp("usr/member/join");
	}

	// 로그아웃 함수
	private void actionDoLogout(Rq rq) {
		// 로그인된 json 형식의 로그인멤버를 삭제 
		rq.removeSessionAttr("loginedMemberJson");
		rq.replace(null, "../../");
	}

	// 로그인 jsp에서 연결
	private void actionDoLogin(Rq rq) {
		// 로그인 jsp에서 로그인아이디, 로그인비밀번호 받아 해당 변수에 저장
		String loginId = rq.getParam("loginId", "");
		String loginPw = rq.getParam("loginPw", "");

		// 비정상적 접근으로 해당 변수값이 존재하지 않을시 리턴
		if (loginId.length() == 0) {
			rq.historyBack("loginId를 입력해주세요.");
			return;
		}

		if (loginPw.length() == 0) {
			rq.historyBack("loginPw를 입력해주세요.");
			return;
		}

		// 로그인아이디와 로그인 비밀번호를 이용하여 로그인 가능한지 파악하는 함수
		ResultData loginRd = memberService.login(loginId, loginPw);

		// loginRd이 F-로 시작시 오류 메세지 출력후 리턴
		if (loginRd.isFail()) {
			rq.historyBack(loginRd.getMsg());
			return;
		}

		// loginRd에 저장된 member값 찾아 저장
		Member member = (Member) loginRd.getBody().get("member");

		// member를 json형식으로 세션에 저장
		rq.setSessionAttr("loginedMemberJson", Ut.toJson(member, ""));
		
		// 회원가입 jsp에서 돌아갈 페이지 찾아 저장 없을시 ../article/list 저장
		String redirectUri = rq.getParam("redirectUri", "../article/list");
		
		// 성공 메세지 출력후 해당 페이지로 이동
		rq.replace(loginRd.getMsg(), redirectUri);
	}

	// 로그인 페이지로 이동 하는 함수
	private void actionShowLogin(Rq rq) {
		rq.jsp("usr/member/login");
	}
}
