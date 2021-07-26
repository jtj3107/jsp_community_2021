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
		rq.jsp("usr/member/findLoginPw");
	}

	private void actionDoFindLoginId(Rq rq) {
		String name = rq.getParam("name", "");
		String email = rq.getParam("email", "");
		
		if (name.length() == 0) {
			rq.historyBack("name를 입력해주세요.");
			return;
		}
		
		if (email.length() == 0) {
			rq.historyBack("email를 입력해주세요.");
			return;
		}
		
		ResultData loginIdRd = memberService.getMemberByNameAndEmail(name, email);

		if (loginIdRd.isFail()) {
			rq.historyBack(loginIdRd.getMsg());
			return;
		}
		
		rq.replace(loginIdRd.getMsg(), "../member/login");
		
	}

	private void actionShowFindLoginId(Rq rq) {
		rq.jsp("usr/member/findLoginId");
	}

	private void actionDoJoin(Rq rq) {
		String loginId = rq.getParam("loginId", "");
		String loginPw = rq.getParam("loginPw", "");
		String name = rq.getParam("name", "");
		String nickname = rq.getParam("nickname", "");
		String email = rq.getParam("email", "");
		String cellphoneNo = rq.getParam("cellphoneNo", "");
		
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
		
		ResultData joinRd = memberService.join(loginId, loginPw, name, nickname, email, cellphoneNo);

		if (joinRd.isFail()) {
			rq.historyBack(joinRd.getMsg());
			return;
		}
		
		String redirectUri = rq.getParam("redirectUri", "../article/list");
		
		rq.replace(joinRd.getMsg(), redirectUri);
		
	}

	private void actionShowJoin(Rq rq) {
		rq.jsp("usr/member/join");
	}

	private void actionDoLogout(Rq rq) {
		rq.removeSessionAttr("loginedMemberJson");
		rq.replace(null, "../../");
	}

	private void actionDoLogin(Rq rq) {
		String loginId = rq.getParam("loginId", "");
		String loginPw = rq.getParam("loginPw", "");

		if (loginId.length() == 0) {
			rq.historyBack("loginId를 입력해주세요.");
			return;
		}

		if (loginPw.length() == 0) {
			rq.historyBack("loginPw를 입력해주세요.");
			return;
		}

		ResultData loginRd = memberService.login(loginId, loginPw);

		if (loginRd.isFail()) {
			rq.historyBack(loginRd.getMsg());
			return;
		}

		Member member = (Member) loginRd.getBody().get("member");

		rq.setSessionAttr("loginedMemberJson", Ut.toJson(member, ""));
		
		String redirectUri = rq.getParam("redirectUri", "../article/list");
		
		rq.replace(loginRd.getMsg(), redirectUri);
	}

	private void actionShowLogin(Rq rq) {
		rq.jsp("usr/member/login");
	}
}
