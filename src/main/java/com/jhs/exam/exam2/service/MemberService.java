package com.jhs.exam.exam2.service;

import com.jhs.exam.exam2.app.App;
import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.container.ContainerComponent;
import com.jhs.exam.exam2.dto.Member;
import com.jhs.exam.exam2.dto.ResultData;
import com.jhs.exam.exam2.repository.MemberRepository;
import com.jhs.exam.exam2.util.Ut;

public class MemberService implements ContainerComponent {
	private MemberRepository memberRepository;
	private EmailService emailService;
	private AttrService attrService;

	public void init() {
		memberRepository = Container.memberRepository;
		emailService = Container.emailService;
		attrService = Container.attrService;
	}

	// 재구현 완료[2021-08-09], [2021-08-16]
	// 로그인 여부를 확인하는 메서드
	public ResultData login(String loginId, String loginPw) {
		// loginId를 이용하여 member값을 구한다
		Member member = getMemberByLoginId(loginId);

		// member값이 존재하지 않을 시 F-1 리턴
		if (member == null) {
			return ResultData.from("F-1", "존재하지 않는 회원입니다.");
		}

		// 찾은 member의 loginPw값과 입력받은 loginPw와 비교하여 틀릴시 F-2 리턴
		if (member.getLoginPw().equals(loginPw) == false) {
			return ResultData.from("F-2", "비밀번호가 틀렸습니다.");
		}

		// 구한 member값을 저장하고 S-1 리턴
		return ResultData.from("S-1", Ut.f("`%s`님 환영합니다.", member.getNickname()), "member", member);
	}

	// 재구현 완료[2021-08-09], [2021-08-16]
	// 회원가입 함수
	public ResultData join(String loginId, String loginPw, String name, String nickname, String email,
			String cellphoneNo) {
		// loginId를 이용하여 member값을 구한다
		Member oldMember = getMemberByLoginId(loginId);
		// member값이 존재 하면 F-1 저장후 리턴
		if (oldMember != null) {
			return ResultData.from("F-1", Ut.f("`%s`아이디는 이미 사용중 입니다.", loginId));
		}
		// name과 email를 통하여 member값을 구해온다
		oldMember = getMemberByNameAndEmail(name, email);
		// member값이 존재 하면 F-2 저장후 리턴
		if (oldMember != null) {
			return ResultData.from("F-2", Ut.f("`%s`이름의 회원님은 이미 `%s`이메일로 회원가입 하셨습니다.", name, email));
		}
		// 해당 변수를 이용하여 member를 만들어주는 함수
		int id = memberRepository.join(loginId, loginPw, name, nickname, email, cellphoneNo);

		setLoginPwModifiedNow(id);
		// S-1저장후 리턴
		return ResultData.from("S-1", "회원 가입이 완료 되었습니다.", "id", id);
	}

	// 재구현 완료[2021-08-09], [2021-08-16]
	// 메일을 발송하는 함수
	// body += "<a href=\"" + siteLoginUri + "\" target=\"_blank\">로그인 하러가기</a>";
	public ResultData sendTempLoginPwToEmail(Member actor) {
		App app = Container.app;
		// 메일 제목과 내용 만들기
		String siteName = app.getSiteName();
		String siteLoginUri = app.getLoginUri();
		String title = "[" + siteName + "] 임시 패스워드 발송";
		String tempPassword = Ut.getTempPassword(6);
		String body = "<h1>임시 패스워드 : " + tempPassword + "<h1>";
		// 내용 + 해당 사이트 로그인페이지로 이동하는 a태크 생성
		body += "<a href=\"" + siteLoginUri + "\" target=\"_blank\">로그인 하러가기</a>";
		// 해당 member의 이메일이 존재하지 않을시 F-0 저장후 리턴
		if (actor.getEmail().length() == 0) {
			return ResultData.from("F-1", "회원님의 이메일이 존재하지 않습니다.");
		}
		// 메일을 발송해주는 함수
		// 만약 notifyRs 값이 1이 아니면 메일 발송에 실패 한것
		int notifyRs = emailService.notify(actor.getEmail(), title, body);
		// notifyRs가 1이 아니면 F-1 저장후 리턴
		if (notifyRs != 1) {
			return ResultData.from("F-1", "이메일 발송이 실패 하였습니다.");
		}
		// 해당 member의 비밀번호를 변경하는 메서드
		setTempLoginPw(actor, tempPassword);
		// 메일발송, 비밀번호 변경이 완료 되면 S-1, 완료 메세지 저장후 리턴
		return ResultData.from("S-1", Ut.f("임시 비밀번호를 `%s`로 발송 하였습니다.", actor.getEmail()));
	}

