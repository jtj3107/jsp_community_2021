package com.jhs.exam.exam2.service;

import java.util.List;

import com.jhs.exam.exam2.app.App;
import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.dto.Member;
import com.jhs.exam.exam2.dto.ResultData;
import com.jhs.exam.exam2.repository.MemberRepository;
import com.jhs.exam.exam2.util.Ut;

public class MemberService {
	private MemberRepository memberRepository = Container.memberRepository;
	// private EmailService emailService = Container.emailService;

	public ResultData login(String loginId, String loginPw) {
		// 로그인아이디로 member가 존재하는지 확인하는 함수
		Member member = getMemberByLoginId(loginId);

		// 해당 member가 존재 하지 않을시 F-1저장후 리턴
		if (member == null) {
			return ResultData.from("F-1", "존재하지 않는 회원의 로그인아이디 입니다.");
		}

		// 해당 member는 존재 하나 member의 비밀번호와 입력한 비밀번호가 틀릴시 F-2저장후 리턴
		if (member.getLoginPw().equals(loginPw) == false) {
			return ResultData.from("F-2", "비밀번호가 일치하지 않습니다.");
		}

		// 모두 이상 없을시 S-1, member값 저장후 리턴
		return ResultData.from("S-1", "환영합니다.", "member", member);
	}

	public ResultData join(String loginId, String loginPw, String name, String nickname, String email,
			String cellphoneNo) {
		// 가입할 로그인 아이디를 받아 member 추적
		Member oldMember = getMemberByLoginId(loginId);

		// 찾은 member가 존재 할시 아이디가 중복 이므로 F-1, 오류메세지 저장후 리턴
		if( oldMember != null) {
			return ResultData.from("F-1", Ut.f("`%s` 로그인 아이디는 이미 사용중 입니다.", loginId));
		}
		
		// 가입자의 이름과 이메일을 받아 member 추적
		oldMember = getMemberByNameAndEmail(name, email);
		
		// 가입자의 이름과 이메일로 member가 존재하면 동일 인물로 판단하여 F-2,오류메세지 저장후 리턴
		if(oldMember != null) {
			return ResultData.from("F-2", Ut.f("`%s`님은 이메일 주소 `%s`로 이미 회원가입하셨습니다.", name, email));
		}
		
		int id = memberRepository.join(loginId, loginPw, name, nickname, email, cellphoneNo);
		
		// S-1, 완료 메세지 저장후 리턴
		return ResultData.from("S-1", Ut.f("회원가입이 완료되었습니다."), "id", id);
	}

	public List<Member> getForPrintMembers() {
		// members를 구하는 함수
		return memberRepository.getForPrintMembers();
	}

	public ResultData getMemberByLoginIdAndEmail(String loginId, String email) {
		// 컨트롤러에서 받아온 로그인아이디, 이메일로 해당 멤버 구한뒤 저장
		Member member = memberRepository.getMemberByLoginIdAndEmail(loginId, email);
		
		// 존재하지 않을시 F-1저장후 리턴
		if(member == null) {
			return ResultData.from("F-1", "존재하지 않는 회원입니다.");
		}
		
		// 해당 멤버의 이메일과 작성한 이메일이 다를시 F-2저장후 리턴
		if(member.getEmail().equals(email) == false) {
			return ResultData.from("F-2", "가입하신 이메일이 아닙니다.");
		}
		
		// 해당 멤버가 구해졌을시 S-1 저장후 메세지, member값 저장후 리턴
		return ResultData.from("S-1", Ut.f("확인 되었습니다."), "member", member);
	}

	public ResultData sendTempLoginPwToEmail(Member actor) {
		// 메일 제목과 내용 만들기
		String siteName = App.getSiteName(); // 사이트 이름 리턴하는 함수
		//String siteLoginUrl = App.getLoginUri();
		String title = "[" + siteName + "] 임시 패스워드 발송"; // 이메일 제목
		String tempPassword = Ut.getTempPassword(6); // 임시 비밀번호 저장
		String body = "<h1>임시 패스워드 : " + tempPassword + "<h1>"; // 내용
		// 내용 + 해당 사이트 로그인페이지로 이동하는 a태크 생성
		body += "<a href=\"" + "http://localhost:8084/jsp_community_2021/usr/member/login" + "\" target=\"_blank\">로그인 하러가기</a>";
	
		// 메일 발송(보내는 매개, 매개 비밀번호, 보내는사람, 사이트이름, 해당 멤버의 이메일, 제목, 내용)
		int sendRs = Ut.sendMail("jtj3926@gmail.com", "khnvjxnxfqxotpnv", "no-reply@lemon-cm.com", "레몬 커뮤니티 알림", actor.getEmail(), title, body);
		
		// sendRs이 1이면 발송 성공 1이 아니면 발송실패
		if( sendRs != 1) {
			return ResultData.from("F-1", "메일 발송에 실패하였습니다.");
		}
		
		// 해당 멤버와 임시 비밀번호를 이용하여 해당 멤버의 비밀번호를 변경하는 함수
		setTempPassword(actor, tempPassword);
		
		// 메일 발송이 완료되면 S-1, 해당 이메일로 발송했다는 메세지 저장후 리턴
		return ResultData.from("S-1", Ut.f("해당 회원의 새로운 비밀번호를" + actor.getEmail() + "로 발송하였습니다."));
	}

	private void setTempPassword(Member actor, String tempPassword) {
		// DB에 접근하여 해당 멤버 비밀번호 변경하는 함수
		memberRepository.setTempPassword(actor, tempPassword);
	}
	
	private Member getMemberByLoginId(String loginId) {
		return memberRepository.getMemberByLoginId(loginId);
	}
	
	public Member getMemberByNameAndEmail(String name, String email) {
		return memberRepository.getMemberByNameAndEmail(name, email);
	}

}