	// 재구현 완료[2021-08-09], [2021-08-16]
	// 해당 회원의 정보를 수정해주는 메서드
	public ResultData modify(Member member, String loginPw, String name, String nickname, String email,
			String cellphoneNo) {
		// 해당 member의 id값을 불러옴
		int memberId = member.getId();
		// name과 email이 회원정보와 하나라도 다를시
		if (member.getName().equals(name) == false || member.getEmail().equals(email) == false) {
			// name과 email를 통하여 member값을 구해온다
			Member oldMember = memberRepository.getMemberByNameAndEmail(name, email);

			// oldMeber값이 존재 할시 F-1, 오류메세지 저장후 리턴
			if (oldMember != null) {
				return ResultData.from("F-1", Ut.f("`%s`이름의 회원님은 이미 `%s`이메일로 회원가입 하셨습니다.", name, email));
			}
		}
		
		setLoginPwModifiedNow(memberId);

		// 회원의 정보를 수정해주는 메서드
		memberRepository.modify(memberId, loginPw, name, nickname, email, cellphoneNo);
		
		// S-1, 완료메세지 저장후 리턴
		return ResultData.from("S-1", "회원정보 수정이 완료되었습니다.");
	}

	// DB에 접근하여 해당 멤버 비밀번호 변경하는 함수
	private void setTempLoginPw(Member actor, String tempLoginPw) {
		memberRepository.modifyPassword(actor.getId(), Ut.sha256(tempLoginPw));

		setIsUsingTempPassword(actor.getId(), true);
	}

	public void setIsUsingTempPassword(int actorId, boolean use) {
		attrService.setValue("member__" + actorId + "__extra__isUsingTempPassword", use, null);
	}

	public boolean isUsingTempPassword(int actorId) {
		return attrService.getValueAsBoolean("member__" + actorId + "__extra__isUsingTempPassword");
	}

	// loginId로 해당 member값을 불러와 리턴하는 함수
	public Member getMemberByLoginId(String loginId) {
		return memberRepository.getMemberByLoginId(loginId);

	}

	// name, email로 해당 member값을 불러와 리턴하는 함수
	public Member getMemberByNameAndEmail(String name, String email) {
		return memberRepository.getMemberByNameAndEmail(name, email);
	}

	// 해당 member가 관리자인지 아닌지 판별하는 함수
	public boolean isAdmin(Member member) {
		return member.getAuthLevel() >= 7;
	}

	// id로 해당 member값을 불러와 리턴하는 함수
	public Member getMemberById(int id) {
		return memberRepository.getMemberById(id);
	}

	public ResultData getMemberRdByLoginId(String loginId) {
		Member oldMember = getMemberByLoginId(loginId);

		if (oldMember != null) {
			return ResultData.from("F-1", Ut.f("`%s`(은)는 이미 사용중인 아이디 입니다.", loginId), "loginId", loginId);
		}

		return ResultData.from("S-1", Ut.f("`%s`(은)는 사용 가능한 아이디 입니다.", loginId), "loginId", loginId);
	}

	private void setLoginPwModifiedNow(int actorId) {
		attrService.setValue("member__" + actorId + "__extra__loginPwModifiedDate", Ut.getNowDateStr(), null);
	}

	public int getOldPasswordDays() {
		return 90;
	}

	public boolean isNeedtoModifyOldLoginPw(int actorId) {
		String date = attrService.getValue("member__" + actorId + "__extra__loginPwModifiedDate");

		if (Ut.isEmpty(date)) {
			return true;
		}

		int pass = Ut.getPassedSecondsFrom(date);

		int oldPasswordDays = getOldPasswordDays();

		if (pass > oldPasswordDays * 60 * 60 * 24) {
			return true;
		}

		return false;
	}

}
